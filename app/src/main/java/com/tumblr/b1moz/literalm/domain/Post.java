package com.tumblr.b1moz.literalm.domain;

import com.google.firebase.database.Exclude;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Post {
    
    private String title;
    private String author;
    private long timestamp;
    private String key;
    private String imageUrl;
    
    public Post() {
    }
    
    public Post(String author, String title, String imageUrl) {
        this.author = author;
        this.title = title;
        this.imageUrl = imageUrl;
        timestamp = Calendar.getInstance().getTimeInMillis();
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    
    @Exclude
    public String getKey() {
        return key;
    }
    
    @Exclude
    public void setKey(String key) {
        this.key = key;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    @Exclude
    public String getDate() {
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(timestamp);
        String date = calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) +
                1) + "/" + calendar.get(Calendar.YEAR);
        return date;
    }
    
}
