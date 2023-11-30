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
        openFile("data.txt");
        readInData();
        dividedDiff();
        printTable();
        newtonForm();
        lagrangeForm();
        simplify();
    }
    
    public static void simplify() {
        System.out.println("Simplified Polynomial");
        System.out.println("-----------------------------------------------------------------------------------------------------------");
        Map<Integer, Double> polynomialTerms = new HashMap<>();
        
        polynomialTerms.put(0, diffTable[0][0]);
        for (int i = 1; i < n; i++) {
            Map<Integer, Double> currentTerm = new HashMap<>();
            currentTerm.put(0, diffTable[0][i]);

            for (int j = 0; j < i; j++) {
                currentTerm = expandTerm(currentTerm, x[j]);
            }

            for (Map.Entry<Integer, Double> entry : currentTerm.entrySet()) {
                polynomialTerms.merge(entry.getKey(), entry.getValue(), Double::sum);
            }
        }

        StringBuilder polynomial = new StringBuilder();
        polynomialTerms.entrySet().stream()
                .sorted(Map.Entry.<Integer, Double>comparingByKey().reversed())
                .forEach(entry -> {
                    double coef = entry.getValue();
                    int power = entry.getKey();
                    if (coef >= 0 && polynomial.length() > 0) {
                        polynomial.append(" + ");
                    }
                    if (coef < 0) {
                        polynomial.append(" - ");
                    }
                    polynomial.append(String.format("%.2f", Math.abs(coef)));
                    if (power > 0) {
                        polynomial.append("x");
                    }
                    if (power > 1) {
                        polynomial.append("^").append(power);
                    }
                });

        System.out.println(polynomial);
        System.out.println("-----------------------------------------------------------------------------------------------------------\n");
    }

    private static Map<Integer, Double> expandTerm(Map<Integer, Double> term, double xi) {
        Map<Integer, Double> expanded = new HashMap<>();

        term.forEach((power, coefficient) -> {
            expanded.merge(power + 1, coefficient, Double::sum);
            expanded.merge(power, -xi * coefficient, Double::sum);
        });

        return expanded;
    }

    public static void lagrangeForm(){
        System.out.println("Langrange Form");
        System.out.println("-----------------------------------------------------------------------------------------------------------");
        String polynomial, xTerms;
        double coef=0;
        polynomial = "";
        xTerms = "";
        boolean first = true;
        for(int i = 0; i < n; i++){
            if(i > 0)
                polynomial += " + ";
            for(int j = 0; j < n; j++){
                if(i!=j){
                    xTerms+=String.format("(x-(%.2f))",x[j]);
                    if(first){
                        coef = x[i]-x[j];
                        first = false;
                    }
                    else{
                        coef*= x[i]-x[j]; 
                    }
                }
            }
            coef = y[i]/coef;
            polynomial += String.format("(%.2f)%s", coef, xTerms);
            first = true;
            xTerms = "";
        }
        System.out.println(polynomial);
        System.out.println("-----------------------------------------------------------------------------------------------------------\n");
    }
    
    public static void newtonForm(){
        System.out.println("Newton Form");
        System.out.println("-----------------------------------------------------------------------------------------------------------");
        String polynomial;
        polynomial = String.format("%.2f", diffTable[0][0]);
        for(int i = 1; i < n; i++){
            polynomial += String.format(" + (%.2f)", diffTable[0][i]);
            for(int j = 0; j< i; j++){
                polynomial += String.format("(x-(%.2f))", x[j]);
            }
        }
        System.out.println(polynomial);
        System.out.println("-----------------------------------------------------------------------------------------------------------\n");
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
