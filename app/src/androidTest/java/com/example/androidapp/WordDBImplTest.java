package com.example.androidapp;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class WordDBImplTest {

    /**
     * Testet das hinzufügen eines Wortes in die Liste.
     */
    @Test
    public void add() {
        WordDB db = new WordDBImpl();
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
        db.add(word);
        assertEquals(1, db.size());
        assertEquals(db.getRandomWord(), word);
    }

    /**
     * Testet, ob tatsächlich ein zufälliges Wort zurück gegeben wird.
     */
    @Test
    public void getRandomWord() {
        WordDB db = new WordDBImpl();
        List<String> testList = new ArrayList<>();
        testList.add("Tupac");
        testList.add("Starlord");
        testList.add("Trump");
        testList.add("Rapper");
        Word word = null;
        Word word2 = null;
        Word word3 = null;
        try {
            word = new WordImpl("Euler", testList);
            word2 = new WordImpl("Helium", testList);
            word3 = new WordImpl("Qt", testList);
        } catch (Exception e) {
            e.printStackTrace();
            fail("This should have worked!");
        }
        db.add(word);
        db.add(word2);
        db.add(word3);
        boolean w1 = false, w2 = false, w3 = false;
        int c1 = 0, c2 = 0, c3 = 0;
        //Um sicher zu gehen, dass alle Wörter mal dran kamen
        for (int i = 0; i < 1000; i++) {
            Word returned = db.getRandomWord();
            if (returned.equals(word)) {
                w1 = true;
                c1++;
            } else if (returned.equals(word2)) {
                w2 = true;
                c2++;
            } else if (returned.equals(word3)) {
                w3 = true;
                c3++;
            } else {
                fail("Whops! This shouldn't happen!");
            }
        }
        //Pseudo-Gleichverteilung scheint gegeben zu sein, Bsp.: W1 334, W2 341, W3 325
        //System.out.println("Word1: " + c1 + ", Word2: " + c2 + ", Word3: " + c3);
        assertTrue(w1);
        assertTrue(w2);
        assertTrue(w3);
    }
}