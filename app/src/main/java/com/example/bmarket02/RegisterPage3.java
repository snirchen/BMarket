package com.example.bmarket02;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterPage3 extends AppCompatActivity {

    private CircleImageView edit_profile;
    private EditText edit_name, edit_password, edit_confirm_password;
    private Button next_btn;
    private static final int PICK_IMAGE_REQUEST = 1;
    public static String name, password;
    ImageButton back_btn;
    public static Uri mImageUri;
    public static StorageReference file_path;
    public static StorageReference profile_image_ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page3);

        edit_profile = findViewById(R.id.edit_profile);
        edit_name = findViewById(R.id.edit_name);
        edit_password = findViewById(R.id.edit_password);
        edit_confirm_password = findViewById(R.id.edit_confirm_password);
        next_btn = findViewById(R.id.edit_image_btn);
        profile_image_ref = FirebaseStorage.getInstance().getReference();

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NextPageAttempt();
            }
        });

        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    public void pickImage() {
        Intent it = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(it, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();
            edit_profile.setImageURI(mImageUri);
        }
    }

    private void NextPageAttempt() {
        name = edit_name.getText().toString();
        password = edit_confirm_password.getText().toString();

        if (mImageUri == null) {
            mImageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                    + "://" + getResources().getResourcePackageName(R.drawable.default_profile_image)
                    + '/' + getResources().getResourceTypeName(R.drawable.default_profile_image)
                    + '/' + getResources().getResourceEntryName(R.drawable.default_profile_image));
        }
        file_path = profile_image_ref.child("profile/" + RegisterPage1.phone + ".jpg");


        Intent it = new Intent(RegisterPage3.this, RegisterPage4.class);
        startActivity(it);
    }
}


