package hangman;

import java.io.File;
import java.io.IOException;

public class EvilHangman {

    public static void main(String[] args) throws IOException, EmptyDictionaryException {

        if(args.length < 3){
            throw new IllegalArgumentException("Three arguments are required.");
        }
        String dictionaryFilename = args[0];
        int wordLength = Integer.parseInt(args[1]);
        int numGuesses = Integer.parseInt(args[2]);

        File myDictionary = new File(dictionaryFilename);

        EvilHangmanGame myGame = new EvilHangmanGame();

        myGame.startGame(myDictionary, wordLength);
    }

}
