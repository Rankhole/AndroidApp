package com.example.androidapp;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Stellt ein Wort da. Wir speichern unser Wort in der Datenbank. Das Wort
 * enthält die dazu gehörenden vier Tabu Wörter.
 */
@Entity
public class Word {

    @PrimaryKey
    @NonNull
    public String word;

    @ColumnInfo(name = "first_tabu")
    String firstTabu;

    @ColumnInfo(name = "second_tabu")
    String secondTabu;

    @ColumnInfo(name = "third_tabu")
    String thirdTabu;

    @ColumnInfo(name = "fourth_tabu")
    String fourthTabu;

    /**
     * Für Testzwecke. Vergleicht zwei Wörter und gibt zurück, ob sie gleich sind.
     *
     * @param other Zweites Wort
     * @return True, wenn biede gleich, sonst false
     */
    boolean equals(Word other) {
        return this.word.equals(other.word) && this.firstTabu.equals(other.firstTabu)
                && this.secondTabu.equals(other.secondTabu) && this.thirdTabu.equals(other.thirdTabu)
                && this.fourthTabu.equals(other.fourthTabu);
    }

}
