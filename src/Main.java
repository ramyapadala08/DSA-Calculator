import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("======== DSA Calculator ========");
        System.out.println("Select operation:");
        System.out.println("1. Evaluate through variables");
        System.out.println("2. Conversions");
        System.out.println("3. Tree Traversing");
        System.out.println("4. Bitwise Operation");
        // System.out.println("5. History");
        System.out.println("5. Step By Step Evaluation of numeric equations");
        System.out.println("6. Step By Step Evaluation of string equations");
        System.out.println("7. Exit");
        System.out.print("Choice of Action: ");
        int choice = sc.nextInt();
        sc.nextLine(); // consume newline

        switch (choice) {
            case 1 -> ExpressionEvaluator.handle();
            case 2 -> Converter.handle();
            case 3 -> ExpressionTreeVisualizer.handle();
            case 4 -> BitwiseEvaluator.handle();
            case 5 -> StepByStepEvaluatorNumeric.handle();
            case 6 -> StepByStepEvaluatorString.handle(); 
            case 7 ->{
                System.out.println("Exiting... Goodbye!");
                System.exit(0);
            }
            default -> System.out.println("Invalid choice.");
        }
    }
}