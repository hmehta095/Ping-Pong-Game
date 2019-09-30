package com.example.parrot.pong1;

public class RacketSprite {
    public int RacketXPosition;
    public int RacketYPosition;

    public RacketSprite(int racketXPosition, int racketYPosition) {
        RacketXPosition = racketXPosition;
        RacketYPosition = racketYPosition;
    }

    public int getRacketXPosition() {
        return RacketXPosition;
    }

    public void setRacketXPosition(int racketXPosition) {
        RacketXPosition = racketXPosition;
    }

    public int getRacketYPosition() {
        return RacketYPosition;
    }

    public void setRacketYPosition(int racketYPosition) {
        RacketYPosition = racketYPosition;
    }
}
