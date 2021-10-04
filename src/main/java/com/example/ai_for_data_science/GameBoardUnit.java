package com.example.ai_for_data_science;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class GameBoardUnit extends Circle {
    private final boolean firstPlayer;

    public GameBoardUnit(boolean fistPlayer, int radius) {
        super(radius, fistPlayer ? Color.LIGHTSALMON : Color.LIGHTBLUE);
        this.firstPlayer = fistPlayer;

        setCenterX(radius);
        setCenterY(radius);
    }

    public boolean isFirstPlayer() {
        return firstPlayer;
    }
}
