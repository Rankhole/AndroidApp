package com.example.androidapp;

import com.example.androidapp.database.Word;
import com.example.androidapp.database.WordDB;
import com.example.androidapp.database.WordDBImpl;
import com.example.androidapp.database.WordImpl;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;

public class GameTest {
    private Game game;
    private String begin;
    private OutputStream os;
    private InputStream is;
    private WordDB wordDB;


    @Test
    public void changeTeamOut(){
        try {
            os = new ByteArrayOutputStream(1000);
            byte[] byteArray = intToByteArray(400); // falsche Zahl, fuehrt keinen TeamWechsel herbei
            is = new ByteArrayInputStream(byteArray);
            this.game=new Game(begin, is, os, null, null, null, null, null, null, null, wordDB);

            byte[] bytes = ((ByteArrayOutputStream) os).toByteArray();
            int out = new BigInteger(bytes).intValue();
            assertEquals(out, 404);
        }catch(IOException e){
            fail("keine Exception erwartet");
        }
    }

    @Test
    public void changeTeamIn() throws IOException {
        try {
            os = new ByteArrayOutputStream(1000);
            byte[] byteArray = intToByteArray(404);
            is = new ByteArrayInputStream(byteArray);
            this.game = new Game(begin, is, os, null, null, null, null, null, null, null, wordDB);
            game.changeTeam();
            fail("keine NullPointerException geworfen worden");
        }catch (NullPointerException e){
            // erwartet
        }
    }

    @Test
    public void startRound() throws Exception {

        os = new ByteArrayOutputStream(100000);
        byte[] byteArray = intToByteArray(1);
        is = new ByteArrayInputStream(byteArray);
        begin = "a";
        wordDB= new WordDBImpl();
        List<String> forbiddenWordsList= new ArrayList<>();
        forbiddenWordsList.add("ist");
        forbiddenWordsList.add("nur");
        forbiddenWordsList.add("ein");
        forbiddenWordsList.add("test");
        Word testWord= new WordImpl("das", forbiddenWordsList);
        wordDB.add(testWord);
        this.game=new Game(begin, is, os, null, null, null, null, null, null, null, wordDB);
        try{
            game.setTEAM_A(true);
            game.startRound();
            byte[] bytes = ((ByteArrayOutputStream) os).toByteArray();
            String word = new String(bytes);
            assertTrue(word.contains("das"));

        }catch (NullPointerException e){

        }
    }

    @Test
    public void active(){
        this.game=new Game(begin, is, os, null, null, null, null, null, null, null, wordDB);
        game.setState(2);
        assertTrue(game.active());
    }


    private byte[] intToByteArray ( final int i ) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        dos.writeInt(i);
        dos.flush();
        return bos.toByteArray();
    }
}