//import java.org.json.JSONArray;
import java.util.*;
import java.net.URLConnection;
import java.net.HttpURLConnection;
//import java.io.*;


public class code2040 {


    public static void main(String[] args) {
        System.out.println("*************************");
        //System.out.println(flipString("cups"));
        String[] haystack = {"red", "yellow", "pink", "green", "purple", "orange", "blue"};
        for(String straw: haystack){
            //System.out.println(straw);
        }
        String needle = "purple";
        //System.out.println(needle);
        //System.out.println(findNeedle(needle, haystack));
        String prefix = "ch";
        String[] array = {"chimp", "chump", "chisle", "bike", "bottle", "envelope", "chapter", "salt", "bag", "belt", "charter", "lamp"};
        startsWith(prefix, array);
    }

    private static String flipString(final String toReverse){
        String toReturn = "";
        char[] flipFlop = toReverse.toCharArray();
        for(int a = 0; a < toReverse.length(); a++){
            toReturn+=flipFlop[toReverse.length()-1-a];
        }
        return toReturn;
    }
    
    private static int findNeedle(final String needle, final String[] haystack){
        int location = 0;
        for(String straw: haystack){
            if(straw.equals(needle)){
                return location;
            }
            else{
                location++;
            }
        }
        return -1;
    }
    
    private static String[] startsWith(String prefix, String[] array){
        String[] results = new String[10];
        int size = 0;
        int capacity = 10;
        for(String potential: array){
            if(potential.startsWith(prefix)){
                if(size < capacity){
                  results[size] = potential;
                  size++;
                }
                else{
                    String[] temp = new String[capacity*2];
                    for(int a = 0; a < capacity; a++){
                        temp[a] = results[a];
                    } 
                    capacity*=2;
                    results = temp;
                    //temp = null;
                }
            }
        }
        for(String starts: results){
            System.out.print(starts);
        }
        return results;
    }
    
    private static String secondDateStamp(String datestamp, int interval){
    }
    
    private static String getToken(String target){
        HttpURLConnection connection = null;
        try{
            URL url = new URL(target);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("email", "caraha@stanford.edu");
            connection.setRequestProperty("github", 
        }
    }
}

