package hangman;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class EvilHangmanGame implements IEvilHangmanGame {

    private Set<String> myDictionary = new HashSet<>();
    Partitioner myPartitioner = new Partitioner();
    private SortedSet<Character> guessedLetters = new TreeSet<>();

    @Override
    public void startGame(File dictionary, int wordLength) throws IOException, EmptyDictionaryException {
        if(dictionary.exists() == false){
            throw new IOException();
        }

        Scanner scanner = new Scanner(dictionary);

        while(scanner.hasNext()){
            String tempWord = scanner.next().toLowerCase(); //Sanitize input (all lower case)

            if(tempWord.length() == wordLength){ //If the word we're looking at it the right length add it to the set
                System.out.println(tempWord);
                myDictionary.add(tempWord);
            }
        }

        if(myDictionary.size() == 0){
            throw new EmptyDictionaryException();
        }

    }

    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {

        //Check to see if we've already guess the letter
        if(guessedLetters.contains(guess)){
            throw new GuessAlreadyMadeException();
        }

        guessedLetters.add(guess);
        myDictionary = myPartitioner.partition(myDictionary, guess);



        return myDictionary;
    }

    /**
     * Returns the set of previously guessed letters, in alphabetical order.
     *
     * @return the previously guessed letters.
     */
    @Override
    public SortedSet<Character> getGuessedLetters() {
        return guessedLetters;
    }

    public String getWordTemplate(){
        String templateWord = myPartitioner.getWordTemplate();

        if (templateWord == null){
            templateWord = "";
            for (int i = 0; i < myDictionary.iterator().next().length(); i++) {
                templateWord += "-";
            }
            return templateWord;
        }
        else{
            return templateWord;
        }
    }

}
