package com.tictactoe;

import java.util.Random;

/* * *
 * This class contains the logic and possible actions of the bot. It keeps its own localized copy of the
 * game board and uses a slightly modified version of the popular minimax algorithm found
 * online. The bot semi cheats by always going first, but it also randomly
 * sabotages itself, granting the human player a chance at victory.
 */
public class TicTacToeBot {
    private String name;
    private char shape;
    private char[][] board;
    private char opponentShape;
    private boolean firstTurn;
    int[] currentMove;

    public TicTacToeBot(String name, char shape) {
        this.name = name;
        this.shape = shape;
        firstTurn = true;
        currentMove = new int[2];
    }

    public String getName() {
        return name;
    }

    public char getShape() {
        return shape;
    }

    private void moveToFirstAvailable() {
        for (int rows = 0; rows < 3; rows++) {
            for (int columns = 0; columns < 3; columns++) {
                if (board[rows][columns] == 'B') {
                    board[rows][columns] = shape;
                    currentMove[0] = rows;
                    currentMove[1] = columns;
                    return;
                }
            }
        }
    }

    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == 'B') {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * The "evaluate" method examines the board's current layout and scores
     * the possible moves for use in the minimax algorithm.
     *
     * @return The score of this particular move.
     */
    private int evaluate() {
        // Rows check
        for (int row = 0; row < 3; row++) {
            if (board[row][0] == board[row][1] && board[row][1] == board[row][2]) {
                if (board[row][0] == shape) {
                    return 10;
                } else if (board[row][0] == opponentShape) {
                    return -10;
                }
            }
        }

        // Columns check
        for (int col = 0; col < 3; col++) {
            if (board[0][col] == board[1][col] && board[1][col] == board[2][col]) {
                if (board[0][col] == shape) {
                    return 10;
                } else if (board[0][col] == opponentShape) {
                    return -10;
                }
            }
        }

        if(board[1][1] != 'B') {
            // Left diagonal check
            if (board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
                if (board[0][0] == shape) {
                    return 10;
                } else if (board[0][0] == opponentShape) {
                    return -10;
                }
            }

            //Right diagonal check
            if (board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
                if (board[0][2] == shape) {
                    return 10;
                } else if (board[0][2] == opponentShape) {
                    return -10;
                }
            }
        }

        return 0;
    }


    /**
     * This method uses a minimax algorithm to judge all board permutations and calculates the highest score move among them.
     *
     * @param depth Used for tracking the depth of the current recursion and for finding the highest priority move.
     * @param isMax Used for swapping between min and max turn.
     * @return The value of this move, which is used to determine if it is an optimal move.
     */
    private int minimax(int depth, boolean isMax) {
        int score = evaluate();

        if (score == 10) { // Maximizer score
            return score;
        } else if (score == -10) {// Minimizer score
            return score;
        } else if (isBoardFull()) {//Draw score
            return 0;
        }

        if(isMax) {
            int best = -1000;

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == 'B') { //If a cell is empty, makes that move then scores it and following moves, then returns the board to its original state.
                        board[i][j] = shape;

                        best = Math.max(best, minimax(depth + 1, !isMax));

                        board[i][j] = 'B';
                    }
                }
            }
            return best - depth;
        } else {
            int best = 1000;

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == 'B') {
                        board[i][j] = opponentShape;

                        best = Math.min(best, minimax(depth + 1, !isMax));

                        board[i][j] = 'B';
                    }
                }
            }
            return best + depth;
        }
    }

    /**
     * This method uses the minimax method to find the optimal move.
     * Both these methods assume the opponent is attempting to play optimally as well.
     *
     * @return A two element array containing the row and column of the "best move."
     */
    private int[] findBestMove() {
        int bestVal = -1000;
        int[] bestMove = new int[2];
        bestMove[0] = -1;
        bestMove[1] = -1;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == 'B') {
                    board[i][j] = shape;

                    int moveVal = minimax(0, false);

                    board[i][j] = 'B';

                    if (moveVal > bestVal) {
                        bestMove[0] = i;
                        bestMove[1] = j;
                        bestVal = moveVal;
                    }
                }
            }
        }

        return bestMove;
    }

    public int[] makeMove(char[][] currentBoard, char playerShape) {
        this.board = currentBoard;
        this.opponentShape = playerShape;

        Random random = new Random(); //Random chance for the bot to perform a non-optimal move. Added to give the human player a chance.
        if (random.nextInt(50) <= 10) {
            moveToFirstAvailable();
            return currentMove;
        }

        if (firstTurn) {
            firstTurn = false;
            if (shape == 'X') {
                board[0][0] = shape;
            } else {
                board[1][1] = shape;
            }

        } else {
            currentMove = findBestMove();
            if (currentMove[0] != -1) {
                board[currentMove[0]][currentMove[1]] = shape;
            }
        }

        return currentMove;
    }
}
