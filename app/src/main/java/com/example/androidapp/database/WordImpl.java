package com.example.androidapp.database;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class WordImpl implements Word {

    private String word;
    private List<String> forbiddenWords;

    public WordImpl(String word, List<String> forbiddenWords) throws Exception {
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


    public Word getWordFromString(String wordString){
        StringTokenizer stringTokenizer= new StringTokenizer(wordString,"||");
        String word=stringTokenizer.nextToken();
        List<String> forbiddenWords= new ArrayList<>();
        while(stringTokenizer.hasMoreTokens()){
            forbiddenWords.add(stringTokenizer.nextToken());
        }
        Word temp=null;
        try {
            temp=new WordImpl(word, forbiddenWords);
        } catch (Exception e) {
        }
        return temp;
    }

    public String getStringOfWord(){
        String wordString=this.getWord()+"||";
        List<String> forbiddenWords=this.getForbiddenWords();
        for(int i=0; i<forbiddenWords.size();i++){
            wordString=wordString+forbiddenWords.get(i)+"||";

        }
        return wordString;
    }
}
