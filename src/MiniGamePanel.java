import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class MiniGamePanel extends JPanel implements ActionListener {
    private Timer timer;
    private int playerX = 50, playerY = 300;
    private int obstacleX = 600;
    private int score = 0;
    private boolean jump = false;
    private int jumpVel = 0;
    private Runnable onGameEnd;
    private boolean won;

    public MiniGamePanel(Runnable onGameEnd) {
        this.onGameEnd = onGameEnd;
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE && !jump) {
                    jump = true;
                    jumpVel = -15;
                }
            }
        });
    }

    public void startGame() {
        timer = new Timer(20, this);
        timer.start();
    }
    public void actionPerformed(ActionEvent e) {
        if (jump) {
            playerY += jumpVel;
            jumpVel += 1;
            if (playerY >= 300) {
                playerY = 300;
                jump = false;
            }
        }

        obstacleX -= 10;
        if (obstacleX < 0) {
            obstacleX = 600 + new Random().nextInt(200);
            score++;
        }

        if (Math.abs(playerX - obstacleX) < 40 && playerY >= 250) {
            timer.stop();
            JOptionPane.showMessageDialog(this, "You did your best, but no health buff for you!");
            won = false;
            onGameEnd.run();
        }

        if (score >= 5) {
            timer.stop();
            JOptionPane.showMessageDialog(this, "You win! Congrats on getting 20 extra health!");
            won = true;
            onGameEnd.run();
        }

        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fillRect(playerX, playerY, 40, 40); // player

        g.setColor(Color.RED);
        g.fillRect(obstacleX, 300, 40, 40); // obstacle

        g.setColor(Color.GREEN);
        g.drawString("Score: " + score, 20, 20);
        g.drawString("i am preparing wait rq", 20, 40);
        g.drawString("Press SPACE to jump", 20, 60);
    }

    public boolean hasWon() {
        return won;
    }

    public void collectedBuff() {
        won = false;
    }
}