package com.example.bmarket02;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {

    private TextView sign_up;
    public static EditText phone_edit, password_edit;
    private Button login_btn;
    public static User current_user = null;
    public static DocumentReference current_user_doc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        FirebaseApp.initializeApp(this);
        Permissions permissions = new Permissions(this);
        permissions.verifyPermissions();

        sign_up = findViewById(R.id.sign_up_txt);
        phone_edit = findViewById(R.id.phone_edit);
        password_edit = findViewById(R.id.edit_password);
        login_btn = findViewById(R.id.login_btn);

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(LoginActivity.this, RegisterPage1.class);
                startActivity(it);
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignIn();
            }
        });
    }

    public void SignIn()
    {
        final String check_phone = phone_edit.getText().toString();
        final String check_password = password_edit.getText().toString();

        SplashScreen.db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            int worked = 0;

                            for (QueryDocumentSnapshot document : task.getResult())
                            {
                                User check = document.toObject(User.class);

                                String phone = check.getPhone();
                                String password = check.getPassword();

                                if (phone.equals(check_phone) && (password.equals(check_password))) {
                                    current_user = check;
                                    current_user_doc = document.getReference();

                                    Intent it = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(it);
                                    worked = 1;
                                }
                            }
                            if(worked == 0) {
                                Toast.makeText(LoginActivity.this, "INFO INCORRECT", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "ERROR", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
