package com.example.demineur;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.sql.Array;


public class MainActivity extends AppCompatActivity {

    private Cell[][] grid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //////IDEAS
    //crazy mode : des mini grilles bougent sur l'écran, de tailles diverses
    //d'une facon ou d'une autre il faut les résoudre avant qu'elle disparaissent / qu'elle remplisse l'écran / je sais pas trop



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
        i.putExtra("gameType", "easy");
        startActivity(i);
    }
    public void onExpertNewGame(View v){
        Intent i = new Intent(MainActivity.this, GameActivity.class);
        i.putExtra("rows", 25);
        i.putExtra("minesCount", 50);
        i.putExtra("gameType", "easy");
        startActivity(i);
    }

    public void onGoToStats(View v){
        Intent i = new Intent(MainActivity.this, StatisticsActivity.class);
        startActivity(i);

    }
}