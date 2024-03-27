package test;

import java.util.*;

/// Board Class
/// Scrabble exercise 9
/// Student Name: Ahigad Genish
/// ID : 31628022
public class Board {


    // Factory for creating and managing flyweight objects of board types squares
    private class BoardSquareFactory {
        private Map<String, BoardSquare> boardSquareCache = new HashMap<>();

        // Create or retrieve a flyweight object based on the given type
        public BoardSquare createBoardSquare(String type) {
            if(boardSquareCache.containsKey(type) == false)
                boardSquareCache.put(type, createNewBoardSquare(type));
            return boardSquareCache.get(type);
        }

        // Create a new flyweight object based on the given square type
        private BoardSquare createNewBoardSquare(String type) {
            switch (type) {
                case "Regular":
                    return new RegularBoardSquare();
                case "Star":
                    return new StarBoardSquare();
                case "TripleWord":
                    return new TripleWordBoardSquare();
                case "DoubleWord":
                    return new DoubleWordBoardSquare();
                case "TripleLetter":
                    return new TripleLetterBoardSquare();
                case "DoubleLetter":
                    return new DoubleLetterBoardSquare();
                default:
                    throw new IllegalArgumentException("Invalid board square type: " + type);
            }
        }
    }

    // Board square interface
    public interface BoardSquare {
        int getWordScoreMultiplier(); // Return the multiplier value (1 for regular squares)

        int getTileScore(Tile tile); // Return the score contribution of the given tile on this square

    }

    public class RegularBoardSquare implements BoardSquare {
        @Override
        public int getWordScoreMultiplier() {
            // Regular square has no multiplier
            return 1;
        }

        @Override
        public int getTileScore(Tile tile) {
            // Scoring logic for a regular square
            return tile.score;
        }

    }
    public static class StarBoardSquare implements BoardSquare{
        public static boolean hasBeenPlaced = false;
        @Override
        public int getWordScoreMultiplier() {

            if(hasBeenPlaced == false){ // Logic for first turn
                hasBeenPlaced = true;
                return 2;
            }
            return 1;  // Not first turn
        }

        @Override
        public int getTileScore(Tile tile) {
            // Scoring logic for a regular square
            return tile.score;
        }

    }
    public class TripleWordBoardSquare implements BoardSquare {
        @Override
        public int getWordScoreMultiplier() {
            // Triple word score multiplier
            return 3;
        }

        @Override
        public int getTileScore(Tile tile) {
            // Scoring logic for a square with triple word score
            return tile.score;
        }

    }

    public class DoubleWordBoardSquare implements BoardSquare {
        @Override
        public int getWordScoreMultiplier() {
            // Double word score multiplier
            return 2;
        }

        @Override
        public int getTileScore(Tile tile) {
            // Scoring logic for a square with double word score
            return tile.score;
        }
    }

    public class TripleLetterBoardSquare implements BoardSquare {
        @Override
        public int getWordScoreMultiplier() {
            // Triple letter score multiplier
            return 1;
        }

        @Override
        public int getTileScore(Tile tile) {
            // Scoring logic for a square with triple letter score
            return tile.score * 3;
        }

    }

    public class DoubleLetterBoardSquare implements BoardSquare {
        @Override
        public int getWordScoreMultiplier() {
            // Double letter score multiplier
            return 1;
        }

        @Override
        public int getTileScore(Tile tile) {
            // Scoring logic for a square with double letter score
            return tile.score * 2;
        }
    }

    // Data Members
    private final int size = 15;
    private BoardSquare[][] board;
    private Tile[][] tilesOnBoard;

    private List<Word> currentWords;
    private final int starIndex = 7;
    private BoardSquareFactory factory;

    // Constructor
    public Board(){
        this.factory = new BoardSquareFactory();
        this.board = new BoardSquare[size][size];
        this.initializeBoard();
        this.tilesOnBoard = new Tile[size][size];
        this.currentWords = new ArrayList<Word>();

    }

    // Initialize board
    private void initializeBoard() {

        for (int i = 0; i < size; i++) {
            for (int j = 0; j <= i; j++) {
                // Create squares for the current position (i, j)
                this.board[i][j] = createSquare(i, j);

                // Mirror the square to the symmetric position (j, i)
                this.board[j][i] = this.board[i][j];
            }
        }

    }

