package com.example.parrot.pong1;

public class BallSprites {


    public int ballXPosition;
    public int ballYPosition;

    public BallSprites(int ballXPosition, int ballYPosition) {
        this.ballXPosition = ballXPosition;
        this.ballYPosition = ballYPosition;
    }


    public int getBallXPosition() {
        return ballXPosition;
    }

    public void setBallXPosition(int ballXPosition) {
        this.ballXPosition = ballXPosition;
    }

    public int getBallYPosition() {
        return ballYPosition;
    }

    public void setBallYPosition(int ballYPosition) {
        this.ballYPosition = ballYPosition;
    }
}
