package com.tictactoe;

/* * *
 * This class keeps track of the board's state, available legal moves, whose turn it is,
 * if the board is in a terminal state, and who won (if applicable) for human-to-human play.
 *
 * The board is also in charge of the bot mode.
 */
public class Board {

    private final String PLAYER_1_NAME = "Player 1";
    private final char PLAYER_1_SHAPE = 'X';

    private final String PLAYER_2_NAME = "Player 2";
    private final char PLAYER_2_SHAPE = 'O';

    private Player player1 = new Player(PLAYER_1_NAME, PLAYER_1_SHAPE);
    private Player player2 = new Player(PLAYER_2_NAME, PLAYER_2_SHAPE);
    private Player currentActivePlayer = player1;

    private int movesMade = 0;
    private boolean gameCompleted = false;
    private int victoryCount = 2;
    private int[] lastMove = new int[2];

    private char[][] boardMatrix = new char[][] {{'B','B','B'}, {'B','B','B'}, {'B','B','B'}};
    private boolean botMode = false;
    private TicTacToeBot bot = new TicTacToeBot("Bot", 'X');

    public void resetBoard() {
        movesMade = 0;
        gameCompleted = false;
        boardMatrix = new char[][] {{'B','B','B'}, {'B','B','B'}, {'B','B','B'}};
        bot = new TicTacToeBot("Bot", 'X');
        if(botMode) {
            botTurn();
            currentActivePlayer = player2;
        } else {
            currentActivePlayer = player1;
        }
    }

    public char[][] getBoard() {
        return boardMatrix;
    }

    public Player getCurrentActivePlayer() {
        return currentActivePlayer;
    }

    public String getWinnerName() {
        if(botMode) {
            if(boardMatrix[lastMove[0]][lastMove[1]] == 'X') {
                return bot.getName();
            } else {
                return player2.getName();
            }
        } else {
            if(boardMatrix[lastMove[0]][lastMove[1]] == 'X') {
                return player1.getName();
            } else {
                return player2.getName();
            }
        }
    }

    public void changePlayer() {
        if(currentActivePlayer.getName().equals(PLAYER_1_NAME)) {
            currentActivePlayer = player2;
        } else {
            currentActivePlayer = player1;
        }
    }

    public boolean moveTo(int row, int column, char shape) {
        boolean moved = false;
        if(boardMatrix[row][column] == 'B' &&
                shape == currentActivePlayer.getShape()) {
            boardMatrix[row][column] = shape;
            moved = true;
            lastMove[0] = row;
            lastMove[1] = column;
            movesMade++;
        }

        return moved;
    }

    public void botTurn() {
        lastMove = bot.makeMove(boardMatrix, PLAYER_2_SHAPE);
        movesMade++;
    }

    public void setBotMode(boolean status) {
        this.botMode = status;
    }

    public boolean isCompleted() {
        return gameCompleted || movesMade >= 9;
    }

    public boolean hasWinner() {
        if(movesMade < 4)  {
            return false;
        }

        if(checkRowsWin() || checkDiagsWin() || checkColumnsWin()) {
            gameCompleted = true;
            return true;
        }
        return false;
    }

    private boolean checkRowsWin() {
        int matches = 0;

        for (char[] matrix : boardMatrix) {
            for (int col = 0; col < boardMatrix[0].length - 1; col++) {
                if (matrix[col] != 'B' && matrix[col] == matrix[col + 1]) {
                    matches++;
                    if (matches >= victoryCount) {
                        gameCompleted = true;
                        return true;
                    }
                } else {
                    matches = 0;
                    break;
                }
            }
        }
        return false;
    }

    private boolean checkDiagsWin() {
        int matches = 0;

        //Left diagonal
        for(int i = 0; i < boardMatrix.length -1; i++) {
            if(boardMatrix[i][i] != 'B' && boardMatrix[i][i] == boardMatrix[i+1][i+1]) {
                matches++;
                if(matches >= victoryCount) {
                    gameCompleted = true;
                    return true;
                }
            } else {
                matches = 0;
                break;
            }
        }

        matches = 0;

        //Right diagonal
        for(int row = 0, col = boardMatrix.length - 1; col > 0; row++, col--) {
            if(boardMatrix[row][col] != 'B' && boardMatrix[row][col] == boardMatrix[row+1][col-1]) {
                matches++;
                if(matches >= victoryCount) {
                    gameCompleted = true;
                    return true;
                }
            } else {
                matches = 0;
                break;
            }
        }
        return false;
    }

    private boolean checkColumnsWin() {
        int matches = 0;
        for(int col = 0; col < boardMatrix[0].length -1; col++) {
            for(int row = 0; row < boardMatrix.length; row++) {
                if(boardMatrix[row][col] != 'B' &&  boardMatrix[row][col] == boardMatrix[row+1][col]) {
                    matches++;
                    if(matches >= victoryCount) {
                        gameCompleted = true;
                        return true;
                    }
                } else {
                    matches = 0;
                    break;
                }
            }
        }
        return false;
    }
}
