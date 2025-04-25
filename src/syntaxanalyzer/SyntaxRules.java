/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package syntaxanalyzer;
import java.util.*;
/**
 *
 * @author furka
 */


public class SyntaxRules {
    // Define valid data types (only int and char are allowed)
    private static final Set<String> VALID_TYPES = new HashSet<>(
        Arrays.asList("int", "char")
    );

    // Define valid arithmetic operators (+, -, *, /)
    private static final Set<String> ARITHMETIC_OPERATORS = new HashSet<>(
        Arrays.asList("+", "-", "*", "/")
    );

    // Define valid relational operators (<, <=, >, >=, ==, !=)
    private static final Set<String> RELATIONAL_OPERATORS = new HashSet<>(
        Arrays.asList("<", "<=", ">", ">=", "==", "!=")
    );

    
    public static boolean isValidType(String type) {
        return VALID_TYPES.contains(type);
    }

 
    public static boolean isValidIdentifier(String identifier) {
        return identifier.matches("^[a-z]$");
    }

    
    public static boolean isIntegerConstant(String value) {
        return value.matches("^\\d+$");
    }

  
    public static boolean isValidArithmeticOperator(String operator) {
        return ARITHMETIC_OPERATORS.contains(operator);
    }


    public static boolean isValidRelationalOperator(String operator) {
        return RELATIONAL_OPERATORS.contains(operator);
    }

   
    public static String[] getRelationalOperators() {
        return RELATIONAL_OPERATORS.toArray(new String[0]);
    }

   
    public static boolean hasSemicolon(String line) {
        return line.trim().endsWith(";");
    }

   
    public static boolean hasMatchingBraces(String line) {
        int count = 0;
        for (char c : line.toCharArray()) {
            if (c == '{') count++;
            if (c == '}') count--;
            if (count < 0) return false; // More closing than opening braces
        }
        return count == 0; // Should be zero if all braces match
    }
}