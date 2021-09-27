package com.example.ai_for_data_science;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class GameController {

    int[][] board = new int[6][7];

    //  0, 0, 0, 0, 0, 0, 0
    //  0, 0, 0, 0, 0, 0, 0
    //  0, 0, 0, 0, 0, 0, 0
    //  0, 0, 0, 0, 0, 0, 0
    //  0, 0, 0, 0, 0, 0, 0
    //  0, 0, 0, 0, 0, 0, 0

    //  00, 01, 02, 03, 04, 05, 06
    //  10, 11, 12, 13, 14, 15, 16
    //  20, 21, 22, 23, 24, 25, 26
    //  30, 31, 32, 33, 34, 35, 36
    //  40, 41, 42, 43, 44, 45, 46
    //  50, 51, 52, 53, 54, 55, 56


    private boolean placeDisc(int col, int player) {
        for (int row = 5; row >= 0; row--) {
            if (board[row][col] == 0) {
                board[row][col] = player;     // places 1 or 2 based on which player's turn it is
                return true;
            }
        }
        return false;
    }



    @FXML
    protected void onMouseClicked(MouseEvent e) {
        Node node = (Node)e.getTarget();
        System.out.println("clicked column: " + GridPane.getColumnIndex(node));
    }

    @FXML
    protected void onMouseEnter() {
    }

    @FXML
    protected void onMouseExit() {
    }

}