package com.example.androidapp;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Simple Testklasse. Testet Room Implementation unserer Datenbank.
 */
@RunWith(AndroidJUnit4.class)
public class SimpleEntityReadWriteTest {
    private WordDao wordDao;
    private AppDatabase db;

    /**
     * Erstellt die Datenbank
     */
    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        wordDao = db.wordDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void writeAndReadWord() throws Exception {
        Word word = new Word();
        word.word = "Auto";
        word.firstTabu = "fahren";
        word.secondTabu = "Transport";
        word.thirdTabu = "Wagen";
        word.fourthTabu = "Reifen";
        wordDao.insertAll(word);
        Word storedWord = wordDao.loadById("Auto");
        assertNotNull(storedWord);
        assertTrue(storedWord.equals(word));
    }
}