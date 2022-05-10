package com.example.bmarket02;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

class ItemsListAdapter extends BaseAdapter {

    private Activity context;
    TextView itemTitle, itemAuthor, itemPrice;
    StorageReference ref;
    ArrayList<Item> items;

    public ItemsListAdapter(Activity context, ArrayList<Item> items) {
        this.context = context;
        this.items = (ArrayList<Item>) items.clone();
        this.items.add(new Item());
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Item getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Item currentItem = getItem(i);
        if (i == getCount() - 1/*currentItem.getItem_name() == null*/) {
            //Toast.makeText(context, "This is a test toast", Toast.LENGTH_SHORT).show();
            return context.getLayoutInflater().inflate(R.layout.item_row_buffer, null);
        } else {
            View itemRowView = context.getLayoutInflater().inflate(R.layout.item_row, null);
            ImageView item_background = itemRowView.findViewById(R.id.item_background);
            final ImageView itemImage = itemRowView.findViewById(R.id.item_image);
            itemImage.setImageResource(R.drawable.ic_default_item_image);

            ref = FirebaseStorage.getInstance().getReference().child("items/" + currentItem.getItem_id() + "/" + "0" + ".jpg");
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
                            itemImage.setImageBitmap(BitmapFactory.decodeFile(finalLocalFile.getPath()));
                        }
                    });

            final ImageView item_bookmark = itemRowView.findViewById(R.id.item_bookmark);
            final Saved saved = new Saved(currentItem.getItem_id(), LoginActivity.current_user.getPhone());
            final ArrayList<Boolean> bookmarked = new ArrayList<>();
            bookmarked.add(false);
            itemTitle = itemRowView.findViewById(R.id.name);
            itemAuthor = itemRowView.findViewById(R.id.message);
            itemPrice = itemRowView.findViewById(R.id.item_price_txt);
            itemTitle.setText(currentItem.getItem_name());
            itemAuthor.setText(currentItem.getItem_author());
            itemPrice.setText("₪" + currentItem.getItem_price());

            UpdateSaved(item_bookmark, currentItem, bookmarked);

            itemImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    View popupView = context.getLayoutInflater().inflate(R.layout.show_item_image_dialog, null);
                    ((ImageView) popupView.findViewById(R.id.item_image)).setImageDrawable(itemImage.getDrawable());
                    builder.setView(popupView);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    //alertDialog.getWindow().setLayout(itemImage.getWidth(), itemImage.getHeight());
                    alertDialog.getWindow().setLayout((int) (1024 / Math.sqrt(2)), 1024);
                }
            });

            item_background.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ItemInfoActivity.class);
                    intent.putExtra("current_item", currentItem);
                    context.startActivity(intent);
                }
            });

            item_bookmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (bookmarked.get(0)) {
                        item_bookmark.setImageResource(R.drawable.ic_bookmark_not_filled);
                    } else {
                        item_bookmark.setImageResource(R.drawable.ic_bookmark_filled);
                    }
                    ToggleSaved(item_bookmark, currentItem, saved, bookmarked);
                }
            });
            return itemRowView;
        }
    }


    public void ToggleSaved(final ImageView item_bookmark, final Item currentItem, final Saved saved, final ArrayList<Boolean> bookmarked) {
        item_bookmark.setEnabled(false);
        SplashScreen.db.collection("saved").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.get("item_id").toString().equals(currentItem.getItem_id())
                                && document.get("user_phone").toString().equals(LoginActivity.current_user.getPhone())) {
                            DeleteDocument(document.getId());
                            break;
                        }
                    }
                    if (!bookmarked.get(0)) {
                        AddDocument(saved);
                    }
                }
                UpdateSaved(item_bookmark, currentItem, bookmarked);
            }
        });
    }

    public static void UpdateSaved(final ImageView item_bookmark, final Item currentItem, final ArrayList<Boolean> bookmarked) {
        SplashScreen.db.collection("saved").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Boolean exists = false;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.get("item_id").toString().equals(currentItem.getItem_id())
                                && document.get("user_phone").toString().equals(LoginActivity.current_user.getPhone())) {
                            item_bookmark.setImageResource(R.drawable.ic_bookmark_filled);
                            exists = true;
                            bookmarked.set(0, true);
                            //bookmarked = true;
                            break;
                        }
                    }
                    if (!exists) {
                        item_bookmark.setImageResource(R.drawable.ic_bookmark_not_filled);
                        bookmarked.set(0, false);
                        //bookmarked = false;
                    }
                    item_bookmark.setEnabled(true);
                }
            }
        });

    }

    public void DeleteDocument(String id) {
        SplashScreen.db.collection("saved").document(id).delete();
    }

    public void AddDocument(Saved saved) {
        SplashScreen.db.collection("saved").add(saved);
    }
}

