package com.example.pomodorogothic.ui.database;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

public interface SessionDAO {
    @Insert
    long insertSession(Session session);
    @Update
    void updateSession(Session session);
    @Delete
    void deleteSession(Session session);

    @Query("SELECT * FROM archives ORDER BY timestamp DESC")
    List<Session> getAllSessions();

    @Query("SELECT * FROM archives WHERE sessionID = :id")
    Session getSessionByID(int id);
}
