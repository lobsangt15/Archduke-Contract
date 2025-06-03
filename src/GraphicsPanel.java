import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GraphicsPanel extends JPanel implements ActionListener, KeyListener, MouseListener {
    private BufferedImage background1;
    private BufferedImage backgroundAsset1;
    private Timer timer;
    private Player player;
    private boolean[] pressedKeys;
    private JButton speak;
    private GoldenKnight boss1;

    public GraphicsPanel() {
        speak = new JButton("Speak");
        timer = new Timer(2, this);
        timer.start();
        try {
            background1 = ImageIO.read(new File("src/Background_0.png"));
            backgroundAsset1 = ImageIO.read(new File("src/Background_1.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        player = new Player();
        boss1 = new GoldenKnight();
        pressedKeys = new boolean[128]; // 128 keys on keyboard, max keycode is 127
        addKeyListener(this);
        addMouseListener(this);
        setFocusable(true); // this line of code + one below makes this panel active for keylistener events
        requestFocusInWindow();// see comment above
        speak.addActionListener(this);
        add(speak);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // the order that things get "painted" matter; we paint the background first
        g.drawImage(background1, 0, 0, null);
        g.drawImage(backgroundAsset1, 0, 0, null);

        // UPDATED!
        g.drawImage(player.getPlayerImage(), player.getxCoord(), player.getyCoord(), player.getWidth(), player.getHeight(), null);
        g.drawImage(boss1.getGoldenKnightImage(), boss1.getxCoord(), boss1.getyCoord(), boss1.getWidth(), boss1.getHeight(), null);

        // draw score
        g.setFont(new Font("Courier New", Font.BOLD, 24));

        // player moves left (A)
        if (pressedKeys[65]) {
            player.faceLeft();
            player.moveLeft();
        }

        // player moves right (D)
        if (pressedKeys[68]) {
            player.faceRight();
            player.moveRight();
        }

        // player jumps (space bar)
        if (pressedKeys[32]) {
            player.moveUp();
        }

        // player jumps (w) added for convenience but will be deleted later
        if (pressedKeys[87]) {
            player.moveUp();
        }

        // player moves down (S)
        if (pressedKeys[83]) {
            player.moveDown();
        }

        speak.setLocation(500, 500);
    }

    // ActionListener interface method
    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    // KeyListener interface methods
    @Override
    public void keyTyped(KeyEvent e) { } // unimplemented

    @Override
    public void keyPressed(KeyEvent e) {
        // see this for all keycodes: https://stackoverflow.com/questions/15313469/java-keyboard-keycodes-list
        // A = 65, D = 68, S = 83, W = 87, left = 37, up = 38, right = 39, down = 40, space = 32, enter = 10
        int key = e.getKeyCode();
        pressedKeys[key] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        pressedKeys[key] = false;
        player.idle();
    }

    // MouseListener interface methods
    @Override
    public void mouseClicked(MouseEvent e) { }  // unimplemented because
            // if you move your mouse while clicking, this method isn't
            // called, so mouseReleased is best

    @Override
    public void mousePressed(MouseEvent e) { } // unimplemented

    @Override
    public void mouseReleased(MouseEvent e) { } // unimplemented

    @Override
    public void mouseEntered(MouseEvent e) { } // unimplemented

    @Override
    public void mouseExited(MouseEvent e) { } // unimplemented
}
