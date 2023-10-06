import java.awt.Graphics;

public class Ball extends Sprite {

    private int xVelocity = 1, yVelocity = -1;

    
    public Ball() {
        width = Settings.BALL_WIDTH;
        height = Settings.BALL_HEIGHT;
        //reset position
        resetPosition();
    }

    //reset ball to initial position
    public void resetPosition() {
        setX(Settings.INITIAL_BALL_X);
        setY(Settings.INITIAL_BALL_Y);
    }

    //update
    public void update() {
        x += xVelocity;
        y += yVelocity;

        //allow ball to bounce off different directions
        if (x <= 0) {
            x = 0;
            xVelocity = 1;
        }
        if (x >= Settings.WINDOW_WIDTH - Settings.BALL_WIDTH) {
            x = Settings.WINDOW_WIDTH - Settings.BALL_WIDTH;
            xVelocity = -1;
        }

        if (y <= 0) {
            y = 0;
            yVelocity = 1;
        }
    }
    public void setXVelocity(int x) {
        xVelocity = x;
    }
    public void setYVelocity(int y) {
        yVelocity = y;
    }
    public int getXVelocity() {
        return xVelocity;
    }
    public int getYVelocity() {
        return yVelocity;
    }
    public void paint(Graphics g) {
        g.fillOval(x, y, width, height);
    }
}
