package sudokusolver.sudoku;

public class OutOfRangeException extends Exception
{
       public OutOfRangeException(String errorMessage)
       {
           super(errorMessage);
       }

       public OutOfRangeException()
       {
           super("Out of range");
       }
}
