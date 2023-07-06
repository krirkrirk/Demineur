package com.example.demineur;

import android.util.Log;

import java.sql.Array;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Grid {
    private ArrayList<Cell> cells;

    private int minesCount;
    private int rows;
    private int columns;
    public boolean isPopulated = false;
    public boolean isTerminated = false;
    public Grid(int rows, int columns, int minesCount){
        this.rows= rows;
        this.columns = columns;
        this.minesCount = minesCount;
        cells = new ArrayList();
        //Création des cells
        for(int i = 0; i<rows; i++){
            for(int j = 0; j<columns; j++){
                cells.add(new Cell(false, 0, i, j, i*columns+j));
            }
        }
    }

    public ArrayList<Cell> getCells(){
        return cells;
    }
    /**
     * Répartie les bombes et mets à jour le compte pour chaque Cell
     * Prérequis : le nombre de bombes est adapté à la taille de la grille
     */
    public void populate(int firstCellClickedPosition){
        ArrayList<Cell> candidateCells = (ArrayList<Cell>) cells.clone();
        ArrayList<Cell> safeCells = getNeighbours(cells.get(firstCellClickedPosition));
        candidateCells.remove(firstCellClickedPosition);
        for(int i = 0; i<safeCells.size(); i++){
            Cell safeCell = safeCells.get(i);
            for(int j = 0; j<candidateCells.size();j++){
                Cell cell = candidateCells.get(j);
                if(cell.position == safeCell.position){
                    candidateCells.remove(j);
                    j--;
                }
            }
        }

        for(int iter = 0; iter<minesCount; iter++){
            int randomIndex = ThreadLocalRandom.current().nextInt(0,  candidateCells.size());
            boolean candidateFound = true;
            //on vérifie qu'on ne crée pas une cell n'ayant que des bombes pour voisins
//            do{
//                randomIndex =ThreadLocalRandom.current().nextInt(0,  candidateCells.size());
//                Cell randomCell = candidateCells.get(randomIndex);
//                for(int i = 0; i< cells.size(); i++){
//                    Cell cell = cells.get(i);
//                    int cellPosition = cell.row * columns + cell.column;
//                    if(!cell.hasBomb && cellPosition != randomCell.position) continue;
//                    ArrayList<Cell> neighbours = getNeighbours(cell);
//                    boolean hasFreeNeighbourCell = false;
//                    for(int j = 0; j<neighbours.size(); j++){
//                        Cell neighbour = neighbours.get(j);
//                        if(!neighbour.hasBomb){
//                            hasFreeNeighbourCell = true;
//                            break;
//                        }
//                    }
//                    if(!hasFreeNeighbourCell) {
//                        candidateCells.remove(randomIndex);
//                        candidateFound = false;
//                        break;
//                    }
//                }
//            } while (!candidateFound);

            Cell chosenCell = candidateCells.get(randomIndex);
            chosenCell.hasBomb = true;
            chosenCell.minesCount = 0;
            ArrayList<Cell> neighbours = getNeighbours(chosenCell);
            for(int i = 0; i<neighbours.size(); i++){
                Cell neighbour = neighbours.get(i);
                if(!neighbour.hasBomb) neighbour.minesCount += 1;
            }
            candidateCells.remove(randomIndex);
        }
        isPopulated = true;
    }
    public void revealCell(int cellPosition){
        Cell cell = cells.get(cellPosition);
        if(cell.state == Cell.CellState.REVEALED) return;
        if(cell.hasBomb){
            return;
        }
        cell.reveal();
        if(cell.minesCount>0) return;
        ArrayList<Cell> neighbours = getNeighbours(cell);
        for(int i = 0; i< neighbours.size(); i++){
            Cell neighbour = neighbours.get(i);
            revealCell(neighbour.position);
        }

    }

    public boolean isSolved(){
        for(int i = 0; i< cells.size(); i++){
            Cell cell = cells.get(i);
            if(!cell.hasBomb && cell.state == Cell.CellState.HIDDEN){
                return false;
            }
        }
        isTerminated = true;
        return true;
    }

    public void reveal(){
        //passer toutes les cells à visible
    }

    public ArrayList<Cell> getNeighbours(Cell cell){
        ArrayList<Cell> neighbours = new ArrayList();
        int cellRow = cell.row;
        int cellColumn = cell.column;
        int cellPosition = cell.position;
        Boolean isFirstColumn = cellColumn == 0;
        Boolean isFirstRow = cellRow == 0;
        Boolean isLastRow = cellRow == rows-1;
        Boolean isLastColumn = cellColumn == columns -1;

        //ajout cell N
        if(!isFirstRow)
            neighbours.add(cells.get(cellPosition - columns));
        //ajout cell W
        if(!isFirstColumn)
            neighbours.add(cells.get(cellPosition-1));
        //ajout cell S
        if(!isLastRow)
            neighbours.add(cells.get(cellPosition+columns));
        //ajout cell E
        if(!isLastColumn)
           neighbours.add(cells.get(cellPosition+1));
        //ajout cell N-W
        if(!isFirstRow && !isFirstColumn)
            neighbours.add(cells.get(cellPosition -columns -1));
        //ajout cell N-E
        if(!isFirstRow && !isLastColumn)
            neighbours.add(cells.get(cellPosition -columns + 1));
        //ajout cell S-W
        if(!isLastRow && !isFirstColumn)
            neighbours.add(cells.get(cellPosition +columns -1));
        //ajout cell S-E
        if(!isLastRow && !isLastColumn)
            neighbours.add(cells.get(cellPosition + columns + 1));

        return neighbours;
    }

    public void reset(){
        //Création des cells
        for(int i = 0; i<cells.size(); i++){
                cells.get(i).reset();
            }
        isTerminated = false;
        isPopulated = false;
    }
}
