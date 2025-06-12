import java.awt.*;
import java.awt.image.BufferedImage;

public class Projectile {
    private int x;
    private int y;
    private int speed;
    private boolean movingRight;
    private BufferedImage image;
    private int lifetime;
    private int currentLifetime;

    public static final int DEFAULT_WIDTH = 100;
    public static final int DEFAULT_HEIGHT = 50;

    public Projectile(int x, int y, int speed, boolean movingRight, BufferedImage image) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.movingRight = movingRight;
        this.image = image;
        this.lifetime = 120; // 2 seconds at 60 FPS
        this.currentLifetime = 0;
    }

    public void move() {
        if (movingRight) {
            x += speed;
        } else {
            x -= speed;
        }
        currentLifetime++;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public BufferedImage getImage() {
        return image;
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, image.getWidth(), image.getHeight());
    }

    public boolean isOffscreen() {
        return currentLifetime >= lifetime || x < -image.getWidth() || x > 1920;
    }
}
