import javax.swing.*;

public class MainFrame {
    public MainFrame() {
        JFrame frame = new JFrame("Super Mario Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(960, 580); // 540 height of image + 40 for window menu bar
        frame.setLocationRelativeTo(null);
        GraphicsPanel panel = new GraphicsPanel();
        frame.add(panel);
        frame.setVisible(true);
    }
}
