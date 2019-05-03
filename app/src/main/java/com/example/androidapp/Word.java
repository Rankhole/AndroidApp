package com.example.androidapp;

import java.util.List;

/**
 * Interface für ein Wort.
 */
public interface Word {

    /**
     * Liefert alle Tabu Wörter für das Wort zurück.
     *
     * @return Liste mit Strings aller Tabu Wörter
     */
    List<String> getForbiddenWords();

    /**
     * Liefert das Wort selbst als String zurück.
     * @return String des Wortes
     */
    String getWord();

}
