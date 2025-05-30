import javax.swing.*;
import java.awt.*;

public class Menu extends JPanel {
    public Menu (MainFrame frame) {
        setLayout(new GridBagLayout());
        setBackground(Color.BLACK);
        JLabel title = new JLabel("Archduke Contractors");
        title.setFont(new Font("Serif", Font.BOLD, 40));

        JButton startButton = new JButton("Start!");
        JButton guideBookButton = new JButton("Guide Book.");
        JButton exitButton = new JButton("Exit.");
        Font buttonFont = new Font("Courier New", Font.BOLD, 40);
        startButton.setFont(buttonFont);
        guideBookButton.setFont(buttonFont);
        exitButton.setFont(buttonFont);


    }
}
