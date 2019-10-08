//Zoe Sarwar cssc0735
//Stephanie Bekker cssc0754

package edu.sdsu.cs;


import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.lang.String;


public class App {
    public static void main(String[] args) throws IOException {
        String filePath = "";
        String pathToUse = "";

        if (args.length > 1) {
            System.out.println("Too Many Arguments");
            System.exit(1);
        }

        if (args.length == 0){
            pathToUse = "./";
        }
        else if (args.length == 1) {
            pathToUse = args[0];
        }

        File file = new File(pathToUse);
        if(file.isDirectory()){
            File[] files = file.listFiles();
            getFile(files, pathToUse);
        }
        else{
            filePath = file.getAbsolutePath();
            runAnalysis(filePath, pathToUse);
        }
    }

    private static void getFile(File[] files, String pathToUse){
        for (int i = 0; i < files.length; i++){
            if(files[i].isDirectory()){
                File[] newFiles = files[i].listFiles();
                getFile(newFiles, pathToUse);
            }
            else if (files[i].getName().endsWith(".txt")){
                String path = files[i].getAbsolutePath();
                runAnalysis(path, pathToUse);
            }
            else if (files[i].getName().endsWith(".java")){
                String path = files[i].getAbsolutePath();
                runAnalysis(path, pathToUse);
            }
        }
    }
    private static void runAnalysis(String filePath, String pathToUse){
        List<String> toWrite = new ArrayList<String>();
        Path file_path = Paths.get(filePath);
        try {
            List<String> lines = Files.readAllLines(file_path, Charset.defaultCharset());

            toWrite.add(String.format("%-67s %-15s", "The length of the longest line is: ",findLongestLine(lines)));
            toWrite.add(String.format("%-67s %-15s", "The average line length is: ", findAvgLineLength(lines)));
            toWrite.add(String.format("%-67s %-15s", "The number of tokens in this file is: ", findNumAllTokens(lines)));
            toWrite.add(String.format("%-67s %-15s", "The number of unique space-delineated tokes(case-sensitive) is: ", findNumUniqueTokens(lines)));
            toWrite.add(String.format("%-67s %-15s", "The number of unique space-delineated tokes(case-insensitive) is: ", findNumUniqueTokensCaseInsensitive(lines)));
            toWrite.add("The most frequently occurring token(case-insensitive) occurs " + findMostFrequentTokenCount(lines) + " times.");
            findMostFrequentToken(lines, toWrite);
            toWrite.add("The ten most frequent tokens(case-insensitive) and their counts are: ");
            findTenFrequentTokens(lines, toWrite);
            toWrite.add("The ten least frequent tokens(case-insensitive) and their counts are: ");
            findTenLeastFrequentTokens(lines, toWrite);

            File writeFile = new File(filePath+".stats");
            String writeFilePath = writeFile.getAbsolutePath();
            Path writeFile_Path = Paths.get(writeFilePath);
            writeToFile(writeFile_Path, toWrite);


        } catch (IOException e) {
            System.out.println(e);
        }
    }
    private static int findLongestLine(List<String> file) {

        int longestLine = 0;
        int newLength = 0;

        longestLine = file.get(0).trim().length();

        for (int i = 0; i < file.size(); i++) {
            newLength = file.get(i).trim().length();

            if (newLength > longestLine) {
                longestLine = newLength;
            }
        }
        return longestLine;
    }

    private static double findAvgLineLength(List<String> file) {
        double lengthSum = 0;
        double avgLength = 0;

        for (int i = 0; i < file.size(); i++) {
            lengthSum += file.get(i).trim().length();
        }
        avgLength = lengthSum / file.size();
        return avgLength;
    }

    private static int findNumAllTokens(List<String> file) {
        String temp;
        String[] arrString;
        int tokenCounter = 0;

        for (int i = 0; i < file.size(); i++) {
            temp = file.get(i).trim();
            arrString = temp.split(" ", -2);

            for (int j = 0; j < arrString.length; j++) {
                tokenCounter++;
            }
        }
        return tokenCounter;
    }