    // Create square in given row and column regarding the board design
    private BoardSquare createSquare(int i, int j) {

        if(i % 7 == 0 && j % 7 == 0 && (j != 7 || i != 7))
            return this.factory.createBoardSquare("TripleWord");

        else if(i == 7 && j == 7)
            return this.factory.createBoardSquare("Star");

        else if(i == j && (i < 5 || i > 9))
            return this.factory.createBoardSquare("DoubleWord");

        else if(i + j == 14 && Math.abs(i - j) >= 6)
            return this.factory.createBoardSquare("DoubleWord");

        else if((i == 3 || i == 7 || i == 11 || i == 14) && (j == 0 || j == 3 || j == 7 || j == 11))
            return this.factory.createBoardSquare("DoubleLetter");

        else if(Math.abs(i - j) % 4 == 0 && (i == 1 || i == 5 || i == 9 | i == 13))
            return this.factory.createBoardSquare("TripleLetter");

        else if((i == 2 || i == 6 | i == 8 || i == 12) && (j == 2 || j == 6 || j == 8))
            return this.factory.createBoardSquare("DoubleLetter");

        else
            return this.factory.createBoardSquare("Regular");

    }


    // Methods

    // Get deep copy of reference to the tiles that on board
    public Tile[][] getTiles(){

        Tile[][] clonedArray = new Tile[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                clonedArray[i][j] = this.tilesOnBoard[i][j];
            }
        }

