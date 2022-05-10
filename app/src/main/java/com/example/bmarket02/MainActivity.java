package com.example.bmarket02;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FloatingActionButton fab;
    AutoCompleteTextView search;
    ImageView search_bar_design, inbox_btn;
    ArrayList<String> books_list_names = new ArrayList<String>();
    ListView items_lst;
    TextView nav_name, nav_phone, inbox_notifications_counter;
    public static ArrayAdapter<String> adapter;
    CircleImageView nav_profile_image;
    final StorageReference ref = FirebaseStorage.getInstance().getReference().child("profile/" + LoginActivity.current_user.getPhone() + ".jpg");
    NavigationView navigationView;
    View hView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = findViewById(R.id.nav_view);
        hView = navigationView.getHeaderView(0);

        nav_name = hView.findViewById(R.id.nav_name);
        nav_name.setText(LoginActivity.current_user.getName());

        nav_phone = hView.findViewById(R.id.nav_phone);
        nav_phone.setText(LoginActivity.current_user.getPhone());

        nav_profile_image = hView.findViewById(R.id.nav_profile_image);

        inbox_btn = findViewById(R.id.inbox_btn);
        inbox_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MainActivity.this, InboxActivity.class);
                startActivity(it);
            }
        });

        inbox_notifications_counter = findViewById(R.id.inbox_notifications_counter);

        File localFile = null;
        try {
            localFile = File.createTempFile("image", "jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }

        final File finalLocalFile = localFile;
        ref.getFile(localFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        nav_profile_image.setImageBitmap(BitmapFactory.decodeFile(finalLocalFile.getPath()));
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });

        items_lst = findViewById(R.id.items_lst);
        items_lst.setAdapter(new ItemsListAdapter(this, SplashScreen.items));

        Book.InitializeBooksList1();
        Book.InitializeBooksList2();
        Book.InitializeBooksList3();

        // to delete duplicates
        Set<Book> set1 = new HashSet<>(Book.books_list);
        Book.books_list.clear();
        Book.books_list.addAll(set1);
        //---------------------

        fab = findViewById(R.id.fab);
        search = findViewById(R.id.search);
        search_bar_design = findViewById(R.id.search_bar_design);

        for (int i = 0; i < Book.books_list.size(); i++) {
            books_list_names.add(Book.books_list.get(i).getName());
        }

        // to delete duplicates
        Set<String> set2 = new HashSet<>(books_list_names);
        books_list_names.clear();
        books_list_names.addAll(set2);
        //---------------------

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, books_list_names);
        //search.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MainActivity.this, BooksCamera.class);
                startActivity(it);
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_settings);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (search.getText().toString().matches("")) {
                    search_bar_design.setImageResource(R.drawable.search_bar_design);
                } else {
                    search_bar_design.setImageResource(R.drawable.search_bar_empty_design);
                }

                ArrayList<Item> forSearch = new ArrayList<>();
                String searchText = search.getText().toString();
                for (Item item : SplashScreen.items) {
                    if (item.getItem_name().contains(searchText) || item.getItem_author().contains(searchText)) {
                        forSearch.add(item);
                    }
                }
                items_lst.setAdapter(new ItemsListAdapter(MainActivity.this, forSearch));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

       SplashScreen.db.collection("items").addSnapshotListener(new EventListener<QuerySnapshot>()
        {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e)
            {
                ((BaseAdapter) items_lst.getAdapter()).notifyDataSetChanged();
                String string = search.getText().toString();
                search.setText(string);
                if (search.getText().toString().matches("")) {
                    search_bar_design.setImageResource(R.drawable.search_bar_design);
                } else {
                    search_bar_design.setImageResource(R.drawable.search_bar_empty_design);
                }

                ArrayList<Item> forSearch = new ArrayList<>();
                String searchText = search.getText().toString();
                for (Item item : SplashScreen.items) {
                    if (item.getItem_name().contains(searchText) || item.getItem_author().contains(searchText)) {
                        forSearch.add(item);
                    }
                }
                items_lst.setAdapter(new ItemsListAdapter(MainActivity.this, forSearch));
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.filter_btn) {
            Toast.makeText(MainActivity.this, "Filter", Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.sort_by_price_lth) {
            SortByPriceLTH();
        } else if (id == R.id.sort_by_price_htl) {
            SortByPriceHTL();
        } else if (id == R.id.sort_by_time_nto) {
            SortByTimeNTO();
        } else if (id == R.id.sort_by_time_otn) {
            SortByTimeOTN();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            //Intent it = new Intent(MainActivity.this, MainActivity.class);
            //startActivity(it);
        } else if (id == R.id.nav_settings) {
            Intent it = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(it);
        } else if (id == R.id.nav_my_items) {
            Intent it = new Intent(MainActivity.this, MyItemsActivity.class);
            startActivity(it);
        } else if (id == R.id.nav_saved_items) {
            Intent it = new Intent(MainActivity.this, SavedItemsActivity.class);
            startActivity(it);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        ((BaseAdapter) items_lst.getAdapter()).notifyDataSetChanged();
        //items_lst.setAdapter(new ItemsListAdapter(this, SplashScreen.items));
        search.setText("");
    }

    public void SortByPriceLTH() {
        ArrayList<Item> temp = new ArrayList<>(SplashScreen.items);
        Collections.sort(temp);
        items_lst.setAdapter(new ItemsListAdapter(this, temp));
    }

    public void SortByPriceHTL() {
        ArrayList<Item> temp = new ArrayList<>(SplashScreen.items);
        Collections.sort(temp);
        Collections.reverse(temp);
        items_lst.setAdapter(new ItemsListAdapter(this, temp));
    }

    public void SortByTimeNTO() {
        ArrayList<Item> temp = new ArrayList<>(SplashScreen.items);
        Collections.reverse(temp);
        items_lst.setAdapter(new ItemsListAdapter(this, temp));
    }

    public void SortByTimeOTN() {
        ArrayList<Item> temp = new ArrayList<>(SplashScreen.items);
        items_lst.setAdapter(new ItemsListAdapter(this, temp));
    }
}
