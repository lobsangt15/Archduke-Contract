// Player.java
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Player {
    private final int MOVE_AMT = 6;
    private BufferedImage right;
    private boolean facingRight;
    private int xCoord;
    private int yCoord;
    private Animation idleAnimation;
    private Animation movingAnimation;
    private Animation jumpingAnimation;
    private Animation jumpingAftermathAnimation;
    private Animation rollingAnimation;
    Animation attackAnimation;
    private Animation currentAnimation;
    private int healthPoints;
    private int damageOutput;
    private boolean isJumping = false;
    boolean isAttacking = false;
    private boolean isFalling = false;
    private boolean isMoving = false;
    private boolean canDoubleJump = false;
    private boolean doubleJumpUsed = false;
    private int doubleJumpDelay = 15;
    private int doubleJumpTimer = 0;
    private int jumpVelocity = -18;
    private int doubleJumpVelocity = -18;
    private int currentVelocity = 0;
    private int gravityForce = 1;
    private final int groundY = 1150;
    private final int PLAYER_ATTACK_RANGE = 70;

    private boolean isRolling = false;

    public Player() {
        facingRight = true;
        xCoord = 300;
        yCoord = 600;

        try {
            right = ImageIO.read(new File("src/images/IdleRight1.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            right = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        }

        healthPoints = 100;
        damageOutput = 10;

        ArrayList<BufferedImage> images = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            String filename = "src/images/IdleRight" + i + ".png";
            try {
                images.add(ImageIO.read(new File(filename)));
            }
            catch (IOException e) {
                System.out.println(e.getMessage() + " " + filename);
            }
        }
        idleAnimation = new Animation(images,100);

        images = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            String filename = "src/images/JumpRight" + i + ".png";
            try {
                images.add(ImageIO.read(new File(filename)));
            }
            catch (IOException e) {
                System.out.println(e.getMessage() + " " + filename);
            }
        }
        jumpingAnimation = new Animation(images,50);

        images = new ArrayList<>();
        for (int i = 1; i < 3; i++) {
            String filename = "src/images/JumpAftermathRight" + i + ".png";
            try {
                images.add(ImageIO.read(new File(filename)));
            }
            catch (IOException e) {
                System.out.println(e.getMessage() + " " + filename);
            }
        }
        jumpingAftermathAnimation = new Animation(images,125, true);

        images = new ArrayList<>();
        for (int i = 1; i < 13; i++) {
            String filename = "src/images/RollRight" + i + ".png";
            try {
                images.add(ImageIO.read(new File(filename)));
            }
            catch (IOException e) {
                System.out.println(e.getMessage() + " " + filename);
            }
        }
        rollingAnimation = new Animation(images,75, true);

        images = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            String filename = "src/images/RunRight" + i + ".png";
            try {
                images.add(ImageIO.read(new File(filename)));
            }
            catch (IOException e) {
                System.out.println(e.getMessage() + " " + filename);
            }
        }
        movingAnimation = new Animation(images,35);

        images = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            String filename = "src/images/AttackRight" + i + ".png";
            try {
                images.add(ImageIO.read(new File(filename)));
            }
            catch (IOException e) {
                System.out.println(e.getMessage() + " " + filename);
            }
        }
        attackAnimation = new Animation(images,50, true);

        currentAnimation = idleAnimation;
    }

    public boolean isRolling() {
        return isRolling;
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public boolean isJumping() {
        return isJumping;
    }

    public boolean isFalling() {
        return isFalling;
    }

    public int getGroundY() {
        return groundY;
    }

    public void setxCoord(int x) {
        this.xCoord = x;
    }

    public void setyCoord(int y) {
        this.yCoord = y;
    }

    public int getxCoord() {
        if (facingRight) {
            return xCoord;
        } else {
            return (xCoord + (getPlayerImage().getWidth()));
        }
    }

    public boolean getDirection () {
        return facingRight;
    }

    public int getyCoord() {
        return yCoord;
    }

    public int getHeight() {
        return getPlayerImage().getHeight();
    }

    public int getWidth() {
        if (facingRight) {
            return getPlayerImage().getWidth();
        } else {
            return getPlayerImage().getWidth() * -1;
        }
    }

    public Rectangle getPlayerAttackHitbox() {
        if (isAttacking) {
            int hitboxX;
            int hitboxY = yCoord;
            int hitboxWidth = PLAYER_ATTACK_RANGE;
            int hitboxHeight = getHeight();

            if (facingRight) {
                hitboxX = xCoord + getPlayerImage().getWidth() - (PLAYER_ATTACK_RANGE / 2);
            } else {
                hitboxX = xCoord - PLAYER_ATTACK_RANGE / 2;
            }
            return new Rectangle(hitboxX, hitboxY, Math.abs(hitboxWidth), hitboxHeight);
        }
        return null;
    }


    public void idle() {
        if (!isJumping && !isFalling && !isRolling && !isAttacking) {
            currentAnimation = idleAnimation;
            isMoving = false;
        }
    }

    public void faceRight() {
        facingRight = true;
    }

    public void faceLeft() {
        facingRight = false;
    }

    public void turn() {
        if (facingRight) {
            faceLeft();
        } else {
            faceRight();
        }
    }

    public void moveLeft() {
        xCoord -= MOVE_AMT;
        if (!isJumping && !isFalling && !isRolling && !isAttacking) {
            currentAnimation = movingAnimation;
        }
    }

    public void moveRight() {
        xCoord += MOVE_AMT;
        if (!isJumping && !isFalling && !isRolling && !isAttacking) {
            currentAnimation = movingAnimation;
        }
    }

    public void jump() {
        if (!isRolling && !isAttacking) {
            if (!isJumping && !isFalling) {
                currentVelocity = jumpVelocity;
                isJumping = true;
                doubleJumpUsed = false;
                doubleJumpTimer = 0;
                currentAnimation = jumpingAnimation;
            } else if (canDoubleJump && !doubleJumpUsed) {
                currentVelocity = doubleJumpVelocity;
                isJumping = true;
                isFalling = false;
                doubleJumpUsed = true;
                currentAnimation = jumpingAnimation;
            }
        }
    }

    public void moveDown() {
        if (!isJumping && !isFalling && yCoord + MOVE_AMT <= groundY) {
            yCoord += MOVE_AMT;
            currentAnimation = jumpingAftermathAnimation;
        }
    }

    public void startRoll() {
        if (!isRolling && !isJumping && !isFalling && !isAttacking) {
            isRolling = true;
            rollingAnimation.reset();
            currentAnimation = rollingAnimation;
        }
    }

    public void updateRoll() {
        if (isRolling) {
            if (facingRight) {
                xCoord += MOVE_AMT * 2;
            } else {
                xCoord -= MOVE_AMT * 2;
            }

            if (rollingAnimation.isDone()) {
                isRolling = false;
                idle();
            }
            if (xCoord < 0) xCoord = 0;
            if (xCoord > 1920 - getWidth()) xCoord = 1920 - getWidth();
        }
    }

    public void AttackRight() {
        if (!isAttacking && !isRolling && !isJumping && !isFalling) {
            currentAnimation = attackAnimation;
            isAttacking = true;
            attackAnimation.reset();
        }
    }

    public void AttackLeft() {
        if (!isAttacking && !isRolling && !isJumping && !isFalling) {
            currentAnimation = attackAnimation;
            isAttacking = true;
            attackAnimation.reset();
        }
    }

    public void applyGravity() {
        if (isJumping || isFalling) {
            yCoord += currentVelocity;
            currentVelocity += gravityForce;

            if (currentVelocity > 0 && isJumping) {
                isJumping = false;
                isFalling = true;
                if (!isAttacking && !isRolling && !isMoving) {
                    currentAnimation = jumpingAftermathAnimation;
                    jumpingAftermathAnimation.reset();
                }
            }
            if (!canDoubleJump && !doubleJumpUsed) {
                doubleJumpTimer++;
                if (doubleJumpTimer >= doubleJumpDelay) {
                    canDoubleJump = true;
                }
            }
            if (yCoord >= 600) {
                yCoord = 600;
                isJumping = false;
                isFalling = false;
                doubleJumpUsed = false;
                canDoubleJump = false;
                doubleJumpTimer = 0;
                currentVelocity = 0;
                if (!isRolling && !isAttacking) {
                    currentAnimation = idleAnimation;
                }
            }
        }
    }

    public BufferedImage getPlayerImage() {
        return currentAnimation.getActiveFrame();
    }

    public int getHealth() {
        return healthPoints;
    }

    public int getDamageOutput() {
        return damageOutput;
    }

    public void takeDamage(int damage) {
        this.healthPoints -= damage;
        if (this.healthPoints < 0) {
            this.healthPoints = 0;
        }
        System.out.println("Player took " + damage + " damage. Current health: " + this.healthPoints);
    }

    public Rectangle playerRect() {
        int imageHeight = getPlayerImage().getHeight();
        int imageWidth = getPlayerImage().getWidth();
        Rectangle rect = new Rectangle(xCoord, yCoord, imageWidth, imageHeight);
        return rect;
    }
}