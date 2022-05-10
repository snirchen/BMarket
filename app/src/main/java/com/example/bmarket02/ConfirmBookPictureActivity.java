package com.example.bmarket02;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

public class ConfirmBookPictureActivity extends AppCompatActivity {

    ImageView recapture_btn, take_another_picture_btn, done_btn, current_image, image_0, image_1, image_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_confirm_book_picture);
        take_another_picture_btn = findViewById(R.id.take_another_picture_btn);
        take_another_picture_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BooksCamera.current_image_index < 2) {
                    BooksCamera.current_image_index++;
                    finish();
                } else {
                    Toast.makeText(ConfirmBookPictureActivity.this, "You can't upload more than 3 pictures", Toast.LENGTH_SHORT).show();
                }
            }
        });
        recapture_btn = findViewById(R.id.recapture_btn);
        recapture_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        done_btn = findViewById(R.id.done_btn);
        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(ConfirmBookPictureActivity.this, UploadItem1.class);
                startActivity(it);
            }
        });


        current_image = findViewById(R.id.current_image);
        image_0 = findViewById(R.id.image_0);
        image_1 = findViewById(R.id.image_1);
        image_2 = findViewById(R.id.image_2);
        current_image.setImageBitmap(BooksCamera.images[BooksCamera.current_image_index]);


        image_0.setImageBitmap(BooksCamera.images[0]);

        if (BooksCamera.images[1] != null) {
            image_1.setImageBitmap(BooksCamera.images[1]);
        }
        if (BooksCamera.images[2] != null) {
            image_2.setImageBitmap(BooksCamera.images[2]);
        }

        image_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                current_image.setImageBitmap(BooksCamera.images[0]);
            }
        });
        image_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BooksCamera.images[1] != null) {
                    current_image.setImageBitmap(BooksCamera.images[1]);
                }
            }
        });
        image_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BooksCamera.images[2] != null) {
                    current_image.setImageBitmap(BooksCamera.images[2]);
                }
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        // To delete the previous pictures
    }

}
