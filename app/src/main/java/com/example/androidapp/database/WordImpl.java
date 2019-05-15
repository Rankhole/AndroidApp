package com.example.androidapp.database;

import java.util.List;

public class WordImpl implements Word {

    private String word;
    private List<String> forbiddenWords;

    WordImpl(String word, List<String> forbiddenWords) throws Exception {
        if (word.equals("") || word == null || forbiddenWords == null)
            throw new Exception("The word or forbidden words cannot be null or empty!");
        if (forbiddenWords.size() != 4)
            throw new Exception("There must be four forbidden words! Actual: " + forbiddenWords.size());
        this.word = word;
        this.forbiddenWords = forbiddenWords;
    }

    @Override
    public List<String> getForbiddenWords() {
        return this.forbiddenWords;
    }

    @Override
    public String getWord() {
        return this.word;
    }
}
