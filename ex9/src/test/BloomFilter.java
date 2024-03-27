package test;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class BloomFilter {

    // Data Members
    private BitSet bitArray;
    private List<MessageDigest> hashFunctions = new ArrayList<>();

    // Constructor
    public BloomFilter(int bitArraySize, String... algorithmNames){
        // Init bit set
        this.bitArray = new BitSet(bitArraySize);
        // Init hash functions
        for(String hashAlgo : algorithmNames){
            try {
                this.hashFunctions.add(MessageDigest.getInstance(hashAlgo));
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
    }
    // Methods

    // Add string into the bit array
    public void add(String newString){

        // Iterate over the hash functions
        for(MessageDigest hash : this.hashFunctions){

            byte[] bytes = hash.digest(newString.getBytes());
            BigInteger number = new BigInteger(1, bytes);
            // Each hash function defines an index that will be set
            int index = number.intValue() % this.bitArray.size();
            this.bitArray.set(Math.abs(index));
        }
    }

    // Contains method to check if a word exist in the bloom filter
    public boolean contains(String anyWord){

        // Iterate over the hash functions
        for(MessageDigest hash : this.hashFunctions) {
            // Get the bytes that the current hash mapping this string

            byte[] bytes = hash.digest(anyWord.getBytes());
            // Get these bytes as integer
            BigInteger number = new BigInteger(1, bytes);
            // Get the index that this hash function send this string and set it
            int index = number.intValue() % this.bitArray.size();
            if(this.bitArray.get(Math.abs(index)) == false)
                return false;

        }
        return true;
    }

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < this.bitArray.size(); i++) {
            boolean curBit = this.bitArray.get(i);
            stringBuilder.append(curBit == true ? "1" : "0");
        }

        return stringBuilder.toString();
    }
}
