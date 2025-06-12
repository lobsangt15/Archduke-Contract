import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Animation implements ActionListener {
    private ArrayList<BufferedImage> frames;
    private Timer timer;
    private int currentFrame;
    private boolean done;
    private boolean singleCycle;

    public Animation(ArrayList<BufferedImage> frames, int delay, boolean singleCycle) {
        this.frames = frames;
        this.singleCycle = singleCycle;
        currentFrame = 0;
        done = false;
        timer = new Timer(delay, this);
        timer.start();
    }

    public Animation(ArrayList<BufferedImage> frames, int delay) {
        this(frames, delay, false);
    }

    public BufferedImage getActiveFrame() {
        return frames.get(currentFrame);
    }

    public ArrayList<BufferedImage> getFrames() {
        return frames;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof Timer) {
            currentFrame++;
            if (currentFrame >= frames.size()) {
                if (singleCycle) {
                    currentFrame = frames.size() - 1;
                    timer.stop();
                    done = true;
                } else {
                    currentFrame = 0;
                }
            }
        }
    }

    public boolean isDone() {
        return done;
    }

    public void reset() {
        currentFrame = 0;
        done = false;
        if (singleCycle && !timer.isRunning()) {
            timer.start();
        }
    }
}