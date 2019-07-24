package main.java.vigenere;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class App {

    // HINT: RIDDLE_BOT riddle keys are: [11, 11, 11, 11]
    //private static final String RIDDLE_MESSAGE = "XHJLOD WHLHLJT VZOGFZ FNMDSUUT NRSEDD CRP DKTZ WZFVIBB XPB GFDS XHIOHXEDD XDYFUHSHXT WMVUXEDD HLSHHW GUNEDD GLIEOJIHFE";
    private static final String RIDDLE_MESSAGE = reverseString("JKPPCM OYL CTLQ JLODPYOPH YTLC YTLC RYTKPPCQ JLODPFE RZQ WTLS YTLC XCZEDCPOYFSE ESRTW JLOYZX XCZED EDFO JLOYFD DCPHZSD HZYD ESRTW JLOTCQ JETYTNTG YT YTLC RYTKPPCQ JLODCFSE VPPH EIPY RZQ XCZEDCPOYFSE JLOTCQ PWKKTCO RYTKPPCQ JGLPS JLODCFSE JETYTNTG YT RZQ JLODPYOPH PKLS SETH CTLQ JLODPFE HZYD PWKKTCO JGLPS JLOYZX YTLC DCPHZSD JLOYFD VPPH DTSE CZQ EDLNPCZQ CPSELPH");
    private static final String FILE_NAME = "src/main/resources/words.txt";
    private static final String FOUND_KEYS_MESSAGE = "Riddle keys were found! The encrypted message is above this sentence.";
    private static final String LOST_KEYS_MESSAGE = "Proper riddle keys don't exist to encrypt the message.";
    private static final int RIDDLE_KEYS = 4;
    private static final int ASCII_START = 'A', ASCII_END = 'Z';
    private static final int LOWEST_SHIFT = 0, HIGHEST_SHIFT = 25;

    private static String reverseString(String input) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(input);
        return stringBuilder.reverse().toString();
    }

    public static void main( String[] args ) {
        List<String> dictionary = uppercasedLines(readFromFile());
        boolean isSolutionFound = doesSolutionExist(dictionary);
        String resultMessage = isSolutionFound ? FOUND_KEYS_MESSAGE : LOST_KEYS_MESSAGE;
        System.out.println(resultMessage);
    }

    private static List<String> uppercasedLines(List<String> originalLines){
        List<String> transformedStrings = new LinkedList<>();
        for(String currentString : originalLines) {
            transformedStrings.add(currentString.toUpperCase());
        }
        return transformedStrings;
    }

    private static List<String> readFromFile() {
        List<String> linesFromTheFile = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(FILE_NAME))) {
            linesFromTheFile = stream.collect(Collectors.toList());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return linesFromTheFile;
    }

    private static boolean doesSolutionExist(List<String> dictionary) {
        boolean isSolutionFound = false;
        for(int firstPossibleKey = LOWEST_SHIFT; firstPossibleKey <= HIGHEST_SHIFT && !isSolutionFound; firstPossibleKey++) {
            for(int secondPossibleKey = LOWEST_SHIFT; secondPossibleKey <= HIGHEST_SHIFT && !isSolutionFound; secondPossibleKey++) {
                for(int thirdPossibleKey = LOWEST_SHIFT; thirdPossibleKey <= HIGHEST_SHIFT && !isSolutionFound; thirdPossibleKey++) {
                    for(int fourthPossibleKey = LOWEST_SHIFT; fourthPossibleKey <= HIGHEST_SHIFT && !isSolutionFound; fourthPossibleKey++) {
                        int[] currentPossibleRiddleKeys = { firstPossibleKey, secondPossibleKey, thirdPossibleKey, fourthPossibleKey };
                        System.out.println("[" +currentPossibleRiddleKeys[0] +
                          ", " + currentPossibleRiddleKeys[1] +
                          ", " + currentPossibleRiddleKeys[2] +
                          ", " + currentPossibleRiddleKeys[3] + "]");
                        String currentPossibleDecodedText = messageCracking(currentPossibleRiddleKeys);
                        if(areAllWordsContainedInDictionary(currentPossibleDecodedText, dictionary)) {
                            System.out.println(currentPossibleDecodedText);
                            isSolutionFound = true;
                        }
                    }
                }
            }
        }
        return isSolutionFound;
    }

    private static String messageCracking(int riddleKeys[]) {
        int currentRiddleKeyIndex = 0;
        String replacedCharactersConcatenation = "";
        for (int i = 0; i < RIDDLE_MESSAGE.length(); i++) {
            if(RIDDLE_MESSAGE.charAt(i) == ' ') {
                replacedCharactersConcatenation += ' ';
            } else {
                int rotationNumber = riddleKeys[currentRiddleKeyIndex % RIDDLE_KEYS];
                int possiblePosition = RIDDLE_MESSAGE.charAt(i) - rotationNumber;
                if (possiblePosition < ASCII_START) {
                    int toBeDeductedFromTheEnd =  possiblePosition - ASCII_START + 1;
                    replacedCharactersConcatenation += (char) (ASCII_END + toBeDeductedFromTheEnd);
                }
                else {
                    replacedCharactersConcatenation += (char) (RIDDLE_MESSAGE.charAt(i) - rotationNumber);
                }
                currentRiddleKeyIndex++;
            }
        }
        return replacedCharactersConcatenation;
    }

    private static boolean areAllWordsContainedInDictionary(String textToBeChecked, List<String> availableWords) {
        return availableWords.containsAll(Arrays.asList(textToBeChecked.split(" ")));
    }
}

