
# 🔍 Tokenized Syntax Analyzer

A **robust Java-based compiler front-end** that implements a **character-by-character tokenizer** and **syntax validator** for a simplified Java-like language.

## 🚀 Project Overview

The Syntax Analyzer processes source code files to **identify tokens** and **verify syntactic correctness** according to predefined grammar rules using a **state machine** implementation.

## ⚙️ Setup & Installation

### 📋 Requirements

* **Java JDK 11** or later
* **NetBeans IDE** (recommended) or any Java IDE

### 🔧 Setup Instructions

* **Clone or Download the Repository**
git clone https://github.com/yourusername/syntax-analyzer.git


### 👨‍💻 Project Setup in NetBeans

* **Open NetBeans**
* `File → Open Project → Select your project folder`
* Navigate to the source folder: `src/syntaxanalyzer`

### 🏗️ Build & Run

* **Build Project**: `F11`
* **Run with Arguments**: 

Right-click project → Properties → Run → Arguments → Add "input.txt output.txt"

* **Execute**: `F6`

## 🎯 Features & Functionality

### 📝 Grammar Support

* **Variable Declarations**: `int x;` or `char c;`
* **Assignment Statements**: `x = 5 + 3;`
* **If Statements**: `if (x > 5) { ... }`
* **While Loops**: `while (x != 0) { ... }`
* **Return Statements**: `return x;`

### 🧩 Tokenization Engine

* **Character-by-character Processing**
* Token types:
  * Keywords (int, char, if, while, return)
  * Identifiers (single lowercase letters)
  * Numbers (integer constants)
  * Operators (+, -, *, /, =, ==, !=, >, <, >=, <=)
  * Punctuation symbols (;, (, ), {, })

### 🔍 Syntax Validation

* **Statement Structure Analysis**
* **Block Structure Tracking**
* **Variable Declaration Checking**
* **Expression Validation**
* **Comprehensive Error Reporting**

### 📊 State Machine Implementation

* **Finite State Automaton** approach
* States represent different processing conditions
* Transitions occur based on input characters
* Final states indicate successful token recognition

## 🛠️ Core Components

### 📚 Class Structure

* **SyntaxAnalyzer**: Main orchestrator of the analysis process
* **Tokenizer**: Implements the state machine for lexical analysis
* **StatementAnalyzer**: Validates different statement types
* **BlockAnalyzer**: Tracks and validates block structures
* **SyntaxRules**: Contains grammar definitions

## 📋 Sample Usage

### Input File (input.txt)
```java
int x;
x = 5 + 3;
if (x > 2) {
    x = x - 1;
}
while (x != 0) {
    x = x - 1;
}
return x;
```

### Output File (output.txt)
```
Statement 1: Valid Variable Declaration
Statement 2: Valid Assignment Statement
Statement 3: Valid If Statement
Statement 4: Valid While Loop
Statement 5: Valid Return Statement
```

## 🌟 Key Features

* **No External Dependencies**: Pure Java implementation
* **Detailed Error Reporting**: Precise error location and message
* **Multi-line Statement Support**: Handles statements across multiple lines
* **Proper Block Structure Analysis**: Tracks nested blocks correctly
* **Character-by-character Processing**: True tokenized implementation

## 🧠 Educational Value

* Demonstrates compiler front-end concepts:
  * **Lexical Analysis** (scanning)
  * **Syntax Analysis** (parsing)
  * **State Machine Design**
  * **Error Detection & Recovery**
  
## 🚦 Usage Instructions

1. Create a text file with Java-like code following the grammar rules
2. Run the analyzer with input and output file paths:
```
java syntaxanalyzer.SyntaxAnalyzer input.txt output.txt
```
3. Review the syntax analysis results in the output file

## 📬 Support & Contact

For support, questions, or collaboration:
📧 **furkanozturk5406@hotmail.com**

---

**The syntax analyzer demonstrates fundamental compiler construction concepts, focusing on the front-end components of lexical analysis and syntax validation.**
