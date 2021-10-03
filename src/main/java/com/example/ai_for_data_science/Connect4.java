package com.example.ai_for_data_science;

import com.example.ai_for_data_science.players.Player;
import com.example.ai_for_data_science.players.Type;
import javafx.animation.TranslateTransition;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.layout.Border;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static javafx.scene.shape.Shape.subtract;

public class Connect4 {

    private static final int BOARD_UNIT_SIZE = 60;
    private static final int BOARD_COLUMNS = 7;
    private static final int BOARD_ROWS = 6;
    private final int EXTRA_SPACE = 5;
    private static final int RADIUS = BOARD_UNIT_SIZE >> 1;
    private Player p1, p2;

    private boolean firstPlayer = true;
    private final GameBoard[][] gameBoard = new GameBoard[BOARD_COLUMNS][BOARD_ROWS];
    private final Pane gameBoardRoot = new Pane();
    private Stage stage;

    /**
     * display the game board
     */
    public void play(String firstPlayer, String secondPlayer) {
        this.p1 = new Player(firstPlayer);
        this.p2 = new Player(secondPlayer);

        if (p1.getType() == Type.HUMAN && p2.getType() == Type.HUMAN){
            p1.setName("First Player");
            p2.setName("Second Player");
        }

        Stage stage = new Stage();

        stage.setTitle("Connect4");
        Pane scene = new Pane();
        scene.setBorder(Border.EMPTY);
        scene.getChildren().add(gameBoardRoot);

        Shape gridBoard = makeGridBoard();
        scene.getChildren().add(gridBoard);
        scene.getChildren().addAll(makeColumns());
        stage.setScene(new Scene(scene));
        stage.show();
        this.stage = stage;

    }

    /**
     * create the game board
     * @return the grid shape with the circles
     */
    private Shape makeGridBoard() {
        Shape shape = new Rectangle(BOARD_COLUMNS  * BOARD_UNIT_SIZE + BOARD_UNIT_SIZE, BOARD_ROWS  * BOARD_UNIT_SIZE + BOARD_UNIT_SIZE);

        // build each element of the grid
        for (int x = 0; x < BOARD_ROWS; x++) {
            for (int y = 0; y < BOARD_COLUMNS; y++) {

                // for each element (row, col) create a circle
                Circle circle = getCircle(x, y);

                // subtract the area of the  circle from the shape
                shape = subtract(shape, circle);
            }
        }

        shape.setFill(Color.DARKGRAY);

        return shape;
    }

    /**
     * create a circle
     * @param row location
     * @param col location
     * @return the circle
     */
    private Circle getCircle(int row, int col) {
        Circle circle = new Circle();
        circle.setRadius(Connect4.RADIUS);

        // the horizontal/vertical position of the center of the circle in pixels and translate to the next
        circle.setCenterX(Connect4.RADIUS);
        circle.setCenterY(Connect4.RADIUS);

        // set the circle on the grid
        circle.setTranslateX(col * (BOARD_UNIT_SIZE + EXTRA_SPACE) + (Connect4.RADIUS >> 1));
        circle.setTranslateY(row * (BOARD_UNIT_SIZE + EXTRA_SPACE) + (Connect4.RADIUS >> 1));

        return circle;
    }

    private List<Rectangle> makeColumns() {
        List<Rectangle> columns = new ArrayList<>();

        for (int col = 0; col < BOARD_COLUMNS; col++) {
            Rectangle colUnit = new Rectangle(BOARD_UNIT_SIZE, (BOARD_ROWS + 1) * BOARD_UNIT_SIZE);
            colUnit.setTranslateX(col * (BOARD_UNIT_SIZE + EXTRA_SPACE) + (BOARD_UNIT_SIZE >> 2));
            colUnit.setFill(Color.TRANSPARENT);

            // add mouse/click events
            colUnit.setOnMouseEntered(e -> colUnit.setFill(Color.rgb(122, 122, 109, 0.2)));
            colUnit.setOnMouseExited(e -> colUnit.setFill(Color.TRANSPARENT));

            clickEvent(col, colUnit);

            columns.add(colUnit);
        }

        return columns;
    }

