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

        player = new Player();
        boss1 = new GoldenKnight(player);
        imp = new FodderEnemy(player);
        pressedKeys = new boolean[128];

        try {
            background1 = ImageIO.read(new File("src/Background_0.png"));
            backgroundAsset1 = ImageIO.read(new File("src/Background_1.png"));
            Npc = ImageIO.read(new File("src/images/Costco_Guys.png"));
            First_Scene = ImageIO.read(new File("src/images/First_Scene.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        addKeyListener(this);
        addMouseListener(this);
        setFocusable(true);
        requestFocusInWindow();
        accept.addActionListener(this);
        decline.addActionListener(this);
        add(accept);
        add(decline);
        accept.setVisible(false);
        decline.setVisible(false);

        timer = new Timer(16, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!inFirstScene && !inFinalScene) {
            g.drawImage(background1, 0, 0, null);
            g.drawImage(backgroundAsset1, 0, 0, null);
            g.drawImage(Npc, 1525, 500, null);
            g.drawImage(player.getPlayerImage(), player.getxCoord(), player.getyCoord(), player.getWidth(), player.getHeight(), null);
        } else if (inFirstScene) {
            g.drawImage(First_Scene, 0, 0, null);
            g.drawImage(player.getPlayerImage(), player.getxCoord(), player.getyCoord(), player.getWidth(), player.getHeight(), null);
            g.drawImage(imp.getFodderEnemyImage(), imp.getxCoord(), imp.getyCoord(), imp.getWidth(), imp.getHeight(), null);
        }

        Font font = new Font("Verdana", Font.BOLD, 40);
        g.setFont(font);
        g.setColor(Color.RED);
        g.drawString("Player Health: " + player.getHealth(), 20, 70);

        if (pressedKeys[65]) {
            player.faceLeft();
            player.moveLeft();
        }

        if (pressedKeys[68]) {
            player.faceRight();
            player.moveRight();
        }

        if (pressedKeys[83]) {
            player.moveDown();
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
            g.setColor(Color.WHITE);
            g.fillRect(100, 600, 400, 100);

            g.setColor(Color.BLACK);
            g.drawRect(100, 600, 400, 100);

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
        System.out.println("Player Y: " + player.getyCoord());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == accept) {
            inFirstScene = true;
        }
        accept.setVisible(false);
        decline.setVisible(false);
        requestFocusInWindow();

        player.applyGravity();
        player.updateRoll();

        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        pressedKeys[key] = true;

        if (key == 67) {
            player.startRoll();
        }

        if (key == 32 && !player.isJumping() && !player.isFalling()) {
            player.jump();
        }

        if (key == 87 && !player.isJumping() && !player.isFalling()) {
            player.jump();
        }

        impAIStart = true;
        goldenKnightAIStart = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        pressedKeys[key] = false;
        if (!player.isJumping() && !player.isFalling() && !player.isRolling() && !player.isAttacking()) {
            player.idle();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) { }

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
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }
}