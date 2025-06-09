import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GoldenKnight extends Player {
    private final int MOVE_AMT = 3;
    private BufferedImage right;
    private boolean facingRight;
    private int xCoord;
    private int yCoord;
    private int targetXCoord;
    private Animation idleAnimation;
    private Animation moveAndAttackAnimation;
    private Animation attackAnimation;
    private Animation currentAnimation;
    private int healthPoints;
    private int damageOutput;
    private int originalSpawnPoint;
    private Player player;
    private int attackRange = 200;
    private boolean isAttacking = false;
    private boolean isReturning = false;
    private boolean isOnCooldown = false;
    private int cooldown = 0;
    private int endlag = 60;
    private int attackTimer = 0;
    private int attackDuration = 30;
    private int speed = 70;

    public GoldenKnight(Player player) {
        this.player = player;
        facingRight = false;
        xCoord = 700; // starting position is (50, 435), right on top of ground
        yCoord = 100;
        originalSpawnPoint = xCoord;
        healthPoints = 1000;
        damageOutput = 35;
        try {
            right = ImageIO.read(new File("src/images/GoldenKnightIdleRight1.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        ArrayList<BufferedImage> images = new ArrayList<>();
        for (int i = 1; i < 8; i++) {
            String filename = "src/images/GoldenKnightIdleRight" + i + ".png";
            try {
                images.add(ImageIO.read(new File(filename)));
            } catch (IOException e) {
                System.out.println(e.getMessage() + " " + filename);
            }
        }
        idleAnimation = new Animation(images, 100);
        currentAnimation = idleAnimation;

        images = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            String filename = "src/images/GoldenKnightAttackRight" + i + ".png";
            try {
                images.add(ImageIO.read(new File(filename)));
            }
            catch (IOException e) {
                System.out.println(e.getMessage() + " " + filename);
            }
        }
        moveAndAttackAnimation = new Animation(images,150);
        currentAnimation = moveAndAttackAnimation;
    }
    public int getxCoord() {
        if (facingRight) {
            return xCoord;
        } else {
            return (xCoord + (getGoldenKnightImage().getWidth()));
        }
    }

    public int getyCoord() {
        return yCoord;
    }

    public int getHeight() {
        return getGoldenKnightImage().getHeight();
    }

    public int getWidth() {
        if (facingRight) {
            return getGoldenKnightImage().getWidth();
        } else {
            return getGoldenKnightImage().getWidth() * -1;
        }
    }

    public void AI(Player player) {
        if (isOnCooldown) {
            cooldown--;
            if (cooldown <= 0) {
                isOnCooldown = false;
                cooldown = 0;
            }
            return;
        }
        if (isAttacking) {
            currentAnimation = moveAndAttackAnimation;
            if (xCoord < targetXCoord) {
                xCoord += speed;
                if (xCoord >= targetXCoord) {
                    xCoord = targetXCoord;
                    isAttacking = false;
                    isReturning = true;
                }
            } else {
                xCoord -= speed;
                if (xCoord <= targetXCoord) {
                    xCoord = targetXCoord;
                    isAttacking = false;
                    isReturning = true;
                }
            }
        } else if (isReturning) {
            currentAnimation = attackAnimation;

            if (xCoord < originalSpawnPoint) {
                xCoord = originalSpawnPoint;
                isReturning = false;
                isAttacking = true;
                cooldown = endlag;
                currentAnimation = idleAnimation;
            } else {
                xCoord -= speed;
                if (xCoord <= originalSpawnPoint) {
                    xCoord = originalSpawnPoint;
                    isReturning = false;
                    isAttacking = true;
                    cooldown = endlag;
                    currentAnimation = idleAnimation;
                }
            }
        } else {
            currentAnimation = idleAnimation;
            int playerLocation = player.getxCoord();
            int distance = Math.abs(playerLocation - xCoord);

            if (distance <= attackRange) {
                currentAnimation = moveAndAttackAnimation;
                if (playerLocation < xCoord) {
                    facingRight = false;
                    isAttacking = true;
                    targetXCoord = playerLocation + 20;
                }
            }
            }
    }

    public BufferedImage getGoldenKnightImage() {
        return currentAnimation.getActiveFrame();
    }
}
