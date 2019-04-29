package com.example.androidapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * DataAccessObject für unsere Wörter. Damit wird der Zugriff auf die Datenbank
 * gehandhabt. Wird durch Room mittels MAGIE bereits automatisch implementiert.
 */
@Dao
public interface WordDao {

    /**
     * Gibt alle Wörter aus der Datenkbank aus.
     *
     * @return Liste aller Wörter
     */
    @Query("SELECT * FROM word")
    List<Word> getAll();

    /**
     * Gibt alle Wörter zurück, dessen ID mit der gesuchten übereinstimmt.
     *
     * @param words Stringarray mit allen gesuchten Wörtern
     * @return Liste aller gesuchten Wörter
     */
    @Query("SELECT * FROM word WHERE word IN (:words)")
    List<Word> loadAllByIds(String[] words);

    /**
     * Gibt das gesuchte Wort zurück.
     *
     * @param word String mit ID des gesuchten Wortes
     * @return Gesuchtes Wort
     */
    @Query("SELECT * FROM word WHERE word = (:word)")
    Word loadById(String word);

    /**
     * Fügt die einzufügenden Wörter ein.
     *
     * @param words Einzufügende Wörter
     */
    @Insert
    void insertAll(Word... words);

    /**
     * Löscht das zu löschende Wort.
     *
     * @param word Zu löschendes Wort
     */
    @Delete
    void delete(Word word);

}
