import javax.swing.*;

public class MainFrame {
    public MainFrame() {
        JFrame frame = new JFrame("Archduke Contractors");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(972, 540); // will be the default size for the frame window and background
        frame.setLocationRelativeTo(null);
        GraphicsPanel panel = new GraphicsPanel();
        frame.add(panel);
        frame.setVisible(true);
    }
}
