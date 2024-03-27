package test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/// Word Class
/// Scrabble exercise 9
/// Student Name: Ahigad Genish
/// ID : 31628022
public class Word {

    // Data Members
    private Tile[] tiles;
    private int row, col;
    private boolean vertical;

    // Constructor
    public Word(Tile[] tiles, int row, int col, boolean vertical) {
        this.tiles = tiles;
        this.row = row;
        this.col = col;
        this.vertical = vertical;
    }

    // Getters
    public Tile[] getTiles() {
        return tiles;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean isVertical() {
        return vertical;
    }

    // Methods

    // Contains method check if current word contains other
    public boolean contains(Word otherWord){

        List<Tile> myTiles = Arrays.asList(this.getTiles());
        List<Tile> otherTiles = Arrays.asList(otherWord.getTiles());

        // Get the string of this word letters
        String myWordString = myTiles.stream()
                .filter(tile -> tile != null) // Null check a
                .map(tile -> String.valueOf(tile.letter))
                .collect(Collectors.joining());

        // Get the string of the other word letters
        String otherWordString = otherTiles.stream()
                .filter(tile -> tile != null) // Null check
                .map(tile -> String.valueOf(tile.letter))
                .collect(Collectors.joining());

        // Check if myWordString contains otherWordString
        return myWordString.contains(otherWordString);
    }

    @Override
    public boolean equals(Object obj) {

        if(obj instanceof Word == false) {
            return false;
        }
        // Casting
        Word anyWord = (Word)obj;
        // Check lengths
        if(anyWord.getTiles().length != this.tiles.length || this.vertical != anyWord.isVertical())
            return false;
        // Check indexes
        if(anyWord.col != this.col || anyWord.row != this.row)
            return false;

        // Check that tiles are equals
        for(int i = 0; i < this.tiles.length; i++){
            if(this.tiles[i] != null && this.tiles[i].equals(anyWord.getTiles()[i]) == false){
                return false;
            }
        }

        return true;

    }
}
