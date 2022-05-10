package com.example.bmarket02;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class RegisterPage4 extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    private Button register_btn;
    Spinner choose_grade_spinner;
    AutoCompleteTextView choose_school_edittxt;
    AlertDialog profile_dialog;
    ArrayList<String> schools_list_names = new ArrayList<String>();
    ArrayAdapter<CharSequence> grades_adapter;
    String grade_chosen;
    ImageButton back_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page4);

        choose_school_edittxt = findViewById(R.id.choose_book_edittxt);
        register_btn = findViewById(R.id.register_btn);
        choose_grade_spinner = findViewById(R.id.choose_grade_spinner);
        grades_adapter = ArrayAdapter.createFromResource(this, R.array.grades_array, android.R.layout.simple_spinner_item);
        grades_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        choose_grade_spinner.setAdapter(grades_adapter);
        choose_grade_spinner.setOnItemSelectedListener(this);
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserRegister();
            }
        });

        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        School.InitializeSchoolsList();
        Set<School> set = new HashSet<>(School.schools_list);
        School.schools_list.clear();
        School.schools_list.addAll(set);

        for (int i = 0; i < School.schools_list.size(); i++) {
            schools_list_names.add(School.schools_list.get(i).getName() + " - " + School.schools_list.get(i).getId());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, schools_list_names);
        choose_school_edittxt.setAdapter(adapter);

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        grade_chosen = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }


    private void UserRegister() {
        User user_for_registration = new User("Hey there ! I'm using Bmarket :)", RegisterPage1.country, grade_chosen, RegisterPage3.name, RegisterPage3.password, RegisterPage1.phone, choose_school_edittxt.getText().toString(), String.valueOf(1));
        InsertInfoToDb(user_for_registration);
    }


    private void InsertInfoToDb(User user_to_insert) {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.profile_create_dialog, null);
        AlertDialog.Builder show = new AlertDialog.Builder(RegisterPage4.this);
        show.setView(alertLayout);
        show.setCancelable(false);
        profile_dialog = show.create();
        profile_dialog.show();

        SplashScreen.db.collection("users").add(user_to_insert).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        profile_dialog.dismiss();
                        MakeNotification();
                        Intent it = new Intent(RegisterPage4.this, LoginActivity.class);
                        startActivity(it);
                    }
                }, 6000);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterPage4.this, "WHA WHA....!", Toast.LENGTH_SHORT).show();
            }
        });

        RegisterPage3.file_path.putFile(RegisterPage3.mImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                if (task.isSuccessful())
                {
                }
                else
                {
                    Toast.makeText(RegisterPage4.this, "upload image got wrong :(", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void MakeNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = "test";
            String description = "test";
            int importance = NotificationManager.IMPORTANCE_HIGH; //Important for heads-up notification
            NotificationChannel channel = new NotificationChannel("1", name, importance);
            channel.setDescription(description);
            channel.setShowBadge(true);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "1")
                .setSmallIcon(R.drawable.googleg_disabled_color_18)
                .setContentTitle(RegisterPage3.name + " just registered,")
                .setContentText("Thank you !")
                // .setDefaults(DEFAULT_SOUND | DEFAULT_VIBRATE) //Important for heads-up notification
                .setPriority(Notification.PRIORITY_MAX); //Important for heads-up notification

        // Register channel with system
        Notification buildNotification = mBuilder.build();
        NotificationManager mNotifyMgr = (NotificationManager) getApplication().getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(001, buildNotification);
    }
}
