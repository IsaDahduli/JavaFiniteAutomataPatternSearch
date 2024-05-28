import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class engine {

    static final int NUMBER_OF_CHARACTERS = 256;

    // Function to calculate the next state in the finite automaton
    static int getNextState(String pattern, int patternLength, int state, char inputChar) {
        if (state < patternLength && inputChar == pattern.charAt(state)) {
            return state + 1;
        }

        int i = 0;
        for (int nextState = state; nextState > 0; nextState--) {
            if (pattern.charAt(nextState - 1) == inputChar) {
                while (i < nextState - 1) {
                    if (pattern.charAt(i) != pattern.charAt(state - nextState + 1 + i)) {
                        break;
                    }
                    i += 1;
                }
                if (i == nextState - 1) {
                    return nextState;
                }
            }
        }
        return 0;
    }

    // Function to compute the Transition Function (TF) for the finite automaton
    static Map<String, Map<Character, String>> computeTF(String pattern, int patternLength) {
        Map<String, Map<Character, String>> transitionFunction = new HashMap<>();

        for (int state = 0; state <= patternLength; state++) {
            Map<Character, String> transitions = new HashMap<>();
            for (int inputChar = 0; inputChar < NUMBER_OF_CHARACTERS; inputChar++) {
                int nextState = getNextState(pattern, patternLength, state, (char) inputChar);
                transitions.put((char) inputChar, "q" + nextState);
            }
            transitionFunction.put("q" + state, transitions);
        }

        return transitionFunction;
    }

    // Function to find occurrences of a pattern in the input text using the finite automaton
    static void findPattern(String pattern, String inputText) {
        int patternLength = pattern.length();
        Map<String, Map<Character, String>> transitionFunction = computeTF(pattern, patternLength);

        String currentState = "q0";
        for (int i = 0; i < inputText.length(); i++) {
            char inputChar = inputText.charAt(i);
            currentState = transitionFunction.get(currentState).get(inputChar);
            if (currentState.equals("q" + patternLength)) {
                System.out.println("ACCEPTED at index " + (i - patternLength + 1));
            } else {
                System.out.println("REJECTED");
            }
        }
    }

    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("text.txt"));
            StringBuilder txtBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                txtBuilder.append(line);
            }
            String inputText = txtBuilder.toString().trim(); // Remove leading/trailing whitespace
            String pattern = "aabcab"; // Pattern to search for
            findPattern(pattern, inputText); // Find occurrences of the pattern in the input text
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
