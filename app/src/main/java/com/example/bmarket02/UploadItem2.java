package com.example.bmarket02;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UploadItem2 extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    Boolean active = true;
    String activity_type, item_condition, item_grade, item_price, item_upload_time;

    ImageButton back_btn;
    Button done_btn;
    Spinner choose_grade_spinner;
    ArrayAdapter<CharSequence> grades_adapter;
    StorageReference item_image_ref;
    StorageReference file_path;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    EditText edit_author, edit_subject, edit_description;
    AutoCompleteTextView choose_book_edittxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_item2);

        choose_book_edittxt = findViewById(R.id.choose_book_edittxt);
        edit_author = findViewById(R.id.edit_author);
        edit_subject = findViewById(R.id.edit_subject);
        edit_description = findViewById(R.id.edit_description);
        Intent intent = getIntent();
        activity_type = intent.getStringExtra("activity_type");
        item_condition = intent.getStringExtra("item_condition");
        item_price = intent.getStringExtra("item_price");
        item_image_ref = FirebaseStorage.getInstance().getReference();
        back_btn = findViewById(R.id.back_btn);
        choose_grade_spinner = findViewById(R.id.choose_grade_spinner);
        grades_adapter = ArrayAdapter.createFromResource(this, R.array.grades_array, android.R.layout.simple_spinner_item);
        grades_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        choose_grade_spinner.setAdapter(grades_adapter);
        choose_grade_spinner.setOnItemSelectedListener(this);
        done_btn = findViewById(R.id.done_btn);
        done_btn.setEnabled(true);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                done_btn.setEnabled(false);
                UploadItem();
            }
        });
        choose_book_edittxt.setAdapter(MainActivity.adapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        item_grade = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void UploadItem() {
        item_upload_time = dateFormat.format(new Date());
        Item item_to_upload = new Item(active,
                activity_type,
                edit_author.getText().toString(),
                item_condition,
                edit_description.getText().toString(),
                item_grade,
                String.valueOf(SplashScreen.all_items.size()),
                choose_book_edittxt.getText().toString(),
                item_price,
                edit_subject.getText().toString(),
                item_upload_time,
                LoginActivity.current_user.getSchool(),
                LoginActivity.current_user.getPhone());
        SplashScreen.db.collection("items").add(item_to_upload);
        for (int i = 0; i < BooksCamera.images.length; i++) {
            if (BooksCamera.images[i] != null) {
                Uri uri = getImageUri(this, BooksCamera.images[i]);
                file_path = item_image_ref.child("items/" + item_to_upload.getItem_id() + "/" + i + ".jpg");
                file_path.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            BooksCamera.images = null;
                            BooksCamera.images = new Bitmap[3];
                            BooksCamera.current_image_index = 0;
                            Intent it = new Intent(UploadItem2.this, MainActivity.class);
                            startActivity(it);
                        }
                    }
                });
            }
        }
    }

    @Override
    protected void onPostResume() {
        done_btn.setEnabled(true);
        super.onPostResume();
    }

    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}
