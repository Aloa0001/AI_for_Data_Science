package com.example.ai_for_data_science;


import com.example.ai_for_data_science.players.algorithms.Minimax;

public class Main {

    public static void main(String[] args) {
        run();
    }

    private static void run() {

        Algorithm player1 = new Minimax();
        Algorithm player2 = new Minimax();

        Connect4 c4 = new Connect4();
        c4.play(player1, player2);
    }
}
