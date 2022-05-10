package com.example.bmarket02;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SavedItemsActivity extends AppCompatActivity {

    ImageButton back_btn;
    ListView items_lst;
    TextView no_items_txt;
    ArrayList<Item> saved_items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_items);

        no_items_txt = findViewById(R.id.no_items_txt);
        back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        items_lst = findViewById(R.id.items_lst);


        SplashScreen.db.collection("saved").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                ArrayList<Saved> temp = new ArrayList<>();
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    temp.addAll(querySnapshot.toObjects(Saved.class));
                    for (Saved saved : temp) {
                        if (saved.getUser_phone().equals(LoginActivity.current_user.getPhone())) {
                            for (Item item : SplashScreen.items) {
                                if (item.getItem_id().equals(saved.getItem_id())) {
                                    saved_items.add(item);
                                }
                            }
                        }
                    }
                }

                if (saved_items.isEmpty()) {
                    no_items_txt.setVisibility(View.VISIBLE);
                }
                items_lst.setAdapter(new ItemsListAdapter(SavedItemsActivity.this, saved_items));
            }
        });
    }
}

