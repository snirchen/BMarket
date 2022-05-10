package com.example.bmarket02;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class ItemInfoActivity extends AppCompatActivity implements EditPriceDialog.EditPriceDialogListener
{

    Button contact_publisher_btn;
    public static Item currentItem;
    ImageButton back_btn;
    ImageView item_bookmark, current_image, arrow_right, arrow_left;
    TextView item_price_txt, item_title, item_author, item_description, activity_type;
    boolean bookmarked = false;
    Saved saved;
    Match match;
    StorageReference ref;
    Bitmap[] item_images = new Bitmap[3];
    int current_image_index = 0;
    public static String request_message;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_item_info);

        contact_publisher_btn = findViewById(R.id.contact_publisher_btn);
        arrow_left = findViewById(R.id.arrow_left);
        arrow_right = findViewById(R.id.arrow_right);
        current_image = findViewById(R.id.current_image);
        activity_type = findViewById(R.id.activity_type);
        item_author = findViewById(R.id.message);
        item_description = findViewById(R.id.item_description);
        item_title = findViewById(R.id.name);
        item_bookmark = findViewById(R.id.item_bookmark);
        back_btn = findViewById(R.id.back_btn);
        item_price_txt = findViewById(R.id.item_price_txt);

        Intent intent = getIntent();
        currentItem = (Item) intent.getSerializableExtra("current_item");
        saved = new Saved(currentItem.getItem_id(), LoginActivity.current_user.getPhone());
        match = new Match("", currentItem.getItem_id(), LoginActivity.current_user.getPhone());

        if (currentItem.getActivity_type().equals("למכירה"))
        {
            request_message = "היי, אשמח לקנות ממך את הספר " + currentItem.getItem_name() + ".";
        } else
        {
            request_message = "היי, אשמח למכור לך את הספר " + currentItem.getItem_name() + ".";
        }

        File localFile = null;
        try
        {
            localFile = File.createTempFile("image", "jpg");
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        final File finalLocalFile = localFile;

        ref = FirebaseStorage.getInstance().getReference().child("items/" + currentItem.getItem_id() + "/" + "0" + ".jpg");
        ref.getFile(localFile)
                .addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task)
                    {
                        if (task.isSuccessful())
                        {
                            item_images[0] = BitmapFactory.decodeFile(finalLocalFile.getPath());
                            current_image.setImageBitmap(item_images[current_image_index]);
                        }
                    }
                });
        ref = FirebaseStorage.getInstance().getReference().child("items/" + currentItem.getItem_id() + "/" + "1" + ".jpg");
        ref.getFile(localFile)
                .addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task)
                    {
                        if (task.isSuccessful())
                        {
                            item_images[1] = BitmapFactory.decodeFile(finalLocalFile.getPath());
                        }
                    }
                });
        ref = FirebaseStorage.getInstance().getReference().child("items/" + currentItem.getItem_id() + "/" + "2" + ".jpg");
        ref.getFile(localFile)
                .addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task)
                    {
                        if (task.isSuccessful())
                        {
                            item_images[2] = BitmapFactory.decodeFile(finalLocalFile.getPath());
                        }
                    }
                });


        item_price_txt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (currentItem.getUser_phone().equals(LoginActivity.current_user.getPhone()) && currentItem.getActive())
                {
                    OpenDialog();
                }
            }
        });

        arrow_right.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (current_image_index < 2)
                {
                    if (item_images[++current_image_index] != null)
                    {
                        current_image.setImageBitmap(item_images[current_image_index]);
                    } else if ((current_image_index < 2) && (item_images[++current_image_index] != null))
                    {
                        current_image.setImageBitmap(item_images[current_image_index]);
                    } else
                    {
                        current_image_index = 0;
                        if (item_images[current_image_index] != null)
                        {
                            current_image.setImageBitmap(item_images[current_image_index]);
                        }
                    }
                } else
                {
                    current_image_index = 0;
                    if (item_images[current_image_index] != null)
                    {
                        current_image.setImageBitmap(item_images[current_image_index]);
                    }
                }
            }
        });
        arrow_left.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (current_image_index > 0)
                {
                    if (item_images[--current_image_index] != null)
                    {
                        current_image.setImageBitmap(item_images[current_image_index]);
                    } else if ((current_image_index > 0) && (item_images[--current_image_index] != null))
                    {
                        current_image.setImageBitmap(item_images[current_image_index]);
                    } else
                    {
                        current_image_index = 2;
                        if (item_images[current_image_index] != null)
                        {
                            current_image.setImageBitmap(item_images[current_image_index]);
                        }
                    }
                } else
                {
                    current_image_index = 2;
                    if (item_images[current_image_index] != null)
                    {
                        current_image.setImageBitmap(item_images[current_image_index]);
                    } else if (item_images[--current_image_index] != null)
                    {
                        current_image.setImageBitmap(item_images[current_image_index]);
                    } else if (item_images[--current_image_index] != null)
                    {
                        current_image.setImageBitmap(item_images[current_image_index]);
                    }
                }
            }
        });

        item_author.setText(currentItem.getItem_author());
        item_description.setText(currentItem.getItem_description());
        item_title.setText(currentItem.getItem_name());
        item_price_txt.setText("₪" + currentItem.getItem_price());
        activity_type.setText(currentItem.getActivity_type());

        UpdateSaved(item_bookmark);

        item_bookmark.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (bookmarked)
                {
                    item_bookmark.setImageResource(R.drawable.ic_bookmark_not_filled);
                } else
                {
                    item_bookmark.setImageResource(R.drawable.ic_bookmark_filled);
                }
                ToggleSaved(item_bookmark);
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

        if (currentItem.getUser_phone().equals(LoginActivity.current_user.getPhone()) && currentItem.getActive())
        {
            contact_publisher_btn.setText("מחק פריט");
            contact_publisher_btn.setBackgroundResource(R.drawable.delete_item_button_style);
        } else
        {
            contact_publisher_btn.setText("צור קשר");
            contact_publisher_btn.setBackgroundResource(R.drawable.next_button_style);
        }


        contact_publisher_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (currentItem.getUser_phone().equals(LoginActivity.current_user.getPhone()))
                { //the owner of the item
                    contact_publisher_btn.setEnabled(false);
                    currentItem.setActive(false);
                    SplashScreen.db.collection("items").whereEqualTo("item_id", currentItem.getItem_id()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task)
                        {
                            if (task.isSuccessful())
                            {
                                QuerySnapshot querySnapshot = task.getResult();
                                if (querySnapshot != null && querySnapshot.size() == 1)
                                {
                                    querySnapshot.getDocuments().get(0).getReference().set(currentItem).addOnCompleteListener(new OnCompleteListener<Void>()
                                    {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            Intent it = new Intent(ItemInfoActivity.this, MainActivity.class);
                                            startActivity(it);
                                        }
                                    });
                                }
                            }
                        }
                    });
                } else // potential buyer
                {
                    Intent intent = new Intent(ItemInfoActivity.this, ChatActivity.class);
                    intent.putExtra("rx_id", currentItem.getUser_phone());
                    intent.putExtra("related_item_text_message", request_message);
                    startActivity(intent);
                }
            }
        });
    }


    public void ToggleSaved(final ImageView item_bookmark)
    {
        item_bookmark.setEnabled(false);
        SplashScreen.db.collection("saved").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task)
            {
                if (task.isSuccessful())
                {
                    for (QueryDocumentSnapshot document : task.getResult())
                    {
                        if (document.get("item_id").toString().equals(currentItem.getItem_id())
                                && document.get("user_phone").toString().equals(LoginActivity.current_user.getPhone()))
                        {
                            DeleteDocument(document.getId());
                            break;
                        }
                    }
                    if (!bookmarked)
                    {
                        AddDocument(saved);
                    }
                }
                UpdateSaved(item_bookmark);
            }
        });
    }

    public void UpdateSaved(final ImageView item_bookmark)
    {
        SplashScreen.db.collection("saved").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task)
            {
                if (task.isSuccessful())
                {
                    Boolean exists = false;
                    for (QueryDocumentSnapshot document : task.getResult())
                    {
                        if (document.get("item_id").toString().equals(currentItem.getItem_id())
                                && document.get("user_phone").toString().equals(LoginActivity.current_user.getPhone()))
                        {
                            item_bookmark.setImageResource(R.drawable.ic_bookmark_filled);
                            exists = true;
                            bookmarked = true;
                            break;
                        }
                    }
                    if (!exists)
                    {
                        item_bookmark.setImageResource(R.drawable.ic_bookmark_not_filled);
                        bookmarked = false;
                    }
                }
            }
        });
        item_bookmark.setEnabled(true);
    }

    public void DeleteDocument(String id)
    {
        SplashScreen.db.collection("saved").document(id).delete();
    }

    public void AddDocument(Saved saved)
    {
        SplashScreen.db.collection("saved").add(saved);
    }

    @Override
    protected void onPostResume()
    {
        super.onPostResume();
        UpdateSaved(item_bookmark);
    }

    public void OpenDialog()
    {
        EditPriceDialog editPriceDialog = new EditPriceDialog();
        editPriceDialog.show(getSupportFragmentManager(), "Enter a Price");
    }

    @Override
    public void applyText(String price)
    {
        item_price_txt.setText("₪" + price);
        currentItem.setItem_price(price);
        SplashScreen.db.collection("items").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task)
            {
                if (task.isSuccessful())
                {
                    for (QueryDocumentSnapshot document : task.getResult())
                    {
                        if (document.get("item_id").toString().equals(currentItem.getItem_id()))
                        {
                            SplashScreen.db.collection("items").document(document.getId()).delete();
                            SplashScreen.db.collection("items").add(currentItem);
                        }
                    }
                }
            }
        });
    }
}
