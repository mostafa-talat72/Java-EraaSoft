package Basic.OPP.TaskCodeforcesSheetAssiutFromFtoO;

public class Multiples {

    public static void main(String[] args) {
            java.util.Scanner input = new java.util.Scanner(System.in);
            int A = input.nextInt();
            int B = input.nextInt();
            Multiples multiples = new Multiples(A, B);
            multiples.displayInfo();
    }
        private int A,B;

        public Multiples(int a, int b) {
            this.A = a;
            this.B = b;
        }

        public int getA() {
            return A;
        }

        public void setA(int a) {
            this.A = a;
        }

        public int getB() {
            return B;
        }

        public void setB(int b) {
            this.B = b;
        }

        public boolean isMultiple() {
            return this.A % this.B == 0 || this.B % this.A == 0;
        }

        public void displayInfo() {
            if (this.isMultiple()) {
                System.out.println("Multiples");
            } else {
                System.out.println("No Multiples");
            }
        }
}
