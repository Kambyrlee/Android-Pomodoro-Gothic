package com.example.pomodorogothic.ui.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;

@Entity(tableName = "archives")
public class Session {
    @PrimaryKey(autoGenerate = true)
    private int sessionID;
    private LocalDateTime timestamp;
    private int roundsCompleted;
    private int workDuration;
    private int shortRestDuration;
    private int longRestDuration;
    public String sessionNotes;

    public Session(LocalDateTime timestamp, int roundsCompleted, int workDuration, int shortRestDuration, int longRestDuration, String sessionNotes) {
        this.timestamp = timestamp;
        this.roundsCompleted = roundsCompleted;
        this.workDuration = workDuration;
        this.shortRestDuration = shortRestDuration;
        this.longRestDuration = longRestDuration;
        this.sessionNotes = sessionNotes;
    }

    public int getSessionID() { return sessionID; }
    public void setSessionID(int sessionID) { this.sessionID = sessionID; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public int getRoundsCompleted() { return roundsCompleted; }
    public void setRoundsCompleted(int roundsCompleted) { this.roundsCompleted = roundsCompleted; }
    public int getWorkDuration() { return workDuration; }
    public void setWorkDuration(int workDuration) { this.workDuration = workDuration; }
    public int getShortRestDuration() { return shortRestDuration; }
    public void setShortRestDuration(int shortRestDuration) { this.shortRestDuration = shortRestDuration; }
    public int getLongRestDuration() { return longRestDuration; }
    public void setLongRestDuration(int longRestDuration) { this.longRestDuration = longRestDuration; }
    public String getSessionNotes() { return sessionNotes; }
    public void setSessionNotes(String sessionNotes) { this.sessionNotes = sessionNotes; }
}
