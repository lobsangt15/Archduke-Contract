import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class GoldenKnight extends Player {
    private BufferedImage idleImage;
    private boolean facingRight;
    private int xCoord;
    private int yCoord;

    private Animation idleAnimation;
    private Animation goldenKnightDeathAnimation;
    private Animation luciferDeathReviveAnimation;
    Animation phaseOneAttackAnimation;
    Animation luciferSlashAnimation;
    private Animation luciferIdleAnimation; // New: Animation for Lucifer's idle state
    private Animation currentAnimation;

    private int healthPoints;
    private int damageOutput;
    private Player player;

    private boolean isDead = false;
    private boolean isRebirthing = false;
    private boolean isPhaseTwo = false;
    private final int PHASE_TWO_HEALTH = 2000;
    private final int PHASE_TWO_DAMAGE_BONUS = 5;
    private final int BASE_SPIKE_DAMAGE = 10;

    boolean isAttacking = false;
    private int attackCooldownTimer = 0;
    private final int ATTACK_COOLDOWN = 300;
    private final int ATTACK_RANGE = 150;

    private Random random = new Random();
    private BufferedImage spikeImage;
    private ArrayList<Spike> activeSpikes;

    public GoldenKnight(Player player) {
        this.player = player;
        facingRight = false;

        healthPoints = 1000;
        damageOutput = 20;

        try {
            idleImage = ImageIO.read(new File("src/images/GoldenKnightIdleRight1.png"));
        } catch (IOException e) {
            System.out.println("Error loading Golden Knight idle image (GoldenKnightIdleRight1.png): " + e.getMessage());
            idleImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        }

        xCoord = 1920 - idleImage.getWidth() - 100;
        yCoord = 680;

        ArrayList<BufferedImage> idleFrames = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            String filename = "src/images/GoldenKnightIdleRight" + i + ".png";
            try {
                idleFrames.add(ImageIO.read(new File(filename)));
            } catch (IOException e) {
                System.out.println(e.getMessage() + " " + filename);
            }
        }
        idleAnimation = new Animation(idleFrames, 50);

        ArrayList<BufferedImage> deathFrames = new ArrayList<>();
        for (int i = 0; i <= 8; i++) {
            String filename = "src/images/LuciferDeathRevive" + i + ".png";
            try {
                deathFrames.add(ImageIO.read(new File(filename)));
            } catch (IOException e) {
                System.out.println("Error loading GoldenKnightDeath" + i + ".png: " + e.getMessage());
            }
        }
        goldenKnightDeathAnimation = new Animation(deathFrames, 70, true);

        ArrayList<BufferedImage> p1AttackFrames = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            String filename = "src/images/GoldenKnightAttackRight" + i + ".png";
            try {
                p1AttackFrames.add(ImageIO.read(new File(filename)));
            } catch (IOException e) {
                System.out.println("Error loading GoldenKnightAttackRight" + i + ".png: " + e.getMessage());
            }
        }
        phaseOneAttackAnimation = new Animation(p1AttackFrames, 70, true);

        ArrayList<BufferedImage> p2AttackFrames = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            String filename = "src/images/LuciferSlash" + i + ".png";
            try {
                p2AttackFrames.add(ImageIO.read(new File(filename)));
            } catch (IOException e) {
                System.out.println("Error loading LuciferSlash" + i + ".png: " + e.getMessage());
            }
        }
        luciferSlashAnimation = new Animation(p2AttackFrames, 70, true);

        ArrayList<BufferedImage> luciferIdleFrames = new ArrayList<>();
        for (int i = 0; i <= 3; i++) {
            String filename = "src/images/tile00" + i + ".png";
            try {
                luciferIdleFrames.add(ImageIO.read(new File(filename)));
            } catch (IOException e) {
                System.out.println("Error loading LUCIFERIDLE" + i + ".png: " + e.getMessage());
            }
        }
        luciferIdleAnimation = new Animation(luciferIdleFrames, 50);

        if (!isPhaseTwo) {
            currentAnimation = idleAnimation;
        } else {
            currentAnimation = luciferIdleAnimation;
        }
    }

    public void idle() {
        if (!isPhaseTwo) {
            currentAnimation = idleAnimation;
        } else {
            currentAnimation = luciferIdleAnimation;
        }
    }

    public void Attack() {
        if (!isAttacking) {
            if (!isPhaseTwo) {
                currentAnimation = phaseOneAttackAnimation;
            } else {
                currentAnimation = luciferSlashAnimation;
            }
            isAttacking = true;
            phaseOneAttackAnimation.reset();
            luciferSlashAnimation.reset();
        }
    }

    private void facePlayer() {
        if (player.getxCoord() < xCoord) {
            facingRight = false;
        } else {
            facingRight = true;
        }
    }

    public int getxCoord() {
        if (facingRight) {
            return xCoord;
        } else {
            return (xCoord + currentAnimation.getActiveFrame().getWidth());
        }
    }

    public int getyCoord() {
        return yCoord;
    }

    public int getHeight() {
        return currentAnimation.getActiveFrame().getHeight();
    }

    public int getWidth() {
        if (facingRight) {
            return currentAnimation.getActiveFrame().getWidth();
        } else {
            return currentAnimation.getActiveFrame().getWidth() * -1;
        }
    }

    public BufferedImage getGoldenKnightImage() {
        return currentAnimation.getActiveFrame();
    }

    public ArrayList<Spike> getActiveSpikes() {
        return activeSpikes;
    }

    public int getHealth() {
        return healthPoints;
    }

    public void takeDamage(int damage) {
        healthPoints -= damage;
        if (healthPoints < 0) {
            healthPoints = 0;
        }
        System.out.println("Golden Knight took " + damage + " damage! Current Health: " + healthPoints);

        if (healthPoints <= 0 && !isDead && !isRebirthing && !isPhaseTwo) {
            isDead = true;
            goldenKnightDeathAnimation.reset();
            System.out.println("Golden Knight Phase One Defeated! Initiating Death Animation.");
        }
    }

    public int getDamageOutput() {
        if (isPhaseTwo) {
            return damageOutput + PHASE_TWO_DAMAGE_BONUS;
        }
        return damageOutput;
    }

    public Rectangle getBossRect() {
        return new Rectangle(xCoord, yCoord, currentAnimation.getActiveFrame().getWidth(), currentAnimation.getActiveFrame().getHeight());
    }

    public Rectangle getAttackHitbox() {
        if (isAttacking) {
            int hitboxX;
            int hitboxY = yCoord;
            int hitboxWidth = currentAnimation.getActiveFrame().getWidth();
            int hitboxHeight = currentAnimation.getActiveFrame().getHeight();

            if (facingRight) {
                hitboxX = xCoord;
            } else {
                hitboxX = xCoord - hitboxWidth;
            }
            return new Rectangle(hitboxX, hitboxY, Math.abs(hitboxWidth), hitboxHeight);
        }
        return null;
    }

    public boolean isDead() {
        return isDead;
    }

    public boolean isRebirthing() {
        return isRebirthing;
    }

    public boolean isPhaseTwo() {
        return isPhaseTwo;
    }

    @Override
    public boolean isAttacking() {
        return isAttacking;
    }
}