    private static int findMostFrequentToken(List<String> file, List<String> write) {
        List<String> tokens = new ArrayList<>();
        List<Integer> tokenCount = new ArrayList<>();
        String tempLine = "";
        String[] arrString = new String[100];
        String tempWord = arrString[0];
        int count = 0;
        int currNum = 0;
        int largestNum = 0;
        int largestNumIndex = 0;
        int tempIndex = 0;
        int listCount = 0;

        for (int i = 0; i < file.size(); i++) {
            tempLine = file.get(i).trim();
            arrString = tempLine.split(" ", -2);

            for (int j = 0; j < arrString.length; j++) {
                tempWord = arrString[j];
                if (!tokens.contains(tempWord)) {
                    tokens.add(listCount, tempWord);
                    count = 1;
                    tokenCount.add(listCount, count);
                    listCount++;
                } else {
                    tempIndex = tokens.indexOf(tempWord);
                    count = tokenCount.get(tempIndex) + 1;
                    tokenCount.set(tempIndex, count);
                }
            }
        }
        for (int k = 0; k < tokenCount.size(); k++) {
            currNum = tokenCount.get(k);
            if(currNum > largestNum) {
                largestNum = currNum;
                largestNumIndex = k;
            }
        }
        ArrayList<Integer> occurrences = indexOfAll(largestNum, (ArrayList<Integer>) tokenCount);
        if(occurrences.size() == 1){
            write.add("The most frequent token(case-sensitive) in this file is: " + tokens.get(largestNumIndex));
        } else {
            write.add("There are multiple tokens(case-sensitive) with the same occurrence: ");
            for(int y = 0; y < occurrences.size(); y++){
                write.add(tokens.get(occurrences.get(y)));
            }
        }
        return 0;
    }
    private static ArrayList<Integer> indexOfAll(Object highestOccurence, ArrayList<Integer> list){
        ArrayList<Integer> indexList = new ArrayList<Integer>();
        for (int i = 0; i < list.size(); i++)
            if(highestOccurence.equals(list.get(i)))
                indexList.add(i);
        return indexList;
    }

    private static int findMostFrequentTokenCount(List<String> file){
        List<String> tokens = new ArrayList<>();
        List<Integer> tokenCount = new ArrayList<>();
        String tempLine = "";
        String[] arrString = new String[100];
        String tempWord = arrString[0];
        int count = 0;
        int currNum = 0;
        int largestNum = 0;
        int tempIndex = 0;
        int listCount = 0;

        for (int i = 0; i < file.size(); i++) {
            tempLine = file.get(i).trim().toLowerCase();
            arrString = tempLine.split(" ", -2);

            for (int j = 0; j < arrString.length; j++) {
                tempWord = arrString[j];
                if (!tokens.contains(tempWord)) {
                    tokens.add(listCount, tempWord);
                    count = 1;
                    tokenCount.add(listCount, count);
                    listCount++;
                } else {
                    tempIndex = tokens.indexOf(tempWord);
                    count = tokenCount.get(tempIndex) + 1;
                    tokenCount.set(tempIndex, count);
                }
            }
        }
        for (int k = 0; k < tokenCount.size(); k++) {
            currNum = tokenCount.get(k);
            if(currNum > largestNum) {
                largestNum = currNum;
            }
        }
        return largestNum;
    }

    private static int findNumUniqueTokens(List<String> file){
        List<String> tokens = new ArrayList<>();
        String tempLine = "";
        String[] arrString = new String[100];
        String tempWord = arrString[0];
        int listCount = 0;

        for (int i = 0; i < file.size(); i++) {
            tempLine = file.get(i).trim();
            arrString = tempLine.split(" ", -2);

            for (int j = 0; j < arrString.length; j++) {
                tempWord = arrString[j];
                if (!tokens.contains(tempWord)) {
                    tokens.add(j, tempWord);
                }
            }
        }
        return tokens.size();
    }

