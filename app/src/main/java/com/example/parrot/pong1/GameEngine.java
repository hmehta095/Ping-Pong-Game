package com.example.parrot.pong1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.constraint.solver.widgets.Rectangle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;

public class GameEngine extends SurfaceView implements Runnable {

    // -----------------------------------
    // ## ANDROID DEBUG VARIABLES
    // -----------------------------------

    // Android debug variables
    final static String TAG="PONG-GAME";

    // -----------------------------------
    // ## SCREEN & DRAWING SETUP VARIABLES
    // -----------------------------------

    // screen size
    int screenHeight;
    int screenWidth;

//    int racketXPosition;
//    int racketYPosition;

    int score;

    // game state
    boolean gameIsRunning;

    // threading
    Thread gameThread;


    // drawing variables
    SurfaceHolder holder;
    Canvas canvas;
    Paint paintbrush;

    BallSprites ball;
    RacketSprite racket;

    int lives = 3;


    // -----------------------------------
    // ## GAME SPECIFIC VARIABLES
    // -----------------------------------

    // ----------------------------
    // ## SPRITES
    // ----------------------------
//    int ballXPosition;      // keep track of ball -x
//    int ballYPosition;      // keep track of ball -y




    // ----------------------------
    // ## GAME STATS - number of lives, score, etc
    // ----------------------------


    public GameEngine(Context context, int w, int h) {
        super(context);


        this.holder = this.getHolder();
        this.paintbrush = new Paint();

        this.screenWidth = w;
        this.screenHeight = h;


        ball = new BallSprites(this.screenWidth/2,this.screenHeight/2);
        racket = new RacketSprite(350,1400);
        this.printScreenInfo();


        // @TODO: Add your sprites to this section
        // This is optional. Use it to:
        //  - setup or configure your sprites
        //  - set the initial position of your sprites
//        this.ballXPosition = this.screenWidth / 2;
//        this.ballYPosition = this.screenHeight / 2;

//        this.ball.setBallXPosition(this.screenWidth/2);
//        this.ball.setBallYPosition(this.screenHeight/2);

//        this.racket.setRacketXPosition() = 350;
//        this.racket.setRacketYPosition() = 1400;

        // @TODO: Any other game setup stuff goes here


    }

    // ------------------------------
    // HELPER FUNCTIONS
    // ------------------------------

    // This funciton prints the screen height & width to the screen.
    private void printScreenInfo() {

        Log.d(TAG, "Screen (w, h) = " + this.screenWidth + "," + this.screenHeight);
    }


    // ------------------------------
    // GAME STATE FUNCTIONS (run, stop, start)
    // ------------------------------
    @Override
    public void run() {
        while (gameIsRunning == true) {
            this.updatePositions();
            this.redrawSprites();
            this.setFPS();
        }
    }


    public void pauseGame() {
        gameIsRunning = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            // Error
        }
    }

    public void startGame() {
        gameIsRunning = true;
        gameThread = new Thread(this);
        gameThread.start();
    }


    // ------------------------------
    // GAME ENGINE FUNCTIONS
    // - update, draw, setFPS
    // ------------------------------

    String directionBallIsMoving = "up";
