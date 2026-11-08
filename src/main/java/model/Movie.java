package model;

import java.sql.Date;
import java.sql.Timestamp;

public class Movie {

    private int movieId;
    private String title;
    private String genre;
    private int duration;
    private String description;
    private Date releaseDate;
    private String status;
    private Timestamp createdAt;
    private String poster;
    
    public Movie() {
    }

    public Movie(int movieId, String title, String genre, int duration,
                 String description, Date releaseDate, String status, Timestamp createdAt) {
        this.movieId = movieId;
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.description = description;
        this.releaseDate = releaseDate;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }
    
    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }
    
    
}