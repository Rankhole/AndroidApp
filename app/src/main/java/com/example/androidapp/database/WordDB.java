package com.example.androidapp.database;

import java.util.List;

public interface WordDB {
    /**
     * Fuegt neues Wort zur Datenbank hinzu.
     * @param word Hinzuzufügendes Wort
     */
    void add(Word word);

    /**
     * Gibt zufaelliges Wort aus der Datenbank zurueck.
     * @return Zufaelliges Wort
     */
    Word getRandomWord() throws Exception;

    /**
     * Gibt die Anzahl der Wörter in der Datnbank zurück.
     *
     * @return Anzahl Wörter
     */
    int size();

    /**
     * Gibt die Liste der Wörter zurück.
     *
     * @return Liste der Wörter
     */
    List<Word> getList();

    /**
     * Setzt die Liste der Wörter.
     *
     * @param list Liste der Wörter
     */
    void setList(List<Word> list);
}
