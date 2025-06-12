// Spike.java
import java.awt.*;
import java.awt.image.BufferedImage;

public class Spike {
    private int x;
    private int y;
    private BufferedImage image;
    private int lifetime;
    private int currentLifetime;
    private boolean active;
    private int damage;

    public Spike(int x, int y, BufferedImage image, int damage) {
        this.x = x;
        this.y = y;
        this.image = image;
        this.lifetime = 240;
        this.currentLifetime = 0;
        this.active = true;
        this.damage = damage;
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

    public void update() {
        currentLifetime++;
        if (currentLifetime >= lifetime) {
            active = false;
        }
    }

    public boolean isActive() {
        return active;
    }

    public int getDamage() {
        return damage;
    }

    public void deactivate() {
        this.active = false;
    }
}