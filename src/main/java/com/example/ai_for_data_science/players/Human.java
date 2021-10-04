package com.example.ai_for_data_science.players;

public class Human extends Player {
    private int score;

    public Human(String name) {
        super(name);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
