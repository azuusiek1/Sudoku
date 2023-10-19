// java -cp "sac-1.0.3;." App
import sac.graph.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
class Sudoku extends sac.graph.GraphStateImpl {
    protected int[][] board;
    protected int n;
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
            //System.out.println("[" + index + "][" + i % (this.n * this.n) + "] =" +  (value.charAt(i) - '0'));
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
    @Override
    public int hashCode() {
        return toString().hashCode();
    }
    @Override
    public List<GraphState> generateChildren() {
        List<GraphState> children = new ArrayList<>();
        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board[i].length; j++) {    
                if (this.board[i][j] == 0) {
                    HashSet<Integer> availableNumbers = new HashSet<>();
                    for(int i_ = 1; i_ <= n*n; i_++) {
                        availableNumbers.add(i_);
                    }
                    for(int x = 0; x < n*n; x++) {
                        availableNumbers.remove(this.board[x][j]);
                        availableNumbers.remove(this.board[i][x]);
                    }

                    int I = (i/n)*n;
                    int J = j-j%n;
                    for(int k = 0; k < n; k++) {
                        for(int kk = 0; kk < n; kk++) {
                            availableNumbers.remove(this.board[I+k][J+kk]);
                        }
                    }
                    for(Integer k : availableNumbers) {
                        Sudoku newSudoku = new Sudoku(this);
                        newSudoku.board[i][j] = k;
                        children.add(newSudoku);
                    }
                    return children;
                }
            }
        }
        return children;
        
    }
    
    @Override
    public boolean isSolution() {
        return (countUnknowns() == 0);
    }

}
class SudokuNew extends Sudoku {
    
    public SudokuNew(int n) {
        super(n);
    }
    public SudokuNew(Sudoku sudoku) {
        super(sudoku);
    }
    public HashSet<Integer> getAvailableNumbers(int i, int j) {
        HashSet<Integer> availableNumbers = new HashSet<>();
        for(int i_ = 1; i_ <= n*n; i_++) {
            availableNumbers.add(i_);
        }
        for(int x = 0; x < n*n; x++) {
            availableNumbers.remove(this.board[x][j]);
            availableNumbers.remove(this.board[i][x]);
        }

        int I = (i/n)*n;
        int J = j-j%n;
        for(int k = 0; k < n; k++) {
            for(int kk = 0; kk < n; kk++) {
                availableNumbers.remove(this.board[I+k][J+kk]);
            }
        }
        return availableNumbers;
    }
    @Override
    public List<GraphState> generateChildren() {
        List<GraphState> children = new ArrayList<>();
        int[] availableNumberInfo = new int[3];
        //x and y from board with minimum of children
        availableNumberInfo[0] = 0;
        availableNumberInfo[1] = 0;
        //minimum children numbers
        availableNumberInfo[2] = Integer.MAX_VALUE;
        HashSet<Integer> minimumPossibilities = new HashSet<>();
        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board[i].length; j++) {    
                if (this.board[i][j] == 0) {
                    HashSet<Integer> availableNumbers = getAvailableNumbers(i, j);
                    //availablenumbers - ilosc rozwiazan
                    if(availableNumbers.size() < availableNumberInfo[2]){
                        availableNumberInfo[0] = i;
                        availableNumberInfo[1] = j;
                        availableNumberInfo[2] = availableNumbers.size();
                        minimumPossibilities = availableNumbers;
                    }
                    if(availableNumbers.size() == 1) {
                        for(Integer k : availableNumbers) {
                            Sudoku newSudoku = new Sudoku(this);
                            newSudoku.board[i][j] = k;
                            children.add(newSudoku);
                            return children;
                        }
                    }
                }
            }
        }
        for(Integer k : minimumPossibilities) {
            Sudoku newSudoku = new Sudoku(this);
            newSudoku.board[availableNumberInfo[0]][availableNumberInfo[1]] = k;
            children.add(newSudoku);
        }
        return children;   
    }
}
class App {
    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader("sudokus.txt"))) {
            String line;
            int totalOldSudoku = 0;
            int totalNewSudoku = 0;
            while ((line = br.readLine()) != null) {
                    long startTime = System.currentTimeMillis();
                    Sudoku sudoku = new Sudoku(3);
                    sudoku.fromString(line);
                    BreadthFirstSearch bfs_old = new BreadthFirstSearch(sudoku);
                    bfs_old.execute();
                    totalOldSudoku += System.currentTimeMillis() - startTime;
                    startTime = System.currentTimeMillis();
                    SudokuNew sudoku_new = new SudokuNew(3);
                    sudoku_new.fromString(line);
                    BreadthFirstSearch bfs_new = new BreadthFirstSearch(sudoku_new);
                    bfs_new.execute();
                    totalNewSudoku += System.currentTimeMillis() - startTime;
                    System.out.println("Comparing: " + bfs_old.getSolutions().size() + ", " + bfs_new.getSolutions().size());
            }
            System.out.println("Times: " + totalOldSudoku + "ms, " + totalNewSudoku + "ms");
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
     
}