package Basic.OPP.Task3;

public class Calculator {
    private int num1, num2, num3;

    public Calculator(int num1, int num2, int num3) {
        if (checkEvenNumber(num1) && checkEvenNumber(num2) && checkEvenNumber(num3)) {
            this.num1 = num1;
            this.num2 = num2;
            this.num3 = num3;
        }
    }

    private boolean checkEvenNumber(int num) {
        if (num % 2 != 0) {
            throw new ArithmeticException("Invalid input, number should be even.");
        }
        return true;
    }

    public int getNum1() {
        return num1;
    }

    public void setNum1(int num1) {
        if (checkEvenNumber(num1)) {
            this.num1 = num1;
        }
    }

    public int getNum2() {
        return num2;
    }

    public void setNum2(int num2) {
        if (checkEvenNumber(num2)) {
            this.num2 = num2;
        }
    }

    public int getNum3() {
        return num3;
    }

    public void setNum3(int num3) {
        if (checkEvenNumber(num3)) {
            this.num3 = num3;
        }
    }

    public int add() {
        return num1 + num2 + num3;
    }

    public void showAddition() {
        System.out.println("Addition is: " + add());
    }

}
