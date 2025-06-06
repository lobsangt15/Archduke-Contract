import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GoldenKnight {
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

    public GoldenKnight() {
        facingRight = true;
        xCoord = 300; // starting position is (50, 435), right on top of ground
        yCoord = 100;
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

    public BufferedImage getGoldenKnightImage() {
        return currentAnimation.getActiveFrame();
    }
}
