package com.example.androidapp.database;

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
    Word getRandomWord();

    /**
     * Gibt die Anzahl der Wörter in der Datnbank zurück.
     *
     * @return Anzahl Wörter
     */
    int size();
}
