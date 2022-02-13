package sudokusolver.sudoku;

import java.util.Scanner;

public class App
{
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args)
    {
        Sudoku sudoku = new Sudoku();
        Field field;

        /* Demo sudoku */
        int[] contents =
        {
            9, 5, 8,  7, 0, 6,  3, 2, 0,
            0, 1, 7,  3, 9, 4,  8, 6, 0,
            4, 0, 0,  0, 0, 0,  9, 1, 0,
            
            5, 0, 0,  0, 0, 0,  7, 0, 0,
            0, 8, 0,  9, 2, 0,  0, 0, 6,
            0, 0, 0,  1, 0, 7,  4, 0, 8,
            
            6, 0, 5,  4, 7, 0,  0, 0, 0,
            0, 0, 0,  0, 0, 2,  0, 0, 9,
            0, 7, 2,  0, 0, 0,  6, 0, 0
        };

        System.out.println("Enter a new sudoku? [y/N]");
        String answer = scanner.nextLine().trim().toLowerCase();
        if (answer.length() != 0)
        {
            if (answer.charAt(0) == 'y')
            {
                for (int i = 0; i < 9; i++)
                {
                    System.out.println("Enter row #" + (i + 1) + ":");
                    String line = scanner.nextLine().trim();
                    for (int j = 0; j < 9; j++)
                    {
                        int n = line.charAt(j) - '0';
                        if (n < 0 || n > 9)
                        {
                            System.out.println("Invalid number at letter #" + (j + 1));
                            return;
                        }
                        contents[i * 9 + j] = n;
                    }
                }
            }
        }

        try
        {
            field = new Field(contents);
        }
        catch (WrongLengthException e)
        {
            e.printStackTrace();
            return;
        }

        sudoku.setField(field);
        sudoku.printSudoku();

        int iteration = 0;
        while (!sudoku.isSolved())
        {
            iteration++;
            if (sudoku.solve())
            {
                System.out.println("Solved sudoku after " + iteration + " iterations.");
                break;
            }
            if ((iteration + 1) % 25 == 0)
            {
                sudoku.printSudoku();
                System.out.format("Sudoku not solved after 25 iterations (Iteration #%d). Continue on? [Y/n]\n", iteration);
                answer = scanner.nextLine().trim().toLowerCase();
                if (answer.length() != 0)
                {
                    if (answer.charAt(0) != 'y')
                    {
                        break;
                    }
                }
            }
        }

        if (!sudoku.isSolved())
        {
            System.out.format("Could not solve, took %d iterations.\n", iteration);
        }

        sudoku.printSudoku();        
    }
}
