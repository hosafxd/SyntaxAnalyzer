package syntaxanalyzer;
import java.util.*;

public class BlockAnalyzer {
    private int braceCount;
    private String lastError;

    public BlockAnalyzer() {
        this.braceCount = 0;
        this.lastError = "";
    }

    public boolean processLine(String line) {
        for (char c : line.toCharArray()) {
            if (c == '{') braceCount++;
            if (c == '}') {
                braceCount--;
                if (braceCount < 0) {
                    lastError = "Unmatched Braces";
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isInsideBlock() {
        return braceCount > 0;
    }

    public boolean isBlockStructureComplete() {
        if (braceCount > 0) {
            lastError = "Unmatched Braces";
            return false;
        }
        return true;
    }

    public String getLastError() {
        return lastError;
    }

    public void reset() {
        braceCount = 0;
        lastError = "";
    }
}