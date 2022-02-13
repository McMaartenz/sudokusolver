package sudokusolver.sudoku;

public class IllegalNumberException extends Exception
{
    public IllegalNumberException(String errorMessage)
    {
        super(errorMessage);
    }

    public IllegalNumberException()
    {
        super("Illegal number provided");
    }
}
