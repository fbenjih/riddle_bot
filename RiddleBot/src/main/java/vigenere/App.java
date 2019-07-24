package main.java.vigenere;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class App {

    private static final String RIDDLE_MESSAGE = "JKPPCM OYL CTLQ JLODPYOPH YTLC YTLC RYTKPPCQ JLODPFE RZQ WTLS YTLC XCZEDCPOYFSE ESRTW JLOYZX XCZED EDFO JLOYFD DCPHZSD HZYD ESRTW JLOTCQ JETYTNTG YT YTLC RYTKPPCQ JLODCFSE VPPH EIPY RZQ XCZEDCPOYFSE JLOTCQ PWKKTCO RYTKPPCQ JGLPS JLODCFSE JETYTNTG YT RZQ JLODPYOPH PKLS SETH CTLQ JLODPFE HZYD PWKKTCO JGLPS JLOYZX YTLC DCPHZSD JLOYFD VPPH DTSE CZQ EDLNPCZQ CPSELPH";
    private static final String FILE_NAME = "src/main/resources/words.txt";
    private static final String FOUND_KEYS_MESSAGE = "Riddle keys were found! The encrypted message is above this sentence.";
    private static final String LOST_KEYS_MESSAGE = "Proper riddle keys don't exist to encrypt the message.";
    private static final int RIDDLE_KEYS = 4;
    private static final int ASCII_START = 'A', ASCII_END = 'Z';
    private static final int LOWEST_SHIFT = 0, HIGHEST_SHIFT = 25;

    public static void main( String[] args ) {
        Set<String> dictionary = uppercasedLines(readFromFile());
        boolean isSolutionFound = doesSolutionExist(dictionary);
        String resultMessage = isSolutionFound ? FOUND_KEYS_MESSAGE : LOST_KEYS_MESSAGE;
        System.out.println(resultMessage);
    }

    private static Set<String> uppercasedLines(Set<String> originalLines){
        Set<String> transformedStrings = new TreeSet<>();
        for(String currentString : originalLines) {
            transformedStrings.add(currentString.toUpperCase());
        }
        return transformedStrings;
    }

    private static Set<String> readFromFile() {
        Set<String> linesFromTheFile = new TreeSet<>();
        try (Stream<String> stream = Files.lines(Paths.get(FILE_NAME))) {
            linesFromTheFile = stream.collect(Collectors.toSet());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return linesFromTheFile;
    }

    private static boolean doesSolutionExist(Set<String> dictionary) {
        for(int firstPossibleKey = LOWEST_SHIFT; firstPossibleKey <= HIGHEST_SHIFT; firstPossibleKey++) {
            for(int secondPossibleKey = LOWEST_SHIFT; secondPossibleKey <= HIGHEST_SHIFT; secondPossibleKey++) {
                for(int thirdPossibleKey = LOWEST_SHIFT; thirdPossibleKey <= HIGHEST_SHIFT; thirdPossibleKey++) {
                    for(int fourthPossibleKey = LOWEST_SHIFT; fourthPossibleKey <= HIGHEST_SHIFT; fourthPossibleKey++) {
                        int[] currentPossibleRiddleKeys = { firstPossibleKey, secondPossibleKey, thirdPossibleKey, fourthPossibleKey };
                        System.out.println("[" +currentPossibleRiddleKeys[0] +
                          ", " + currentPossibleRiddleKeys[1] +
                          ", " + currentPossibleRiddleKeys[2] +
                          ", " + currentPossibleRiddleKeys[3] + "]");
                        String currentPossibleDecodedText = messageCracking(currentPossibleRiddleKeys);
                        if(areAllWordsContainedInDictionary(currentPossibleDecodedText, dictionary)) {
                            System.out.println(currentPossibleDecodedText);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private static String messageCracking(int riddleKeys[]) {
        int currentRiddleKeyIndex = 0;
        StringBuilder result = new StringBuilder();
        String reversedRiddleMessage = reverseString(RIDDLE_MESSAGE);
        for (int i = 0; i < reversedRiddleMessage.length(); i++) {
            if(reversedRiddleMessage.charAt(i) == ' ') {
                result.append(' ');
            } else {
                int rotationNumber = riddleKeys[currentRiddleKeyIndex % RIDDLE_KEYS];
                int possiblePosition = reversedRiddleMessage.charAt(i) - rotationNumber;
                if (possiblePosition < ASCII_START) {
                    int toBeDeductedFromTheEnd =  possiblePosition - ASCII_START + 1;
                    result.append((char) (ASCII_END + toBeDeductedFromTheEnd));
                }
                else {
                    result.append((char) (reversedRiddleMessage.charAt(i) - rotationNumber));
                }
                currentRiddleKeyIndex++;
            }
        }
        return result.toString();
    }

    private static String reverseString(String input) {
        StringBuilder stringBuilder = new StringBuilder(input);
        return stringBuilder.reverse().toString();
    }

    private static boolean areAllWordsContainedInDictionary(String textToBeChecked, Set<String> availableWords) {
        return availableWords.containsAll(Arrays.asList(textToBeChecked.split(" ")));
    }
}
