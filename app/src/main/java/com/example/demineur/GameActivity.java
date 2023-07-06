package com.example.demineur;

import static com.google.android.material.internal.ContextUtils.getActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {

    private Grid grid;
    private int minesCount;
    private int displayedMinesCount;
    private int seconds = 0;
    private boolean running = false;
    private String gameType;
    private ImageButton faceButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent intent = getIntent();

        this.faceButton =  findViewById(R.id.face);
        FaceTouchListener faceTouchListener = new FaceTouchListener();
        this.faceButton.setOnTouchListener(faceTouchListener);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        int columns = 11;
        int rows = intent.getIntExtra("rows", 10);
        this.gameType = intent.getStringExtra("gameType");
        this.minesCount = intent.getIntExtra("minesCount", 5);
        this.displayedMinesCount = minesCount;

        updateMineCounterImages(minesCount);

        grid = new Grid(rows, columns, minesCount);
        ArrayList<Cell> cells = grid.getCells();
        TableLayout table = findViewById(R.id.gridTableLayout);

        for(int i = 0; i< rows; i++){
            TableRow tableRow = new TableRow(this);
            for(int j = 0; j<columns; j++){
                int cellPosition = i*columns+j;
                Cell cell = cells.get(cellPosition);
                ImageButton cellView = new ImageButton(this);
                cellView.setImageResource(R.drawable.closed);
                cellView.setPadding(0, 0, 0, 0);
                cellView.setScaleType(ImageView.ScaleType.FIT_XY);

                TableRow.LayoutParams params = new TableRow.LayoutParams(width/11, (int) Math.round(0.9*height/rows));
                cellView.setLayoutParams(params);
                cell.setCellView(cellView);
                cellView.setOnClickListener(view -> {
                    if(grid.isTerminated) return;
                    if(cell.state != Cell.CellState.HIDDEN) return;
                    if(!grid.isPopulated) {
                        onNewGame(cellPosition);
                    }
                    if(cell.hasBomb){
                        onLostGame();
                        cell.explode();
                    } else {
                        grid.revealCell(cellPosition);
                        if(grid.isSolved()){
                            onWonGame();
                        }
                    }
                });
                cellView.setOnLongClickListener(view -> {
                    if(!grid.isPopulated || grid.isTerminated) return true;
                    if (cell.state == Cell.CellState.FLAGGED) {
                        cell.unflag();
                        displayedMinesCount++;
                        updateMineCounterImages(displayedMinesCount);
                    } else if (cell.state == Cell.CellState.HIDDEN){
                        cell.flag();
                        displayedMinesCount--;
                        updateMineCounterImages(displayedMinesCount);
                    }
                    return true;
                });
                tableRow.addView(cellView);
            }
            table.addView(tableRow);
        }

    }

    public void onNewGame(int firstCellClicked){
        SharedPreferences sh = getSharedPreferences("Démineur", Context.MODE_PRIVATE);
        SharedPreferences.Editor shEdit = sh.edit();
        int gamesPlayed= sh.getInt("gamesPlayed_"+gameType, 0);
        shEdit.putInt("gamesPlayed_"+gameType, gamesPlayed+1);
        shEdit.apply();
        grid.populate(firstCellClicked);
        startTimer();
    }

    public void onLostGame(){
        stopTimer();
        ArrayList<Cell> cells = grid.getCells();
        for(int i = 0; i<cells.size(); i++){
            Cell cell = cells.get(i);
            cell.reveal();
        }
        grid.isTerminated = true;
        faceButton.setImageResource(R.drawable.face_lose);
    }

    public void onWonGame(){
        grid.isTerminated = true;
        ArrayList<Cell> cells = grid.getCells();
        for(int i = 0; i<cells.size(); i++){
            Cell cell = cells.get(i);
            cell.reveal();
        }

        SharedPreferences sh = getSharedPreferences("Démineur", Context.MODE_PRIVATE);
        SharedPreferences.Editor shEdit = sh.edit();

        int highscore = sh.getInt("highscore_"+gameType, 999);
        if(highscore > seconds){
            shEdit.putInt("highscore_"+gameType, seconds);
        }
        int gamesWon = sh.getInt("gamesWon_"+gameType, 0);
        shEdit.putInt("gamesWon_"+gameType, gamesWon+1);

        shEdit.apply();

        stopTimer();
        faceButton.setImageResource(R.drawable.face_win);
    }

    public void stopTimer(){
        running = false;
    }

    public int getNumberImage(int number){
        switch(number){
            case 0 : return R.drawable.d0;
            case 1 : return R.drawable.d1;
            case 2 : return R.drawable.d2;
            case 3 : return R.drawable.d3;
            case 4 : return R.drawable.d4;
            case 5 : return R.drawable.d5;
            case 6 : return R.drawable.d6;
            case 7 : return R.drawable.d7;
            case 8 : return R.drawable.d8;
            case 9 : return R.drawable.d9;
            default: return R.drawable.d0;
        }
    }
    public void timerTick(){
        if(!running) return;
        seconds++;
        runOnUiThread(() -> updateTimerImages());
        new Handler().postDelayed(() -> timerTick(), 1000);
    }
    public void startTimer(){
        running = true;
        new Handler().postDelayed(() -> timerTick(), 1000);
    }
    public void onResetGame(){
        grid.reset();
        resetTimer();
    }

    public void resetTimer()
    {
        seconds = 0;
        running = false;

    }

    public void onFaceClick(){
        onResetGame();
        updateTimerImages();
        updateMineCounterImages(minesCount);
    }

    class FaceTouchListener implements View.OnTouchListener {
        public boolean onTouch(View v, MotionEvent event)
        {
            switch(event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    // touch down code
                    faceButton.setImageResource(R.drawable.face_pressed);
                    break;

                case MotionEvent.ACTION_MOVE:
                    // touch move code
                    break;

                case MotionEvent.ACTION_UP:
                    // touch up code
                    faceButton.setImageResource(R.drawable.face_unpressed);
                    onFaceClick();
                    break;
            }
            return true;
        }
    }


    public void updateMineCounterImages(int minesNumber){
        ImageView minesCounterTens = findViewById(R.id.minesCounterTens);
        ImageView minesCounterUnits = findViewById(R.id.minesCounterUnits);

        if(minesNumber<0){
            minesCounterTens.setImageResource(R.drawable.dminus);
            minesCounterUnits.setImageResource(getNumberImage(Math.abs(minesNumber)%10));
        } else {
            minesCounterUnits.setImageResource(getNumberImage(minesNumber%10));
            minesCounterTens.setImageResource(getNumberImage(Math.round(Math.abs(minesNumber)/10)%10));
        }
    }

    public void updateTimerImages(){
        ImageView timerUnits =  findViewById(R.id.timerUnits);
        ImageView timerTens =  findViewById(R.id.timerTens);
        ImageView timerHundreds = findViewById(R.id.timerHundreds);
        timerUnits.setImageResource(getNumberImage(seconds%10));
        timerTens.setImageResource(getNumberImage(Math.round(seconds/10)%10));
        timerHundreds.setImageResource(getNumberImage(Math.round(seconds/100)%10));
    }
}