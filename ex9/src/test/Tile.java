package test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/// Tile and Bag Classes
/// Scrabble exercise 9
/// Student Name: Ahigad Genish
/// ID : 31628022
public class Tile {

    // Data Members
    public final char letter;
    public final int score;

    // Constructor
    private Tile(char newLetter, int newScore){
        this.letter = newLetter;
        this.score = newScore;
    }

    // Methods

    @Override
    public int hashCode() {

        // Choose a prime number as an initial value
        int hashCode = 17;

        // Combine hash codes of the fields
        hashCode = 31 * hashCode + Character.hashCode(this.letter);
        hashCode = 31 * hashCode * Integer.hashCode(this.score);

        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Tile){
            // Casting
            Tile anyTile = (Tile)obj;
            if(anyTile != null)
                return this.score == anyTile.score && this.letter == anyTile.letter;
        }
        return false;
    }

    static class Bag{

        // Data Members
        private int[] letterQuantity;
        private Tile[] tiles;

        private final int maximumTiles = 98;
        private final int numberOfLetters = 26;

        // Constructor
        public  Bag(){
            this.InitializeBag();
        }

        private void InitializeBag() {
            this.letterQuantity = new int[numberOfLetters];
            this.InitializeTiles();
        }

        // Initialize tiles method
        private void InitializeTiles() {

            this.tiles = new Tile[numberOfLetters];

            for(int i = 0 ; i < 9; i++){
                this.put(new Tile('A' , 1));
                this.put(new Tile('I' , 1));
                this.put(new Tile('E' , 1)); // E is 12 so 9 here
            }

            for(int i = 0 ; i < 2; i++) {
                this.put(new Tile('B', 3));
                this.put(new Tile('C', 3));
                this.put(new Tile('F' , 4));
                this.put(new Tile('H' , 4));
                this.put(new Tile('M' , 3));
                this.put(new Tile('P' , 3));
                this.put(new Tile('V' , 4));
                this.put(new Tile('W' , 4));
                this.put(new Tile('Y' , 4));
            }

            for(int i = 0 ; i < 4; i++) {
                this.put(new Tile('D' , 2));
                this.put(new Tile('L' , 1));
                this.put(new Tile('S' , 1));
                this.put(new Tile('U' , 1));
                this.put(new Tile('O' , 1)); // O is 8 so 4 here
                this.put(new Tile('O' , 1)); // O is 8 so more 4 here
            }

            for(int i = 0 ; i < 3; i++) {
                this.put(new Tile('G' , 2));
                this.put(new Tile('E' , 1)); // E is 12 so more 3 here
                this.put(new Tile('N' , 1)); // N is 6 so 3 here
                this.put(new Tile('N' , 1)); // N is 6 so more 3 here
                this.put(new Tile('R' , 1)); // R is 6 so 3 here
                this.put(new Tile('R' , 1)); // R is 6 so more 3 here
                this.put(new Tile('T' , 1)); // T is 6 so 3 here
                this.put(new Tile('T' , 1)); // T is 6 so more 3 here
            }


            // Letters with quantity 1
            this.put(new Tile('J' , 8));
            this.put(new Tile('K' , 5));
            this.put(new Tile('Q' , 10));
            this.put(new Tile('X' , 8));
            this.put(new Tile('Z' , 10));
        }

        // Methods

        // Return the relevant indexes that has tiles
        private List<Integer> allAvailableTiles(){

            List<Integer> availableIndexes = new ArrayList<>();
            for(int i =0;i < this.letterQuantity.length; i++){

                if(this.letterQuantity[i] > 0){
                    availableIndexes.add(i);
                }
            }
            return availableIndexes;

        }

        // Return the number of tiles in bag
        public int size(){

            int sum = 0;
            for(int quantity : this.letterQuantity)
                sum += quantity;

            return sum;
        }


        // Return random Tile which available
        public Tile getRand(){
            // If no tiles
            if(size() == 0)
                return null;

            // Get list of available indexes with at least one tile
            List<Integer> availableIndexes = this.allAvailableTiles();

            // Generate random index
            Random rand = new Random();
            int randomAvailablePlace = rand.nextInt(availableIndexes.size());
            int randIndex = availableIndexes.get(randomAvailablePlace);

            // Decrease by 1 the quantity
            this.letterQuantity[randIndex] -= 1;

            // Return random tile
            Tile theRandomTile = this.tiles[randIndex];
            return theRandomTile;

        }

        // Return the tile index with a given letter if it's valid. O.w return -1
        private int getTileIndex(char anyLetter){
            int index =  anyLetter - 'A';
            if(index >= 0 && index < this.numberOfLetters)
                return index;
            return -1;
        }

        // Return tile with a given letter if available
        public Tile getTile(char anyLetter){

            int letterIndex = this.getTileIndex(anyLetter);

            if(letterIndex != -1 && this.letterQuantity[letterIndex] > 0)
                return this.tiles[letterIndex];

            return null;
        }

        // Put given tile into the bag
        public void put(Tile anyTile){

            if(bagIsFull() == true){
                return; // Full bag
            }

            int letterIndex = this.getTileIndex(anyTile.letter);
            this.tiles[letterIndex] = anyTile;
            if(letterIndex != -1)
                this.letterQuantity[letterIndex] += 1; // Increase quantity of the given tile
        }

        // Return true if bag is full
        private boolean bagIsFull(){
            return this.size() == maximumTiles;
        }

        // Return deep copy of the quantities array
        public int[] getQuantities(){

            return this.letterQuantity.clone();
        }
    }
}
