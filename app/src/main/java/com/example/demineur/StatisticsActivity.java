package com.example.demineur;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Arrays;

public class StatisticsActivity extends AppCompatActivity {

    private int gameType;
    private int[] highscores;
    private int[] gamesPlayed;
    private int[] gamesWon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        this.gameType = 0;

        SharedPreferences sh = getSharedPreferences("Démineur", Context.MODE_PRIVATE);
        this.highscores = new int[]{sh.getInt("highscore_easy", 999), sh.getInt("highscore_medium", 999),sh.getInt("highscore_expert", 999) };
        this.gamesPlayed = new int[]{sh.getInt("gamesPlayed_easy", 0), sh.getInt("gamesPlayed_medium", 0),sh.getInt("gamesPlayed_expert", 0) };
        this.gamesWon = new int[]{sh.getInt("gamesWon_easy", 0), sh.getInt("gamesWon_medium", 0),sh.getInt("gamesWon_expert", 0) };
        updateButtons();
        updateStats();

    }

    public void updateStats(){
        TextView highscoreTV =  findViewById(R.id.highscore);
        TextView gamesPlayedTV =  findViewById(R.id.gamesPlayed);
        TextView gamesWonTV =  findViewById(R.id.gamesWon);
        TextView winPercentTV =  findViewById(R.id.winPercent);
        highscoreTV.setText(String.valueOf(highscores[gameType]));
        gamesPlayedTV.setText(String.valueOf(gamesPlayed[gameType]));
        gamesWonTV.setText(String.valueOf(gamesWon[gameType]));

        double percent = gamesPlayed[gameType] == 0 ? 0 : (double)Math.round(100.0 * 100*gamesWon[gameType]/gamesPlayed[gameType])/100.0;
        String text = percent+"%";
        winPercentTV.setText(text);
    }

    public void updateButtons(){
        Button[] buttons = {findViewById(R.id.easy), findViewById(R.id.medium), findViewById(R.id.expert)};
        for(int i = 0; i<3; i++){
            if(gameType == i){
                Button button = buttons[i];
                button.setBackgroundColor(getResources().getColor(R.color.grey));
            } else {
                Button button = buttons[i];
                button.setBackgroundColor(getResources().getColor(R.color.grey_dark));
            }
        }
    }
    public void onClickEasy(View v){
        if(gameType == 0) return;
        gameType = 0;
        updateButtons();

        updateStats();
    }
    public void onClickMedium(View v){
        if(gameType==1)
            return;
        gameType = 1;
        updateButtons();
        updateStats();
    }
    public void onClickExpert(View v){
        if(gameType == 2)
            return;
        gameType = 2;
        updateButtons();

        updateStats();
    }
}