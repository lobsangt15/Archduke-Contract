import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class NPC {
    private final int MOVE_AMT = 3;
    private BufferedImage right;
    private boolean facingRight;
    private int xCoord;
    private int yCoord;
    private Animation idleAnimation;
    private Animation currentAnimation;

    public NPC() {
        facingRight = true;
        xCoord = 500; // starting position is (50, 435), right on top of ground
        yCoord = 100;

        try {
            right = ImageIO.read(new File("src/images/oldman-idle-1.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        ArrayList<BufferedImage> images = new ArrayList<>();
        for (int i = 1; i < 9; i++) {
            String filename = "src/images/oldman-idle-" + i + ".png";
            try {
                images.add(ImageIO.read(new File(filename)));
            } catch (IOException e) {
                System.out.println(e.getMessage() + " " + filename);
            }
        }
        idleAnimation = new Animation(images, 125);
        currentAnimation = idleAnimation;
    }
    public int getxCoord() {
        if (facingRight) {
            return xCoord;
        } else {
            return (xCoord + (getNPCImage().getWidth()));
        }
    }

    public int getyCoord() {
        return yCoord;
    }

    public int getHeight() {
        return getNPCImage().getHeight();
    }

    public int getWidth() {
        if (facingRight) {
            return getNPCImage().getWidth();
        } else {
            return getNPCImage().getWidth() * -1;
        }
    }

    public BufferedImage getNPCImage() {
        return currentAnimation.getActiveFrame();
    }
}
