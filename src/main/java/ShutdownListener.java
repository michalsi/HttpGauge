import java.util.Scanner;

class ShutdownListener implements Runnable {
    private boolean running = false;
    private boolean exit = false;

    ShutdownListener() {
        System.out.println("The program will continue running until it receives signal from the user");
        System.out.println("Enter any char to exit the program... ");
    }

    @Override
    public void run() {
        running = true;
        Scanner sc = new Scanner(System.in);
        exit = true;
    }

    boolean isRunning() {
        return running;
    }

    boolean shouldExit() {
        return exit;
    }
}
