package model;

import java.sql.Timestamp;

public class Room {

    private int roomId;
    private String roomName;
    private int capacity;
    private String status;
    private Timestamp createdAt;

    public Room() {
    }

    public Room(int roomId, String roomName, int capacity, String status, Timestamp createdAt) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.capacity = capacity;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getRoomId() {
        return roomId;
    }
    
    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }
    
    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getCapacity() {
        return capacity;
    }
    
    public void setCapacity(int capacity) {
        this.capacity = capacity;
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
}
