package Stack;
import java.util.Scanner;
import java.util.Stack;

public class PostFixtoInFix {
	
	
	    public static void main(String[] args) {
	        Scanner scanner = new Scanner(System.in);
	        System.out.println("Enter a postfix expression:");
	        String postfix = scanner.nextLine();
	        
	        if (isValidPostfix(postfix)) {
	            String infix = postfixToInfix(postfix);
	            System.out.println("Infix Expression: " + infix);
	        } else {
	            System.out.println("Invalid postfix expression.");
	        }
	    }

	    public static boolean isValidPostfix(String postfix) {
	        int operandCount = 0;
	        int operatorCount = 0;

	        for (int i = 0; i < postfix.length(); i++) {
	            char c = postfix.charAt(i);
	            if (isOperand(c)) {
	                operandCount++;
	            } else if (isOperator(c)) {
	                operatorCount++;
	            }
	        }
	        // A valid postfix must have at least one more operand than operators
	        // and it must end with an operator.
	        return operandCount > operatorCount && isOperator(postfix.charAt(postfix.length() - 1));
	    }

	    public static String postfixToInfix(String postfix) {
	        Stack<String> stack = new Stack<>();
	        
	        for (int i = 0; i < postfix.length(); i++) {
	            char c = postfix.charAt(i);
	            
	            if (isOperand(c)) {
	                stack.push(String.valueOf(c));
	            } else if (isOperator(c)) {
	                String b = stack.pop();
	                String a = stack.pop();
	                String expr = "(" + a + c + b + ")";
	                stack.push(expr);
	            }
	        }

	        return stack.pop();
	    }

	    public static boolean isOperand(char c) {
	        return (c >= '0' && c <= '9') || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
	    }

	    public static boolean isOperator(char c) {
	        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^';
	    }
	}
