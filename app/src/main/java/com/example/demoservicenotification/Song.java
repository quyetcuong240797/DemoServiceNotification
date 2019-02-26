package com.example.demoservicenotification;

public class Song {
    private int file;
    private String mNameSong;
    private String mArtisSong;

    public Song(int file, String mNameSong, String mArtisSong) {
        this.file = file;
        this.mNameSong = mNameSong;
        this.mArtisSong = mArtisSong;
    }

    public int getFile() {
        return file;
    }

    public void setFile(int file) {
        this.file = file;
    }

    public String getmNameSong() {
        return mNameSong;
    }

    public void setmNameSong(String mNameSong) {
        this.mNameSong = mNameSong;
    }

    public String getmArtisSong() {
        return mArtisSong;
    }

    public void setmArtisSong(String mArtisSong) {
        this.mArtisSong = mArtisSong;
    }
}
