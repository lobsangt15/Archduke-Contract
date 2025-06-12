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
    private boolean isAttacking = false;
    private boolean isAlive = true;

    public FodderEnemy(Player player) {
        this.player = player;
        facingRight = true;
        xCoord = 900; // starting position is (50, 435), right on top of ground
        yCoord = 500;
        healthPoints = 1000;
        damageOutput = 35;
        try {
            right = ImageIO.read(new File("src/images/demon_idle_1.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        ArrayList<BufferedImage> images = new ArrayList<>();
        for (int i = 1; i < 7; i++) {
            String filename = "src/images/demon_idle_" + i + ".png";
            try {
                images.add(ImageIO.read(new File(filename)));
            } catch (IOException e) {
                System.out.println(e.getMessage() + " " + filename);
            }
        }
        idleAnimation = new Animation(images, 150);
        currentAnimation = idleAnimation;


        images = new ArrayList<>();
        for (int i = 1; i < 13; i++) {
            String filename = "src/images/demon_walk_" + i + ".png";
            try {
                images.add(ImageIO.read(new File(filename)));
            }
            catch (IOException e) {
                System.out.println(e.getMessage() + " " + filename);
            }
        }
        movingAnimation = new Animation(images,150);

        images = new ArrayList<>();
        for (int i = 1; i < 16; i++) {
            String filename = "src/images/demon_cleave_" + i + ".png";
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
        if (!isAlive) {
            return;
        }

        int playerXCoord = player.getxCoord();
        int distance = Math.abs(playerXCoord - xCoord);

        if (healthPoints <= 0) {
            isAlive = false;
            currentAnimation = idleAnimation;
            return;
        }

        // Always face the player
        facingRight = playerXCoord >= xCoord;


        if (distance < attackRange) {
            if (!isAttacking || attackAnimation.isDone()) {
                attackAnimation.reset();
                currentAnimation = attackAnimation;
                isAttacking = true;

                // Optional: deal damage here
            }
            // Do not move while attacking
        } else if (distance <= followRange) {
            if (!isAttacking || attackAnimation.isDone()) {
                isAttacking = false;
                currentAnimation = movingAnimation;
                if (playerXCoord < xCoord) {
                    xCoord -= MOVE_AMT;
                } else {
                    xCoord += MOVE_AMT;
                }
            }
        } else {
            currentAnimation = idleAnimation;
            isAttacking = false;
        }
    }


    public BufferedImage getFodderEnemyImage() {
        return currentAnimation.getActiveFrame();
    }
}
