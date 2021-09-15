package com.example.cs571hw9;

public class ReviewInfo {
    private String author;
    private String date;
    private String score;
    private String content;

    public ReviewInfo(String author, String date, String score, String content) {
        this.author = author;
        this.date = date;
        this.score = score;
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
