package syntaxanalyzer;
import java.util.*;

public class StatementAnalyzer {
    private Set<String> declaredVariables;
    private String lastError;

    public StatementAnalyzer() {
        this.declaredVariables = new HashSet<>();
        this.lastError = "";
    }

    public boolean isValidStatement(String statement) {
        statement = statement.trim();

        // Skip empty statements
        if (statement.isEmpty()) {
            return true;
        }

        // Check for missing semicolon first (except for if/while statements)
        if (!statement.startsWith("if") && !statement.startsWith("while") &&
            !statement.endsWith(";") && !statement.endsWith("}")) {
            lastError = "Missing semicolon";
            return false;
        }

        // Handle different statement types
        if (statement.startsWith("int") || statement.startsWith("char")) {
            return isValidVariableDeclaration(statement);
        } else if (statement.contains("=") && !statement.contains("==") && 
                  !statement.startsWith("if") && !statement.startsWith("while")) {
            return isValidAssignment(statement);
        } else if (statement.startsWith("if")) {
            return isValidIfStatement(statement);
        } else if (statement.startsWith("while")) {
            return isValidWhileStatement(statement);
        } else if (statement.startsWith("return")) {
            return isValidReturnStatement(statement);
        }

        lastError = "Unknown statement type";
        return false;
    }

    private boolean isValidVariableDeclaration(String statement) {
        // Remove semicolon if present
        if (statement.endsWith(";")) {
            statement = statement.substring(0, statement.length() - 1).trim();
        } else {
            lastError = "Missing semicolon";
            return false;
        }

        String[] parts = statement.split("\\s+");

        // Check basic format
        if (parts.length != 2) {
            lastError = "Invalid declaration format";
            return false;
        }

        // Check type
        String type = parts[0];
        if (!SyntaxRules.isValidType(type)) {
            lastError = "Invalid type";
            return false;
        }

        // Check identifier (must be single lowercase letter)
        String identifier = parts[1];
        if (!SyntaxRules.isValidIdentifier(identifier)) {
            lastError = "Invalid identifier";
            return false;
        }

        // Check if variable is already declared
        if (declaredVariables.contains(identifier)) {
            lastError = "Variable already declared";
            return false;
        }

        declaredVariables.add(identifier);
        return true;
    }

    private boolean isValidAssignment(String statement) {
        // Remove semicolon
        if (statement.endsWith(";")) {
            statement = statement.substring(0, statement.length() - 1).trim();
        } else {
            lastError = "Missing semicolon";
            return false;
        }

        String[] parts = statement.split("=", 2);
        if (parts.length != 2) {
            lastError = "Invalid assignment format";
            return false;
        }

        String identifier = parts[0].trim();
        String expression = parts[1].trim();

        // Check if variable exists
        if (!declaredVariables.contains(identifier)) {
            lastError = "Undeclared variable: " + identifier;
            return false;
        }

        return isValidExpression(expression);
    }

    private boolean isValidExpression(String expression) {
        // Simple tokenization of the expression
        List<String> tokens = tokenizeExpression(expression);
        
        // Should alternate between terms and operators
        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i);
            
            if (i % 2 == 0) {
                // Even positions should be terms (variables or numbers)
                if (!SyntaxRules.isIntegerConstant(token) && !declaredVariables.contains(token)) {
                    lastError = "Invalid term in expression: " + token;
                    return false;
                }
            } else {
                // Odd positions should be operators
                if (!SyntaxRules.isValidArithmeticOperator(token)) {
                    lastError = "Invalid operator in expression: " + token;
                    return false;
                }
            }
        }
        
        // Expression should end with a term
        if (tokens.size() % 2 == 0) {
            lastError = "Expression ends with an operator";
            return false;
        }
        
        return true;
    }

    // Simple expression tokenizer
    private List<String> tokenizeExpression(String expression) {
        List<String> tokens = new ArrayList<>();
        StringBuilder currentToken = new StringBuilder();
        
        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            
            if (Character.isWhitespace(c)) {
                // Handle any completed token before the whitespace
                if (currentToken.length() > 0) {
                    tokens.add(currentToken.toString());
                    currentToken = new StringBuilder();
                }
            } else if ("+-*/".indexOf(c) != -1) {
                // This is an operator
                if (currentToken.length() > 0) {
                    tokens.add(currentToken.toString());
                    currentToken = new StringBuilder();
                }
                tokens.add(String.valueOf(c));
            } else {
                // This is part of a term
                currentToken.append(c);
            }
        }
        
        // Add the last token if any
        if (currentToken.length() > 0) {
            tokens.add(currentToken.toString());
        }
        
        return tokens;
    }

    private boolean isValidCondition(String condition) {
        String[] operators = SyntaxRules.getRelationalOperators();
        
        // Try each operator to find a match
        for (String operator : operators) {
            if (condition.contains(operator)) {
                // Split at the operator (only at the first occurrence)
                int opIndex = condition.indexOf(operator);
                String left = condition.substring(0, opIndex).trim();
                String right = condition.substring(opIndex + operator.length()).trim();
                
                return isValidExpression(left) && isValidExpression(right);
            }
        }
        
        lastError = "Missing or invalid relational operator";
        return false;
    }

    private boolean isValidIfStatement(String statement) {
        // Extract condition between parentheses
        int openParen = statement.indexOf('(');
        int closeParen = findMatchingCloseParen(statement, openParen);
        
        if (openParen == -1 || closeParen == -1) {
            lastError = "Invalid or missing parentheses";
            return false;
        }
        
        String condition = statement.substring(openParen + 1, closeParen).trim();
        
        // Check for block - relaxed validation
        int openBrace = statement.indexOf('{', closeParen);
        if (openBrace == -1) {
            lastError = "Missing opening brace";
            return false;
        }
        
        // Validate condition
        return isValidCondition(condition);
    }

    private int findMatchingCloseParen(String text, int openPos) {
        if (openPos == -1 || openPos >= text.length() || text.charAt(openPos) != '(') {
            return -1;
        }
        
        int balance = 1;
        for (int i = openPos + 1; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '(') balance++;
            else if (c == ')') {
                balance--;
                if (balance == 0) return i;
            }
        }
        
        return -1; // No matching close parenthesis
    }

    private boolean isValidWhileStatement(String statement) {
        // While statements follow the same validation as if statements
        return isValidIfStatement(statement);
    }

    private boolean isValidReturnStatement(String statement) {
        if (!statement.endsWith(";")) {
            lastError = "Missing semicolon";
            return false;
        }

        // Remove "return" keyword and semicolon
        String expression = statement.substring(6, statement.length() - 1).trim();
        
        // Expression can be empty for void functions (not in the requirements but good practice)
        if (expression.isEmpty()) {
            return true;
        }
        
        return isValidExpression(expression);
    }

    public String getLastError() {
        return lastError;
    }

    public Set<String> getDeclaredVariables() {
        return new HashSet<>(declaredVariables);
    }
}