package io.apptik.json;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("Press \"ENTER\" to continue...");
                if (scanner.nextLine().equals("exit")) break;
                //do smth cool now
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {

        }
    }
}
