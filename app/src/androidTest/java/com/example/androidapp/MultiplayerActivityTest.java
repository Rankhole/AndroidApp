package com.example.androidapp;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MultiplayerActivityTest {

    //@Rule
    //public ActivityTestRule<MultiplayerActivity> mActivityTestRule = new ActivityTestRule<MultiplayerActivity>(MultiplayerActivity.class);

    //private MultiplayerActivity mActivity = null;

    //ConnectActivity Monitor
    //Instrumentation.ActivityMonitor monitorConnectActivity = getInstrumentation().addMonitor(ConnectActivity.class.getName(),null,false);

    @Before
    public void setUp() throws Exception {
        //mActivity = mActivityTestRule.getActivity();
    }

    @Test
    public void nothing() {

    }

    /**
     * FÜRS ERSTE IST DIESER TEST AUS.
     * Das Problem: die MultiplayerActivity erstellt beim onCreate die Game Instanzen, welche aber
     * nur in Abhängigkeit mit vorheriger Ausführung der ConnectActivity richtig funktionieren.
     *
     * @Test public void testWordDBLoad(){
     * WordDB db = mActivity.getWordDB();
     * assertNotNull(db);
     * try {
     * Word word = db.getRandomWord();
     * assertEquals("erstes", word.getWord());
     * assertEquals("wort", word.getForbiddenWords().get(0));
     * assertEquals("nur", word.getForbiddenWords().get(1));
     * assertEquals("ein", word.getForbiddenWords().get(2));
     * assertEquals("test", word.getForbiddenWords().get(3));
     * System.out.println(word.getWord());
     * } catch(Exception e){
     * e.printStackTrace();
     * }
     * }
     **/

    @After
    public void tearDown() throws Exception {
        //mActivity = null;
    }
}
