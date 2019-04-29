package com.example.androidapp;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Word.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract WordDao wordDao();

}
