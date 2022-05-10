package com.example.bmarket02;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyItemsActivity extends AppCompatActivity {

    ImageButton back_btn;
    ListView items_lst;
    TextView no_items_txt;
    ArrayList<Item> my_items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_items);

        no_items_txt = findViewById(R.id.no_items_txt);
        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        items_lst = findViewById(R.id.items_lst);
        for (Item item : SplashScreen.items)
        {
            if(LoginActivity.current_user.getPhone().equals(item.getUser_phone())) {
                my_items.add(item);
            }
        }

        if(my_items.isEmpty())
        {
            no_items_txt.setVisibility(View.VISIBLE);
        }
        items_lst.setAdapter(new ItemsListAdapter(this, my_items));
    }
}
