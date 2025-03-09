package application;

public class KnightsTour {
    private static final int N = 8; // Size of the chessboard

    // Possible moves for the knight
    private static final int[] xMove = {2, 1, -1, -2, -2, -1, 1, 2};
    private static final int[] yMove = {1, 2, 2, 1, -1, -2, -2, -1};

    // Main method
    public static void main(String[] args) {
        KnightsTour knightTour = new KnightsTour();
        knightTour.solveKnightTour();
    }

    // Method to solve the Knight's Tour problem
    public void solveKnightTour() {
        int[][] board = new int[N][N];

        // Initialize the chessboard with 0 (unvisited)
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                board[i][j] = 0;
            }
        }

        // Starting position for the knight
        int startX = 0, startY = 0;
        board[startX][startY] = 1; // Mark the first move

        // Attempt to solve the problem
        if (tryNextMove(2, startX, startY, board)) {
            printBoard(board);
        } else {
            System.out.println("No solution exists for the Knight's Tour.");
        }
    }

    // Backtracking method to try the next move
    private boolean tryNextMove(int moveCount, int x, int y, int[][] board) {
        // Base case: all moves are completed
        if (moveCount > N * N) {
            return true;
        }

        // Try all possible moves
        for (int k = 0; k < xMove.length; k++) {
            int nextX = x + xMove[k];
            int nextY = y + yMove[k];

            if (isValidMove(nextX, nextY, board)) {
                board[nextX][nextY] = moveCount; // Mark the move

                System.out.printf("After move %d:\n", moveCount);
                printBoard(board);
                try {
                    Thread.sleep(1000); // Pause for 500 milliseconds
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                if (tryNextMove(moveCount + 1, nextX, nextY, board)) {
                    return true;
                }

                // Backtrack
                board[nextX][nextY] = 0;
            }
        }

        return false; // No valid moves found
    }

    // Method to check if the move is valid
    private boolean isValidMove(int x, int y, int[][] board) {
        return x >= 0 && x < N && y >= 0 && y < N && board[x][y] == 0;
    }

    // Method to print the chessboard
    private void printBoard(int[][] board) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                System.out.printf("%2d ", board[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }
}

