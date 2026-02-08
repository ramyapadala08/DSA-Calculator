import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExpressionTreeVisualizer {
    private static class Node {
        String value;
        Node left, right;

        Node(String value) {
            this.value = value;
        }
    }

    public static void handle() {
        Scanner sc = new Scanner(System.in);
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        try (BufferedWriter inputWriter = new BufferedWriter(new FileWriter("input.txt", true));
                BufferedWriter outputWriter = new BufferedWriter(new FileWriter("output.txt", true))) {

            // Session header
            inputWriter.write("\n====== Session Started at " + timestamp + " ======\n");
            outputWriter.write("\n====== Session Started at " + timestamp + " ======\n");

            System.out.println("Enter the expression(Example: 3 4 + 5 *):");
            String expr = sc.nextLine();
            System.out.println("Enter the type of expression (infix / prefix / postfix):");
            String type = sc.nextLine().toLowerCase();

            inputWriter.write("Expression: " + expr + " (" + type + ")\n");

            Node root = null;
            try {
                switch (type) {
                    case "postfix" -> root = buildFromPostfix(expr.trim().split("\\s+"));
                    case "prefix" -> root = buildFromPrefix(expr.trim().split("\\s+"));
                    case "infix" -> root = buildFromInfix(expr.trim());
                    default -> {
                        System.out.println(" Invalid expression type. Must be infix, prefix, or postfix.");
                        inputWriter.write(" Invalid expression type.\n");
                    }
                }
            } catch (IllegalArgumentException e) {
                System.out.println(" Error: " + e.getMessage());
                outputWriter.write(" Error: " + e.getMessage() + "\n");
            }

            if (root != null) {
                System.out.println("Tree Structure (Visual Representation):");
                outputWriter.write("Tree for: " + expr + " (" + type + ")\n");

                // Print the tree in a visual format
                printVisualTree(root, outputWriter);
            }

            // Session footer
            inputWriter.write("====== Session End ======\n");
            outputWriter.write("====== Session End ======\n");

        } catch (IOException e) {
            System.err.println("File error: " + e.getMessage());
        }
    }

    // Build from Postfix
    private static Node buildFromPostfix(String[] tokens) {
        Stack<Node> stack = new Stack<>();
        for (String token : tokens) {
            if (isOperator(token)) {
                if (stack.size() < 2)
                    throw new IllegalArgumentException("Invalid postfix expression: insufficient operands for operator '" + token + "'");
                Node right = stack.pop();
                Node left = stack.pop();
                Node node = new Node(token);
                node.left = left;
                node.right = right;
                stack.push(node);
            } else {
                stack.push(new Node(token));
            }
        }
        if (stack.size() != 1)
            throw new IllegalArgumentException("Invalid postfix expression: leftover operands or operators.");
        return stack.peek();
    }    

    // Build from Prefix
    private static Node buildFromPrefix(String[] tokens) {
        Stack<Node> stack = new Stack<>();
        for (int i = tokens.length - 1; i >= 0; i--) {
            String token = tokens[i];
            if (isOperator(token)) {
                if (stack.size() < 2)
                    throw new IllegalArgumentException("Invalid prefix expression: insufficient operands for operator '" + token + "'");
                Node node = new Node(token);
                node.left = stack.pop();
                node.right = stack.pop();
                stack.push(node);
            } else {
                stack.push(new Node(token));
            }
        }
        if (stack.size() != 1)
            throw new IllegalArgumentException("Invalid prefix expression: leftover operands or operators.");
        return stack.peek();
    }    

    // Build from Infix using Shunting Yard
    private static Node buildFromInfix(String expr) {
        String[] postfix = infixToPostfix(expr);
        return buildFromPostfix(postfix);
    }

    private static String[] infixToPostfix(String expr) {
        List<String> output = new ArrayList<>();
        Stack<String> ops = new Stack<>();
        StringTokenizer st = new StringTokenizer(expr, "+-*/^() ", true);
        while (st.hasMoreTokens()) {
            String token = st.nextToken().trim();
            if (token.isEmpty())
                continue;
            if (isOperand(token)) {
                output.add(token);
            } else if (token.equals("(")) {
                ops.push(token);
            } else if (token.equals(")")) {
                while (!ops.isEmpty() && !ops.peek().equals("(")) {
                    output.add(ops.pop());
                }
                if (ops.isEmpty() || !ops.peek().equals("(")) {
                    throw new IllegalArgumentException("Mismatched parentheses in infix expression.");
                }
                ops.pop(); // remove '('                
            } else {
                while (!ops.isEmpty() && precedence(token) <= precedence(ops.peek())) {
                    output.add(ops.pop());
                }
                ops.push(token);
            }
        }
        while (!ops.isEmpty())
            output.add(ops.pop());
        return output.toArray(new String[0]);
    }

    // Calculate tree height for traversal
    private static int getHeight(Node node) {
        if (node == null)
            return 0;
        return 1 + Math.max(getHeight(node.left), getHeight(node.right));
    }

    // Print a visual representation of the tree as a traditional ASCII tree
    private static void printVisualTree(Node root, BufferedWriter writer) throws IOException {
        if (root == null)
            return;

        // Get the height of the tree
        int height = getHeight(root);

        // Calculate the width of the tree display
        int width = (int) Math.pow(2, height) * 2 - 1;

        // Create a character grid for the tree
        char[][] grid = new char[height * 2][width];
        for (char[] row : grid) {
            Arrays.fill(row, ' ');
        }

        // Fill the grid with the tree structure
        fillGrid(grid, root, 0, 0, width - 1);

        // Print the tree
        for (char[] row : grid) {
            // Find the last non-space character to trim trailing spaces
            int lastNonSpace = row.length - 1;
            while (lastNonSpace >= 0 && row[lastNonSpace] == ' ') {
                lastNonSpace--;
            }

            // Build the line
            String line = new String(row, 0, lastNonSpace + 1);
            System.out.println(line);
            writer.write(line + "\n");
        }
    }

    // Recursively fill the grid with the tree structure
    private static void fillGrid(char[][] grid, Node node, int depth, int left, int right) {
        if (node == null)
            return;

        int mid = (left + right) / 2;

        // Place node value at the center of its space
        char[] nodeChars = node.value.toCharArray();
        int startPos = mid - nodeChars.length / 2;
        for (int i = 0; i < nodeChars.length; i++) {
            grid[depth][startPos + i] = nodeChars[i];
        }

        // If there are children, draw branches
        if (node.left != null) {
            // Calculate left child's position
            int leftMid = (left + mid - 1) / 2;

            // Draw left branch: '/'
            grid[depth + 1][leftMid + 1] = '/';

            // Draw horizontal connector: '_'
            for (int i = leftMid + 2; i < mid; i++) {
                grid[depth + 1][i] = '_';
            }

            // Recursively fill left subtree
            fillGrid(grid, node.left, depth + 2, left, mid - 1);
        }

        if (node.right != null) {
            // Calculate right child's position
            int rightMid = (mid + 1 + right) / 2;

            // Draw right branch: '\'
            grid[depth + 1][rightMid - 1] = '\\';

            // Draw horizontal connector: '_'
            for (int i = mid + 1; i < rightMid - 1; i++) {
                grid[depth + 1][i] = '_';
            }

            // Recursively fill right subtree
            fillGrid(grid, node.right, depth + 2, mid + 1, right);
        }
    }

    private static boolean isOperator(String s) {
        return "+-*/^".contains(s);
    }

    private static boolean isOperand(String s) {
        return s.matches("[a-zA-Z0-9]+");
    }

    private static int precedence(String op) {
        return switch (op) {
            case "+", "-" -> 1;
            case "*", "/" -> 2;
            case "^" -> 3;
            default -> -1;
        };
    }
}