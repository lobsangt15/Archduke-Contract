import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Player {
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
    private int fallSpeed;
    private int gravity;
    private int jumpStrength;

    public Player() {
        facingRight = true;
        xCoord = 300; // starting position is (50, 435), right on top of ground
        yCoord = 100;
        healthPoints = 100;
        damageOutput = 15;
        currentAnimation = idleAnimation;
        try {
            right = ImageIO.read(new File("src/images/IdleRight1.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        //The code below is used to create an ArrayList of BufferedImages to use for an Animation object
        //By creating all the BufferedImages beforehand, we don't have to worry about lagging trying to read image files during gameplay
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
        idleAnimation = new Animation(images,50);

        // up animation





        // down animation



        // left




        // right
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
        movingAnimation = new Animation(images,50);





        currentAnimation = idleAnimation;


    }

    //This function is changed from the previous version to let the player turn left and right
    //This version of the function, when combined with getWidth() and getHeight()
    //Allow the player to turn without needing separate images for left and right
    public int getxCoord() {
        if (facingRight) {
            return xCoord;
        } else {
            return (xCoord + (getPlayerImage().getWidth()));
        }
    }

    public int getyCoord() {
        return yCoord;
    }
    //These functions are newly added to let the player turn left and right
    //These functions when combined with the updated getxCoord()
    //Allow the player to turn without needing separate images for left and right
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

    public void idle() {
        currentAnimation = idleAnimation;
    }

    public void faceRight() {
        facingRight = true;
    }

    public void faceLeft() {
        facingRight = false;
    }

    // newly added, used for right-clicking to turn
    public void turn() {
        if (facingRight) {
            faceLeft();
        } else {
            faceRight();
        }
    }

    public void moveRight() {
        if (xCoord + MOVE_AMT <= 920) {
            currentAnimation = movingAnimation;
            xCoord += MOVE_AMT;
        }
    }

    public void moveLeft() {
        if (xCoord - MOVE_AMT >= 0) {
            currentAnimation = movingAnimation;
            xCoord -= MOVE_AMT;
        }
    }

    public void moveUp() {
        if (yCoord - MOVE_AMT >= 0) {
            ArrayList<BufferedImage> images = new ArrayList<>();
            for (int i = 1; i < 4; i++) {
                String filename = "src/images/JumpRight" + i + ".png";
                try {
                    images.add(ImageIO.read(new File(filename)));
                }
                catch (IOException e) {
                    System.out.println(e.getMessage() + " " + filename);
                }
            }
            currentAnimation = new Animation(images,50);
            yCoord -= MOVE_AMT;
        }
    }

    public void moveDown() {
        if (yCoord + MOVE_AMT <= 435) {
            yCoord += MOVE_AMT;
        }
    }

    public BufferedImage getPlayerImage() {
        return currentAnimation.getActiveFrame();  // updated
    }

    // we use a "bounding Rectangle" for detecting collision
    public Rectangle playerRect() {
        int imageHeight = getPlayerImage().getHeight();
        int imageWidth = getPlayerImage().getWidth();
        Rectangle rect = new Rectangle(xCoord, yCoord, imageWidth, imageHeight);
        return rect;
    }
}