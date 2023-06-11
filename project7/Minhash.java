package project7;

import java.util.*;
import java.io.*;

//file1 : /Users/grushadharod/Desktop/file1.txt
//file2 : /Users/grushadharod/Desktop/file2.txt
//java project7.Main /Users/grushadharod/Desktop/file1.txt /Users/grushadharod/Desktop/file2.txt

public class Minhash {

  public void generateShingles(String content, Set<Integer> shingles){
    int nGram = 2;
    //Set<String> shingles = new HashSet<>();   
    String[] arr = content.split(" ");
    for(int i = 0; i < arr.length - nGram; i++){
      String shingle = arr[i] +  " " + arr[i + 1] + " " + arr[i + 2];//content.substring(i, i + nGram);      
      shingles.add(shingle.hashCode());
    }
    //return shingles;
  }

  public int[] generateHashFunc(int numHashFunc){
    int limit = Integer.MAX_VALUE;
    int[] hashFunc = new int[numHashFunc];
    Random rand = new Random();
    Set<Integer> uniq = new HashSet<>();
    for(int i = 0; i < numHashFunc; i++){
      int a = rand.nextInt(limit);
      while(uniq.contains(a)){
        a = rand.nextInt(limit);
      }
      uniq.add(a);
      hashFunc[i] = a;
    }
    return hashFunc;
  }

  public double calcJSim(String content1, String content2, int numHashFunc, int nGram, int numFiles){
    
    Set<Integer> set1 = new HashSet<>();
    generateShingles(content1, set1);
    System.out.println(set1);
    
    Set<Integer> set2 = new HashSet<>();
    generateShingles(content2, set2);
    System.out.println(set2);    

   //Set<String> allShingles = new HashSet<>();
    // allShingles.addAll(set1);
    // allShingles.addAll(set2);

    // int sz = shingles.size();
    // System.out.println(sz);

    int[] aCoeff = generateHashFunc(numHashFunc);
    int[] bCoeff = generateHashFunc(numHashFunc);

    int[][] signatures = new int[numHashFunc][numFiles];

    // for(int[] row: signatures){
    //   row[0] = Integer.MAX_VALUE;
    //   row[1] = Integer.MAX_VALUE;
    // }

    int prime = 42949673;

    // for(String shingle: allShingles){
    //   int hashCode = shingle.hashCode();
    //   // System.out.println(hashCode);
    //   for(int i = 0; i < numHashFunc; i++){
    //     int hashValue = (hashFunc[i] * hashCode) % prime; // % sz
    //     // System.out.println(hashValue);
    //     if(hashValue < signatures[i][0]){
    //       signatures[i][0] = hashValue;
    //     }
    //     if(hashValue < signatures[i][1]){
    //       signatures[i][1] = hashValue;
    //     }
    //   }
    // }
    // int minHashCode = prime + 1;
    for(int j = 0; j < numHashFunc; j++){
      int minHashCode = prime + 1;
      for(Integer shingle: set1){
        int hashCode = (aCoeff[j] * shingle + bCoeff[j]) % prime;
        if(hashCode < minHashCode){
          minHashCode = hashCode;
        }
      }
      signatures[j][0] = minHashCode;
    }

    for(int j = 0; j < numHashFunc; j++){
      int minHashCode = prime + 1;
      for(Integer shingle: set2){
        int hashCode = (aCoeff[j] * shingle + bCoeff[j]) % prime;
        if(hashCode < minHashCode){
          minHashCode = hashCode;
        }
      }
      signatures[j][1] = minHashCode;
    }

    int matches = 0;
    for(int i = 0; i < numHashFunc; i++){
      if(signatures[i][0] == signatures[i][1]){
        matches++;
      }
    }
    // System.out.println(matches);
    return (double)matches / (double)numHashFunc;
  }

  String readFile(String fileName){
    StringBuilder sb = new StringBuilder();
    try {
      File myObj = new File(fileName);
      Scanner myReader = new Scanner(myObj);
      while (myReader.hasNextLine()) {
        sb.append(myReader.nextLine() + " ");        
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