    /**
     * setting action for click event
     * @param column the column
     * @param colUnit the clicked unit
     */
    private void clickEvent(int column, Rectangle colUnit) {
        colUnit.setOnMouseClicked(e -> playerMove(new GameBoard(firstPlayer), column));
    }

    /**
     *
     * @param gameBoard game
     * @param column disc insertion
     */
    private void playerMove(GameBoard gameBoard, int column) {
        if (p1.getType() == Type.HUMAN){

        }else{

        }
        int rowIndex = BOARD_ROWS - 1;
        do {
            if (getGameBoard(column, rowIndex).isEmpty()) {
                break;
            }

            rowIndex--;
        } while (rowIndex >= 0);

        if (rowIndex < 0) {
            System.out.println("selected column is full");
            //
            return;
        }

        this.gameBoard[column][rowIndex] = gameBoard;
        gameBoardRoot.getChildren().add(gameBoard);
        gameBoard.setTranslateX(column * (BOARD_UNIT_SIZE + EXTRA_SPACE) + (BOARD_UNIT_SIZE >> 2));

        final int currentRow = rowIndex;

        TranslateTransition animation = new TranslateTransition(Duration.seconds(0.1), gameBoard);
        animation.setToY(rowIndex * (BOARD_UNIT_SIZE + EXTRA_SPACE) + (BOARD_UNIT_SIZE >> 2));
        animation.setOnFinished(e -> {
            if (checkWinner(column, currentRow)) {
                gameOver();
            }

            firstPlayer = !firstPlayer;
        });
        animation.play();
    }

    private boolean checkWinner(int column, int row) {
        var colAlignment = IntStream.rangeClosed(column - 3, column + 3)
                .mapToObj(col -> new Point2D(col, row))
                .collect(Collectors.toList());

        var rowAlignment = IntStream.rangeClosed(row - 3, row + 3)
                .mapToObj(r -> new Point2D(column, r))
                .collect(Collectors.toList());

        var firstDiagonal = new Point2D(column - 3, row - 3);
        var firstDiag = IntStream.rangeClosed(0, 6)
                .mapToObj(i -> firstDiagonal.add(i, i))
                .collect(Collectors.toList());

        var secondDiagonal = new Point2D(column - 3, row + 3);
        var secondDiag = IntStream.rangeClosed(0, 6)
                .mapToObj(i -> secondDiagonal.add(i, -i))
                .collect(Collectors.toList());

        return !(!checkWinningCondition(rowAlignment) && !checkWinningCondition(colAlignment) && !checkWinningCondition(firstDiag) && !checkWinningCondition(secondDiag));
    }

    private boolean checkWinningCondition(List<Point2D> points) {
        int collection = 0;

        for (Point2D point2D : points) {
            int column = (int) point2D.getX();
            int row = (int) point2D.getY();

            var gameBoard = getGameBoard(column, row).orElse(new GameBoard(!firstPlayer));
            if (gameBoard.firstPlayer == firstPlayer) {
                collection++;
                if (collection == 4) {
                    return true;
                }
            } else {
                collection = 0;
            }
        }

        return false;
    }

    private void gameOver() {

        setStatistics();
        this.stage.close();

        System.out.println("Winner: " + (firstPlayer ? p1.getName() : p2.getName()));
        new Dashboard().run();
    }

    private void setStatistics() {

        // TODO Add the game data to each player's data

    }

    private Optional<GameBoard> getGameBoard(int column, int row) {
        if (column < 0 || column >= BOARD_COLUMNS
                || row < 0 || row >= BOARD_ROWS)
            return Optional.empty();

        return Optional.ofNullable(gameBoard[column][row]);
    }

    private static class GameBoard extends Circle {
        private final boolean firstPlayer;

        public GameBoard(boolean fistPlayer) {
            super(RADIUS, fistPlayer ? Color.RED : Color.YELLOW);
            this.firstPlayer = fistPlayer;

            setCenterX(RADIUS);
            setCenterY(RADIUS);
        }
    }
}
