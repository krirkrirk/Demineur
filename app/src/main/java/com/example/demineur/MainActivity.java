package com.example.demineur;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.sql.Array;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onEasyNewGame(View v){
        Intent i = new Intent(MainActivity.this, GameActivity.class);
        i.putExtra("rows", 15);
        i.putExtra("minesCount", 15);
        i.putExtra("gameType", "easy");
        startActivity(i);
    }
    public void onMediumNewGame(View v){
        Intent i = new Intent(MainActivity.this, GameActivity.class);
        i.putExtra("rows", 25);
        i.putExtra("minesCount", 25);
        i.putExtra("gameType", "medium");
        startActivity(i);
    }
    public void onExpertNewGame(View v){
        Intent i = new Intent(MainActivity.this, GameActivity.class);
        i.putExtra("rows", 25);
        i.putExtra("minesCount", 50);
        i.putExtra("gameType", "expert");
        startActivity(i);
    }

    public void onGoToStats(View v){
        Intent i = new Intent(MainActivity.this, StatisticsActivity.class);
        startActivity(i);

    }
}