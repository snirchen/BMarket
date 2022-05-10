package com.example.bmarket02;

import java.util.Date;

public class VoiceMessage extends Message {
    private String voice_file_path;

    public VoiceMessage(String message_id, String rx_id, String voice_file_path, Date timestamp, String tx_id) {
        super(message_id, rx_id, timestamp, tx_id);
        this.voice_file_path = voice_file_path;
    }

    public VoiceMessage() {
    }

    public String getVoiceFilePath() {
        return voice_file_path;
    }

    public void setVoiceFilePath(String voice_file_path) {
        this.voice_file_path = voice_file_path;
    }
}
