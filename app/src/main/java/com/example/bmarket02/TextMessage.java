package com.example.bmarket02;

import java.util.Date;

public class TextMessage extends Message {
    private String text;

    public TextMessage(String message_id, String rx_id, String text, Date timestamp, String tx_id) {
        super(message_id, rx_id, timestamp, tx_id);
        this.text = text;
    }

    public TextMessage() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
