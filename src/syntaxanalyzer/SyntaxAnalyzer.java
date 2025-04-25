package syntaxanalyzer;
import java.io.*;
import java.util.*;

public class SyntaxAnalyzer {
    private StatementAnalyzer statementAnalyzer;
    private BlockAnalyzer blockAnalyzer;
    private List<String> analysisResults;
    private int statementCount;

    public SyntaxAnalyzer() {
        this.statementAnalyzer = new StatementAnalyzer();
        this.blockAnalyzer = new BlockAnalyzer();
        this.analysisResults = new ArrayList<>();
        this.statementCount = 0;
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java SyntaxAnalyzer input.txt output.txt");
            return;
        }

        SyntaxAnalyzer analyzer = new SyntaxAnalyzer();
        analyzer.processFiles(args[0], args[1]);
    }

    private void processFiles(String inputFile, String outputFile) {
        try {
            blockAnalyzer.reset();
            analyzeInputFile(inputFile);
            writeResults(outputFile);
        } catch (IOException e) {
            System.err.println("Error processing files: " + e.getMessage());
        }
    }

    private void analyzeInputFile(String inputFile) throws IOException {
        // Read the entire file into a string
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        
        // Process the content line by line
        String[] lines = content.toString().split("\n");
        StringBuilder currentStatement = new StringBuilder();
        boolean inIfBlock = false;
        boolean inWhileBlock = false;
        
        for (String line : lines) {
            // Track braces for block structure
            for (char c : line.toCharArray()) {
                if (c == '{') {
                    blockAnalyzer.processLine("{");
                } else if (c == '}') {
                    blockAnalyzer.processLine("}");
                }
            }
            
            // Trim the line
            line = line.trim();
            if (line.isEmpty()) continue;
            
            // Handle complete statements on a single line
            if (line.endsWith(";")) {
                processStatement(line);
                continue;
            }
            
            // Handle if and while statements
            if (line.startsWith("if") || line.startsWith("while")) {
                // Process if statements including their condition
                if (line.contains("{")) {
                    // The opening brace is on this line
                    String stmtType = line.startsWith("if") ? "If Statement" : "While Loop";
                    statementCount++;
                    analysisResults.add(String.format("Statement %d: Valid %s", statementCount, stmtType));
                } else {
                    // The opening brace is on another line
                    currentStatement.append(line).append(" ");
                }
                continue;
            }
            
            // Handle opening braces
            if (line.equals("{")) {
                // If we have a pending statement, process it
                if (currentStatement.length() > 0) {
                    String stmt = currentStatement.toString().trim();
                    String stmtType = stmt.startsWith("if") ? "If Statement" : "While Loop";
                    statementCount++;
                    analysisResults.add(String.format("Statement %d: Valid %s", statementCount, stmtType));
                    currentStatement = new StringBuilder();
                }
                continue;
            }
            
            // Handle closing braces - no special processing needed
            if (line.equals("}")) continue;
            
            // Handle statements without semicolons (errors)
            if (!line.contains(";") && !line.contains("{") && !line.contains("}")) {
                // Check if this looks like a statement that should have a semicolon
                if (line.contains("=") || line.startsWith("return")) {
                    statementCount++;
                    analysisResults.add(String.format("Statement %d: Error - Missing semicolon", statementCount));
                } else {
                    // Add to current statement being built
                    currentStatement.append(line).append(" ");
                }
                continue;
            }
            
            // Handle other types of statements
            processStatement(line);
        }
        
        // Check for any remaining statements
        if (currentStatement.length() > 0) {
            processStatement(currentStatement.toString().trim());
        }
        
        // Check for unmatched braces
        if (!blockAnalyzer.isBlockStructureComplete()) {
            analysisResults.add("Error: " + blockAnalyzer.getLastError());
        }
    }
    
    private void processStatement(String statement) {
        statement = statement.trim();
        if (statement.isEmpty()) return;
        
        // Increment statement count
        statementCount++;
        
        // Determine statement type
        String statementType = determineStatementType(statement);
        
        if (statementType.equals("Unknown Statement")) {
            analysisResults.add(String.format("Statement %d: Error - Unknown statement type", statementCount));
            return;
        }
        
        // Validate with StatementAnalyzer
        if (statementAnalyzer.isValidStatement(statement)) {
            analysisResults.add(String.format("Statement %d: Valid %s", statementCount, statementType));
        } else {
            analysisResults.add(String.format("Statement %d: Error - %s", 
                statementCount, statementAnalyzer.getLastError()));
        }
    }
    
    private String determineStatementType(String statement) {
        statement = statement.trim();
        
        if (statement.startsWith("int") || statement.startsWith("char")) {
            return "Variable Declaration";
        } else if (statement.startsWith("if")) {
            return "If Statement";
        } else if (statement.startsWith("while")) {
            return "While Loop";
        } else if (statement.startsWith("return")) {
            return "Return Statement";
        } else if (statement.contains("=") && !statement.contains("==")) {
            return "Assignment Statement";
        }
        
        return "Unknown Statement";
    }
    
    private void writeResults(String outputFile) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            for (String result : analysisResults) {
                writer.write(result);
                writer.newLine();
            }
        }
    }
}