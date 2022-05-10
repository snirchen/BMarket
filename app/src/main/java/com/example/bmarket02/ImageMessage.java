package com.example.bmarket02;

import java.util.Date;

public class ImageMessage extends Message {
    private String image_file_path;

    public ImageMessage(String message_id, String rx_id, String image_file_path, Date timestamp, String tx_id) {
        super(message_id, rx_id, timestamp, tx_id);
        this.image_file_path = image_file_path;
    }

    public ImageMessage() {
    }

    public String getImageFilePath() {
        return image_file_path;
    }

    public void setImageFilePath(String image_file_path) {
        this.image_file_path = image_file_path;
    }
}
