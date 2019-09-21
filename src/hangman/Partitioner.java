package hangman;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Partitioner {

    public Set<String> partition(Set<String> originalSet, char letter) {
        Map<String, Set<String>> myMap = new HashMap<>();

        //Generate and populate map with keys (word forms) and values (sets of possible words)
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

        //Find which set is the biggest
        int biggestSize = 0;
        Set<Set<String>> biggestCollections = new HashSet<>();
        for (Set<String> collection : myMap.values()) {
            if (collection.size() > biggestSize) {
                biggestSize = collection.size();
                biggestCollections.clear();
            }
            if (collection.size() == biggestSize) {
                biggestCollections.add(collection);
            }
        }

        System.out.println(biggestCollections);

        if (biggestCollections.size() == 1) { //Now that we have only the sets with the most words
            System.out.println("Only one");
            return biggestCollections.iterator().next();
        } else {
            System.out.println("More than one");

            //Check 1: See if there a set where the letter doesn't appear and choose that one
            for (Set<String> collection : biggestCollections) {
                if (containsGuessedLetter(collection, letter) == false) {
                    System.out.println("Found a collection, among the largest, that doesn't contain the guessed letter\n" + collection.toString());
                    return collection;
                }
            }

            //Check 2: Check to see if there is one with the least amount of instances of the guessed letter
            //TODO: see if there is a better way to put this out into it's own method
            int fewestLetters = 999;
            Set<Set<String>> temp = new HashSet<>();
            temp.addAll(biggestCollections);
            for (Set<String> collection : temp) {
                String s = collection.iterator().next();
                int i = s.length() - s.replaceAll(Character.toString(letter), "").length();
                if (i < fewestLetters) {
                    biggestCollections.clear();
                    fewestLetters = i;
                }
                if (i == fewestLetters) {
                    biggestCollections.add(collection);
                }
            }
            if (biggestCollections.size() == 1) {
                System.out.println("Found a system with the least instances of the guessed letter: " + biggestCollections.toString());
                return biggestCollections.iterator().next();
            }

            //TODO: Choose the one with the rightmost guessed letter (repeat till a group is chosen)
            while (biggestCollections.size() != 1) {
                biggestCollections = findRightMostSet(biggestCollections, letter, biggestCollections.iterator().next().iterator().next().length()); //Todo: make this not horrible
            }

            if (biggestCollections.size() == 1) {
                System.out.println("Found a system with the furthest right instance of the guessed letter: " + biggestCollections.toString());
                return biggestCollections.iterator().next();
            } else {


            }


            return null; //TODO: get rid of this

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

    private Set<Set<String>> findRightMostSet(Set<Set<String>> mySet, char c, int substringIndex){
        Set<Set<String>> temp = new HashSet<>();
        temp.addAll(mySet);
        mySet.clear();

        int largestIndex = 0;
        for (Set<String> set : temp) {
            String s = set.iterator().next();
            int i = findIndex(s.substring(0, substringIndex), c);
            if(i > largestIndex){
                mySet.clear();
                largestIndex = i;
            }
            if(i == largestIndex){
                mySet.add(set);
            }
        }

        if(mySet.size() == 1){
            return mySet;
        }
        else{
            return findRightMostSet(mySet, c, largestIndex - 1); //TODO: Make sure that this ' - 1' need to be here
        }

    }

}