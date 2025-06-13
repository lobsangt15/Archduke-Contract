// GraphicsPanel.java
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.sound.sampled.*;

public class GraphicsPanel extends JPanel implements ActionListener, KeyListener, MouseListener {
    private BufferedImage background1;
    private BufferedImage backgroundAsset1;
    private BufferedImage First_Scene;
    private BufferedImage Npc;
    private BufferedImage titleImage;
    private BufferedImage luciferTitleImage;
    private Timer timer;
    private Player player;
    private boolean[] pressedKeys;
    private GoldenKnight goldenKnight;
    private JLabel label;
    private JButton accept;
    private JButton decline;
    private boolean inFirstScene;
    private boolean inFinalScene;
    private boolean hasPlayedNpcSound = false;
    public boolean isHit = false;

    public GraphicsPanel() {
        accept = new JButton("ACCEPT");
        decline = new JButton("DECLINE");

        player = new Player();
        goldenKnight = new GoldenKnight(player);

        pressedKeys = new boolean[128];

        try {
            background1 = ImageIO.read(new File("src/Background_0.png"));
            backgroundAsset1 = ImageIO.read(new File("src/Background_1.png"));
            Npc = ImageIO.read(new File("src/images/Costco_Guys.png"));
            First_Scene = ImageIO.read(new File("src/images/First_Scene.jpg"));
            titleImage = ImageIO.read(new File("src/images/TITLEARCHKNIGHT.png"));
            luciferTitleImage = ImageIO.read(new File("src/images/LUCIFERTITLE.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            titleImage = new BufferedImage(500, 100, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = titleImage.createGraphics();
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0,0,500,100);
            g2d.setColor(Color.BLACK);
            g2d.drawString("TITLE", 200, 50);
            g2d.dispose();

            luciferTitleImage = new BufferedImage(500, 100, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2dLucifer = luciferTitleImage.createGraphics();
            g2dLucifer.setColor(Color.RED);
            g2dLucifer.fillRect(0,0,500,100);
            g2dLucifer.setColor(Color.BLACK);
            g2dLucifer.drawString("LUCIFER", 180, 50);
            g2dLucifer.dispose();
            System.out.println("Using placeholder images for titles due to loading error.");
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
            player.setyCoord(800);
            g.drawImage(player.getPlayerImage(), player.getxCoord(), player.getyCoord(), player.getWidth(), player.getHeight(), null);

            g.drawImage(goldenKnight.getGoldenKnightImage(), goldenKnight.getxCoord(), goldenKnight.getyCoord(), goldenKnight.getWidth(), goldenKnight.getHeight(), null);
            BufferedImage currentTitleImage = goldenKnight.isPhaseTwo() ? luciferTitleImage : titleImage;
            if (currentTitleImage != null) {
                int titleX = (getWidth() - currentTitleImage.getWidth()) / 2;
                int titleY = 20;
                g.drawImage(currentTitleImage, titleX, titleY, null);
            }

            Font font = new Font("Verdana", Font.BOLD, 40);
            g.setFont(font);
            g.setColor(Color.RED);
            g.drawString("Player Health: " + player.getHealth(), 20, 70);
            g.drawString("Golden Knight Health: " + goldenKnight.getHealth(), 20, 120);
        }

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

        if ((!inFirstScene && !inFinalScene) && player.getxCoord() <= 1750 && player.getxCoord() >= 1400) {
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
            if (!hasPlayedNpcSound) {
                playSound();
                hasPlayedNpcSound = true;
            }

            accept.setLocation(100, 700);
            decline.setLocation(200, 700);
        } else {
            accept.setVisible(false);
            decline.setVisible(false);
            hasPlayedNpcSound = false;
        }
        System.out.println("Player Y: " + player.getyCoord());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == accept) {
            inFirstScene = true;
            player.setxCoord(50);
            player.setyCoord(player.getGroundY() - player.getHeight());
        }
        accept.setVisible(false);
        decline.setVisible(false);
        requestFocusInWindow();

        player.applyGravity();
        player.updateRoll();

        if (inFirstScene) {
            handleCollisions();
        }

        if (player.isAttacking() && player.attackAnimation.isDone()) {
            player.isAttacking = false;
            player.idle();
            System.out.println("Player attack animation finished!");
        }

        if (goldenKnight.isAttacking() && (goldenKnight.phaseOneAttackAnimation.isDone() || goldenKnight.luciferSlashAnimation.isDone())) {
            goldenKnight.isAttacking = false;
            goldenKnight.idle();
            System.out.println("Golden Knight attack animation finished!");
        }

        repaint();
    }

    private void handleCollisions() {
        Rectangle playerRect = player.playerRect();

        if (player.isAttacking()) {
            Rectangle playerAttackHitbox = player.getPlayerAttackHitbox();
            if (playerAttackHitbox != null && playerAttackHitbox.intersects(goldenKnight.getBossRect()) && !goldenKnight.isDead() && !goldenKnight.isRebirthing()) {
                if (!isHit) {
                    goldenKnight.takeDamage(player.getDamageOutput());
                    System.out.println("Player successfully hit Golden Knight!");
                    isHit = true;
                }
            }
        }

        if (goldenKnight.isAttacking()) {
            Rectangle goldenKnightAttackHitbox = goldenKnight.getAttackHitbox();
            if (goldenKnightAttackHitbox != null && goldenKnightAttackHitbox.intersects(playerRect)) {
                if (!isHit) {
                    player.takeDamage(goldenKnight.getDamageOutput());
                    System.out.println("Golden Knight hit player! Player Health: " + player.getHealth());
                    isHit = true;
                }
            }
        }
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

        if (key == 40) {
            if (!goldenKnight.isAttacking()) {
                goldenKnight.Attack();
                isHit = false;
            }
        }

        if (key == 39) {
            goldenKnight.moveRight();
        }

        if (key == 37) {
            goldenKnight.moveLeft();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        pressedKeys[key] = false;
        if (!player.isJumping() && !player.isFalling() && !player.isRolling() && !player.isAttacking()) {
            player.idle();
        }
        if (!goldenKnight.isAttacking()) {
            goldenKnight.idle();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) { }

    @Override
    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            isHit = false;
            if (!player.isAttacking() && !player.isRolling() && !player.isJumping() && !player.isFalling()) {
                if (!(player.getDirection())) {
                    player.AttackLeft();
                } else {
                    player.AttackRight();
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }

    public void playSound() {
        File audioFile;
        if (!inFirstScene) {
            audioFile = new File("src/NPC_Speech.wav");
        } else {
            audioFile = new File("src/Big_Booms.wav");
        }
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}