package com.example.androidapp;

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
}
