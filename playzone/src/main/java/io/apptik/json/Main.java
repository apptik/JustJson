package io.apptik.json;

public class Main {
    public static void main(String[] args) {
        try {
            ApiJJson client1 = new ApiJJson(requestQueue1,
                    System.getProperty("darksky"));
            ApiJackson client2 = new ApiJackson(requestQueue2,
                    System.getProperty("darksky"));
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
