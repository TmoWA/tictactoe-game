package com.tictactoe;

import java.io.File;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class MainController {

    private String xFilePath = "src/X-picture-2.png";
    private String oFilePath = "src/O-picture-2.png";
    private String blankImageFilePath = "src/blank-picture.png";

    private final String PLAYER_1_SHAPE = "X";
    private File xImageFile = new File(xFilePath);
    private ClickableImage xImage;

    private final String PLAYER_2_SHAPE = "O";
    private File oImageFile = new File(oFilePath);
    private ClickableImage oImage;


    private final String BLANK_IMAGE_NAME = "blank";

    @FXML
    private Label turnLabel;
    private SimpleStringProperty playerTurnProperty = new SimpleStringProperty();

    @FXML
    private GridPane gridPane;

    private Board board;
    private boolean botMode = false;

    @FXML
    private void initialize() {
        turnLabel.textProperty().bind(playerTurnProperty);
        createBoard();
        board = new Board();

        playerTurnProperty.set(board.getCurrentActivePlayer().getName() + "'s turn");
    }

    private void createBoard() {
        File blankImageFile = new File(blankImageFilePath);
        ClickableImage blankImage;

        for(int rows = 0; rows < 3; rows++) {
            for(int columns = 0; columns < 3; columns++) {
                blankImage = new ClickableImage(BLANK_IMAGE_NAME, new Image(blankImageFile.toURI().toString()));
                if(botMode) {
                    blankImage.setOnMouseClicked(this::imageSelectedBotMode);
                } else {
                    blankImage.setOnMouseClicked(this::imageSelectedManualMode);
                }
                gridPane.add(blankImage, rows, columns);
            }
        }
    }

    @FXML
    private void resetBoard() {
        board.resetBoard();
        createBoard();
        if(botMode) {
            playerTurnProperty.set("Bot mode. You are 'O' and the bot is 'X'");
        } else {
            playerTurnProperty.set(board.getCurrentActivePlayer().getName() + "'s turn");
        }
        displayBoard();
    }

    private void displayBoard() {
        char[][] currentBoard = board.getBoard();

        for(int rows = 0; rows < 3; rows++) {
            for(int columns = 0; columns < 3; columns++) {
                if(currentBoard[rows][columns] == 'X') {
                    xImage = new ClickableImage(PLAYER_1_SHAPE, new Image(xImageFile.toURI().toString()));
                    gridPane.add(xImage, columns, rows);
                } else if (currentBoard[rows][columns] == 'O') {
                    oImage = new ClickableImage(PLAYER_2_SHAPE, new Image(oImageFile.toURI().toString()));
                    gridPane.add(oImage, columns, rows);
                }
            }
        }
    }

    private void imageSelectedBotMode(MouseEvent event) {
        if(board.isCompleted()) {
            return;
        }

        ClickableImage temp;
        if(event.getSource() instanceof ClickableImage) {
            temp = (ClickableImage) event.getSource();
            if(!temp.getName().equals(BLANK_IMAGE_NAME)) {
                return;
            }
        } else {
            return;
        }

        int row = GridPane.getRowIndex(temp);
        int column = GridPane.getColumnIndex(temp);

        if(board.getBoard()[row][column] != 'B') {
            return;
        }

        if(!board.isCompleted()) {
            board.moveTo(row, column, 'O');
        }
        displayBoard();
        checkForWinner();

        if(!board.isCompleted()) {
            board.botTurn();
        }
        displayBoard();
        checkForWinner();
    }

    private void imageSelectedManualMode(MouseEvent event) {
        if(board.isCompleted()) {
            return;
        }

        ClickableImage temp;

        if(event.getSource() instanceof ClickableImage) {
            temp = (ClickableImage) event.getSource();

            if(!temp.getName().equals(BLANK_IMAGE_NAME)) {
                return;
            }
        } else {
            return;
        }

        int row = GridPane.getRowIndex(temp);
        int column = GridPane.getColumnIndex(temp);
        Player currentPlayer = board.getCurrentActivePlayer();

        board.moveTo(row, column, currentPlayer.getShape());

        board.changePlayer();
        playerTurnProperty.set(board.getCurrentActivePlayer().getName() + "'s turn");


        displayBoard();
        checkForWinner();
    }

    private void checkForWinner() {
        String victoryMessage = "";

        if(board.hasWinner()) {
            victoryMessage = board.getWinnerName() + " is victorious! Reset to play again.";
        } else if(board.isCompleted()) {
            victoryMessage = "Draw! Reset to play again.";
        }

        if(!victoryMessage.isBlank()) {
            playerTurnProperty.set(victoryMessage);
        }
    }

    @FXML
    private void botMode() {
        botMode = true;
        board.setBotMode(botMode);
        resetBoard();
    }

    @FXML
    private void manualMode() {
        botMode = false;
        board.setBotMode(botMode);
        resetBoard();
    }
}

