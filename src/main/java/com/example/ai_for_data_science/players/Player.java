package com.example.ai_for_data_science.players;

public class Player {
    private Type type;
    private String name;

    public Player(String name) {
        this.type = name.equals("Human") ? Type.HUMAN : name.equals("Minimax") ? Type.MINIMAX :
                name.equals("Decision Tree") ? Type.DECISION_TREE : name.equals("Gaussian") ? Type.GAUSSIAN : Type.SVM;
        this.name = name;
    }

    public int move(){
        return 5;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
