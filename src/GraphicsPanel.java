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
    private BufferedImage First_Scene;
    private BufferedImage Npc;
    private Timer timer;
    private Player player;
    private boolean[] pressedKeys;
    private GoldenKnight boss1;
    private FodderEnemy imp;
    private JLabel label;
    private boolean impAIStart = false;
    private boolean goldenKnightAIStart = false;
    private JButton accept;
    private JButton decline;
    private boolean inFirstScene;
    private boolean inFinalScene;

    public GraphicsPanel() {
        accept = new JButton("ACCEPT");
        decline = new JButton("DECLINE");
        timer = new Timer(16, this);
        timer.start();
        try {
            background1 = ImageIO.read(new File("src/Background_0.png"));
            backgroundAsset1 = ImageIO.read(new File("src/Background_1.png"));
            Npc = ImageIO.read(new File("src/images/Costco_Guys.png"));
            First_Scene = ImageIO.read(new File("src/images/First_Scene.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        player = new Player();
        boss1 = new GoldenKnight(player);
        imp = new FodderEnemy(player);
        pressedKeys = new boolean[128]; // 128 keys on keyboard, max keycode is 127
        addKeyListener(this);
        addMouseListener(this);
        setFocusable(true); // this line of code + one below makes this panel active for keylistener events
        requestFocusInWindow();// see comment above
        accept.addActionListener(this);
        decline.addActionListener(this);
        add(accept);
        add(decline);
        accept.setVisible(false);
        decline.setVisible(false);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        super.paintComponent(g);
        if (!inFirstScene && !inFinalScene) {
            // the order that things get "painted" matter; we paint the background first
            g.drawImage(background1, 0, 0, null);
            g.drawImage(backgroundAsset1, 0, 0, null);
            g.drawImage(Npc, 1525, 500, null);
            g.drawImage(player.getPlayerImage(), player.getxCoord(), player.getyCoord(), player.getWidth(), player.getHeight(), null);
        } else if (inFirstScene) {
            g.drawImage(First_Scene, 0, 0, null);
            g.drawImage(player.getPlayerImage(), player.getxCoord(), player.getyCoord(), player.getWidth(), player.getHeight(), null);
            g.drawImage(imp.getFodderEnemyImage(), imp.getxCoord(), imp.getyCoord(), imp.getWidth(), imp.getHeight(), null);
        }

        // UPDATED!
        //g.drawImage(player.getPlayerImage(), player.getxCoord(), player.getyCoord(), player.getWidth(), player.getHeight(), null);
        //g.drawImage(boss1.getGoldenKnightImage(), boss1.getxCoord(), boss1.getyCoord(), boss1.getWidth(), boss1.getHeight(), null);
        //g.drawImage(imp.getFodderEnemyImage(), imp.getxCoord(), imp.getyCoord(), imp.getWidth(), imp.getHeight(), null);
        player.applyGravity();

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
            player.jump();
        }

        // temporary
        if (pressedKeys[87]) {
            player.jump();
        }

        // player moves down (S)
        if (pressedKeys[83]) {
            player.moveDown();
        }


        // player rolls (C)
        if (pressedKeys[67]) {
            if (!(player.getDirection())) {
                player.RollLeft();
            }
            if (player.getDirection()) {
                player.RollRight();
            }
        }

        if (impAIStart) {
            imp.AI(player);
        }
        if (goldenKnightAIStart) {
            boss1.AI(player);
        }

        if ((!inFirstScene && !inFinalScene) && player.getxCoord() <= 1500 && player.getxCoord() >= 1196) {
            accept.setVisible(true);
            decline.setVisible(true);
            // Draw white box
            g.setColor(Color.WHITE);
            g.fillRect(100, 600, 400, 100);

            // Draw border
            g.setColor(Color.BLACK);
            g.drawRect(100, 600, 400, 100);

            // Draw text
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Hey, it's the Costco Guys here.", 120, 630);
            g.drawString("A golden knight has abducted the Rizzler.", 120, 655);
            g.drawString("Find him for 5 big booms!", 120, 680);

            accept.setLocation(100, 700);
            decline.setLocation(200, 700);
        } else {
            accept.setVisible(false);
            decline.setVisible(false);
        }
        System.out.println("Player X: " + player.getyCoord());
    }

    // ActionListener interface method
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == accept) {
            inFirstScene = true;
        }
        accept.setVisible(false);
        decline.setVisible(false);
        requestFocusInWindow();
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
        impAIStart = true;
        goldenKnightAIStart = true;
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
    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            if (!(player.getDirection())) {
                player.AttackLeft();
            }
            if (player.getDirection()) {
                player.AttackRight();
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (player.isAttacking()) {
            Timer waitForAttack = new Timer(50, null);
            waitForAttack.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    if (player.attackAnimation.isDone()) {
                        player.isAttacking = false;
                        waitForAttack.stop();

                        player.idle();
                        System.out.println("Attack animation finished!");
                    }
                }
            });
            waitForAttack.start();
        } else {
            player.idle();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) { } // unimplemented

    @Override
    public void mouseExited(MouseEvent e) { } // unimplemented
}

