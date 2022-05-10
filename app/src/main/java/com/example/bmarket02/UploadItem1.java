package com.example.bmarket02;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class UploadItem1 extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ImageButton back_btn;
    Button next_btn;
    String item_condition;
    String activity_type = "למכירה";
    Spinner choose_condition_spinner;
    ArrayAdapter<CharSequence> condition_adapter;
    ImageView radio_sell, radio_buy;
    EditText edit_price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_item1);
        edit_price = findViewById(R.id.edit_price);
        choose_condition_spinner = findViewById(R.id.choose_condition_spinner);
        choose_condition_spinner.setOnItemSelectedListener(this);
        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        next_btn = findViewById(R.id.next_btn);
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(UploadItem1.this, UploadItem2.class);
                it.putExtra("activity_type", activity_type);
                it.putExtra("item_condition", item_condition);
                it.putExtra("item_price", edit_price.getText().toString());
                startActivity(it);
            }
        });
        condition_adapter = ArrayAdapter.createFromResource(this, R.array.condition_array, android.R.layout.simple_spinner_item);
        condition_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        choose_condition_spinner.setAdapter(condition_adapter);

        radio_sell = findViewById(R.id.radio_sell);
        radio_sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity_type = "למכירה";
                radio_sell.setImageResource(R.drawable.ic_sell_radio_filled);
                radio_buy.setImageResource(R.drawable.ic_buy_radio_not_filled);
            }
        });

        radio_buy = findViewById(R.id.radio_buy);
        radio_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity_type = "דרוש";
                radio_buy.setImageResource(R.drawable.ic_buy_radio_filled);
                radio_sell.setImageResource(R.drawable.ic_sell_radio_not_filled);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        item_condition = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
