import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class StepByStepEvaluatorString {

    public static void main(String[] args) throws IOException {
        // Call the handle method to take input from the user
        handle();
    }

    // Method to handle user input and call the conversion functions
    public static void handle() throws IOException {
        // Initialize Scanner to read input from the console
        Scanner scanner = new Scanner(System.in);

        // Request and read the expression from the user
        System.out.println("\n==== String Step By Step Evaluation ====");
        System.out.print("Enter the expression(Example: AB+C*): ");
        String expression = scanner.nextLine();

        // Request and read the conversion type from the user
        System.out.print(
                "Enter the conversion type (Postfix to Infix, Prefix to Postfix, Postfix to Prefix, Infix to Postfix, Infix to Prefix, Prefix to Infix): ");
        String conversionType = scanner.nextLine();

        // File setup for input and output
        BufferedWriter inputWriter = new BufferedWriter(new FileWriter("input.txt"));
        BufferedWriter outputWriter = new BufferedWriter(new FileWriter("output.txt", true));

        // Writing the input expression to input.txt
        inputWriter.write("Expression: " + expression + "\n");
        inputWriter.write("Conversion Type: " + conversionType + "\n");
        inputWriter.write("--------------------------------------------------\n");

        // Perform conversion based on the type
        String result = "";
        StringBuilder steps = new StringBuilder();
        switch (conversionType) {
            case "Postfix to Infix":
                result = postfixToInfix(expression, steps, outputWriter);
                break;
            case "Prefix to Postfix":
                result = prefixToPostfix(expression, steps, outputWriter);
                break;
            case "Postfix to Prefix":
                result = postfixToPrefix(expression, steps, outputWriter);
                break;
            case "Infix to Postfix":
                result = infixToPostfix(expression, steps, outputWriter);
                break;
            case "Infix to Prefix":
                result = infixToPrefix(expression, steps, outputWriter);
                break;
            case "Prefix to Infix":
                result = prefixToInfix(expression, steps, outputWriter);
                break;
            default:
                System.out.println("Invalid conversion type.");
        }

        // Log the conversion result and steps to output.txt
        logToFiles(expression, conversionType, result, inputWriter, outputWriter, steps);

        // Close the writers
        inputWriter.close();
        outputWriter.close();
    }

    // Method to log conversion steps to both console and output file
    private static void logToFiles(String expression, String conversionType, String result,
            BufferedWriter inputWriter, BufferedWriter outputWriter, StringBuilder steps) throws IOException {
        // Create formatter for full session timestamp format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy");
        String sessionHeader = "--- Session: " + ZonedDateTime.now(ZoneId.systemDefault()).format(formatter) + " ---";

        // Writing input to input.txt
        inputWriter.write("Expression: " + expression + "\n");
        inputWriter.write("Conversion Type: " + conversionType + "\n");
        inputWriter.write("--------------------------------------------------\n");

        // Writing the session header and result to output.txt
        outputWriter.write(sessionHeader + "\n");
        outputWriter.write("Conversion Type: " + conversionType + "\n");
        outputWriter.write("Input Expression: " + expression + "\n");
        outputWriter.write("Converted Expression: " + result + "\n");
        outputWriter.write("Steps:\n");
        outputWriter.write(steps.toString());
        outputWriter.write("--------------------------------------------------\n");
    }

    // Postfix to Infix Conversion
    private static String postfixToInfix(String expr, StringBuilder steps, BufferedWriter outputWriter)
            throws IOException {
        Stack<String> stack = new Stack<>();
        steps.append("Postfix to Infix Conversion Steps:\n");

        for (char c : expr.toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                stack.push(String.valueOf(c));
            } else {
                String b = stack.pop();
                String a = stack.pop();
                String res = "(" + a + " " + c + " " + b + ")";
                stack.push(res);
                steps.append("Step: ").append(a).append(" ").append(c).append(" ").append(b)
                        .append(" -> ").append(res).append("\n");
            }
        }

        // Print to console
        System.out.print(steps);
        // Log to output file
        logStepsToFile(steps, outputWriter);
        return stack.pop();
    }

    // Prefix to Postfix Conversion
    private static String prefixToPostfix(String expr, StringBuilder steps, BufferedWriter outputWriter)
            throws IOException {
        Stack<String> stack = new Stack<>();
        steps.append("Prefix to Postfix Conversion Steps:\n");

        for (int i = expr.length() - 1; i >= 0; i--) {
            char c = expr.charAt(i);
            if (Character.isLetterOrDigit(c)) {
                stack.push(String.valueOf(c));
            } else {
                String a = stack.pop();
                String b = stack.pop();
                String res = a + b + c;
                stack.push(res);
                steps.append("Step: ").append(c).append(" ").append(a).append(" ").append(b)
                        .append(" -> ").append(res).append("\n");
            }
        }

        // Print to console
        System.out.print(steps);
        // Log to output file
        logStepsToFile(steps, outputWriter);
        return stack.pop();
    }

    // Postfix to Prefix Conversion
    private static String postfixToPrefix(String expr, StringBuilder steps, BufferedWriter outputWriter)
            throws IOException {
        Stack<String> stack = new Stack<>();
        steps.append("Postfix to Prefix Conversion Steps:\n");

        for (char c : expr.toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                stack.push(String.valueOf(c));
            } else {
                String b = stack.pop();
                String a = stack.pop();
                String res = c + a + b;
                stack.push(res);
                steps.append("Step: ").append(a).append(" ").append(c).append(" ").append(b)
                        .append(" -> ").append(res).append("\n");
            }
        }

        // Print to console
        System.out.print(steps);
        // Log to output file
        logStepsToFile(steps, outputWriter);
        return stack.pop();
    }

    // Infix to Postfix Conversion
    private static String infixToPostfix(String expr, StringBuilder steps, BufferedWriter outputWriter)
            throws IOException {
        Stack<Character> ops = new Stack<>();
        Stack<String> vals = new Stack<>();
        steps.append("Infix to Postfix Conversion Steps:\n");

        for (char c : expr.toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                vals.push(String.valueOf(c));
            } else if (c == '(') {
                ops.push(c);
            } else if (c == ')') {
                while (!ops.isEmpty() && ops.peek() != '(') {
                    vals.push(applyOperator(ops.pop(), vals, steps));
                }
                ops.pop();
            } else {
                while (!ops.isEmpty() && precedence(c) <= precedence(ops.peek())) {
                    vals.push(applyOperator(ops.pop(), vals, steps));
                }
                ops.push(c);
            }
        }

        while (!ops.isEmpty()) {
            vals.push(applyOperator(ops.pop(), vals, steps));
        }

        // Print to console
        System.out.print(steps);
        // Log to output file
        logStepsToFile(steps, outputWriter);
        return vals.pop();
    }

    // Infix to Prefix Conversion
    private static String infixToPrefix(String expr, StringBuilder steps, BufferedWriter outputWriter)
            throws IOException {
        // Reverse the expression and change parentheses
        StringBuilder reversed = new StringBuilder(expr).reverse();
        for (int i = 0; i < reversed.length(); i++) {
            char c = reversed.charAt(i);
            if (c == '(')
                reversed.setCharAt(i, ')');
            else if (c == ')')
                reversed.setCharAt(i, '(');
        }

        // Generate the postfix expression from the reversed infix
        StringBuilder postfixSteps = new StringBuilder();
        String postfix = infixToPostfix(reversed.toString(), postfixSteps, outputWriter);

        // Log the postfix steps generated from reversed infix
        steps.append("Infix to Prefix Conversion Steps (via reversed infix -> postfix):\n");
        steps.append(postfixSteps);

        // Reverse the postfix expression to get the prefix result
        String prefix = new StringBuilder(postfix).reverse().toString();

        // Log the final prefix
        steps.append("Final Prefix Expression: ").append(prefix).append("\n");

        return prefix;
    }

    // Prefix to Infix Conversion
    private static String prefixToInfix(String expr, StringBuilder steps, BufferedWriter outputWriter)
            throws IOException {
        Stack<String> stack = new Stack<>();
        steps.append("Prefix to Infix Conversion Steps:\n");

        for (int i = expr.length() - 1; i >= 0; i--) {
            char c = expr.charAt(i);
            if (Character.isLetterOrDigit(c)) {
                stack.push(String.valueOf(c));
            } else {
                String a = stack.pop();
                String b = stack.pop();
                String res = "(" + a + " " + c + " " + b + ")";
                stack.push(res);
                steps.append("Step: ").append(c).append(" ").append(a).append(" ").append(b)
                        .append(" -> ").append(res).append("\n");
            }
        }

        // Print to console
        System.out.print(steps);
        // Log to output file
        logStepsToFile(steps, outputWriter);
        return stack.pop();
    }

    // Helper method to log steps to file
    private static void logStepsToFile(StringBuilder steps, BufferedWriter outputWriter) throws IOException {
        outputWriter.write(steps.toString());
    }

    // Helper method to apply operator during infix to postfix conversion
    private static String applyOperator(char operator, Stack<String> vals, StringBuilder steps) {
        String b = vals.pop();
        String a = vals.pop();
        String result = a + b + operator;
        vals.push(result);
        steps.append("Step: ").append(a).append(" ").append(operator).append(" ").append(b)
                .append(" -> ").append(result).append("\n");
        return result;
    }

    // Helper method to determine precedence of operators
    private static int precedence(char operator) {
        switch (operator) {
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
}
