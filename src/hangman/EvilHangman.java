package hangman;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedSet;

public class EvilHangman {

    public static void main(String[] args) throws IOException, EmptyDictionaryException, GuessAlreadyMadeException {

        if(args.length < 3){
            throw new IllegalArgumentException("Three arguments are required.");
        }
        String dictionaryFilename = args[0];
        int wordLength = Integer.parseInt(args[1]);
        int numGuesses = Integer.parseInt(args[2]);

        File myDictionary = new File(dictionaryFilename);
        EvilHangmanGame myGame = new EvilHangmanGame();
        Scanner input = new Scanner(System.in);
        String myWord = generateWord(wordLength);
        Set<String> oldSet = new HashSet<>();

        myGame.startGame(myDictionary, wordLength);

        while(numGuesses > 0) {
            System.out.printf("You have %d guesses left\nUsed letters: ", numGuesses);
            SortedSet<Character> guessedLetters = myGame.getGuessedLetters();
            for (Character letter : guessedLetters) {
                System.out.print(letter + " ");
            }
            System.out.printf("\b\nWord: %s\nEnter guess: ", myWord);

            char[] tempGuess = input.next().toLowerCase().toCharArray(); //Input

            if(tempGuess.length != 1){
                //TODO: Throw excpetion: invalid entry for a guess (needs to be just one character)
            }

            Set<String> newSet = myGame.makeGuess(tempGuess[0]);

        }
    }

    private static String generateWord(int wordLength){
        String word = "";
        for (int i = 0; i < wordLength; i++) {
            word += "-";
        }
        return word;
    }

}
