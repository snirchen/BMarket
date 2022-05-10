package com.example.bmarket02;

import java.util.Date;
import java.sql.Timestamp;

public class Message {
    private String message_id, rx_id;
    private Date timestamp;
    private String tx_id;

    public Message(String message_id, String rx_id, Date timestamp, String tx_id) {
        this.message_id = message_id;
        this.rx_id = rx_id;
        this.timestamp = timestamp;
        this.tx_id = tx_id;
    }

    public Message() {
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getRx_id() {
        return rx_id;
    }

    public void setRx_id(String rx_id) {
        this.rx_id = rx_id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getTx_id() {
        return tx_id;
    }

    public void setTx_id(String tx_id) {
        this.tx_id = tx_id;
    }
}
