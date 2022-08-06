package hangman;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedSet;

public class EvilHangman {

    public static void main(String[] args) throws IOException, EmptyDictionaryException{

        if(args.length < 3){
            throw new IllegalArgumentException("Three arguments are required.");
        }
        String dictionaryFilename = args[0];
        int wordLength = Integer.parseInt(args[1]);
        int numGuesses = Integer.parseInt(args[2]);

        File myDictionary = new File(dictionaryFilename);
        EvilHangmanGame myGame = new EvilHangmanGame();
        Scanner input = new Scanner(System.in);
        String myWord;
        Set<String> oldSet = new HashSet<>();
        Set<String> newSet;

        myGame.startGame(myDictionary, wordLength);

        while(numGuesses > 0) {
            System.out.printf("You have %d guesses left\nUsed letters: ", numGuesses);
            SortedSet<Character> guessedLetters = myGame.getGuessedLetters();
            for (Character letter : guessedLetters) {
                System.out.print(letter + " ");
            }

            myWord = myGame.getWordTemplate();
            System.out.printf("\b\nWord: %s\nEnter guess: ", myWord);

            String tempGuess = input.nextLine();

            //Check for invalid input
            if(tempGuess == null || tempGuess.length() != 1 || !Character.isAlphabetic(tempGuess.charAt(0))){ //Checks to see if alpha
                System.out.printf("Invalid Input\n");
                continue;
            }

            try {
                newSet = myGame.makeGuess(tempGuess.charAt(0));
            } catch (GuessAlreadyMadeException e) {
                System.out.printf("You already used that letter\n");
                continue;
            }

            //Check to see if we guessed a word
            if (myWord.equals(myGame.getWordTemplate())){
                System.out.printf("Sorry, there are no %s's\n\n", tempGuess);
                numGuesses--;
            }
            else{
                int i = myGame.getWordTemplate().length() - myGame.getWordTemplate().replaceAll(tempGuess, "").length();
                System.out.printf("Yes, there is %d %s\n\n", i, tempGuess);
            }

            if(!myGame.getWordTemplate().contains("-")){
                System.out.printf("You win! %s", myGame.getWordTemplate());
                break;
            }
            if(numGuesses == 0){
                System.out.printf("You lose!\nThe word was: %s\n", newSet.iterator().next());
                break;
            }

            myWord = myGame.getWordTemplate();
            oldSet.clear();
            oldSet.addAll(newSet);

        }


    }

}
