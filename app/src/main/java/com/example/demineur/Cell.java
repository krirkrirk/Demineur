package com.example.demineur;


import android.util.Log;
import android.widget.ImageButton;

public class Cell {
    public enum CellState {
        HIDDEN,
        FLAGGED,
        REVEALED,
        EXPLODED
    }
    public CellState state;
    public Boolean hasBomb;
    public int minesCount;
    public int row;
    public int column;
    public int position;
    public ImageButton cellView;

    public int getImage(){
        if(state == CellState.EXPLODED)
            return R.drawable.mine_red;
        if(state == CellState.HIDDEN)
            return R.drawable.closed;
        if(state == CellState.FLAGGED)
            return R.drawable.flag;
        if(hasBomb) return R.drawable.mine;
        switch(minesCount){
            case 0 :
                return R.drawable.type0;
            case 1 :
                return R.drawable.type1;
            case 2 :
                return R.drawable.type2;
            case 3 :
                return R.drawable.type3;
            case 4 :
                return R.drawable.type4;
            case 5 :
                return R.drawable.type5;
            case 6 :
                return R.drawable.type6;
            case 7 :
                return R.drawable.type7;
            case 8 :
                return R.drawable.type8;

            default: return R.drawable.closed;
        }
    }
    public Cell(Boolean hasBomb, int minesCount, int row, int column, int position){
        this.state = CellState.HIDDEN;
        this.hasBomb = hasBomb;
        this.minesCount = minesCount;
        this.row = row;
        this.column = column;
        this.position = position;
    }

    public void setCellView(ImageButton cv){
        this.cellView = cv;
    }
    public void reveal(){
        this.state = CellState.REVEALED;
        cellView.setImageResource(getImage());
    }

    public void unflag(){
        if(state != CellState.FLAGGED) return;
        state = CellState.HIDDEN;
        cellView.setImageResource(getImage());
    }

    public void flag(){
        if(state != CellState.HIDDEN) return;
        state = CellState.FLAGGED;
        cellView.setImageResource(getImage());
    }

    public void reset(){
        this.state = CellState.HIDDEN;
        this.hasBomb = false;
        this.minesCount = 0;
        cellView.setImageResource(getImage());
    }

    public void explode(){
        state = CellState.EXPLODED;
        cellView.setImageResource(getImage());
    }
}
