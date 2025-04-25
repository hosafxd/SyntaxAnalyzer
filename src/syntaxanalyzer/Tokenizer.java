/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package syntaxanalyzer;
import java.util.*;


/**
 *
 * @author furka
 */


/*
checks text one character at a time to make sure
variables and numbers follow the rules.
*/

/**
processes input character by character
identifies tokens according to the state machine logic.
 */
public class Tokenizer {
    //types of tokens
    public enum TokenType {
        KEYWORD, IDENTIFIER, NUMBER, OPERATOR, PUNCTUATION, UNKNOWN
    }

    //structure
    public static class Token {
        private String value;
        private TokenType type;

        public Token(String value, TokenType type) {
            this.value = value;
            this.type = type;
        }

        public String getValue() {
            return value;
        }

        public TokenType getType() {
            return type;
        }

        @Override
        public String toString() {
            return String.format("%s(%s)", type, value);
        }
    }

    // States
    private enum State {
        INITIAL, 
        IDENTIFIER, 
        NUMBER, 
        OPERATOR, 
        PUNCTUATION,
        COMMENT, 
        WHITESPACE,
        ERROR
    }

    // Keywords that are accepted
    private static final Set<String> KEYWORDS = new HashSet<>(
        Arrays.asList("int", "char", "if", "while", "return")
    );

    private boolean isLetter(char c) {
        return Character.isLetter(c);
    }

    private boolean isDigit(char c) {
        return Character.isDigit(c);
    }

    private boolean isOperatorChar(char c) {
        return "+-*/=<>!".indexOf(c) != -1;
    }

    private boolean isPunctuation(char c) {
        return "(){};".indexOf(c) != -1;
    }

    private boolean isWhitespace(char c) {
        return Character.isWhitespace(c);
    }

   
    public List<Token> tokenize(String input) {
        List<Token> tokens = new ArrayList<>();
        StringBuilder currentToken = new StringBuilder();
        State currentState = State.INITIAL;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            
            switch (currentState) {
                case INITIAL:
                    if (isLetter(c)) {
                        currentToken.append(c);
                        currentState = State.IDENTIFIER;
                    } else if (isDigit(c)) {
                        currentToken.append(c);
                        currentState = State.NUMBER;
                    } else if (isOperatorChar(c)) {
                        currentToken.append(c);
                        currentState = State.OPERATOR;
                    } else if (isPunctuation(c)) {
                        currentToken.append(c);
                        currentState = State.PUNCTUATION;
                    } else if (isWhitespace(c)) {
                        // Skip whitespace
                        currentState = State.WHITESPACE;
                    } else {
                        // Invalid ch
                        currentToken.append(c);
                        currentState = State.ERROR;
                    }
                    break;

                case IDENTIFIER:
                    if (isLetter(c) || isDigit(c)) {
                        currentToken.append(c);
                    } else {
                        // Finalize identifier token
                        String value = currentToken.toString();
                        TokenType type = KEYWORDS.contains(value) ? TokenType.KEYWORD : TokenType.IDENTIFIER;
                        tokens.add(new Token(value, type));
                        
                        // Reset for next token
                        currentToken = new StringBuilder();
                        currentState = State.INITIAL;
                        i--; // Reprocess current character
                    }
                    break;

                case NUMBER:
                    if (isDigit(c)) {
                        currentToken.append(c);
                    } else {
                        // Finalize number token
                        tokens.add(new Token(currentToken.toString(), TokenType.NUMBER));
                        
                        // Reset for next token
                        currentToken = new StringBuilder();
                        currentState = State.INITIAL;
                        i--; // Reprocess current character
                    }
                    break;

                case OPERATOR:
                    //two-char operators like ==, <=, >=, !=
                    if (isOperatorChar(c) && isCompoundOperator(currentToken.toString(), c)) {
                        currentToken.append(c);
                        tokens.add(new Token(currentToken.toString(), TokenType.OPERATOR));
                        currentToken = new StringBuilder();
                        currentState = State.INITIAL;
                    } else {
                        // Single-char oprators
                        tokens.add(new Token(currentToken.toString(), TokenType.OPERATOR));
                        currentToken = new StringBuilder();
                        currentState = State.INITIAL;
                        i--; // Reprocess current character
                    }
                    break;

                case PUNCTUATION:
                    // Punctuation is always a single character
                    tokens.add(new Token(currentToken.toString(), TokenType.PUNCTUATION));
                    currentToken = new StringBuilder();
                    currentState = State.INITIAL;
                    i--; // Reprocess current character
                    break;

                case WHITESPACE:
                    if (!isWhitespace(c)) {
                        currentState = State.INITIAL;
                        i--; // Reprocess current character
                    }
                    break;

                case ERROR:
                    // For simplicity, we'll just skip to the next whitespace
                    if (isWhitespace(c)) {
                        tokens.add(new Token(currentToken.toString(), TokenType.UNKNOWN));
                        currentToken = new StringBuilder();
                        currentState = State.INITIAL;
                    } else {
                        currentToken.append(c);
                    }
                    break;
            }
        }

        // Handle remaining tokens
        if (currentToken.length() > 0) {
            switch (currentState) {
                case IDENTIFIER:
                    String value = currentToken.toString();
                    TokenType type = KEYWORDS.contains(value) ? TokenType.KEYWORD : TokenType.IDENTIFIER;
                    tokens.add(new Token(value, type));
                    break;
                case NUMBER:
                    tokens.add(new Token(currentToken.toString(), TokenType.NUMBER));
                    break;
                case OPERATOR:
                    tokens.add(new Token(currentToken.toString(), TokenType.OPERATOR));
                    break;
                case PUNCTUATION:
                    tokens.add(new Token(currentToken.toString(), TokenType.PUNCTUATION));
                    break;
                case ERROR:
                    tokens.add(new Token(currentToken.toString(), TokenType.UNKNOWN));
                    break;
            }
        }

        return tokens;
    }
    
    

    

    //flag whether combining the current operator character with the next character yields a valid compound operator.
    private boolean isCompoundOperator(String current, char next) {
        String compound = current + next;
        return compound.equals("==") || compound.equals("<=") || 
               compound.equals(">=") || compound.equals("!=");
    }
}