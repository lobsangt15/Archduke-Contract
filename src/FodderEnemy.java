import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class FodderEnemy extends Player {
    private final int MOVE_AMT = 3;
    private BufferedImage right;
    private boolean facingRight;
    private int xCoord;
    private int yCoord;
    private Animation idleAnimation;
    private Animation movingAnimation;
    private Animation attackAnimation;
    private Animation currentAnimation;
    private int healthPoints;
    private int damageOutput;
    private Player player;
    private int attackRange = 30;
    private int followRange = 75;

    public FodderEnemy(Player player) {
        this.player = player;
        facingRight = true;
        xCoord = 900; // starting position is (50, 435), right on top of ground
        yCoord = 500;
        healthPoints = 1000;
        damageOutput = 35;
        try {
            right = ImageIO.read(new File("src/images/FodderEnemyIdle1.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        ArrayList<BufferedImage> images = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            String filename = "src/images/FodderEnemyIdle" + i + ".png";
            try {
                images.add(ImageIO.read(new File(filename)));
            } catch (IOException e) {
                System.out.println(e.getMessage() + " " + filename);
            }
        }
        idleAnimation = new Animation(images, 150);
        currentAnimation = idleAnimation;


        images = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            String filename = "src/images/FodderEnemyFlying" + i + ".png";
            try {
                images.add(ImageIO.read(new File(filename)));
            }
            catch (IOException e) {
                System.out.println(e.getMessage() + " " + filename);
            }
        }
        movingAnimation = new Animation(images,150);

        images = new ArrayList<>();
        for (int i = 1; i < 9; i++) {
            String filename = "src/images/FodderEnemyAttack" + i + ".png";
            try {
                images.add(ImageIO.read(new File(filename)));
            }
            catch (IOException e) {
                System.out.println(e.getMessage() + " " + filename);
            }
        }
        attackAnimation = new Animation(images,150);
    }

    public int getxCoord() {
        if (facingRight) {
            return xCoord;
        } else {
            return (xCoord + (getFodderEnemyImage().getWidth()));
        }
    }

    public int getyCoord() {
        return yCoord;
    }

    public int getHeight() {
        return getFodderEnemyImage().getHeight();
    }

    public int getWidth() {
        if (facingRight) {
            return getFodderEnemyImage().getWidth();
        } else {
            return getFodderEnemyImage().getWidth() * -1;
        }
    }


    public void AI(Player player) {
        int playerXCoord = player.getxCoord();
        int distance = Math.abs(playerXCoord - xCoord);

        if (distance < attackRange) {
            currentAnimation = attackAnimation;
        } else if (distance <= followRange) {
            currentAnimation = movingAnimation;
        }
        if (playerXCoord < xCoord) {
            xCoord -= MOVE_AMT;
            facingRight = true;
        } else if (playerXCoord > xCoord) {
            xCoord += MOVE_AMT;
            facingRight = false;
        }
        else {
            currentAnimation = idleAnimation;
        }
    }


    public BufferedImage getFodderEnemyImage() {
        return currentAnimation.getActiveFrame();
    }
}
