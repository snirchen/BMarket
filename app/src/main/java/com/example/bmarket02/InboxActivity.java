package com.example.bmarket02;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class InboxActivity extends AppCompatActivity
{

    ImageButton back_btn;
    ArrayList<String> my_chats = new ArrayList<>();
    TextView no_chats_txt;
    ListView chats_lst;
    String last_message = "";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });

        no_chats_txt = findViewById(R.id.no_chats_txt);
        no_chats_txt.setVisibility(View.INVISIBLE);

        chats_lst = findViewById(R.id.chats_lst);


        for (Message message : SplashScreen.all_messages)
        {
            if (message.getRx_id().equals(LoginActivity.current_user.getPhone()) && !my_chats.contains(message.getTx_id()))
            {
                my_chats.add(message.getTx_id());
            } else if (message.getTx_id().equals(LoginActivity.current_user.getPhone()) && !my_chats.contains(message.getRx_id()))
            {
                my_chats.add(message.getRx_id());
            }
        }


        if (my_chats.isEmpty())
        {
            no_chats_txt.setVisibility(View.VISIBLE);
        }

        chats_lst.setAdapter(new BaseAdapter()
        {
            @Override
            public int getCount()
            {
                return my_chats.size();
            }

            @Override
            public String getItem(int i)
            {
                return my_chats.get(i);
            }

            @Override
            public long getItemId(int i)
            {
                return 0;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup)
            {
                final String chat_id = getItem(i);
                View chatRowView = getLayoutInflater().inflate(R.layout.chat_row, null);
                chatRowView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        Intent intent = new Intent(InboxActivity.this, ChatActivity.class);
                        intent.putExtra("rx_id", chat_id);
                        startActivity(intent);
                    }
                });

                for (Message message : SplashScreen.all_messages)
                {
                    if (message.getRx_id().equals(chat_id) || message.getTx_id().equals(chat_id))
                    {
                        if (message.getRx_id().equals(LoginActivity.current_user.getPhone()) || message.getTx_id().equals(LoginActivity.current_user.getPhone()))
                        {
                            {
                                if (message instanceof TextMessage)
                                {
                                    TextMessage textMessage = (TextMessage) message;
                                    last_message = textMessage.getText();
                                    break;
                                }
                                break;
                            }
                        }
                    }
                }

                final CircleImageView chatPicture = chatRowView.findViewById(R.id.chat_picture);
                final TextView name = chatRowView.findViewById(R.id.name), message = chatRowView.findViewById(R.id.last_message);
                final ImageButton chat_btn = chatRowView.findViewById(R.id.chat_btn);
                message.setText(last_message);


                chat_btn.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        Intent intent = new Intent(InboxActivity.this, ChatActivity.class);
                        intent.putExtra("rx_id", chat_id);
                        startActivity(intent);
                    }
                });

                chatPicture.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(InboxActivity.this);
                        View popupView = getLayoutInflater().inflate(R.layout.show_item_image_dialog, null);
                        ((ImageView) popupView.findViewById(R.id.item_image)).setImageDrawable(chatPicture.getDrawable());
                        builder.setView(popupView);
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        //alertDialog.getWindow().setLayout(itemImage.getWidth(), itemImage.getHeight());
                        alertDialog.getWindow().setLayout(800, 800);
                    }
                });

                StorageReference ref = FirebaseStorage.getInstance().getReference().child("profile/" + chat_id + ".jpg");
                File localFile = null;
                try
                {
                    localFile = File.createTempFile("image", "jpg");
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
                final File finalLocalFile = localFile;
                ref.getFile(localFile)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>()
                        {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot)
                            {
                                chatPicture.setImageBitmap(BitmapFactory.decodeFile(finalLocalFile.getPath()));
                            }
                        }).addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception exception)
                    {

                    }
                });

                SplashScreen.db.collection("users").whereEqualTo("phone", chat_id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        if (task.isSuccessful())
                        {
                            User user = task.getResult().getDocuments().get(0).toObject(User.class);
                            name.setText(user.getName());
                        }
                    }
                });
                return chatRowView;
            }
        });

    }

    @Override
    protected void onPostResume()
    {
        super.onPostResume();

        // to make newest chat on top
        my_chats.clear();
        for (Message message : SplashScreen.all_messages)
        {
            if (message.getRx_id().equals(LoginActivity.current_user.getPhone()) && !my_chats.contains(message.getTx_id()))
            {
                my_chats.add(message.getTx_id());
            } else if (message.getTx_id().equals(LoginActivity.current_user.getPhone()) && !my_chats.contains(message.getRx_id()))
            {
                my_chats.add(message.getRx_id());
            }
        }

        // to update last message
        ((BaseAdapter) chats_lst.getAdapter()).notifyDataSetChanged();

        if (my_chats.isEmpty())
        {
            no_chats_txt.setVisibility(View.VISIBLE);
        }
    }
}
