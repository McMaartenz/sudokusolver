package sudokusolver.sudoku;

public class WrongLengthException extends Exception
{
    public WrongLengthException(String errorMessage)
    {
        super(errorMessage);
    }

    public WrongLengthException()
    {
        super("The wrong length was provided for an object");
    }
}
