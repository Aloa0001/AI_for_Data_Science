package com.example.ai_for_data_science.players.algorithms;

import com.example.ai_for_data_science.Algorithm;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

public class DecisionTree implements Algorithm {

    private List<DataTuple> trainingData = new ArrayList<DataTuple>();
    private List<DataTuple> testData = new ArrayList<DataTuple>();

    private HashMap<Integer, Integer[]> attributes = new HashMap<Integer, Integer[]>();
    private Node rootNode;


    private class Node {

        public String label = "";
        public HashMap<String, Node> childNodes = new HashMap<String, Node>();

        public Node() {
        }

        public Node(String label) {
            this.label = label;
        }

        public boolean IsLeaf()
        {
            return childNodes.size() == 0;
        }

        public void Print(String indent) {
            if (this.IsLeaf()) {
                System.out.print(indent);
                System.out.println("  └─ Return " + label);
            }
            Map.Entry<String, Node>[] nodes = childNodes.entrySet().toArray(new Map.Entry[0]);
            for (int i = 0; i < nodes.length; i++) {
                System.out.print(indent);
                if (i == nodes.length - 1) {
                    System.out.println("└─ \"" + label + "\" = " + nodes[i].getKey() + ":");
                    nodes[i].getValue().Print(indent + "    ");
                } else {
                    System.out.println("├─ \"" + label + "\" = " + nodes[i].getKey() + ":");
                    nodes[i].getValue().Print(indent + (char)0x007C + "   ");
                }
            }

        }
    }


    @Override
    public int returnMove(int[] gameBoard) {
        return 0;
    }


    public void Print() {
        try {
            System.setOut(new PrintStream(new FileOutputStream("DT_model.txt")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        rootNode.Print("");
    }


    public int[] evalFourCells(int[] gameBoard) {

        int[] fourCellEvals = new int[69];

        int counter = 0;

        // Horizontal check
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 7-3; col++) {
                fourCellEvals[counter] = (gameBoard[row * 7 + col] % 2) * 2 - 1;
                fourCellEvals[counter] += (gameBoard[row * 7 + col + 1] % 2) * 2 - 1;
                fourCellEvals[counter] += (gameBoard[row * 7 + col + 2] % 2) * 2 - 1;
                fourCellEvals[counter] += (gameBoard[row * 7 + col + 3] % 2) * 2 - 1;
            }
            ++counter;
        }

        // Vertical check
        for (int col = 0; col < 7; col++) {
            for (int row = 0; row < 6-3; row++) {
                fourCellEvals[counter] = (gameBoard[col + row * 7] % 2) * 2 - 1;
                fourCellEvals[counter] += (gameBoard[col + (row + 1) * 7] % 2) * 2 - 1;
                fourCellEvals[counter] += (gameBoard[col + (row + 2) * 7] % 2) * 2 - 1;
                fourCellEvals[counter] += (gameBoard[col + (row + 3) * 7] % 2) * 2 - 1;
            }
            ++counter;
        }

        // diagonal (down + left) check
        for (int col = 3; col < 7; col++){
            for (int row = 0; row < 6-3; row++){
                fourCellEvals[counter] = (gameBoard[col + row * 7] % 2) * 2 - 1;
                fourCellEvals[counter] += (gameBoard[col - 1 + (row + 1) * 7] % 2) * 2 - 1;
                fourCellEvals[counter] += (gameBoard[col - 2 + (row + 2) * 7] % 2) * 2 - 1;
                fourCellEvals[counter] += (gameBoard[col - 3 + (row + 3) * 7] % 2) * 2 - 1;
            }
            ++counter;
        }

        // diagonal (up + left) check
        for (int col = 3; col < 7; col++){
            for (int row = 3; row < 6; row++){
                fourCellEvals[counter] = (gameBoard[col + row * 7] % 2) * 2 - 1;
                fourCellEvals[counter] += (gameBoard[col - 1 + (row - 1) * 7] % 2) * 2 - 1;
                fourCellEvals[counter] += (gameBoard[col - 2 + (row - 2) * 7] % 2) * 2 - 1;
                fourCellEvals[counter] += (gameBoard[col - 3 + (row - 3) * 7] % 2) * 2 - 1;
            }
            ++counter;
        }

        return fourCellEvals;
    }
}
