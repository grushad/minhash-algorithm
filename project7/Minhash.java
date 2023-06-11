package project7;

import java.util.*;
import java.io.*;

//file1 : /Users/grushadharod/Desktop/file1.txt
//file2 : /Users/grushadharod/Desktop/file2.txt
//java project7.Main /Users/grushadharod/Desktop/file1.txt /Users/grushadharod/Desktop/file2.txt

public class Minhash {

  public void generateShingles(String content, Set<Integer> shingles){
    int nGram = 3;  
    String[] arr = content.split(" ");
    for(int i = 0; i < arr.length - nGram; i++){
      String shingle = arr[i] +  " " + arr[i + 1] + " " + arr[i + 2];
      shingles.add(shingle.hashCode());
    }    
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

  public double calcJSim(String content1, String content2, int numHashFunc, int numFiles){
    
    Set<Integer> set1 = new HashSet<>();
    generateShingles(content1, set1);
    
    Set<Integer> set2 = new HashSet<>();
    generateShingles(content2, set2);

    int[] aCoeff = generateHashFunc(numHashFunc);
    int[] bCoeff = generateHashFunc(numHashFunc);

    int[][] signatures = new int[numHashFunc][numFiles];

    int prime = 67957;
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
    System.out.println(matches);
    // double ans = (double)matches / (double)numHashFunc;
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
    int numHashFunc = 1000, numFiles = 2;     
    String content1 = readFile(fA);    
    String content2 = readFile(fB);

    return calcJSim(content1, content2, numHashFunc, numFiles);
  }

}
