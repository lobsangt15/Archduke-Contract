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
    private Animation phaseOneAttackAnimation;
    private Animation luciferSlashAnimation;
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

    private boolean isAttacking = false;
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
            spikeImage = ImageIO.read(new File("src/images/SpikeProjectile.png"));
        } catch (IOException e) {
            System.out.println("Error loading Golden Knight idle image (GoldenKnightIdleRight1.png): " + e.getMessage());
            idleImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = idleImage.createGraphics();
            g2d.setColor(new Color(255, 215, 0, 150));
            g2d.fillRect(0, 0, 100, 100);
            g2d.dispose();
            System.out.println("Using placeholder image for Golden Knight idle.");

            System.out.println("Error loading SpikeProjectile.png: " + e.getMessage());
            spikeImage = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2dSpike = spikeImage.createGraphics();
            g2dSpike.setColor(Color.GRAY);
            g2dSpike.fillRect(0,0,50,50);
            g2dSpike.dispose();
        }

        xCoord = 1920 - idleImage.getWidth() - 100;
        yCoord = player.getGroundY() - idleImage.getHeight();

        activeSpikes = new ArrayList<>();

        ArrayList<BufferedImage> idleFrames = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            try {
                idleFrames.add(ImageIO.read(new File("src/images/GoldenKnightIdleRight" + i + ".png")));
            } catch (IOException e) {
                System.out.println("Error loading GoldenKnightIdleRight" + i + ".png: " + e.getMessage());
                if (idleFrames.isEmpty()) {
                    idleFrames.add(idleImage);
                }
            }
        }
        if (idleFrames.isEmpty()) idleFrames.add(idleImage);
        idleAnimation = new Animation(idleFrames, 100);

        ArrayList<BufferedImage> deathFrames = new ArrayList<>();
        for (int i = 0; i <= 8; i++) {
            try {
                deathFrames.add(ImageIO.read(new File("src/images/GoldenKnightDeath" + i + ".png")));
            } catch (IOException e) {
                System.out.println("Error loading GoldenKnightDeath" + i + ".png: " + e.getMessage());
                deathFrames.add(idleImage);
            }
        }
        if (deathFrames.isEmpty()) deathFrames.add(idleImage);
        goldenKnightDeathAnimation = new Animation(deathFrames, 100, true);

        ArrayList<BufferedImage> reviveFrames = new ArrayList<>();
        for (int i = 0; i <= 8; i++) {
            try {
                reviveFrames.add(ImageIO.read(new File("src/images/LuciferDeathRevive" + i + ".png")));
            } catch (IOException e) {
                System.out.println("Error loading LuciferDeathRevive" + i + ".png: " + e.getMessage());
                reviveFrames.add(idleImage);
            }
        }
        if (reviveFrames.isEmpty()) reviveFrames.add(idleImage);
        luciferDeathReviveAnimation = new Animation(reviveFrames, 100, true);

        ArrayList<BufferedImage> p1AttackFrames = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            try {
                p1AttackFrames.add(ImageIO.read(new File("src/images/GoldenKnightAttackRight" + i + ".png")));
            } catch (IOException e) {
                System.out.println("Error loading GoldenKnightAttackRight" + i + ".png: " + e.getMessage());
                p1AttackFrames.add(idleImage);
            }
        }
        if (p1AttackFrames.isEmpty()) p1AttackFrames.add(idleImage);
        phaseOneAttackAnimation = new Animation(p1AttackFrames, 70, true);

        ArrayList<BufferedImage> p2AttackFrames = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            try {
                p2AttackFrames.add(ImageIO.read(new File("src/images/LuciferSlash" + i + ".png")));
            } catch (IOException e) {
                System.out.println("Error loading LuciferSlash" + i + ".png: " + e.getMessage());
                p2AttackFrames.add(idleImage);
            }
        }
        if (p2AttackFrames.isEmpty()) p2AttackFrames.add(idleImage);
        luciferSlashAnimation = new Animation(p2AttackFrames, 70, true);

        ArrayList<BufferedImage> luciferIdleFrames = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            try {
                luciferIdleFrames.add(ImageIO.read(new File("src/images/LUCIFERIDLE" + i + ".png")));
            } catch (IOException e) {
                System.out.println("Error loading LUCIFERIDLE" + i + ".png: " + e.getMessage());
                if (luciferIdleFrames.isEmpty()) {
                    luciferIdleFrames.add(idleImage); // Fallback to original idle if no Lucifer idle frames
                }
            }
        }
        if (luciferIdleFrames.isEmpty()) luciferIdleFrames.add(idleImage);
        luciferIdleAnimation = new Animation(luciferIdleFrames, 100);


        currentAnimation = idleAnimation;
    }

    private void facePlayer() {
        if (player.getxCoord() < xCoord) {
            facingRight = false;
        } else {
            facingRight = true;
        }
    }

    public void AI(Player player) {
        facePlayer();

        if (isDead) {
            currentAnimation = goldenKnightDeathAnimation;
            if (goldenKnightDeathAnimation.isDone()) {
                isDead = false;
                isRebirthing = true;
                luciferDeathReviveAnimation.reset();
                luciferDeathReviveAnimation.setCurrentFrame(luciferDeathReviveAnimation.getFrames().size() - 1);
                System.out.println("Golden Knight is dead, starting rebirth.");
            }
            return;
        }

        if (isRebirthing) {
            currentAnimation = luciferDeathReviveAnimation;
            if (luciferDeathReviveAnimation.getCurrentFrame() > 0) {
                luciferDeathReviveAnimation.decrementFrame();
            } else {
                isRebirthing = false;
                isPhaseTwo = true;
                healthPoints = PHASE_TWO_HEALTH;
                System.out.println("Golden Knight rebirthed! Phase Two initiated with " + healthPoints + " HP.");
                currentAnimation = luciferIdleAnimation; // Set to Lucifer idle here
            }
            return;
        }

        if (attackCooldownTimer > 0) {
            attackCooldownTimer--;
        }

        if (!isAttacking && attackCooldownTimer == 0) {
            int distanceToPlayer = Math.abs(player.getxCoord() - xCoord);
            if (distanceToPlayer <= ATTACK_RANGE) {
                isAttacking = true;
                attackCooldownTimer = ATTACK_COOLDOWN;
                if (isPhaseTwo) {
                    currentAnimation = luciferSlashAnimation;
                    luciferSlashAnimation.reset();
                    System.out.println("Golden Knight (Lucifer) uses Lucifer Slash!");
                    if (random.nextBoolean()) {
                        spawnSpikes(BASE_SPIKE_DAMAGE + PHASE_TWO_DAMAGE_BONUS);
                    }
                } else {
                    currentAnimation = phaseOneAttackAnimation;
                    phaseOneAttackAnimation.reset();
                    System.out.println("Golden Knight uses normal attack!");
                    if (random.nextBoolean()) {
                        spawnSpikes(BASE_SPIKE_DAMAGE);
                    }
                }
            } else {
                if (isPhaseTwo) {
                    currentAnimation = luciferIdleAnimation;
                } else {
                    currentAnimation = idleAnimation;
                }
            }
        } else if (isAttacking) {
            if (currentAnimation.isDone()) {
                isAttacking = false;
                if (isPhaseTwo) {
                    currentAnimation = luciferIdleAnimation;
                } else {
                    currentAnimation = idleAnimation;
                }
                System.out.println("Golden Knight attack animation finished.");
            }
        }

        activeSpikes.removeIf(s -> !s.isActive());
    }

    private void spawnSpikes(int spikeDamage) {
        int numSpikes = 3;
        for (int i = 0; i < numSpikes; i++) {
            int spikeX = random.nextInt(1920 - spikeImage.getWidth());
            activeSpikes.add(new Spike(spikeX, player.getGroundY() - spikeImage.getHeight(), spikeImage, spikeDamage));
        }
        System.out.println("Spikes spawned!");
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
