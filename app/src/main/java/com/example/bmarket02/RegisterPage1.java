package com.example.bmarket02;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mukesh.countrypicker.Country;
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.OnCountryPickerListener;

public class RegisterPage1 extends AppCompatActivity implements OnCountryPickerListener {

    ImageView country_flag_imageView;
    TextView country_name_textView, country_dial_textView;
    ImageButton country_picker_button, next_btn;
    ImageButton back_btn;
    EditText phone_number_edittext;
    CountryPicker countryPicker;
    private Boolean mVerificationInProgress = false;
    private  PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;
    public static String country, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page1);

        country_flag_imageView = findViewById(R.id.country_flag_imageView);
        country_name_textView = findViewById(R.id.country_name_textView);
        country_dial_textView = findViewById(R.id.country_dial_textView);
        country_picker_button = findViewById(R.id.country_picker_button);
        phone_number_edittext = findViewById(R.id.phone_edit);
        next_btn = findViewById(R.id.edit_image_btn);

        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        countryPicker = new CountryPicker.Builder().with(this).listener(this).build();
        country_picker_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countryPicker.showDialog(getSupportFragmentManager());
            }
        });

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NextPageAttempt();
            }
        });
    }

    @Override
    public void onSelectCountry(Country country) {
        country_flag_imageView.setImageResource(country.getFlag());
        country_name_textView.setText(country.getName());
        country_dial_textView.setText(country.getDialCode());
    }

    private void NextPageAttempt()
    {
        phone = country_dial_textView.getText().toString() + phone_number_edittext.getText().toString();
        country = country_name_textView.getText().toString();

        Intent intent = new Intent(RegisterPage1.this, RegisterPage3.class);
        startActivity(intent);
    }
}
