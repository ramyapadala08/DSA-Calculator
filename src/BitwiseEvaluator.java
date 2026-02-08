import java.io.*;
import java.util.*;

public class BitwiseEvaluator {

    public static int evaluate(String expr, Map<Character, Integer> vars) {
        validateExpression(expr);

        Stack<Integer> values = new Stack<>();
        Stack<String> ops = new Stack<>();

        expr = expr.replaceAll("\\s", "");

        for (int i = 0; i < expr.length();) {
            char c = expr.charAt(i);

            if (Character.isLetter(c)) {
                if (!vars.containsKey(c)) {
                    throw new IllegalArgumentException("Undefined variable: '" + c + "'");
                }
                values.push(vars.get(c));
                i++;
            } else if (c == '(') {
                ops.push("(");
                i++;
            } else if (c == ')') {
                while (!ops.isEmpty() && !ops.peek().equals("(")) {
                    applyTopOperator(values, ops.pop());
                }
                if (!ops.isEmpty() && ops.peek().equals("(")) {
                    ops.pop(); // pop the '('
                } else {
                    throw new IllegalArgumentException("Mismatched parentheses.");
                }
                i++;
            } else {
                String op;
                if (i + 1 < expr.length()
                        && (expr.substring(i, i + 2).equals("<<") || expr.substring(i, i + 2).equals(">>"))) {
                    op = expr.substring(i, i + 2);
                    i += 2;
                } else {
                    op = String.valueOf(c);
                    i++;
                }

                if (!isValidOperator(op)) {
                    throw new IllegalArgumentException("Unknown operator: " + op);
                }

                // Special handling for unary ~
                if (op.equals("~")) {
                    while (!ops.isEmpty() && precedence(op) < precedence(ops.peek())) {
                        applyTopOperator(values, ops.pop());
                    }
                } else {
                    while (!ops.isEmpty() && precedence(op) <= precedence(ops.peek())) {
                        applyTopOperator(values, ops.pop());
                    }
                }

                ops.push(op);
            }
        }

        while (!ops.isEmpty()) {
            applyTopOperator(values, ops.pop());
        }

        if (values.size() != 1) {
            throw new IllegalStateException("Invalid expression.");
        }

        return values.pop();
    }

    private static void applyTopOperator(Stack<Integer> values, String op) {
        if (op.equals("~")) {
            if (values.isEmpty())
                throw new IllegalStateException("Missing operand for unary operator.");
            int val = values.pop();
            values.push(~val);
        } else {
            if (values.size() < 2)
                throw new IllegalStateException("Missing operands for binary operator.");
            int b = values.pop();
            int a = values.pop();
            values.push(switch (op) {
                case "&" -> a & b;
                case "|" -> a | b;
                case "^" -> a ^ b;
                case "<<" -> a << b;
                case ">>" -> a >> b;
                default -> throw new IllegalArgumentException("Unknown operator: " + op);
            });
        }
    }

    private static int precedence(String op) {
        return switch (op) {
            case "~" -> 4;
            case "<<", ">>" -> 3;
            case "&" -> 2;
            case "^" -> 1;
            case "|" -> 0;
            default -> -1;
        };
    }

    private static boolean isValidOperator(String op) {
        return Set.of("~", "&", "|", "^", "<<", ">>", "(", ")").contains(op);
    }

    private static void validateExpression(String expr) {
        if (expr == null || expr.trim().isEmpty()) {
            throw new IllegalArgumentException("Expression is empty.");
        }
        if (!expr.matches("[a-zA-Z0-9\\s()~&|^<>]*")) {
            throw new IllegalArgumentException("Expression contains invalid characters.");
        }
    }

    public static void handle() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter bitwise expression (e.g., ~a & (b << c)): ");
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
            while (true) {
                System.out.print("Enter value for " + var + ": ");
                String input = scanner.nextLine();
                try {
                    int value = Integer.parseInt(input.trim());
                    vars.put(var, value);
                    inputLog.append(var).append(" = ").append(value).append("\n");
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number. Please enter a valid integer.");
                }
            }
        }

        // Write to input.txt
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("input.txt", true))) {
            bw.write(inputLog.toString());
            bw.write("---Sesion End ----");
            bw.write("------------------------------\n\n");
        } catch (IOException e) {
            System.out.println("Error writing to input.txt: " + e.getMessage());
        }

        int result;
        try {
            result = evaluate(expression, vars);
            System.out.println("Result: " + result);
        } catch (Exception e) {
            String errorMsg = "Evaluation error: " + e.getMessage();
            System.out.println(errorMsg);

            // Also log the error to input.txt
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("input.txt", true))) {
                bw.write("Error: " + e.getMessage() + "\n\n");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

            return;
        }

        StringBuilder outputLog = new StringBuilder();
        outputLog.append("--- Session Start: ").append(new Date()).append(" ---\n");
        outputLog.append("Result: ").append(result).append("\n");
        outputLog.append("----- Session End -------\n");
        outputLog.append("------------------------------\n\n");

        // Write to output.txt
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("output.txt", true))) {
            bw.write(outputLog.toString());
        } catch (IOException e) {
            System.out.println("Error writing to output.txt: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        handle(); // Run via file input/output
    }
}
