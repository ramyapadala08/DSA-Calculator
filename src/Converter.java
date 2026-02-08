import java.io.*;
import java.util.*;

public class Converter {

    // Infix to Postfix conversion
    public static String infixToPostfix(String infix) {
        Stack<Character> stack = new Stack<>();
        StringBuilder postfix = new StringBuilder();

        for (char c : infix.toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                postfix.append(c);
            } else if (c == '(') {
                stack.push(c);
            } else if (c == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    postfix.append(stack.pop());
                }
                if (!stack.isEmpty() && stack.peek() == '(') {
                    stack.pop();
                }
            } else {
                while (!stack.isEmpty() && precedence(c) <= precedence(stack.peek()) && stack.peek() != '(') {
                    postfix.append(stack.pop());
                }
                stack.push(c);
            }
        }

        while (!stack.isEmpty()) {
            if (stack.peek() == '(') {
                return "Invalid Expression";
            }
            postfix.append(stack.pop());
        }

        return postfix.toString();
    }

    // Postfix to Infix conversion
    public static String postfixToInfix(String postfix) {
        Stack<String> stack = new Stack<>();

        for (char c : postfix.toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                stack.push(String.valueOf(c));
            } else {
                if (stack.size() < 2) {
                    return "Invalid Expression";
                }
                String op2 = stack.pop();
                String op1 = stack.pop();
                stack.push("(" + op1 + c + op2 + ")");
            }
        }

        if (stack.size() != 1) {
            return "Invalid Expression";
        }

        String result = stack.pop();
        if (result.charAt(0) == '(' && result.charAt(result.length() - 1) == ')') {
            result = result.substring(1, result.length() - 1);
        }

        return result;
    }

    // Infix to Prefix conversion
    public static String infixToPrefix(String infix) {
        StringBuilder reversedInfix = new StringBuilder(infix).reverse();

        for (int i = 0; i < reversedInfix.length(); i++) {
            if (reversedInfix.charAt(i) == '(') {
                reversedInfix.setCharAt(i, ')');
            } else if (reversedInfix.charAt(i) == ')') {
                reversedInfix.setCharAt(i, '(');
            }
        }

        String postfix = infixToPostfix(reversedInfix.toString());

        return new StringBuilder(postfix).reverse().toString();
    }

    // Prefix to Infix conversion
    public static String prefixToInfix(String prefix) {
        Stack<String> stack = new Stack<>();

        for (int i = prefix.length() - 1; i >= 0; i--) {
            char c = prefix.charAt(i);

            if (Character.isLetterOrDigit(c)) {
                stack.push(String.valueOf(c));
            } else {
                if (stack.size() < 2) {
                    return "Invalid Expression";
                }
                String op1 = stack.pop();
                String op2 = stack.pop();
                stack.push("(" + op1 + c + op2 + ")");
            }
        }

        if (stack.size() != 1) {
            return "Invalid Expression";
        }

        String result = stack.pop();
        if (result.charAt(0) == '(' && result.charAt(result.length() - 1) == ')') {
            result = result.substring(1, result.length() - 1);
        }

        return result;
    }

    // Postfix to Prefix conversion
    public static String postfixToPrefix(String postfix) {
        String infix = postfixToInfix(postfix);
        return infixToPrefix(infix);
    }

    // Prefix to Postfix conversion
    public static String prefixToPostfix(String prefix) {
        String infix = prefixToInfix(prefix);
        return infixToPostfix(infix);
    }

    // Helper method to determine operator precedence
    private static int precedence(char op) {
        switch (op) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            case '^':
                return 3;
            default:
                return -1;
        }
    }

    private static void writeSession(String session) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt", true))) {
            writer.write(session);
        } catch (IOException e) {
            System.out.println("Error writing session to output.txt");
            e.printStackTrace();
        }
    }

    // Method to write conversion result to output.txt
    private static void writeOutput(String output) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt", true))) {
            writer.write(output);
        } catch (IOException e) {
            System.out.println("Error writing to output.txt");
            e.printStackTrace();
        }
    }

    private static void writeInput(String sessionInput) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("input.txt", true))) {
            writer.write(sessionInput);
        } catch (IOException e) {
            System.out.println("Error writing to input.txt");
            e.printStackTrace();
        }
    }

    private static boolean areParenthesesBalanced(String expr) {
        Stack<Character> stack = new Stack<>();
        for (char c : expr.toCharArray()) {
            if (c == '(') stack.push(c);
            else if (c == ')') {
                if (stack.isEmpty()) return false;
                stack.pop();
            }
        }
        return stack.isEmpty();
    }
    

    // Method to handle user input and conversion logic
    // Updated handle method with session start and end logging
    public static void handle() {
        Scanner sc = new Scanner(System.in);

        // Get current timestamp
        String timeStamp = new java.text.SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy")
                .format(new java.util.Date());

        // Prompt user to choose the type of conversion
        System.out.println("Choose conversion type: ");
        System.out.println("1. Infix to Postfix");
        System.out.println("2. Postfix to Infix");
        System.out.println("3. Infix to Prefix");
        System.out.println("4. Prefix to Infix");
        System.out.println("5. Postfix to Prefix");
        System.out.println("6. Prefix to Postfix");
        int choice = sc.nextInt();
        sc.nextLine(); // Consume newline character

        System.out.println("Enter expression to convert (Example:a+b*(c^d-e)^(f+g*h)-i):");
        String expr = sc.nextLine().trim();

        String exprType = "";
        String result = "";

        if (expr.isEmpty()) {
            String error = "ERROR: Empty input expression at " + timeStamp + "\n---------------------------\n";
            writeInput(error);
            writeOutput(error);
            writeSession(error);
            System.out.println("Error: Input expression is empty.");
            return;
        }
    
        if (expr.length() > 1000) {
            String error = "ERROR: Expression exceeds 1000 characters at " + timeStamp + "\n---------------------------\n";
            writeInput(error);
            writeOutput(error);
            writeSession(error);
            System.out.println("Error: Expression too long. Max allowed is 1000 characters.");
            return;
        }
    
        if (!expr.matches("[a-zA-Z0-9+\\-*/^() ]+")) {
            String error = "ERROR: Invalid characters in expression at " + timeStamp + "\n---------------------------\n";
            writeInput(error);
            writeOutput(error);
            writeSession(error);
            System.out.println("Error: Expression contains invalid characters.");
            return;
        }
    
        // Additional validation for infix expressions (used in 1, 3)
        if ((choice == 1 || choice == 3) && !areParenthesesBalanced(expr)) {
            String error = "ERROR: Unbalanced parentheses in infix expression at " + timeStamp + "\n---------------------------\n";
            writeInput(error);
            writeOutput(error);
            writeSession(error);
            System.out.println("Error: Parentheses are not balanced.");
            return;
        }
    
        try {
            switch (choice) {
                case 1:
                    result = infixToPostfix(expr);
                    exprType = "Postfix";
                    break;
                case 2:
                    result = postfixToInfix(expr);
                    exprType = "Infix";
                    break;
                case 3:
                    result = infixToPrefix(expr);
                    exprType = "Prefix";
                    break;
                case 4:
                    result = prefixToInfix(expr);
                    exprType = "Infix";
                    break;
                case 5:
                    result = postfixToPrefix(expr);
                    exprType = "Prefix";
                    break;
                case 6:
                    result = prefixToPostfix(expr);
                    exprType = "Postfix";
                    break;
                default:
                    System.out.println("Invalid choice.");
                    return;
            }
        } catch (Exception e) {
            String error = "ERROR during conversion: " + e.getMessage() + " at " + timeStamp + "\n---------------------------\n";
            writeInput(error);
            writeOutput(error);
            writeSession(error);
            System.out.println("Error: Exception occurred - " + e.getMessage());
            return;
        }

        StringBuilder sessionLog = new StringBuilder();
        sessionLog.append("--- Session Start: ").append(timeStamp).append(" ---\n");
        sessionLog.append("Expression Type: ").append(exprType).append("\n");
        sessionLog.append("Expression: ").append(expr).append("\n");
        sessionLog.append("Converted Expression: ").append(result).append("\n");
        sessionLog.append("--- Session End ---\n");
        sessionLog.append("---------------------------\n");

        // Write input
        writeInput(sessionLog.toString());
        writeSession(sessionLog.toString());

        System.out.println(sessionLog);
    }

    public static void main(String[] args) {
        handle();
    }
}
