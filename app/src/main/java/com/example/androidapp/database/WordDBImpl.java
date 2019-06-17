package com.example.androidapp.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    public Word getRandomWord() throws Exception {
        if (size() == 0)
            throw new Exception("No words in database! Can't pick random one.");
        return words.get(new Random().nextInt(words.size()));
    }

    @Override
    public int size() {
        return words.size();
    }

    @Override
    public List<Word> getList() {
        return this.words;
    }

    @Override
    public void setList(List<Word> list) {
        this.words = list;
    }


}
