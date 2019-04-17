package com.example.androidapp;

import java.util.List;

public interface WordDB {
    /**
     * fuegt neues Wort zur Datenbank hinzu
     * @param word
     */
    public void add(String word);

    /**
     * gibt zufaelliges Wort aus der Datenbank zurueck
     * @return zufaelliges Wort
     */
    public String getRandomWord();

    /**
     * gibt zu einem Begriff Liste von Woerten zurueck, die nicht erwaehnt werden duerfen
     * @param word  Begriff zu welchem man die Liste haben moechte
     * @return  Liste an Woertern
     */
    public List<String> getForbiddenWords(String word);
}
