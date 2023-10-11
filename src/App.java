// java -cp "sac-1.0.3;." App
import sac.graph.GraphState;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Arrays;
class Sudoku {
    private int[][] board;
    private int n;
    public Sudoku(int n) {
        board = new int[n*n][n*n];
        this.n = n;
    }
    // Copy constructor
    public Sudoku(Sudoku app) {
        int n = app.n;
        board = new int[n*n][n*n];
        this.n = n;
        for (int i = 0; i < app.board.length; i++) {
            board[i] = Arrays.copyOf(app.board[i], n*n);
        }

    }
    // Converts sudoku board to string
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board.length; j++) {
                s.append(board[i][j] + " ");
            }
            s.append("\n");
        }
        return s.toString();
    }
    // Converts string to sudoku board.
    // Example: 200419500001005090800200000048060900000000608000000000000008060020040003003006450
    // =
    // 2 0 0 4 1 9 5 0 0
    // 0 0 1 0 0 5 0 9 0
    // 8 0 0 2 0 0 0 0 0
    // 0 4 8 0 6 0 9 0 0
    // 0 0 0 0 0 0 6 0 8
    // 0 0 0 0 0 0 0 0 0
    // 0 0 0 0 0 8 0 6 0
    // 0 2 0 0 4 0 0 0 3
    // 0 0 3 0 0 6 4 5 0
    public void fromString(String value) {
        int index = 0;
        for (int i = 0; i < value.length(); i++) {
            if(i % (this.n * this.n) == 0 && i != 0) {
                index++;
            }
            this.board[index][i % (this.n * this.n)] = value.charAt(i) - '0';
            System.out.println("[" + index + "][" + i % (this.n * this.n) + "] =" +  (value.charAt(i) - '0'));
        }
    }
    public boolean isValidSudoku() {
        int size = n * n;
        // Rows checking
        for (int i = 0; i < size; i++) {
            HashSet<Integer> rowSet = new HashSet<>();
            for (int j = 0; j < size; j++) {
                if (board[i][j] != 0) {
                    if (rowSet.contains(board[i][j])) {
                        return false;
                    }
                    rowSet.add(board[i][j]);
                }
            }
        }

        // Column checking
        for (int i = 0; i < size; i++) {
            HashSet<Integer> colSet = new HashSet<>();
            for (int j = 0; j < size; j++) {
                if (board[j][i] != 0) {
                    if (colSet.contains(board[j][i])) {
                        return false;
                    }
                    colSet.add(board[j][i]);
                }
            }
        }

        // Subboards checkign
        for (int row = 0; row < size; row += n) {
            for (int col = 0; col < size; col += n) {
                HashSet<Integer> blockSet = new HashSet<>();
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        int num = board[row + i][col + j];
                        if (num != 0) {
                            if (blockSet.contains(num)) {
                                return false;
                            }
                            blockSet.add(num);
                        }
                    }
                }
            }
        }

        return true;
    }
    public int countUnknowns() {
        int count = 0;
        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board[i].length; j++) {
                if (this.board[i][j] == 0) {
                    count++;
                }
            }
        }
        return count;
    }

}
class App {
    public static void main(String[] args) throws Exception {
        Sudoku sudoku = new Sudoku(3);
        System.out.println(sudoku.toString());
        sudoku.fromString("200419500001005090800200000048060900000000608000000000000008060020040003003006450");
        System.out.println(sudoku.toString());
        if (sudoku.isValidSudoku()) {
            System.out.println("Sudoku is correctly filled. [OK]");
        } else {
            System.out.println("Sudoku isn't correctly filled. [NOK]");
        }
        System.out.println("Unknown tiles: " + sudoku.countUnknowns());
    }
}