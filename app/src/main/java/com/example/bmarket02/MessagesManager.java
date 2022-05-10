package com.example.bmarket02;

import android.app.Activity;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class MessagesManager
{

    private OnMessagesChanged onMessagesChanged = new OnMessagesChanged()
    {
        @Override
        public void onMessagesChanged(List<Message> newMessages)
        {

        }
    };
    private List<Message> messages;

    public List<Message> getMessages()
    {
        return messages;
    }

    public MessagesManager()
    {
        this.messages = new ArrayList<>();
        SplashScreen.db.collection("messages").orderBy("timestamp", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>()
        {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e)
            {
                messages.clear();
                QuerySnapshot querySnapshot = queryDocumentSnapshots;
                if (querySnapshot != null)
                {
                    for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments())
                    {
                        Message messageToAdd = null;
                        if (documentSnapshot.contains("text"))
                        {
                            messageToAdd = documentSnapshot.toObject(TextMessage.class);
                        } else if (documentSnapshot.contains("voice_file_path"))
                        {
                            messageToAdd = documentSnapshot.toObject(VoiceMessage.class);
                        } else if (documentSnapshot.contains("image_file_path"))
                        {
                            messageToAdd = documentSnapshot.toObject(ImageMessage.class);
                        }
                        if (messageToAdd != null)
                            messages.add(messageToAdd);
                    }
                    onMessagesChanged.onMessagesChanged(messages);
                }
            }
        });
    }

    public void setOnMessagesChanged(OnMessagesChanged onMessagesChanged)
    {
        this.onMessagesChanged = onMessagesChanged;
    }
}
