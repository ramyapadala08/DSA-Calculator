import java.io.*;
import java.util.*;

public class StepByStepEvaluatorNumeric {

    public static void handle() {
        Scanner scanner = new Scanner(System.in);
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt", true));
             BufferedWriter inputWriter = new BufferedWriter(new FileWriter("input.txt", true))) {
            
            // Get the current timestamp for session start
            String startTime = new Date().toString();
            
            // Writing session start and timestamp to output file
            writer.write("--- Session Start: " + startTime + " ---\n");
    
            // Input handling
            System.out.println("Enter Expression Type (postfix / prefix / infix): ");
            String expressionType = scanner.nextLine().trim().toLowerCase();
    
            System.out.println("Enter the Expression(Example: 5 1 2 + 4 * + 3 -): ");
            String expression = scanner.nextLine().trim();
    
            // Write the expression to input.txt
            inputWriter.write("--- Session Start: " + startTime + " ---\n");
            inputWriter.write("Expression Type: " + expressionType + "\n");
            inputWriter.write("Expression: " + expression + "\n");
            
            // Proceed with main logic
            writer.write("Expression Type: " + expressionType + "\n");
            writer.write("Expression: " + expression + "\n");
    
            switch (expressionType) {
                case "postfix":
                    postfixToInfix(expression, writer);
                    evaluatePostfix(expression, writer);
                    break;
                case "prefix":
                    prefixToInfix(expression, writer);
                    String postfix = convertPrefixToPostfix(expression);
                    writer.write("Converted to Postfix: " + postfix + "\n");
                    evaluatePostfix(postfix, writer);
                    break;
                case "infix":
                    evaluateInfix(expression, writer);
                    break;
                default:
                    writer.write("Invalid expression type. Please use postfix, prefix, or infix.\n");
                    System.out.println("Invalid expression type. Please use postfix, prefix, or infix.");
                    break;
            }
    
            // Output final results to console
            printOutput(expressionType, expression);
            writer.write("---------------------------\n\n");
    
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    private static void printOutput(String expressionType, String expression) {
        switch (expressionType) {
            case "postfix":
                System.out.println("Postfix Evaluation for: " + expression);
                break;
            case "prefix":
                System.out.println("Prefix Evaluation for: " + expression);
                break;
            case "infix":
                System.out.println("Infix Evaluation for: " + expression);
                break;
            default:
                System.out.println("Invalid expression type.");
                break;
        }
    }

    // === Conversion and Evaluation Helpers ===

    public static void postfixToInfix(String postfix, BufferedWriter writer) {
        Stack<String> stack = new Stack<>();

        try {
            writer.write("Postfix to Infix Conversion:\n");

            for (char c : postfix.toCharArray()) {
                if (Character.isDigit(c)) {
                    stack.push(String.valueOf(c));
                    writer.write("Push " + c + " -> Stack: " + stack + "\n");
                } else if (c == ' ' || c == '\t') {
                    continue;
                } else {
                    String b = stack.pop();
                    String a = stack.pop();
                    String result = "(" + a + c + b + ")";
                    stack.push(result);
                    writer.write(
                            "Apply " + c + " to " + a + ", " + b + " -> Push " + result + " -> Stack: " + stack + "\n");
                }
            }

            writer.write("Infix Expression: " + stack.pop() + "\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void evaluatePostfix(String postfix, BufferedWriter writer) {
        Stack<Double> stack = new Stack<>();

        try {
            writer.write("\nPostfix Evaluation Steps:\n");

            for (String token : postfix.trim().split("\\s+")) {
                if (token.matches("-?\\d+(\\.\\d+)?")) {
                    double num = Double.parseDouble(token);
                    stack.push(num);
                    writer.write("Push " + num + " -> Stack: " + stack + "\n");
                } else {
                    double b = stack.pop();
                    double a = stack.pop();
                    double result = switch (token) {
                        case "+" -> a + b;
                        case "-" -> a - b;
                        case "*" -> a * b;
                        case "/" -> a / b;
                        default -> throw new IllegalArgumentException("Invalid operator: " + token);
                    };
                    stack.push(result);
                    writer.write("Apply " + token + " to " + a + ", " + b + " -> Result: " + result + " -> Stack: "
                            + stack + "\n");
                }
            }

            String finalResult = String.valueOf(stack.pop());
            writer.write("Final Result: " + finalResult + "\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void prefixToInfix(String prefix, BufferedWriter writer) {
        Stack<String> stack = new Stack<>();

        try {
            writer.write("Prefix to Infix Conversion:\n");

            for (int i = prefix.length() - 1; i >= 0; i--) {
                char c = prefix.charAt(i);
                if (Character.isDigit(c)) {
                    stack.push(String.valueOf(c));
                    writer.write("Push " + c + " -> Stack: " + stack + "\n");
                } else if (c == ' ' || c == '\t') {
                    continue;
                } else {
                    String a = stack.pop();
                    String b = stack.pop();
                    String result = "(" + a + c + b + ")";
                    stack.push(result);
                    writer.write(
                            "Apply " + c + " to " + a + ", " + b + " -> Push " + result + " -> Stack: " + stack + "\n");
                }
            }

            writer.write("Infix Expression: " + stack.pop() + "\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String convertPrefixToPostfix(String prefix) {
        Stack<String> stack = new Stack<>();
        for (int i = prefix.length() - 1; i >= 0; i--) {
            char c = prefix.charAt(i);
            if (Character.isDigit(c)) {
                stack.push(String.valueOf(c));
            } else if (c == ' ' || c == '\t') {
                continue;
            } else {
                String a = stack.pop();
                String b = stack.pop();
                stack.push(a + " " + b + " " + c);
            }
        }
        return stack.pop();
    }

    public static void evaluateInfix(String infix, BufferedWriter writer) {
        Stack<Double> values = new Stack<>();
        Stack<Character> ops = new Stack<>();

        try {
            writer.write("\nInfix Evaluation Steps:\n");

            for (int i = 0; i < infix.length(); i++) {
                char c = infix.charAt(i);
                if (Character.isDigit(c)) {
                    double val = Character.getNumericValue(c);
                    values.push(val);
                    writer.write("Push " + val + " -> Values: " + values + "\n");
                } else if (c == '(') {
                    ops.push(c);
                } else if (c == ')') {
                    while (!ops.isEmpty() && ops.peek() != '(') {
                        double b = values.pop();
                        double a = values.pop();
                        char op = ops.pop();
                        double result = applyOperator(op, b, a);
                        values.push(result);
                        writer.write("Apply " + op + " to " + a + ", " + b + " -> Result: " + result + " -> Values: "
                                + values + "\n");
                    }
                    ops.pop(); // remove '('
                } else if (isOperator(c)) {
                    while (!ops.isEmpty() && precedence(ops.peek()) >= precedence(c)) {
                        double b = values.pop();
                        double a = values.pop();
                        char op = ops.pop();
                        double result = applyOperator(op, b, a);
                        values.push(result);
                        writer.write("Apply " + op + " to " + a + ", " + b + " -> Result: " + result + " -> Values: "
                                + values + "\n");
                    }
                    ops.push(c);
                    writer.write("Push operator " + c + " -> Ops: " + ops + "\n");
                }
            }

            while (!ops.isEmpty()) {
                double b = values.pop();
                double a = values.pop();
                char op = ops.pop();
                double result = applyOperator(op, b, a);
                values.push(result);
                writer.write("Apply " + op + " to " + a + ", " + b + " -> Result: " + result + " -> Values: " + values
                        + "\n");
            }

            String finalResult = String.valueOf(values.pop());
            writer.write("Final Result: " + finalResult + "\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean isOperator(char c) {
        return "+-*/".indexOf(c) != -1;
    }

    private static double applyOperator(char op, double b, double a) {
        return switch (op) {
            case '+' -> a + b;
            case '-' -> a - b;
            case '*' -> a * b;
            case '/' -> a / b;
            default -> throw new IllegalArgumentException("Unsupported operator: " + op);
        };
    }

    private static int precedence(char op) {
        return switch (op) {
            case '+', '-' -> 1;
            case '*', '/' -> 2;
            default -> -1;
        };
    }

    public static void main(String[] args) {
        handle();
    }
}