        return clonedArray;
    }

    // Check if can place the word
    public boolean boardLegal(Word anyWord){

        int col = anyWord.getCol();
        int row = anyWord.getRow();
        int len = anyWord.getTiles().length;
        boolean isVertical =  anyWord.isVertical();
        boolean inBound =  isVertical == true ? ((row + len - 1 >= 0) && (row + len - 1 < size)) :  ((col + len - 1 >= 0) && (col + len -1 < size));
        // Check word located in the board bounds
        if(row < 0 || row >= size || col < 0 || col >= size || inBound == false)
            return false;

       boolean valid = false;

        for(int i = 0; i< len; i++){

            int currentRow = isVertical == true ? row + i : row;
            int currentCol = isVertical == true ? col : col + i;

            if(anyWord.getTiles()[i] == null)
                continue;

            if(letterReplacedExistTile(currentRow, currentCol, anyWord.getTiles()[i].letter) == true)  // Check current tile not replaces current tiles with other letter
                return false;

            if(onStarSquare(currentRow, currentCol) == true) // Check letter lay on star square
                valid = true;
            else if(letterOnExistTile(currentRow, currentCol, anyWord.getTiles()[i].letter) == true) // Check letter lay on exist tile
                valid = true;
            else if(letterAdjacentExistTile(currentRow, currentCol) == true) // Check letter adjacent to exist tile
                valid = true;

        }

        return valid;
    }

    // Check if current letter not replaced an exist tile
    private boolean letterReplacedExistTile(int row, int col, char letter) {

        if(this.tilesOnBoard[row][col] != null && this.tilesOnBoard[row][col].letter != letter)
            return true;
        return false;
    }

    // Check if letter adjacent to exist tile
    private boolean letterAdjacentExistTile(int row, int col) {

        int upRow = row - 1;
        int downRow = row + 1;
        int leftCol = col - 1;
        int rightCol = col + 1;

        if(upRow >= 0 && this.tilesOnBoard[upRow][col] != null)
            return true;
        else if(downRow < size && this.tilesOnBoard[downRow][col] != null)
            return true;
        else if(leftCol >= 0 && this.tilesOnBoard[row][leftCol] != null)
            return true;
        else if (rightCol < size && this.tilesOnBoard[row][rightCol] != null)
            return true;

        return false;
    }

    // Check letter lay on star square
    private boolean onStarSquare(int row, int col) {
        return col == this.starIndex && row == this.starIndex;
    }

    // Check letter lay on exist tile
    private boolean letterOnExistTile(int row, int col, char letter) {

      return this.tilesOnBoard[row][col] != null && this.tilesOnBoard[row][col].letter == letter;
    }

    // Return new words that creates by the new word
    public List<Word> getWords(Word newWord){

        List<Word> newWords = new ArrayList<Word>();

        // Iterate over the word and complete missing tiles
        for(int i = 0; i < newWord.getTiles().length; i ++ ) {

            int currentRow = newWord.isVertical() == true ? newWord.getRow() + i : newWord.getRow();
            int currentCol = newWord.isVertical() == true ? newWord.getCol() : newWord.getCol() + i;

            if (newWord.getTiles()[i] == null) {  // Complete missing tiles if it's null with current ties on board
                newWord.getTiles()[i] = this.tilesOnBoard[currentRow][currentCol];
            }
        }

        // Iterate over the word and try to create new words from each letter
        for(int i = 0; i < newWord.getTiles().length; i ++ ){

           int currentRow = newWord.isVertical() == true ? newWord.getRow() + i : newWord.getRow();
           int currentCol = newWord.isVertical() == true ? newWord.getCol()  : newWord.getCol()  + i;
           int upRow = currentRow - 1;
           int downRow = currentRow + 1;
           int leftCol = currentCol - 1;
           int rightCol = currentCol + 1;

           Tile currentTile =  currentTile = newWord.getTiles()[i];

           // Try to create vertical word
           List<Tile> verticalTiles = new ArrayList<Tile>();

           while(upRow >= 0 && this.tilesOnBoard[upRow][currentCol] != null){
               verticalTiles.add(this.tilesOnBoard[upRow][currentCol]);
               upRow -= 1;
           }

           Collections.reverse(verticalTiles);
           verticalTiles.add(currentTile);

           while(downRow < size  && this.tilesOnBoard[downRow][currentCol] != null){
               verticalTiles.add(this.tilesOnBoard[downRow][currentCol]);
               downRow += 1;
           }

           // It this new word is at least length 2, and the current word not contains that and this word not exist in board yet
           if(verticalTiles.size() >= 2){
               Word candidate = new Word(verticalTiles.toArray(new Tile[0]), upRow + 1, currentCol, true );
               if(this.currentWords.contains(candidate) == false  && newWord.contains(candidate) == false)
                   newWords.add(candidate);
           }
            // Try to create horizontal word
           List<Tile> horizontalTiles = new ArrayList<Tile>();

           while(leftCol >= 0 && this.tilesOnBoard[currentRow][leftCol] != null){
               horizontalTiles.add(this.tilesOnBoard[currentRow][leftCol]);
               leftCol -= 1;
           }

           Collections.reverse(horizontalTiles);
           horizontalTiles.add(currentTile);

           while(rightCol < size  && this.tilesOnBoard[currentRow][rightCol] != null){
               horizontalTiles.add(this.tilesOnBoard[currentRow][rightCol]);
               rightCol += 1;
           }
            // It this new word is at least length 2, and not the current word not contains that and this word not exist in board yet
           if(horizontalTiles.size() >= 2){
               Word candidate = new Word(horizontalTiles.toArray(new Tile[0]), currentRow, leftCol + 1, false );
               if(this.currentWords.contains(candidate) == false && newWord.contains(candidate) == false)
                   newWords.add(candidate);
           }

       }
        // Add the new Word also
        newWords.add(newWord);
        return newWords;
    }

    // Check if word exist in the dictionary
    public boolean dictionaryLegal(Dictionary anyDictionary, Word newWord){
        String word = "";
        for(Tile tile : newWord.getTiles()){
            word += tile.letter;
        }
        return anyDictionary.query(word);
    }

    // Try place the new word
    public int tryPlaceWord(Word anyWord) {

        if(this.boardLegal(anyWord) == false)
            return 0;

        int newScore = 0;
        // Get new words created by this word
        List<Word> newWords = this.getWords(anyWord);
        for(Word newWord: newWords){

            if(this.dictionaryLegal(newWord) == true){
                newScore += this.getScore(newWord);
            }
            else{
                return 0;
            }
        }
        // Place new words created by this word
        for(Word newWord : newWords){
            this.placeWord(newWord);
        }

        return newScore;
    }

    // Place new word on board
    private void placeWord(Word newWord) {

        int col = newWord.getCol();
        int row = newWord.getRow();
        int len = newWord.getTiles().length;


        for(int i = 0 ; i < len ; i ++) {
            int currentRow = newWord.isVertical() == true ? row + i : row;
            int currentCol = newWord.isVertical() == true ? col : col + i;

            if(newWord.getTiles()[i] != null)
                this.tilesOnBoard[currentRow][currentCol] = newWord.getTiles()[i];

        }

        this.currentWords.add(newWord);
    }

    // Get score of word by type of squares it is lay
    public int getScore(Word anyWord){

        int totalScore = 0;
        int wordMultiplier = 1;

        int col = anyWord.getCol();
        int row = anyWord.getRow();
        int len = anyWord.getTiles().length;

        for(int i = 0 ; i < len ; i ++){

            int currentRow = anyWord.isVertical() == true ? row + i : row;
            int currentCol = anyWord.isVertical() == true ? col : col + i;

            BoardSquare currentSquare = board[currentRow][currentCol];
            Tile currentTile = anyWord.getTiles()[i] == null ? this.tilesOnBoard[currentRow][currentCol] : anyWord.getTiles()[i];
            totalScore += currentSquare.getTileScore(currentTile);

            // Apply the word multiplier if the square has one
            wordMultiplier *= currentSquare.getWordScoreMultiplier();
        }

        // Apply the total score multiplier for the word
        return totalScore * wordMultiplier;

    }

}
