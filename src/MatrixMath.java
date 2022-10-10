
public class MatrixMath {


    //Calculates and returns the matrix multiplication product of two matricies
    public int[][] matrixMultiply(int[][] a, int[][] b){
        /*
            matrix[row][col] --> m[2][3] = | 0 0 0 |
                                           | 0 0 0 |

            Thus: m[2][3] * p[3][1]

            -->     | 1 0 0 |   *   | x |   = | x |
                    | 0 1 0 |       | y |     | y |
                                    | z |
        */

        int colsA = a[0].length;
        int rowsA = a.length;
        int colsB = b[0].length;
        int rowsB = b.length;

        int[][] result = new int[rowsA][colsB];

        //check dimensions
        if(!(colsA == rowsB)) return null;
        
        for(int row = 0; row < rowsA; row++){
            for(int col = 0; col < colsB; col++){
                int sum = 0;
                for(int i = 0; i < rowsB; i++){
                    sum += a[row][i] * b[i][col];
                }
                result[row][col] = sum; 
            }
        }

        return result;
    }

}
