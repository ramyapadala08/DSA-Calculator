1. PROJECT OVERVIEW

This is a Java-based console application that performs:

Infix/Prefix/Postfix conversions
Postfix and bitwise expression evaluation
Expression tree construction and traversal
Syntax error detection and suggestions
History tracking and replay
Trie-based autocomplete for operators/functions
It uses core data structures like Stack, Binary Tree, and Trie, implemented using Java's primitive and non-primitive data types only (e.g., arrays, ArrayList, HashMap).
All user interactions are managed through Main.java, and the system reads/writes from files (input.txt, output.txt) to log each session with timestamps.

2. DIRECTORY STRUCTURE

DSA_Calculator/
│
├── src/                         # Main source code directory
│   ├── Main.java                # Main entry point for the application
│
├── controller/                  # Handles input/output and session logging
│   ├── InputHandler.java        # Manages input collection (from console and file)
│   ├── OutputHandler.java       # Manages output to the console and output file
│   ├── SessionLogger.java       # Logs session data with timestamps
│   └── ExpressionSession.java   # Manages individual expression sessions
│
├── processor/                   # Performs main tasks: evaluation, conversion, etc.
│   ├── ExpressionConverter.java # Converts expressions (Infix/Postfix/Prefix)
│   ├── ExpressionEvaluator.java # Evaluates expressions (Postfix, etc.)
│   ├── ExpressionTreeBuilder.java # Builds and traverses expression trees
│   └── ErrorDetector.java       # Detects syntax errors and provides suggestions
│
├── autocomplete/                # Trie-based autocomplete for operators/functions
│   └── TrieAutocomplete.java    # Implements autocomplete using Trie data structure
│
├── history/                     # Handles session history and replay functionality
│   └── HistoryManager.java      # Manages history tracking and session replay
│
└── utils/                       # Utility classes for validation and supporting functions
    └── ExpressionValidator.java # Validates expressions for correct syntax
│
├── data/                        # Directory for input/output files
│   ├── input.txt                # User inputs are written here
│   └── output.txt               # Processed output and results are written here
│
└── README.txt                   # Project documentation (this file)
3. HOW IT WORKS
The program starts via Main.java, which offers a menu of options: Convert, Evaluate, Build Tree, Autocomplete, History Replay, etc.

The user selects an option, and the corresponding task is processed using the appropriate functionality.

InputHandler.java reads input from the console or input.txt and writes it to the file.

Based on the user’s selection, processor classes handle the conversion, evaluation, tree building, or error detection.

Results are written to output.txt with a timestamp and session separator.

SessionLogger.java ensures each session is properly logged with timestamping and separation.

ExpressionSession.java manages the user input/output for each individual session, ensuring data integrity and consistent processing across tasks. Each session operates with its own context, storing the results and tracking the execution.

HistoryManager.java keeps a record of sessions, which can be viewed or replayed on demand.

4. INPUT/OUTPUT FORMAT
Format of input.txt:
Single-line or multi-line expressions are stored here.

Example:

makefile
Copy
Edit
A = 3
B = 5
(A + B) * 2
Format of output.txt:
Each session is separated by a timestamp and session delimiter. It includes the input expression, conversion results, evaluation results, expression trees, and autocomplete suggestions.

Example:

yaml
Copy
Edit
--- Session [Timestamp: 2025-05-02 14:45:10] ---
Expression: (A + B) * 2
Converted to Postfix: A B + 2 *
Evaluated Result: 16
Expression Tree: Printed via Inorder Traversal
Autocomplete Suggestions: +, -, *, /, ^
----------------------------------------------
5. HOW TO RUN
Compile the Java Files:
Navigate to the src/ directory and run the following command to compile all .java files:

css
Copy
Edit
javac src/**/*.java
Run the Application:
Run the application using:

css
Copy
Edit
java src.Main
All interaction will happen through the console.

Outputs will be written to data/output.txt.

History can be viewed and replayed as per the menu options.

6. REQUIREMENTS
Java SE 8 or above

No external libraries or dependencies

Console/terminal for interaction

7. FEATURES & MODULES
Expression Conversion (Infix/Postfix/Prefix): Convert between different expression formats.

Postfix Evaluation: Evaluate postfix expressions.

Expression Tree: Construct and traverse an expression tree.

Error Detection & Suggestions: Detect errors in expressions and provide suggestions for correction.

History Tracking & Replay: Track all user sessions and replay any past session.

Autocomplete for Operators/Functions: Use a Trie data structure to suggest operators or functions while typing.

ExpressionSession Management: Manages individual sessions with distinct input-output handling and ensures session data integrity.