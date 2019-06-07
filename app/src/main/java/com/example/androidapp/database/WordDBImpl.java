package com.example.androidapp.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

public class WordDBImpl implements WordDB {

    private List<Word> words;

    public WordDBImpl() {
        words = new ArrayList<>();
    }

    @Override
    public void add(Word word) {
        words.add(word);
    }

    @Override
    public Word getRandomWord() {
        return words.get(new Random().nextInt(words.size()));
    }

    @Override
    public int size() {
        return words.size();
    }


}
