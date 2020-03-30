package com.example.artemis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ProfanityFilter {

    static List<String> words = new ArrayList<>();
    static int largestWordLength = 0;

    public static void load() {
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new URL("https://docs.google.com/spreadsheets/d/1hIEi2YG3ydav1E06Bzf2mQbGZ12kh2fe4ISgLg_UBuM/export?format=csv")
                            .openConnection().getInputStream()));
            String line = "";
            int counter = 0;
            while ((line = reader.readLine()) != null) {
                counter++;
                String[] content = null;
                try {
                    content = line.split(",");
                    if (content.length == 0) {
                        continue;
                    }
                    String word = content[0];
                    if (word.length() > largestWordLength) {
                        largestWordLength = word.length();
                    }
                    words.add(word.replaceAll(" ", ""));

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            System.out.println("Loaded " + counter + " words to filter out");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void loadStaticList() {

        String[] listOfWords = {"sex", "porn", "wild", "mental"};

        String line = "";
        int counter = 0;
        for (String content : listOfWords) {
            counter++;
            String word = content;
            if (word.length() > largestWordLength) {
                largestWordLength = word.length();
            }
            words.add(word.replaceAll(" ", ""));
        }
        System.out.println("Loaded " + counter + " words to filter out");
    }

    public static ArrayList<String> badWordsFound(String input) {
        if (input == null) {
            return new ArrayList<>();
        }
        ArrayList<String> badWords = new ArrayList<>();
        input = input.toLowerCase().replaceAll("[^a-zA-Z]", "");

        // iterate over each letter in the word
        for (int start = 0; start <= input.length(); start++) {
            // from each letter, keep going to find bad words until either the end of the sentence is reached, or the max word length is reached.
            for (int offset = 1; offset < (input.length() + 1 - start) && offset <= largestWordLength; offset++) {
                String wordToCheck = input.substring(start, start + offset);
                if (words.contains(wordToCheck)) {
                    badWords.add(wordToCheck);
                    break;
                }
            }
        }

        for (String s : badWords) {
            System.out.println(s + " qualified as a bad word in a username");
        }
        return badWords;
    }

    public static Boolean isBadString(String input) {
        return badWordsFound(input).size() > 0 ? true : false;
    }

/*
    public static void main(String[] args) {
        ProfanityFilter.load();
        System.out.println(ProfanityFilter.isBadString("porn"));
    }
*/


}
