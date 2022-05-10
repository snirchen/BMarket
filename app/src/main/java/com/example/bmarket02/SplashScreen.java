package com.example.bmarket02;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class SplashScreen extends AppCompatActivity
{
    public static FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static ArrayList<Item> items;
    public static ArrayList<Item> all_items;
    public static List<Message> all_messages;
    public static MessagesManager messagesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        all_items = new ArrayList<>();
        items = new ArrayList<>();
        messagesManager = new MessagesManager();
        all_messages = messagesManager.getMessages();

        db.collection("items").addSnapshotListener(new EventListener<QuerySnapshot>()
        {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e)
            {
                db.collection("items").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        ArrayList<Item> temp = new ArrayList<>();
                        items.clear();
                        all_items.clear();
                        temp.addAll(task.getResult().toObjects(Item.class));
                        for (Item item : temp)
                        {
                            all_items.add(item);
                            if (item.getActive())
                                items.add(item);
                        }
                    }
                });
            }
        });
        Intent it = new Intent(SplashScreen.this, LoginActivity.class);
        startActivity(it);
    }
}