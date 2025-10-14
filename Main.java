import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Message:");
        String message = input.nextLine();
        System.out.print(message);
    }
}

class Animal {
    void Sound() {
        System.out.println("Animal makes a sound!");
    }
}