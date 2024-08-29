package fidness;

import fidness.Client.App;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Intentar establecer el Look and Feel del sistema
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
            // Si falla, se usará el Look and Feel por defecto de Java
        }

        // Iniciar la aplicación en el Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            // Aquí podríamos inicializar recursos globales si fuera necesario

            // Crear y mostrar la ventana principal de la aplicación
            new App();
        });
    }
}