//    String directionBallIsMoving = "right";
    String personTapped="";

    // 1. Tell Android the (x,y) positions of your sprites
    public void updatePositions() {
        // @TODO: Update the position of the sprites

        if (directionBallIsMoving == "down") {
            this.ball.setBallYPosition(this.ball.getBallYPosition()+25);

            // if ball hits the floor, then change its direciton
            if (this.ball.getBallYPosition() >= this.screenHeight) {
                //Log.d(TAG, "BALL HIT THE FLOOR / OUT OF BOUNDS");

                // Restart the game
                // Put everything back in their default positions
                // --------------------
                // restart the ball
                this.ball.setBallXPosition(this.screenWidth/2);
                this.ball.setBallYPosition(this.screenHeight/2);

                // restart the racket position
//                this.racketXPosition = 350;
//                this.racketYPosition = 1400;

                this.racket.setRacketXPosition(350);
                this.racket.setRacketYPosition(1400);

                // restart hte direction
                directionBallIsMoving = "down";

                // clear any previous user actions
                personTapped = "";

//                reset the score
                this.score = 0;

                lives = lives -1;


            }
        }
        if (directionBallIsMoving == "up") {
//            this.ballYPosition = this.ballYPosition - 25;
            this.ball.setBallYPosition(this.ball.getBallYPosition()-25);

            // if ball hits ceiling, then change directions
            if (this.ball.getBallYPosition() <= 0 ) {
                // hit upper wall
                //Log.d(TAG,"BALL HIT CEILING / OUT OF BOUNDS ");
                directionBallIsMoving = "down";
            }
        }


        // calculate the racket's new position
        if (personTapped.contentEquals("right")){
//            this.racketXPosition = this.racketXPosition + 10;
            this.racket.setRacketXPosition(this.racket.getRacketXPosition()+10);

            if (this.racket.getRacketXPosition() > this.screenWidth - 400) {
                personTapped = "left";
            }
        }
        else if (personTapped.contentEquals("left")){
//            this.racketXPosition = this.racketXPosition - 10;
            this.racket.setRacketXPosition(this.racket.getRacketXPosition()-10);
            if (this.racket.getRacketXPosition() <= 0 ) {
                personTapped = "right";
            }
        }



        // @TODO: Collision detection code

        // detect when ball hits the racket
        // ---------------------------------

        // 1. if ball hits racket, bounce off racket

        // Check that ball is inside the x and y boundaries
        // of the racket

        // BallY + 50 because we want to make a more precise collision
        // When bottom left corner of ball touches racket, then bounce!
        // (ballY+50) = bottom left
        if (
                (ball.getBallYPosition() + 50) >= (this.racket.getRacketYPosition()) &&
                        ball.getBallXPosition() >= this.racket.getRacketXPosition() &&
                        ball.getBallXPosition() <= this.racket.getRacketXPosition() + 400) {

            // ball is touching racket
            Log.d(TAG, "Ball IS TOUCHING RACKET!");
            directionBallIsMoving = "up";

            // increase the game score!
            this.score = this.score + 2;
        }


        // 2. if ball misses racket, then keep going down

        // 3. if ball falls off bottom of screen, restart the ball in middle
    }
    // 2. Tell Android to DRAW the sprites at their positions
    public void redrawSprites() {
        if (this.holder.getSurface().isValid()) {
            this.canvas = this.holder.lockCanvas();

            //----------------
            // Put all your drawing code in this section

            // configure the drawing tools
            this.canvas.drawColor(Color.RED);
            paintbrush.setColor(Color.WHITE);

            //@TODO: Draw the sprites (rectangle, circle, etc)

            // 1. Draw the ball
            this.canvas.drawRect(
                    ball.getBallXPosition(),
                    ball.getBallYPosition(),
                    ball.getBallXPosition() + 50,
                    ball.getBallYPosition() + 50,
                    paintbrush);
            // this.canvas.drawRect(left, top, right, bottom, paintbrush);

            // 1. Draw the ball
            this.canvas.drawRect(
                    ball.getBallXPosition(),
                    ball.getBallYPosition(),
                    ball.getBallXPosition() + 50,
                    ball.getBallYPosition() + 50,
                    paintbrush);

            paintbrush.setTextSize(55);
            canvas.drawText("Lives Remaining: " + lives,this.screenWidth-500,50,paintbrush);

            if(lives < 1){
                paintbrush.setTextSize(100);
                canvas.drawText("Game Over",this.screenWidth/2 - 200,this.screenHeight/2,paintbrush);
            }


//            draw the racket
            paintbrush.setColor(Color.YELLOW);
            this.canvas.drawRect(this.racket.getRacketXPosition(),this.racket.getRacketYPosition(),
                    this.racket.getRacketXPosition()+400,this.racket.getRacketYPosition()+50,paintbrush);


            //@TODO: Draw game statistics (lives, score, etc)
            paintbrush.setTextSize(60);
            canvas.drawText("Score: " +this.score , 20, 100, paintbrush);

            //----------------
            this.holder.unlockCanvasAndPost(canvas);
        }
    }

    // Sets the frame rate of the game
    public void setFPS() {
        try {
            gameThread.sleep(20);
        }
        catch (Exception e) {

        }
    }

    // ------------------------------
    // USER INPUT FUNCTIONS
    // ------------------------------

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int userAction = event.getActionMasked();
        //@TODO: What should happen when person touches the screen?
        if (userAction == MotionEvent.ACTION_DOWN) {
            // user pushed down on screen

//            Get the position of the tap
            float fingerXPosition = event.getX();
            float fingerYPosition = event.getY();

            Log.d(TAG,"Person Pressed: " + fingerXPosition + fingerYPosition);

            if(fingerXPosition <= this.screenWidth/2){
                //move racket left
                personTapped = "left";
            }
            else if(fingerYPosition > this.screenWidth/2){
                //move racket right
                personTapped = "right";
            }

        }
        else if (userAction == MotionEvent.ACTION_UP) {
            // user lifted their finger
        }
        return true;
    }
}