package com.example.androidapp;

import com.example.androidapp.database.Word;
import com.example.androidapp.database.WordImpl;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class WordImplTest {

    @Test
    public void constructorTest() {
        Word testWord;
        try {
            testWord = new WordImpl("", new ArrayList<String>());
            fail("No exception thrown!");
        } catch (Exception e) {
            //all good
        }
        try {
            testWord = new WordImpl(null, new ArrayList<String>());
            fail("No exception thrown!");
        } catch (Exception e) {
            //all good
        }
        try {
            testWord = new WordImpl("Eminem", new ArrayList<String>());
            fail("No exception thrown!");
        } catch (Exception e) {
            //all good
        }
        try {
            List<String> testList = new ArrayList<>();
            testList.add("Tupac");
            testList.add("Starlord");
            testList.add("Trump");
            testList.add("Rapper");
            testWord = new WordImpl("Eminem", testList);
        } catch (Exception e) {
            fail("This should have worked!");
        }
    }

    @Test
    public void getForbiddenWords() {
        List<String> testList = new ArrayList<>();
        testList.add("Tupac");
        testList.add("Starlord");
        testList.add("Trump");
        testList.add("Rapper");
        Word word = null;
        try {
            word = new WordImpl("Madagaskar", testList);
        } catch (Exception e) {
            e.printStackTrace();
            fail("This should have worked!");
        }
        List<String> actual = word.getForbiddenWords();
        assertEquals(actual.size(), testList.size());
        for (int i = 0; i < actual.size(); i++) {
            assertEquals(actual.get(i), testList.get(i));
        }
    }

    @Test
    public void getWord() {
        List<String> testList = new ArrayList<>();
        testList.add("Tupac");
        testList.add("Starlord");
        testList.add("Trump");
        testList.add("Rapper");
        Word word = null;
        String wordString = "Johan";
        try {
            word = new WordImpl(wordString, testList);
        } catch (Exception e) {
            e.printStackTrace();
            fail("This should have worked!");
        }

        String actual = word.getWord();
        assertEquals(actual, wordString);
    }
}