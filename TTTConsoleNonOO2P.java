package com.example;

import java.util.Scanner;
/**
 * Tic-Tac-Toe: Two-player console, non-graphics, non-OO version.
 * All variables/methods are declared as static (belong to the class)
 *  in the non-OO version.
 */
public class TTTConsoleNonOO2P {
    // Name-constants to represent the seeds and cell contents
    public static final int EMPTY = 0;
    public static final int CROSS = 1;
    public static final int NOUGHT = 2;

    // Name-constants to represent the various states of the game
    public static final int PLAYING = 0;
    public static final int DRAW = 1;
    public static final int CROSS_WON = 2;
    public static final int NOUGHT_WON = 3;

    // The game board and the game status
    // default value
    public static int ROWS = 3;
    public static int COLS = 3; // number of rows and columns
    public static int[][] board = new int[ROWS][COLS]; // game board in 2D array
    //  containing (EMPTY, CROSS, NOUGHT)
    public static int currentState;  // the current state of the game
    // (PLAYING, DRAW, CROSS_WON, NOUGHT_WON)
    public static int currentPlayer; // the current player (CROSS or NOUGHT)
    public static int currntRow, currentCol; // current seed's row and column

    public static Scanner in = new Scanner(System.in); // the input Scanner

    /** The entry main method (the program starts here) */
    public static void main(String[] args) {
        // Initialize the game-board and current status
        initGame();
        // Play the game once
        do {
            playerMove(currentPlayer); // update currentRow and currentCol
            updateGame(currentPlayer, currntRow, currentCol); // update currentState
            printBoard();
            // Print message if game-over
            if (currentState == CROSS_WON) {
                System.out.println("[i] 'X' won! Bye!");
            } else if (currentState == NOUGHT_WON) {
                System.out.println("[i] 'O' won! Bye!");
            } else if (currentState == DRAW) {
                System.out.println("[i] It's a Draw! Bye!");
            }
            // Switch player
            currentPlayer = (currentPlayer == CROSS) ? NOUGHT : CROSS;
        } while (currentState == PLAYING); // repeat if not game-over
    }

    /** Initialize the game-board contents and the current states */
    public static void initGame() {
        System.out.println("[*] Enter size of board, eg. 3, 4, 5, ... etc");
        System.out.print(">> Board size: ");
        ROWS = in.nextInt();

        if (ROWS < 3) {
            System.out.println("[i] Board size should be 3 or greater.\n");
            initGame();
        }

        COLS = ROWS;
        board = new int[ROWS][COLS];

        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                board[row][col] = EMPTY;  // all cells empty
            }
        }

        currentState = PLAYING; // ready to play
        currentPlayer = CROSS;  // cross plays first
    }

    /** Player with the "theSeed" makes one move, with input validation.
     Update global variables "currentRow" and "currentCol". */
    public static void playerMove(int theSeed) {
        boolean validInput = false;  // for input validation

        do {
            if (theSeed == CROSS) {
                System.out.print("\n[*] Player 'X', enter your move (row[1-"+ROWS+"] column[1-"+COLS+"]): ");
            } else {
                System.out.print("\n[*] Player 'O', enter your move (row[1-"+ROWS+"] column[1-"+COLS+"]): ");
            }

            System.out.print("\n>> row: ");
            int row = in.nextInt() - 1;  // array index starts at 0 instead of 1

            System.out.print(">> column: ");
            int col = in.nextInt() - 1;

            System.out.print("\n");

            if (row >= 0 && row < ROWS && col >= 0 && col < COLS && board[row][col] == EMPTY) {
                currntRow = row;
                currentCol = col;
                board[currntRow][currentCol] = theSeed;  // update game-board content
                validInput = true;  // input okay, exit loop
            } else {
                System.out.println("[x] This move at (" + (row + 1) + "," + (col + 1)
                        + ") is not valid. Try again...");
            }
        } while (!validInput);  // repeat until input is valid
    }

    /** Update the "currentState" after the player with "theSeed" has placed on
     (currentRow, currentCol). */
    public static void updateGame(int theSeed, int currentRow, int currentCol) {
        if (hasWon(theSeed, currentRow, currentCol)) {  // check if winning move
            currentState = (theSeed == CROSS) ? CROSS_WON : NOUGHT_WON;
        } else if (isDraw()) {  // check for draw
            currentState = DRAW;
        }
        // Otherwise, no change to currentState (still PLAYING).
    }

    /** Return true if it is a draw (no more empty cell) */
    // TODO: Shall declare draw if no player can "possibly" win
    public static boolean isDraw() {
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                if (board[row][col] == EMPTY) {
                    return false;  // an empty cell found, not draw, exit
                }
            }
        }
        return true;  // no empty cell, it's a draw
    }

    /** Return true if the player with "theSeed" has won after placing at
     (currentRow, currentCol) */
    public static boolean hasWon(int theSeed, int currentRow, int currentCol) {
        boolean isThreeInRow = checkRows(theSeed, currentRow);
        boolean isThreeInCol = checkCols(theSeed, currentCol);
        boolean isThreeInDiagonal = checkDiagonal(theSeed);
        boolean tmpIsThreeInDiagonalRev = checkDiagonalReverse(theSeed);

        return (isThreeInRow
                || isThreeInCol
                || isThreeInDiagonal
                || tmpIsThreeInDiagonalRev);
    }

    /** Print the game board */
    public static void printBoard() {
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                printCell(board[row][col]); // print each of the cells
                if (col != COLS - 1) {
                    System.out.print("|");   // print vertical partition
                }
            }

            System.out.println();

            if (row != ROWS - 1) {
                String linePerCell = "---";
                String line = "";

                for (int i = 0; i < ROWS; i++) {
                    line = line + linePerCell;

                    if ((i % 3) == 0) {
                        line = line + "--";
                    }

                    if (i == (ROWS - 1)) {
                        line = line + "-";
                    }
                }

                System.out.println(line); // print horizontal partition
            }
        }

        System.out.println();
    }

    /** Print a cell with the specified "content" */
    public static void printCell(int content) {
        switch (content) {
            case EMPTY:  System.out.print("   "); break;
            case NOUGHT: System.out.print(" O "); break;
            case CROSS:  System.out.print(" X "); break;
        }
    }

    private static boolean checkRows(int theSeed, int currentRow) {
        boolean isThreeInCol = true;

        for (int i = 0; i < COLS; i++) {

            if (board[currentRow][i] == theSeed) {
                isThreeInCol = isThreeInCol && true;
            } else {
                isThreeInCol = isThreeInCol && false;
            }
        }

        return isThreeInCol;
    }

    private static boolean checkCols(int theSeed, int currentCol) {
        boolean isThreeInCol = true;

        for (int i = 0; i < COLS; i++) {
            if (board[i][currentCol] == theSeed) {
                isThreeInCol = isThreeInCol && true;
            } else {
                isThreeInCol = isThreeInCol && false;
            }
        }

        return isThreeInCol;
    }

    private static boolean checkDiagonal(int theSeed) {
        boolean bool = true;

        for (int i = 0; i < ROWS; i++) {
            if (board[i][i] == theSeed) {
                bool = bool && true;
            } else {
                bool = bool && false;
            }
        }

        return bool;
    }

    private static boolean checkDiagonalReverse(int theSeed) {
        boolean bool = true;

        for (int i = 0; i <= (ROWS - 1); i++) {
            int j = (ROWS - 1) - i;

            if (board[j][i] == theSeed) {
                bool = bool && true;
            } else {
                bool = bool && false;
            }
        }

        return bool;
    }
}