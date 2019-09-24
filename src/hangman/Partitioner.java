package hangman;

import java.util.*;

public class Partitioner {

    private String templatedWord = "";
    private SortedSet<Character> guessedLetters = new TreeSet<>();

    public Set<String> partition(Set<String> originalSet, char letter) {
        Map<String, Set<String>> myMap = new HashMap<>();

        guessedLetters.add(letter);

        //Generate and populate map with keys (word forms) and values (sets of possible words)
        if (myMap.size() == 0) {

            for (String word : originalSet) {
                String tempKey = "";

                for (int i = 0; i < word.length(); i++) {
                    if (word.toCharArray()[i] == letter) {
                        tempKey += letter;
                    } else {
                        tempKey += "-";
                    }

                }

                if (myMap.get(tempKey) == null) {
                    myMap.put(tempKey, new HashSet<>());
                }
                myMap.get(tempKey).add(word);
            }
        }
        else{
            Map<String, Set<String>> tempMap = new HashMap<>();
            tempMap.putAll(myMap);

            for (Map.Entry<String, Set<String>> stringSetEntry : tempMap.entrySet()) {
                
            }
        }


        //Find which set is the biggest
        Map<String, Set<String>> tempMap = new HashMap<>();


        int biggestSize = 0;
        for (Set<String> collection : myMap.values()) {
            if (collection.size() > biggestSize) {
                biggestSize = collection.size();
            }
        }
        for (Map.Entry<String, Set<String>> entry : myMap.entrySet()) {
            if (entry.getValue().size() > biggestSize) {
                myMap.remove(entry);
            }
        }


        if (myMap.size() == 1) { //Now that we have only the sets with the most words
            System.out.println("Only one: " + myMap);
            return myMap.values().iterator().next();
        } else {
            System.out.println("More than one");

            //Check 1: See if there a set where the letter doesn't appear and choose that one
            for (Map.Entry<String, Set<String>> entry : myMap.entrySet()) {
                if (containsGuessedLetter(entry.getValue(), letter) == false) {
                    myMap.clear();
                    myMap.put(entry.getKey(), entry.getValue());
                    System.out.println("Found a collection, among the largest, that doesn't contain the guessed letter\n" + myMap);
                    return myMap.values().iterator().next();
                }
            }

            //Check 2: Check to see if there is one with the least amount of instances of the guessed letter
            //TODO: see if there is a better way to put this out into it's own method
            int fewestLetters = 999;
            tempMap.clear();
            tempMap.putAll(myMap);
            for (Set<String> mySet : myMap.values()) {
                String s = mySet.iterator().next();
                int i = s.length() - s.replaceAll(Character.toString(letter), "").length();
                if (i < fewestLetters) {
                    fewestLetters = i;
                }
            }
            for (Map.Entry<String, Set<String>> entry : tempMap.entrySet()) {
                String s = entry.getValue().iterator().next();
                int i = s.length() - s.replaceAll(Character.toString(letter), "").length();
                if (i > fewestLetters) {
                    myMap.remove(entry.getKey(), entry.getValue());
                    System.out.println("Should've removed this entry:" + entry + ", so here's the map: " + myMap);
                }
            }
            if (myMap.size() == 1) {
                System.out.println("Found a system with the least instances of the guessed letter: " + myMap);
                return myMap.values().iterator().next();
            }

            //Check 3: Choose the one with the rightmost guessed letter (repeat till a group is chosen)
            findRightMostSet(myMap, letter);

            System.out.println("Found a system with the furthest right instance of the guessed letter: " + myMap);
            return myMap.values().iterator().next();

        }
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
        tempMap.clear();
        tempMap.putAll(myMap);
        myMap.clear();

        int furthestIndex = 0;

        //System.out.printf("The totaly length of the word: %d\n", tempMap.values().iterator().next().iterator().next().length() - 1);
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
                System.out.println("Another loop is requires.  Map currently: " + myMap);
                furthestIndex = 0;
                tempMap.clear();
                tempMap.putAll(myMap);
                myMap.clear();
            }

        }

    }

    public String getWordTemplate() {
//        if (myMap.isEmpty()) {
//            return null;
//        }
//        return myMap.keySet().iterator().next().toString();
        return templatedWord;
    }



}