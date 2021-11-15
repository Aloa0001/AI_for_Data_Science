package com.example.ai_for_data_science;


import com.example.ai_for_data_science.players.algorithms.Human;
import com.example.ai_for_data_science.players.algorithms.Minimax;
import com.example.ai_for_data_science.players.algorithms.RandomMove;

public class Main {

    public static void main(String[] args) {
        run();
    }

    private static void run() {

        Algorithm player1 = new Minimax(true);
        Algorithm player2 = new Human();

//        Algorithm player1 = new Minimax(true);
//        Algorithm player2 = new Minimax(false);

//        Algorithm player1 = new Minimax(true);
//        Algorithm player2 = new RandomMove();

//        Algorithm player1 = new RandomMove();
//        Algorithm player2 = new Minimax(false);

        System.out.println(player1.getClass().getSimpleName() + " vs " + player2.getClass().getSimpleName());

        Connect4 c4 = new Connect4();
        c4.play(player1, player2);

        player1.printResults();
        // player2.printResults();
    }
}
