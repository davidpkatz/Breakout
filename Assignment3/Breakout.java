/*
 * File: Breakout.java
 * -------------------
 * Name:
 * Section Leader:
 *
 * This file will eventually implement the game of Breakout.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.awt.*;
import java.awt.event.*;

public class Breakout extends GraphicsProgram {

    /**
     * Width and height of application window in pixels
     */
    public static final int APPLICATION_WIDTH = 404;
    public static final int APPLICATION_HEIGHT = 600;

    /**
     * Dimensions of game board (usually the same)
     */
    private static final int WIDTH = APPLICATION_WIDTH;
    private static final int HEIGHT = APPLICATION_HEIGHT;

    /**
     * Dimensions of the paddle
     */
    private static final int PADDLE_WIDTH = 60;
    private static final int PADDLE_HEIGHT = 10;

    /**
     * Offset of the paddle up from the bottom
     */
    private static final int PADDLE_Y_OFFSET = 30;

    /**
     * Number of bricks per row
     */
    private static final int NBRICKS_PER_ROW = 10;

    /**
     * Number of rows of bricks
     */
    private static final int NBRICK_ROWS = 10;

    /**
     * Separation between bricks
     */
    private static final int BRICK_SEP = 4;

    /**
     * Width of a brick
     */
    private static final int BRICK_WIDTH =
            (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

    /**
     * Height of a brick
     */
    private static final int BRICK_HEIGHT = 8;

    /**
     * Radius of the ball in pixels
     */
    private static final int BALL_RADIUS = 10;
    private static final int BALL_DIAMETER = BALL_RADIUS * 2;

    /**
     * Offset of the top brick row from the top
     */
    private static final int BRICK_Y_OFFSET = 70;

    /**
     * Number of turns
     */
    private static final int NTURNS = 3;
    private static int turns = NTURNS;

    //instance variables
    private double vx, vy;
    private double lastX;
    private double lastY;
    private static final int DELAY = 10;
    private int nBricks = NBRICKS_PER_ROW * NBRICK_ROWS;

/* Method: run() */

    /**
     * Runs the Breakout program.
     */
    public void run() {
		/* You fill this in, along with any subsidiary methods */
        setupGame();
        //for ()
        playGame();
    }

    private void setupGame() {
        removeAll();
        addBricks();
        addPaddle();
        addBall();
    }

    private GRect brick;
    private GOval ball;
    private GRect paddle;
    private GLabel lives;

    public void addBricks() {

        for (int row = 0; row < NBRICKS_PER_ROW; row++) {
            int bricksInRow = NBRICKS_PER_ROW;
            for (int brickNumber = 0; brickNumber < bricksInRow; brickNumber++) {
                int x = BRICK_SEP + ((brickNumber * BRICK_WIDTH) + BRICK_SEP * brickNumber);

                int y = BRICK_Y_OFFSET + (row + 1) * BRICK_HEIGHT + (row + 2) * BRICK_SEP;
                brick = new GRect(x, y, BRICK_WIDTH, BRICK_HEIGHT);
                brick.setFilled(true);
                add(brick);

                if (row < 2) {
                    brick.setColor(Color.RED);
                }
                if (row == 2 || row == 3) {
                    brick.setColor(Color.ORANGE);
                }
                if (row == 4 || row == 5) {
                    brick.setColor(Color.YELLOW);
                }
                if (row == 6 || row == 7) {
                    brick.setColor(Color.GREEN);
                }
                if (row == 8 || row == 9) {
                    brick.setColor(Color.CYAN);
                }
            }
        }
    }

    private void addPaddle() {
        int x = (getWidth() / 2) - (PADDLE_WIDTH / 2);
        int y = getHeight() - PADDLE_Y_OFFSET;
        paddle = new GRect(x, y, PADDLE_WIDTH, PADDLE_HEIGHT);
        paddle.setColor(Color.BLACK);
        paddle.setFilled(true);
        add(paddle);
        addMouseListeners();
    }

    public void addBall() {
        double x = (getWidth() / 2) - BALL_RADIUS;
        double y = (getHeight() / 2) - BALL_RADIUS;
        ball = new GOval(x, y, BALL_RADIUS, BALL_RADIUS);
        ball.setFillColor(Color.BLACK);
        ball.setFilled(true);
        add(ball);
    }
    public GLabel addLives() {
        if (lives != null) {
            remove(lives);
        }
        GLabel lives = new GLabel(turns + " lives remaining.", BRICK_SEP, 20);
        add(lives);
        return lives;
    }

    /**
     * Called on mouse drag to reposition the object
     */
    public void mouseClicked(MouseEvent e) {
        lastX = e.getX();
        lastY = e.getY();
        gobj = getElementAt(lastX, lastY);
    }
    public void mouseMoved(MouseEvent e) {
        if (gobj != null) {
            paddle.move(e.getX() - lastX, 0);
            lastX = e.getX();
            lastY = e.getY();
            if ( paddle.getX() > (WIDTH - PADDLE_WIDTH)){
                paddle.setLocation((double) (WIDTH - PADDLE_WIDTH), (double) (HEIGHT - PADDLE_Y_OFFSET));
            }
            if ( paddle.getX() < 0){
                paddle.setLocation((double) 0, (double) (APPLICATION_HEIGHT - PADDLE_Y_OFFSET));
            }
        }
    }
    private void playGame() {
        addBall();
        if (lives != null) {
            remove(lives);
        }
        addLives();
        println(turns + " lives remaining.");
        if (turns == 0) {
            gameOver();
            }
        //for (int i = 0; i > NTURNS; i++) {
        waitForClick();
        getVelocity();
        while (nBricks != 0) {
            //
            moveBall();
            //if (nBricks == 0) {
            //   youWin();
            //}
            //gameOver();
            //} //if nBricks = 0
        }
        removeAll();
        youWin();
    }
    private void getVelocity() {
        vx = rgen.nextDouble(1.0, 3.0);
        vy = 4.0;
        if (rgen.nextBoolean(0.5)) {
            vx = -vx;
        }
    }

    private void moveBall() {

        ball.move(vx, vy);
        pause(DELAY);

        GObject collider = getCollidingObject();
        if (collider == paddle) {
            if (ball.getY() < 0 || ball.getY() < (APPLICATION_HEIGHT - (2 * BALL_RADIUS))) {
                vy = -vy;
            }
        }
        if (ball.getY() < 0) {
            vy = -vy;
        }
        if (ball.getX() > getWidth() - (2 * BALL_RADIUS) || (ball.getX() < 0)) {
            vx = -vx;
        }
        if (ball.getY() > getHeight() - (2 * BALL_RADIUS)) {
            remove(ball);
            turns--;
            playGame();
        }
        if (collider != null && collider != paddle) {
            vy = -vy;
            remove(collider);
            nBricks--;
            println(nBricks);
        }
    }

    private GObject getCollidingObject() {
        if (getElementAt(ball.getX(), ball.getY()) != null) {
            println("collider1 " + getElementAt(ball.getX(), ball.getY()));
            return getElementAt(ball.getX(), ball.getY());
        }
        if (getElementAt(ball.getX() + BALL_DIAMETER, ball.getY()) != null) {
            println("collider2 " + getElementAt(ball.getX(), ball.getY()));
            return getElementAt(ball.getX() + BALL_DIAMETER, ball.getY());
        }
        if (getElementAt(ball.getX() + BALL_DIAMETER, ball.getY() + BALL_DIAMETER) != null) {
            println("collider3 " + getElementAt(ball.getX(), ball.getY()));
            return getElementAt(ball.getX() + BALL_DIAMETER, ball.getY() + BALL_DIAMETER);
        }
        if (getElementAt(ball.getX(), ball.getY() + BALL_DIAMETER) != null) {
            println("collider4 " + getElementAt(ball.getX(), ball.getY()));
            return getElementAt(ball.getX(), ball.getY() + BALL_DIAMETER);
        }
        return null;
    }

    private void gameOver() {
        removeAll();
        pause(DELAY * 10);
        GLabel gameOver = new GLabel("Game Over", getWidth() /2, getHeight() / 2);
        double x = gameOver.getWidth() / 2;
        gameOver.move(-x, 0);
        add(gameOver);
        waitForClick();
        turns = NTURNS;
        run();
    }

    private void youWin() {
        GLabel youWin = new GLabel("You Win", getWidth() /2, getHeight() / 2);
        double x = youWin.getWidth() / 2;
        youWin.move(-x, 0);
        add(youWin);
        println("You Win");
        waitForClick();
        turns = NTURNS;
        run();
    }

    private RandomGenerator rgen = RandomGenerator.getInstance();
    private GObject gobj;
}
