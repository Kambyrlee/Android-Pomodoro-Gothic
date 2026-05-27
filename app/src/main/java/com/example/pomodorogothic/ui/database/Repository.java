package com.example.pomodorogothic.ui.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class Repository {
    private final SessionDAO sessionDAO;
    private final ExecutorService executor;

    public Repository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        sessionDAO = db.sessionDAO();
        executor = Executors.newFixedThreadPool(4);
    }

    public LiveData<List<Session>> getAllSessions() {
        return sessionDAO.getAllSessions();
    }

    public void insertSession(Session session, Consumer<Long> callback) {
        executor.execute(() -> {
            long id = sessionDAO.insertSession(session);
            if (callback != null) {
                callback.accept(id);
            }
        });
    }
}
