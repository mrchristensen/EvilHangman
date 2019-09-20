package hangman;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedSet;

public class EvilHangmanGame implements IEvilHangmanGame {
    private Set<String> myDictionary = new HashSet<>();
    private Set<Character> guessedLetters = new HashSet<>();
    /**
     * Starts a new game of evil hangman using words from <code>dictionary</code>
     * with length <code>wordLength</code>.
     * <p>
     * This method should set up everything required to play the game,
     * but should not actually play the game. (ie. There should not be
     * a loop to prompt for input from the user.)
     *
     * @param dictionary Dictionary of words to use for the game
     * @param wordLength Number of characters in the word to guess
     * @throws IOException              if the dictionary does not exist or an error occurs when reading it.
     * @throws EmptyDictionaryException if the dictionary does not contain any words.
     */
    @Override
    public void startGame(File dictionary, int wordLength) throws IOException, EmptyDictionaryException {
        if(dictionary.exists() == false){
            throw new IOException();
        }

        Scanner scanner = new Scanner(dictionary);

        while(scanner.hasNext()){
            String tempWord = scanner.next().toLowerCase(); //Sanitize input (all lower case)

            if(tempWord.length() == wordLength){ //If the word we're looking at it the right length add it to the set
                myDictionary.add(tempWord);
            }
        }

        if(myDictionary.size() == 0){
            throw new EmptyDictionaryException();
        }

    }

    /**
     * Make a guess in the current game.
     *
     * @param guess The character being guessed
     * @return The set of strings that satisfy all the guesses made so far
     * in the game, including the guess made in this call. The game could claim
     * that any of these words had been the secret word for the whole game.
     * @throws GuessAlreadyMadeException if the character <code>guess</code>
     *                                   has already been guessed in this game.
     */
    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
        return null;
    }

    /**
     * Returns the set of previously guessed letters, in alphabetical order.
     *
     * @return the previously guessed letters.
     */
    @Override
    public SortedSet<Character> getGuessedLetters() {
        return null;
    }
}
