package com.example.bmarket02;

import android.app.Activity;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MessageListAdapter extends BaseAdapter
{

    private Activity context;
    ArrayList<Message> messages;

    public MessageListAdapter(Activity context, ArrayList<Message> messages)
    {
        this.context = context;
        this.messages = (ArrayList<Message>) messages.clone();
    }

    public ArrayList<Message> getMessages()
    {
        return messages;
    }

    @Override
    public int getCount()
    {
        return messages.size();
    }

    @Override
    public Message getItem(int i)
    {
        return messages.get(i);
    }

    @Override
    public long getItemId(int i)
    {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        Message message = getItem(i);
        View bubble = null;

        if (message instanceof TextMessage)
        {
            if (message.getRx_id().equals(LoginActivity.current_user.getPhone())) // User is the receiver
            {
                bubble = context.getLayoutInflater().inflate(R.layout.chat_message_text_left, null);
            } else if (message.getTx_id().equals(LoginActivity.current_user.getPhone())) // User is the sender
            {
                bubble = context.getLayoutInflater().inflate(R.layout.chat_message_text_right, null);
            }
            TextView txt = bubble.findViewById(R.id.txt);
            TextMessage textMessage = (TextMessage) message;
            txt.setText(textMessage.getText());
        }/*
        else if (message instanceof ImageMessage) {
            if (message.getRx_id().equals(LoginActivity.current_user.getPhone())) {
                // User is the receiver
                //bubble = context.getLayoutInflater().inflate(R.layout.chat_message_image_left, null);
            } else if (message.getTx_id().equals(LoginActivity.current_user.getPhone())) {
                // User is the sender
                bubble = context.getLayoutInflater().inflate(R.layout.chat_message_image_right, null);
            }
            ImageMessage imageMessage = (ImageMessage) message;
            ImageView img = bubble.findViewById(R.id.img);
            // Get the image
            // imageMessage.getImageFilePath();

        } else {
            // Voice Message
            if (message.getRx_id().equals(LoginActivity.current_user.getPhone())) {
                // User is the receiver
                bubble = context.getLayoutInflater().inflate(R.layout.chat_message_voice_left, null);
            } else if (message.getTx_id().equals(LoginActivity.current_user.getPhone())) {
                // User is the sender
                bubble = context.getLayoutInflater().inflate(R.layout.chat_message_voice_right, null);
            }
            VoiceMessage voiceMessage = (VoiceMessage) message;

            // Get the voice recording
            // voiceMessage.getVoiceFilePath();
        }*/
        final View finalTxtBubble = bubble.findViewById(R.id.txt);
        final View finalBubble = bubble;
        finalTxtBubble.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View view)
            {
                finalBubble.setBackgroundColor(Color.argb(255, 179, 224, 255));
                return true;
            }
        });

        return bubble;
    }
}
