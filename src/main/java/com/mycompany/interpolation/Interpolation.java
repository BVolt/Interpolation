/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.interpolation;
import java.io.*;
import java.util.*;

public class Interpolation {
    private static BufferedReader inFile;
    private static double[] x;
    private static double[] y;
    private static double[][] diffTable;
    private static int n;


    public static void main(String[] args) {
        openFile("data-1.txt");
        readInData();
        dividedDiff();
        printTable();
        newtonForm();
        lagrangeForm();
    }
    
    public static void lagrangeForm(){
        String polynomial;
        double coef=0;
        polynomial = "";
        boolean first = true;
        for(int i = 0; i < n; i++){
            
            for(int j = 0; j < n; j++){
                
                if(i!=j){
                    if(first){
                        coef = x[i]-x[j];
                        first = false;
                    }
                    else{
                        coef*= x[i]-x[j]; 
                    }
                }
            }
            System.out.println(diffTable[0][i]/coef);
            first = true;
        }
        System.out.println(polynomial);
    }
    
    public static void newtonForm(){
        String polynomial;
        polynomial = String.format("%.2f", diffTable[0][0]);
        for(int i = 1; i < n; i++){
            polynomial += String.format(" + (%.2f)", diffTable[0][i]);
            for(int j = 0; j< i; j++){
                polynomial += String.format("(x-%.2f)", x[j]);
            }
        }
        System.out.println(polynomial);
    }
    
    public static void printTable(){
        String fString, colHeader;
        fString = String.format("%-15s%-15s", "x", "f[]");
        for(int i = 1; i < n; i++){
            colHeader = "f[";
            for(int j = 0; j < i; j++){
                colHeader +=","; 
            }
            colHeader+="]";
            fString+= String.format("%-15s", colHeader);
        }
        System.out.println("Divided Differences");
        System.out.println("-----------------------------------------------------------------------------------------------------------");
        System.out.println(fString);
        System.out.println("-----------------------------------------------------------------------------------------------------------\n");
        for(int i = 0; i < n; i++){
            System.out.printf("%-15.2f", x[i]);
            for(int j = 0; j < n; j++){
                if(diffTable[i][j] == 0 && j >= n-i)
                    System.out.printf("%-15s", "");
                else
                    System.out.printf("%-15.2f ", diffTable[i][j]);
            }
            System.out.println();
        }
        System.out.println("-----------------------------------------------------------------------------------------------------------\n");
    
    }
    
    public static void dividedDiff(){
        diffTable = new double[n][n];
        for(int i = 0; i < n; i++){
            diffTable[i][0] = y[i];
        }
        for(int i = 1; i < n; i++){
            for(int j = 0; j < n-i; j++){
                diffTable[j][i] = (diffTable[j][i-1] - diffTable[j+1][i-1])/(x[j]-x[i+j]);
            }
        }
    }
    
    public static void readInData(){
        String line;
        String[] splitline;
        try{
            line = inFile.readLine();
            splitline = line.split(" ", 11);
            n = splitline.length;
            x = new double[n];
            y = new double[n];
            for(int i = 0; i < n; i++){
                x[i] = Double.parseDouble(splitline[i]);
            }
            line = inFile.readLine();
            splitline = line.split(" ", 11);
            for(int i = 0; i < n; i++){
                y[i] = Double.parseDouble(splitline[i]);
            }
        }catch(IOException e){
        
        }
    }
    
    public static void openFile(String s){
        try{
            inFile = new BufferedReader(new FileReader(s));
        }catch(IOException e){
        
        }
    }
}
