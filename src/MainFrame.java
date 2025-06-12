// MainFrame.java
import javax.swing.*;
import java.awt.*;

public class MainFrame {
    public MainFrame() {
        JFrame frame = new JFrame("Archduke Contractors");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1920, 1207);
        frame.setLocationRelativeTo(null);
        GraphicsPanel panel = new GraphicsPanel();
        frame.add(panel);
        frame.setVisible(true);
    }
}