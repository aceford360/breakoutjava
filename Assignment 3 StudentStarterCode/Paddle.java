import java.awt.Graphics;

public class Paddle extends Sprite {

    private int xVelocity;

    public Paddle() {
        width = Settings.PADDLE_WIDTH;
        height = Settings.PADDLE_HEIGHT;
        resetPosition();
    }

    public void resetPosition() {
        //reset the position of paddle
        x = Settings.INITIAL_PADDLE_X;
        y = Settings.INITIAL_PADDLE_Y;
    }

    public void update() {
        x += xVelocity;

        //prevent the paddle from moving outside of the screen
        if (x < 0) {
            x = 0;
        }
        if (x > Settings.WINDOW_WIDTH - Settings.PADDLE_WIDTH) {
            x = Settings.WINDOW_WIDTH - Settings.PADDLE_WIDTH;
        }
    }

    public void paint(Graphics g) {
        g.fillRect(x, y, width, height);
    }

    public void setXVelocity(int vel) {
        xVelocity = vel;
    }
}
