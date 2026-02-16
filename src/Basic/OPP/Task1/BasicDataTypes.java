package Basic.OPP.Task1;

import java.util.Scanner;

public class BasicDataTypes {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        BasicDataTypes BDT = new BasicDataTypes
                (
                        input.nextInt(),
                        input.nextLong(),
                        input.next().charAt(0),
                        input.nextFloat(),
                        input.nextDouble()
                );
        BDT.printBasicDataTypes();
    }
    private int intNum;
    private long longNum;
    private char character;
    private float floatNum;
    private double doubleNum;

    public BasicDataTypes(int intNum, long longNum, char character, float floatNum, double doubleNum) {
        this.intNum = intNum;
        this.longNum = longNum;
        this.character = character;
        this.floatNum = floatNum;
        this.doubleNum = doubleNum;
    }

    public int getIntNum() {
        return intNum;
    }

    public void setIntNum(int intNum) {
        this.intNum = intNum;
    }

    public long getLongNum() {
        return longNum;
    }

    public void setLongNum(long longNum) {
        this.longNum = longNum;
    }

    public char getCharacter() {
        return character;
    }

    public void setCharacter(char character) {
        this.character = character;
    }

    public float getFloatNum() {
        return floatNum;
    }

    public void setFloatNum(float floatNum) {
        this.floatNum = floatNum;
    }

    public double getDoubleNum() {
        return doubleNum;
    }

    public void setDoubleNum(double doubleNum) {
        this.doubleNum = doubleNum;
    }

    public void printBasicDataTypes(){
        System.out.println(intNum);
        System.out.println(longNum);
        System.out.println(character);
        System.out.println(floatNum);
        System.out.println(doubleNum);

    }
}
