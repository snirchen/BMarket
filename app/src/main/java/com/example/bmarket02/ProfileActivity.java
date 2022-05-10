package com.example.bmarket02;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity implements EditNameDialog.EditNameDialogListener {

    public static TextView name_txt, school_txt, grade_txt, phone_txt, about_txt;
    ImageButton edit_profile;
    Button sign_out_btn;
    private Uri mImageUri;
    private static final int PICK_IMAGE = 1;
    ImageButton back_btn;
    private CircleImageView profile_picture;
    private static final int PICK_IMAGE_REQUEST = 1;
    final StorageReference ref = FirebaseStorage.getInstance().getReference().child("profile/" + LoginActivity.current_user.getPhone() + ".jpg");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_profile);

        name_txt = findViewById(R.id.name_txt);
        school_txt = findViewById(R.id.school_txt);
        grade_txt = findViewById(R.id.grade_txt);
        phone_txt = findViewById(R.id.phone_txt);
        about_txt = findViewById(R.id.about_txt);
        profile_picture = findViewById(R.id.chat_picture);
        edit_profile = findViewById(R.id.edit_image_btn);
        sign_out_btn = findViewById(R.id.sign_out_btn);
        name_txt.setText(LoginActivity.current_user.getName());
        school_txt.setText(LoginActivity.current_user.getSchool());
        grade_txt.setText(LoginActivity.current_user.getGrade());
        phone_txt.setText(LoginActivity.current_user.getPhone());
        about_txt.setText(LoginActivity.current_user.getAbout());

        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        File localFile = null;
        try {
            localFile = File.createTempFile("image", "jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }

        final File finalLocalFile = localFile;
        ref.getFile(localFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        profile_picture.setImageBitmap(BitmapFactory.decodeFile(finalLocalFile.getPath()));
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });


        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });

        sign_out_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.phone_edit.setText("");
                LoginActivity.password_edit.setText("");
                Intent it = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(it);
            }
        });


        name_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenDialog();
            }
        });
    }

    public void OpenDialog() {
        EditNameDialog editNameDialog = new EditNameDialog();
        editNameDialog.show(getSupportFragmentManager(), "Enter your name");
    }

    @Override
    public void applyText(String name) {
        name_txt.setText(name);
        LoginActivity.current_user.setName(name);
        LoginActivity.current_user_doc.set(LoginActivity.current_user);
    }



    private void OpenGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            {
                mImageUri = data.getData();
                profile_picture.setImageURI(mImageUri);
                ref.putFile(mImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful())
                        {

                        }
                        else
                        {
                            Toast.makeText(ProfileActivity.this, "upload image got wrong :(", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }
}
