package com.tictactoe;

public class Player {
    private String name;
    private char shape;

    public Player(String name, char shape) {
        this.name = name;
        this.shape = shape;
    }

    public String getName() {
        return name;
    }

    public char getShape() {
        return shape;
    }
}