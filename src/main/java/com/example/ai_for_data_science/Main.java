package com.example.ai_for_data_science;


import com.example.ai_for_data_science.players.algorithms.*;

import java.io.IOException;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        run();
    }

    private static void run() {

        Random r = new Random();

        try {
            LinearRegression.preProcessData();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        RandomMinimax player1 = new RandomMinimax(true, 3, true);
//        RandomMinimax player2 = new RandomMinimax(false, 3, false);

//        Algorithm player1 = new LinearRegression();
//        Algorithm player2 = new RandomMove();


//        System.out.println(player1.getClass().getSimpleName() + " vs " + player2.getClass().getSimpleName());
//
//        Connect4 c4 = new Connect4();
//
//        while (true) {
//            c4.play(player1, player2);
//            c4.reset();
//            player1.setRandomCount(r.nextInt(5));
//            player2.setRandomCount(r.nextInt(5));
//        }

        //player1.printResults();
        // player2.printResults();
    }
}
