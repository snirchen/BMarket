package com.example.bmarket02;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ScrollView;
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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity
{

    ImageButton send_image_btn;
    EditText editText;
    TextView send_btn, rx_name;
    ImageButton send_voice_messege_btn, upload_image_btn;
    ImageButton back_btn;
    private static final int PICK_IMAGE_REQUEST = 1;
    public static Uri mImageUri;
    String rx_id, related_item_text_message;
    ListView messages_listview;
    ArrayList<Message> chat_messages = new ArrayList<>();
    CircleImageView chat_picture;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_chat);

        editText = findViewById(R.id.editText);
        upload_image_btn = findViewById(R.id.upload_image_btn);
        send_voice_messege_btn = findViewById(R.id.send_voice_messege_btn);
        rx_name = findViewById(R.id.rx_name);
        send_btn = findViewById(R.id.send_btn);
        send_image_btn = findViewById(R.id.send_image_btn);
        back_btn = findViewById(R.id.back_btn);
        messages_listview = findViewById(R.id.messages_listview);
        chat_picture = findViewById(R.id.chat_picture);

        Intent intent = getIntent();
        if (intent != null)
        {
            if (intent.getExtras().getString("rx_id") != null)
            {
                rx_id = intent.getExtras().getString("rx_id");
            }

            if (intent.getExtras().getString("related_item_text_message") != null)
            {
                related_item_text_message = intent.getExtras().getString("related_item_text_message");
                editText.setText(related_item_text_message);
                send_btn.setVisibility(View.VISIBLE);
                send_voice_messege_btn.setVisibility(View.INVISIBLE);
                upload_image_btn.setVisibility(View.INVISIBLE);
            }
        }

        StorageReference ref = FirebaseStorage.getInstance().getReference().child("profile/" + rx_id + ".jpg");
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
                        chat_picture.setImageBitmap(BitmapFactory.decodeFile(finalLocalFile.getPath()));
                    }
                }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception exception)
            {

            }
        });

        for (Message message : SplashScreen.all_messages)
        {
            if (message.getRx_id().equals(rx_id) || message.getTx_id().equals(rx_id))
            {
                if (message.getRx_id().equals(LoginActivity.current_user.getPhone()) || message.getTx_id().equals(LoginActivity.current_user.getPhone()))
                {
                    chat_messages.add(message);
                }
            }
        }
        Collections.reverse(chat_messages);
        messages_listview.setAdapter(new MessageListAdapter(this, chat_messages));


        SplashScreen.messagesManager.setOnMessagesChanged(new OnMessagesChanged()
        {
            @Override
            public void onMessagesChanged(List<Message> newMessages)
            {
                chat_messages.clear();
                for (Message message : newMessages)
                {
                    if (message.getRx_id().equals(rx_id) || message.getTx_id().equals(rx_id))
                    {
                        if (message.getRx_id().equals(LoginActivity.current_user.getPhone()) || message.getTx_id().equals(LoginActivity.current_user.getPhone()))
                        {
                            chat_messages.add(message);
                        }
                    }
                }
                Collections.reverse(chat_messages);
                messages_listview.setAdapter(new MessageListAdapter(ChatActivity.this, chat_messages));
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });

        send_image_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent it = new Intent(ChatActivity.this, ChatCamera.class);
                startActivity(it);
            }
        });

        upload_image_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                pickImage();
            }
        });

        send_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Message message = new TextMessage(String.valueOf(400000000 + SplashScreen.all_messages.size()), rx_id, editText.getText().toString(), new Date(), LoginActivity.current_user.getPhone());
                SplashScreen.db.collection("messages").add(message);
                editText.setText("");
            }
        });

        SplashScreen.db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task)
            {
                ArrayList<User> temp = new ArrayList<>();
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null)
                {
                    temp.addAll(querySnapshot.toObjects(User.class));
                    for (User user : temp)
                    {
                        if (user.getPhone().equals(rx_id))
                        {
                            rx_name.setText(user.getName());
                            break;
                        }
                    }
                }
            }
        });

        editText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                if (editText.getText().toString().matches(""))
                {
                    send_btn.setVisibility(View.INVISIBLE);
                    send_voice_messege_btn.setVisibility(View.VISIBLE);
                    upload_image_btn.setVisibility(View.VISIBLE);
                } else
                {
                    send_btn.setVisibility(View.VISIBLE);
                    send_voice_messege_btn.setVisibility(View.INVISIBLE);
                    upload_image_btn.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable)
            {

            }
        });

    }

    public void pickImage()
    {
        Intent it = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(it, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            mImageUri = data.getData();
        }
    }
}
