package fidness.Client;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            App app = new App();
            app.showLoginPanel();
        });
    }
}