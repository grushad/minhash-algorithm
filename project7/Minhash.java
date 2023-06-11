package project7;

import java.util.*;
import java.io.*;

//file1 : /Users/grushadharod/Desktop/file1.txt
//file2 : /Users/grushadharod/Desktop/file2.txt
//java project7.Main /Users/grushadharod/Desktop/file1.txt /Users/grushadharod/Desktop/file2.txt

public class Minhash {

  public Set<String> generateShingles(String content, int nGram){
    Set<String> shingles = new HashSet<>();        
    for(int i = 0; i <= content.length() - nGram; i++){
      String shingle = content.substring(i, i + nGram);      
      shingles.add(shingle);
    }    
    return shingles;
  }

  public int[] generateHashFunc(int nGram, int numHashFunc){
    int[] hashFunc = new int[numHashFunc];
    Random rand = new Random();

    for(int i = 0; i < numHashFunc; i++){
      int a = rand.nextInt(nGram) + 1;
      int b = rand.nextInt(nGram) + 1;
      hashFunc[i] = a * b;
    }
    return hashFunc;
  }

  public double calcJSim(String content1, String content2, int numHashFunc, int nGram, int numFiles){
    
    Set<String> set1 = generateShingles(content1, nGram);
    Set<String> set2 = generateShingles(content2, nGram);

    Set<String> allShingles = new HashSet<>();
    allShingles.addAll(set1);
    allShingles.addAll(set2);

    int sz = allShingles.size();

    int[] hashFunc = generateHashFunc(sz, numHashFunc);
    int[][] signatures = new int[numHashFunc][numFiles];

    for(int[] row: signatures){
      row[0] = Integer.MAX_VALUE;
      row[1] = Integer.MAX_VALUE;
    }

    for(String shingle: allShingles){
      int hashCode = shingle.hashCode();
      for(int i = 0; i < numHashFunc; i++){
        int hashValue = (hashFunc[i] * hashCode) % sz;
        if(hashValue < signatures[i][0]){
          signatures[i][0] = hashValue;
        }
        if(hashValue < signatures[i][1]){
          signatures[i][1] = hashValue;
        }
      }
    }

    int matches = 0;
    for(int i = 0; i < numHashFunc; i++){
      if(signatures[i][0] == signatures[i][1]){
        matches++;
      }
    }
    return (double)matches / (double)numHashFunc;
  }

  String readFile(String fileName){
    StringBuilder sb = new StringBuilder();
    try {
      File myObj = new File(fileName);
      Scanner myReader = new Scanner(myObj);
      while (myReader.hasNextLine()) {
        sb.append(myReader.nextLine());        
      }
      myReader.close();
    } catch (FileNotFoundException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
    return sb.toString();
  }
  public double jaccard(String fA, String fB) {
    /**
     * fA: Name of first file
     * fB: Name of second file
     */  
    // Your code goes here 
    int numHashFunc = 100, nGram = 5, numFiles = 2; 
    //read files
    String content1 = readFile(fA);
    System.out.println(content1);
    
    String content2 = readFile(fB);
    System.out.println(content2);

    return calcJSim(content1, content2, numHashFunc, nGram, numFiles);
  }

}
