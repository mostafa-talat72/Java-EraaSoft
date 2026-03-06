package Basic.OPP.TaskCodeforcesSheet1AssiutFromFtoO;

public class Calculator {

    public static void main(String[] args) {
        java.util.Scanner input = new java.util.Scanner(System.in);
        String expression = input.nextLine();
        Calculator calculator = new Calculator(expression);
        System.out.println(calculator.calculateExpression());
    }
    private String expression;


    public Calculator(String expression) {
        this.expression = expression;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public int calculateExpression() {
        String part1="";
        char operator = ' ';
        String part2="", num="";

        for(int i=0;i<expression.length();i++) {
            if (expression.charAt(i) == '+' || expression.charAt(i) == '-' || expression.charAt(i) == '*' || expression.charAt(i) == '/') {
                operator = expression.charAt(i);
                part1 = num;
                num = "";
                continue;
            }
            num += expression.charAt(i);
        }
        part2 = num;


        int num1 = Integer.parseInt(part1);
        int num2 = Integer.parseInt(part2);
        int result = 0;

        switch (operator) {
            case '+':
                result = num1 + num2;
                break;
            case '-':
                result = num1 - num2;
                break;
            case '*':
                result = num1 * num2;
                break;
            case '/':
                result = num1 / num2;
                break;
        }
        return result;
    }
}
