package hangman;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class EvilHangmanGame implements IEvilHangmanGame {

    private Set<String> myDictionary = new HashSet<>();
    private String templatedWord = "";
    private int wordLength;
    private SortedSet<Character> guessedLetters = new TreeSet<>();

    @Override
    public void startGame(File dictionary, int wordLength) throws IOException, EmptyDictionaryException {
        myDictionary = new HashSet<>();
        templatedWord = "";
        this.wordLength = wordLength;
        guessedLetters = new TreeSet<>();


        if(dictionary.exists() == false){
            throw new IOException();
        }

        Scanner scanner = new Scanner(dictionary);

        while(scanner.hasNext()){
            String tempWord = scanner.next().toLowerCase(); //Sanitize input (all lower case)

            if(tempWord.length() == wordLength){ //If the word we're looking at it the right length add it to the set
                //System.out.println(tempWord);
                myDictionary.add(tempWord);
            }
        }

        if(myDictionary.size() == 0){
            throw new EmptyDictionaryException();
        }

        //Generates the initial template word
        for (int i = 0; i < wordLength; i++) {
            templatedWord += "-";
        }

        //templatedWord = new String(new char[wordLength]).replace('\0', '-');

    }

    @Override
    public Set<String> makeGuess(char guessedLetter) throws GuessAlreadyMadeException {

        guessedLetter = Character.toLowerCase(guessedLetter);
        //Check to see if we've already guess the letter
        if (guessedLetters.contains(guessedLetter)) {
            throw new GuessAlreadyMadeException();
        }
        guessedLetters.add(guessedLetter);

        Map<String, Set<String>> myMap = new HashMap<>();


        //Generate and populate map with keys (word forms) and values (sets of possible words)


        for (String word : myDictionary) {

            String tempKey = "";

            for (int i = 0; i < wordLength; i++) {
                if (templatedWord.toCharArray()[i] == '-' && word.toCharArray()[i] == guessedLetter) {
                    tempKey += guessedLetter;
                } else {
                    tempKey += templatedWord.toCharArray()[i];
                }

            }

            if (myMap.get(tempKey) == null) {
                myMap.put(tempKey, new HashSet<>());
            }
            myMap.get(tempKey).add(word);
        }


        //Find which set is the biggest
        Map<String, Set<String>> tempMap = new HashMap<>();
        tempMap.putAll(myMap);
        myMap.clear();

        int biggestSize = 0;
        for (Map.Entry<String, Set<String>> entry : tempMap.entrySet()) {
            if (entry.getValue().size() > biggestSize) {
                myMap.clear();
                biggestSize = entry.getValue().size();
            }
            if(entry.getValue().size() == biggestSize){
                myMap.put(entry.getKey(), entry.getValue());
            }
        }

        if (myMap.size() == 1) { //Now that we have only the sets with the most words
            //System.out.println("Only one: " + myMap);
            templatedWord = myMap.keySet().iterator().next();
            myDictionary.clear();
            myDictionary.addAll(myMap.values().iterator().next());
            return myDictionary;
        } else {
            //System.out.println("More than one");

            //Check 1: See if there a set where the letter doesn't appear and choose that one
            for (Map.Entry<String, Set<String>> entry : myMap.entrySet()) {
                if (containsGuessedLetter(entry.getValue(), guessedLetter) == false) {
                    myMap.clear();
                    myMap.put(entry.getKey(), entry.getValue());
                    //System.out.println("Found a collection, among the largest, that doesn't contain the guessed letter\n" + myMap);
                    templatedWord = myMap.keySet().iterator().next();
                    myDictionary.clear();
                    myDictionary.addAll(myMap.values().iterator().next());
                    return myDictionary;
                }
            }

            //Check 2: Check to see if there is one with the least amount of instances of the guessed letter
            int fewestLetters = 999;
            tempMap.clear();
            tempMap.putAll(myMap);
            for (Set<String> mySet : myMap.values()) {
                String s = mySet.iterator().next();
                int i = s.length() - s.replace(Character.toString(guessedLetter), "").length();
                if (i < fewestLetters) {
                    fewestLetters = i;
                }
            }
            for (Map.Entry<String, Set<String>> entry : tempMap.entrySet()) {
                String s = entry.getValue().iterator().next();
                int i = s.length() - s.replaceAll(Character.toString(guessedLetter), "").length();
                if (i > fewestLetters) {
                    myMap.remove(entry.getKey(), entry.getValue());
                    //System.out.println("Should've removed this entry:" + entry + ", so here's the map: " + myMap);
                }
            }
            if (myMap.size() == 1) {
                //System.out.println("Found a system with the least instances of the guessed letter: " + myMap);
                templatedWord = myMap.keySet().iterator().next();
                myDictionary.clear();
                myDictionary.addAll(myMap.values().iterator().next());
                return myDictionary;
            }

            //Check 3: Choose the one with the rightmost guessed letter (repeat till a group is chosen)
            findRightMostSet(myMap, guessedLetter);

            //System.out.println("Found a system with the furthest right instance of the guessed letter: " + myMap);
            templatedWord = myMap.keySet().iterator().next();
            myDictionary.clear();
            myDictionary.addAll(myMap.values().iterator().next());
            return myDictionary;

        }


        //return myDictionary;
    }

    @Override
    public SortedSet<Character> getGuessedLetters() {
        return guessedLetters;
    }

    String getWordTemplate() {
        return templatedWord;
    }

    private boolean containsGuessedLetter(Set<String> mySet, char letter) {
        for (String s : mySet) {
            if (s.contains(Character.toString(letter))) {
                return true;
            }
        }
        return false;
    }

    private int findIndex(String s, char c) {
        for (int i = s.length() - 1; i > -1; i--) {
            if (s.charAt(i) == c) {
                return i;
            }
        }
        return 0;
    }

    private void findRightMostSet(Map<String, Set<String>> myMap, char c) {

        Map<String, Set<String>> tempMap = new HashMap<>();
        tempMap.putAll(myMap);
        myMap.clear();

        int furthestIndex = 0;

        //System.out.printf("The totally length of the word: %d\n", tempMap.values().iterator().next().iterator().next().length() - 1);
        int temp = tempMap.values().iterator().next().iterator().next().length() - 1;
        for (int i = temp; i > 0; i--) {

            for (Map.Entry<String, Set<String>> entry : tempMap.entrySet()) {
                int tempFurthest = entry.getValue().iterator().next().lastIndexOf(c, i);
                if (tempFurthest > furthestIndex) {
                    furthestIndex = tempFurthest;
                    myMap.clear();
                }
                if (tempFurthest == furthestIndex) {
                    myMap.put(entry.getKey(), entry.getValue());
                }

            }

            if (myMap.size() == 1) {
                return;
            } else {
                //System.out.println("Another loop is requires.  Map currently: " + myMap);
                furthestIndex = 0;
                tempMap.clear();
                tempMap.putAll(myMap);
                myMap.clear();
            }

        }
    }

}
