import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;
import javax.swing.Timer;

public class BreakoutPanel extends JPanel implements ActionListener, KeyListener {

    static final long serialVersionUID = 2L;

    private boolean gameRunning = true;
    private int livesLeft = 3;
    private String screenMessage = "";
    private Ball ball;
    private Paddle paddle;
    private Brick bricks[];

    public BreakoutPanel(Breakout game) {

        addKeyListener(this);
        setFocusable(true);

        Timer timer = new Timer(5, this);
        timer.start();

        //create a new ball object
        ball = new Ball();
        //create a new paddle object
        paddle = new Paddle();
        //create a new bricks array
        bricks = new Brick[Settings.TOTAL_BRICKS];
        createBricks();
    }

    private void createBricks() {
        int counter = 0;
        int x_space = 0;
        int y_space = 0;
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 5; y++) {
                bricks[counter] = new Brick((x * Settings.BRICK_WIDTH) + Settings.BRICK_HORI_PADDING + x_space, (y * Settings.BRICK_HEIGHT) + Settings.BRICK_VERT_PADDING + y_space);
                counter++;
                y_space++;
            }
            x_space++;
            y_space = 0;
        }
    }

    private void paintBricks(Graphics g) {
        for (Brick brick : bricks) {
            brick.paint(g);
        }
    }

    private void update() {
        if (gameRunning) {
            //update the ball and paddle
            ball.update();
            paddle.update();
            collisions();
            repaint();
        }
    }

    //screen message to game over message
    private void gameOver() {
        screenMessage = "Game Over. Press Enter to Play Again.";
        stopGame();
    }

    //screen message to game won message
    private void gameWon() {
        screenMessage = "You Won! Press Enter to Play Again.";
        stopGame();
    }


    // stop the game
    private void stopGame() {
        gameRunning = false;
    }

    private void collisions() {
        //check for lose
        if (ball.y > 450) {
            //if no lives, game over
            livesLeft--;
            if (livesLeft <= 0) {
                gameOver();
                return;
            } else {
                ball.resetPosition();
                ball.setYVelocity(-1);
            }
        }
    
        //check if bricks all knocked
        boolean bricksLeft = false;
        for (int i = 0; i < bricks.length; i++) {
            if (!bricks[i].isBroken() && ball.getRectangle().intersects(bricks[i].getRectangle())) {
                Rectangle brickRect = bricks[i].getRectangle();
                Rectangle ballRect = ball.getRectangle();
    
                int hitDirection = 0;
    
                //determine the hit direction
                if (ballRect.intersectsLine(brickRect.getMinX(), brickRect.getMinY(), brickRect.getMaxX(), brickRect.getMinY())) {
                    hitDirection = 1;
                } else if (ballRect.intersectsLine(brickRect.getMinX(), brickRect.getMaxY(), brickRect.getMaxX(), brickRect.getMaxY())) {
                    hitDirection = 2;
                } else if (ballRect.intersectsLine(brickRect.getMinX(), brickRect.getMinY(), brickRect.getMinX(), brickRect.getMaxY())) {
                    hitDirection = 3;
                } else if (ballRect.intersectsLine(brickRect.getMaxX(), brickRect.getMinY(), brickRect.getMaxX(), brickRect.getMaxY())) {
                    hitDirection = 4;
                }
    
                //update ball velocity based on the hit direction
                switch (hitDirection) {
                    case 1:
                    case 2:
                        ball.setYVelocity(-ball.getYVelocity());
                        break;
                    case 3:
                    case 4:
                        ball.setXVelocity(-ball.getXVelocity());
                        break;
                }
    
                bricks[i].setBroken(true); //mark the brick as broken
            }
        }
    
        //check for paddle collision
        if (ball.getRectangle().intersects(paddle.getRectangle())) {
            Rectangle paddleRect = paddle.getRectangle();
            Rectangle ballRect = ball.getRectangle();
    
            //calculate the side of the paddle that the ball hits
            int hitDirection = 0;
    
            //determine the hit direction
            if (ballRect.intersectsLine(paddleRect.getMinX(), paddleRect.getMinY(), paddleRect.getMaxX(), paddleRect.getMinY())) {
                hitDirection = 1;
            } else if (ballRect.intersectsLine(paddleRect.getMinX(), paddleRect.getMaxY(), paddleRect.getMaxX(), paddleRect.getMaxY())) {
                hitDirection = 2;
            } else if (ballRect.intersectsLine(paddleRect.getMinX(), paddleRect.getMinY(), paddleRect.getMinX(), paddleRect.getMaxY())) {
                hitDirection = 3;
            } else if (ballRect.intersectsLine(paddleRect.getMaxX(), paddleRect.getMinY(), paddleRect.getMaxX(), paddleRect.getMaxY())) {
                hitDirection = 4;
            }
    
            //update ball velocity based on the hit direction
            switch (hitDirection) {
                case 1:
                case 2:
                    ball.setYVelocity(-ball.getYVelocity());
                    break;
                case 3:
                case 4:
                    ball.setXVelocity(-ball.getXVelocity());
                    break;
            }
        }
    
        //check if there are any bricks left
        for (Brick brick : bricks) {
            if (!brick.isBroken()) {
                bricksLeft = true;
                break;
            }
        }
    
        if (!bricksLeft) {
            gameWon();
        }
    }

    //reset the game
    private void resetGame() {
        ball.resetPosition();
        paddle.resetPosition();
        createBricks();
        livesLeft = 3;
        screenMessage = "";
        gameRunning = true;
    }
    

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        ball.paint(g);
        paddle.paint(g);
        paintBricks(g);

        //lives left in the top left-hand corner
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("Lives Left: " + livesLeft, 10, 20);

        //screen message
        if (screenMessage != null) {
            int messageWidth = g.getFontMetrics().stringWidth(screenMessage);
            g.drawString(screenMessage, (Settings.WINDOW_WIDTH / 2) - (messageWidth / 2), Settings.MESSAGE_POSITION);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_LEFT) {
            paddle.setXVelocity(-3);
        } else if (keyCode == KeyEvent.VK_RIGHT) {
            paddle.setXVelocity(3);
        //reset the game when Enter key is pressed and the game is not running
        } else if (keyCode == KeyEvent.VK_ENTER && !gameRunning) {
            resetGame(); 
        }
    }


    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_RIGHT) {
            paddle.setXVelocity(0);
        }
    }

    @Override
    public void keyTyped(KeyEvent arg0) {

    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        update();
    }
}
