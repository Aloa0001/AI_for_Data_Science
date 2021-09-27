package com.example.ai_for_data_science;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class GameController {
    int[][] board = new int[6][7];

    @FXML
    private GridPane visualBoard;
    private GridPane lastHighlighted = null;
    private final String boardNormalHex = "#949D9D";
    private final String boardHighlightHex = "#767D7D";


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
        int col = getColFromMouseEvent(e);
        System.out.println("clicked column: " + col);
    }


    @FXML
    protected void onMouseEnter(MouseEvent e) {
        Integer col = getColFromMouseEvent(e);

        if (col != null) {
            lastHighlighted	= (GridPane)visualBoard.getChildren().get(col);
            changeNodeColor(visualBoard.getChildren().get(col), boardHighlightHex);
        }
        else {
            lastHighlighted = null;
        }
    }

    @FXML
    protected void onMouseExit(MouseEvent e) {
        if (lastHighlighted != null)
            changeNodeColor(lastHighlighted, boardNormalHex);
    }

    private Integer getColFromMouseEvent(MouseEvent e) {
        Node target = (Node) e.getTarget();
        Integer col = GridPane.getColumnIndex(target);
        return col;
    }

    private void changeNodeColor(Node node, String hex) {
        node.setStyle("-fx-background-color:" + hex + ";");
    }

    private void changeCellColor(int row, int col, String hex) {
        Circle cell = (Circle)((GridPane)visualBoard.getChildren().get(col)).getChildren().get(row);
        if (hex == "" || hex == "#" || hex == null) {
            hex = "#FFFFFF";
        }
        cell.setFill(Color.web(hex));
    }

}