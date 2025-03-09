package Stack;

import java.util.Scanner;
import java.util.Stack;

public class InFixtoPostFix {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Infix Expression:");
        String expression = scanner.nextLine();
        String postfix = infixToPostfix(expression);
        System.out.println("Output = " + postfix);
        scanner.close();
    }

    public static String infixToPostfix(String expression) {
        Stack<Character> stack = new Stack<>();
        StringBuilder postfix = new StringBuilder();
        
        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            
            if (c == ' ' || c == ',') continue;
            else if (isOperator(c)) {
                while (!stack.isEmpty() && stack.peek() != '(' && hasHigherPrecedence(stack.peek(), c)) {
                    postfix.append(stack.pop());
                }
                stack.push(c);
            } else if (isOperand(c)) {
                postfix.append(c);
            } else if (c == '(') {
                stack.push(c);
            } else if (c == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    postfix.append(stack.pop());
                }
                stack.pop();
            }
        }

        while (!stack.isEmpty()) {
            postfix.append(stack.pop());
        }

        return postfix.toString();
    }

    public static boolean isOperand(char c) {
        return (c >= '0' && c <= '9') || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    public static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '$';
    }

    public static boolean isRightAssociative(char op) {
        return op == '$';
    }

    public static int getOperatorWeight(char op) {
        switch (op) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            case '$':
                return 3;
            default:
                return -1;
        }
    }

    public static boolean hasHigherPrecedence(char op1, char op2) {
        int op1Weight = getOperatorWeight(op1);
        int op2Weight = getOperatorWeight(op2);

        if (op1Weight == op2Weight) {
            return !isRightAssociative(op1);
        }
        return op1Weight > op2Weight;
    }
}
