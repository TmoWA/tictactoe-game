package com.tictactoe;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ClickableImage extends ImageView{
    private final String IMAGE_NAME;

    public ClickableImage(String imageName, Image image) {
        super(image);
        this.IMAGE_NAME = imageName;
    }

    public String getName() {
        return IMAGE_NAME;
    }
}