    private static int findNumUniqueTokensCaseInsensitive(List<String> file){
        List<String> tokens = new ArrayList<>();
        String tempLine = "";
        String[] arrString = new String[100];
        String tempWord = arrString[0];
        int listCount = 0;

        for (int i = 0; i < file.size(); i++) {
            tempLine = file.get(i).trim().toLowerCase();
            arrString = tempLine.split(" ", -2);

            for (int j = 0; j < arrString.length; j++) {
                tempWord = arrString[j];
                if (!tokens.contains(tempWord)) {
                    tokens.add(j, tempWord);
                }
            }
        }
        return tokens.size();
    }
    private static int findTenFrequentTokens(List<String> file, List<String> write) {
        List<String> tokens = new ArrayList<>();
        List<Integer> tokenCount = new ArrayList<>();
        String tempLine = "";
        String[] arrString = new String[100];
        String tempWord = arrString[0];
        int count = 0;
        int currNum = 0;
        int largestNum = 0;
        int tempIndex = 0;
        int listCount = 0;
        int largestNumIndex = 0;

        for (int i = 0; i < file.size(); i++) {
            tempLine = file.get(i).trim().toLowerCase();
            arrString = tempLine.split(" ", -2);

            for (int j = 0; j < arrString.length; j++) {
                tempWord = arrString[j];
                if (!tokens.contains(tempWord)) {
                    tokens.add(listCount, tempWord);
                    count = 1;
                    tokenCount.add(listCount, count);
                    listCount++;
                } else {
                    tempIndex = tokens.indexOf(tempWord);
                    count = tokenCount.get(tempIndex) + 1;
                    tokenCount.set(tempIndex, count);
                }
            }
        }
        for(int y = 0; y < 10; y++){
            for (int k = 0; k < tokenCount.size(); k++) {
                currNum = tokenCount.get(k);
                if(currNum > largestNum) {
                    largestNum = currNum;
                    largestNumIndex = k;
                }
            }
            write.add(String.format("%s %-15s %s %-5d", "Token:", tokens.get(largestNumIndex), "Count:", tokenCount.get(largestNumIndex)));
            tokenCount.remove(largestNumIndex);
            tokens.remove(largestNumIndex);
            largestNum = 0;
        }
        return 0;
    }
    private static int findTenLeastFrequentTokens(List<String> file, List<String> write) {
        List<String> tokens = new ArrayList<>();
        List<Integer> tokenCount = new ArrayList<>();
        String tempLine = "";
        String[] arrString = new String[100];
        String tempWord = arrString[0];
        int count = 0;
        int currNum = 0;
        int smallestNum = 1;
        int tempIndex = 0;
        int listCount = 0;
        int smallestNumIndex = 0;

        for (int i = 0; i < file.size(); i++) {
            tempLine = file.get(i).trim().toLowerCase();
            arrString = tempLine.split(" ", -2);

            for (int j = 0; j < arrString.length; j++) {
                tempWord = arrString[j];
                if (!tokens.contains(tempWord)) {
                    tokens.add(listCount, tempWord);
                    count = 1;
                    tokenCount.add(listCount, count);
                    listCount++;
                } else {
                    tempIndex = tokens.indexOf(tempWord);
                    count = tokenCount.get(tempIndex) + 1;
                    tokenCount.set(tempIndex, count);
                }
            }
        }
        for(int y = 0; y < 10; y++){
            for (int k = 0; k < tokenCount.size(); k++) {
                if(tokenCount.contains(smallestNum)){
                    currNum = tokenCount.get(k);
                    if(currNum == smallestNum) {
                        smallestNumIndex = k;
                    }
                } else{
                    smallestNum++;
                    k--;
                }
            }
            write.add(String.format("%s %-15s %s %-5d", "Token:", tokens.get(smallestNumIndex), "Count:", tokenCount.get(smallestNumIndex)));
            tokenCount.remove(smallestNumIndex);
            tokens.remove(smallestNumIndex);
        }
        return 0;
    }

    private static void writeToFile( Path location, List<String> toWrite ) throws IOException {
        Files.write(location,toWrite,Charset.defaultCharset());
    }
}