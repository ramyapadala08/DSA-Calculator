import java.io.*;
import java.util.*;

public class ExpressionEvaluator {

    public static void handle() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Choose expression type: 1. Infix  2. Prefix  3. Postfix");
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        System.out.print("Enter expression (Example: ab+): ");
        String expression = scanner.nextLine();

        Set<Character> variablesInExpr = new HashSet<>();
        for (char c : expression.toCharArray()) {
            if (Character.isLetter(c)) {
                variablesInExpr.add(c);
            }
        }

        Map<Character, Integer> vars = new HashMap<>();
        StringBuilder inputLog = new StringBuilder();
        inputLog.append("--- Session: ").append(new Date()).append(" ---\n");
        inputLog.append("Expression: ").append(expression).append("\n");

        for (char var : variablesInExpr) {
            System.out.print("Enter value for " + var + ": ");
            int value = scanner.nextInt();
            vars.put(var, value);
            inputLog.append(var).append(" = ").append(value).append("\n");
        }

        inputLog.append("------------------------------\n\n");

        // Write to input.txt
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("input.txt", true))) {
            bw.write(inputLog.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        int result = 0;
        boolean errorOccurred = false;
        String errorMessage = "";

        try {
            validateExpression(expression, choice);

            switch (choice) {
                case 1 -> result = evaluateInfix(expression, vars);
                case 2 -> result = evaluatePrefix(expression, vars);
                case 3 -> result = evaluatePostfix(expression, vars);
                default -> throw new IllegalArgumentException("Invalid choice");
            }

            System.out.println("Result: " + result);

        } catch (Exception e) {
            errorOccurred = true;
            errorMessage = e.getMessage();
            System.out.println("Error: " + errorMessage);
        }

        // Write to output.txt
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("output.txt", true))) {
            StringBuilder outputLog = new StringBuilder();
            outputLog.append("--- Session: ").append(new Date()).append(" ---\n");
            if (errorOccurred) {
                outputLog.append("Error: ").append(errorMessage).append("\n");
            } else {
                outputLog.append("Result: ").append(result).append("\n");
            }
            outputLog.append("------------------------------\n\n");
            bw.write(outputLog.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Error detection
    public static void validateExpression(String expr, int choice) {
        if (!expr.matches("[a-zA-Z0-9+\\-*/() ]+")) {
            throw new IllegalArgumentException("Invalid characters in expression.");
        }

        if (choice == 1) {
            int balance = 0;
            for (char c : expr.toCharArray()) {
                if (c == '(') balance++;
                else if (c == ')') balance--;
                if (balance < 0) throw new IllegalArgumentException("Mismatched parentheses.");
            }
            if (balance != 0) throw new IllegalArgumentException("Mismatched parentheses.");
        }

        if (choice == 2 || choice == 3) {
            int countVars = 0, countOps = 0;
            for (char c : expr.toCharArray()) {
                if (Character.isLetter(c)) countVars++;
                else if ("+-*/".indexOf(c) != -1) countOps++;
            }
            if ((choice == 2 && countVars - 1 != countOps) || (choice == 3 && countVars - 1 != countOps)) {
                throw new IllegalArgumentException("Invalid number of operands/operators.");
            }
        }
    }

    public static int evaluatePostfix(String expr, Map<Character, Integer> vars) {
        Stack<Integer> stack = new Stack<>();
        for (char c : expr.toCharArray()) {
            if (Character.isLetter(c)) {
                stack.push(vars.get(c));
            } else {
                if (stack.size() < 2) throw new IllegalArgumentException("Insufficient operands.");
                int b = stack.pop();
                int a = stack.pop();
                stack.push(applyOp(c, a, b));
            }
        }
        return stack.pop();
    }

    public static int evaluatePrefix(String expr, Map<Character, Integer> vars) {
        Stack<Integer> stack = new Stack<>();
        for (int i = expr.length() - 1; i >= 0; i--) {
            char c = expr.charAt(i);
            if (Character.isLetter(c)) {
                stack.push(vars.get(c));
            } else {
                if (stack.size() < 2) throw new IllegalArgumentException("Insufficient operands.");
                int a = stack.pop();
                int b = stack.pop();
                stack.push(applyOp(c, a, b));
            }
        }
        return stack.pop();
    }

    public static int evaluateInfix(String expr, Map<Character, Integer> vars) {
        Stack<Integer> values = new Stack<>();
        Stack<Character> ops = new Stack<>();

        for (char c : expr.toCharArray()) {
            if (Character.isWhitespace(c))
                continue;

            if (Character.isLetter(c)) {
                values.push(vars.get(c));
            } else if (c == '(') {
                ops.push(c);
            } else if (c == ')') {
                while (!ops.isEmpty() && ops.peek() != '(') {
                    int b = values.pop();
                    int a = values.pop();
                    values.push(applyOp(ops.pop(), a, b));
                }
                if (ops.isEmpty()) throw new IllegalArgumentException("Mismatched parentheses.");
                ops.pop(); // pop '('
            } else {
                while (!ops.isEmpty() && precedence(c) <= precedence(ops.peek())) {
                    int b = values.pop();
                    int a = values.pop();
                    values.push(applyOp(ops.pop(), a, b));
                }
                ops.push(c);
            }
        }

        while (!ops.isEmpty()) {
            int b = values.pop();
            int a = values.pop();
            values.push(applyOp(ops.pop(), a, b));
        }

        return values.pop();
    }

    private static int precedence(char op) {
        return switch (op) {
            case '+', '-' -> 1;
            case '*', '/' -> 2;
            default -> -1;
        };
    }

    private static int applyOp(char op, int a, int b) {
        return switch (op) {
            case '+' -> a + b;
            case '-' -> a - b;
            case '*' -> a * b;
            case '/' -> {
                if (b == 0) throw new ArithmeticException("Division by zero");
                yield a / b;
            }
            default -> throw new IllegalArgumentException("Unsupported operator: " + op);
        };
    }
}
