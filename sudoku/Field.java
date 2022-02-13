package sudokusolver.sudoku;

import java.util.ArrayList;
import java.util.Arrays;

public class Field
{
    private int[] contents = new int[81];

    public int[] getContents()
    {
        return contents;
    }

    public int[] getRow(int row) throws OutOfRangeException
    {
        if (row < 0 || row > 9)
        {
            throw new OutOfRangeException(String.format("Row was %d, expected between 0 and 9", row));
        }
        return Arrays.copyOfRange(contents, row * 9, (row + 1) * 9);
    }

    public int[] getColumn(int col) throws OutOfRangeException
    {
        if (col < 0 || col > 9)
        {
            throw new OutOfRangeException(String.format("Column was %d, expected between 0 and 9", col));
        }
        int[] result = new int[9];
        for (int i = 0; i < 9; i++)
        {
            result[i] = contents[getIndexFromPoint(new Point(col, i))];
        }
        return result;
    }

    public int[] getBlock(Point point) throws OutOfRangeException
    {
        return getBlock(point.x, point.y);
    }

    public int[] getBlock(int x, int y) throws OutOfRangeException
    {
        if (x < 0 || x > 2 || y < 0 || y > 2)
        {
            throw new OutOfRangeException(String.format("Coordinates out of range for (x, y): (%d, %d), expected between 0 and 2 for both X and Y", x, y));
        }
        Point block_base = new Point((x % 3) * 3, (y % 3) * 3);
        int[] result = new int[9];
        for (int j = 0; j < 3; j++)
        {
            for (int i = 0; i < 3; i++)
            {
                result[(j * 3) + i] = contents[getIndexFromPoint(new Point(block_base.x + i, block_base.y + j))];
            }
        }
        return result;
    }

    public boolean verifyNumberAt(int index, int value) throws OutOfRangeException, IllegalNumberException
    {
        if (index < 0 || index > 80)
        {
            throw new OutOfRangeException(String.format("Index out of range for value %d, must be between 0 and 80", index));
        }
        if (value < 1 || value > 9)
        {
            throw new IllegalNumberException(String.format("Illegal number '%d' was provided, must be between 1 and 9", value));
        }
        
        int y = index / 9;
        int x = index % 9;

        int[] row = getRow(y);
        int[] col = getColumn(x);
        int[] block = getBlock(x / 3, y / 3);
        
        return  !(listContains(row, value) ||
                  listContains(col, value) ||
                  listContains(block, value));
    }

    public void setContents(int[] contents) throws WrongLengthException
    {
        if (contents.length != 81)
        {
            throw new WrongLengthException(String.format("A length of %d was given, expected 81", contents.length));
        }
        this.contents = contents;
    }

    public int getNumberAt(int x, int y) throws OutOfRangeException
    {
        return getNumberAt(Field.getIndexFromPoint(x, y));
    }

    public int getNumberAt(Point point) throws OutOfRangeException
    {
        return getNumberAt(Field.getIndexFromPoint(point));
    }

    public int getNumberAt(int index) throws OutOfRangeException
    {
        if (index < 0 || index > 80)
        {
            throw new OutOfRangeException(String.format("Index out of range for value %d, must be between 0 and 80", index));
        }
        return contents[index];
    }

    public void setNumberAt(Point point, int value) throws OutOfRangeException, IllegalNumberException
    {
        setNumberAt(Field.getIndexFromPoint(point), value);
    }

    public void setNumberAt(int x, int y, int value) throws OutOfRangeException, IllegalNumberException
    {
        setNumberAt(Field.getIndexFromPoint(x, y), value);
    }

    public int[] getPossibleNumbersAt(Point point) throws OutOfRangeException
    {
        return getPossibleNumbersAt(point.x, point.y);
    }

    public int[] getPossibleNumbersAt(int x, int y) throws OutOfRangeException
    {
        ArrayList<Integer> result = new ArrayList<>();
        for (int n = 1; n < 10; n++)
        {
            try
            {
                if (verifyNumberAt(getIndexFromPoint(x, y), n))
                {
                    result.add(n);
                }
            }
            catch (IllegalNumberException e)
            {
                System.out.println("Internal Exception");
                e.printStackTrace();
            }
        }
        return toPrimitive(result);
    }

    private static int[] toPrimitive(ArrayList<Integer> result)
    {
        int[] out = new int[result.size()];
        for (int i = 0; i < out.length; i++)
        {
            out[i] = result.get(i);
        }
        return out;
    }

    public void setNumberAt(int index, int value) throws OutOfRangeException, IllegalNumberException
    {
        if (index < 0 || index > 80)
        {
            throw new OutOfRangeException(String.format("Index out of range for value %d, must be between 0 and 80", index));
        }
        if (value < 1 || value > 9)
        {
            throw new IllegalNumberException(String.format("Illegal number '%d' was provided, must be between 1 and 9", value));
        }
        contents[index] = value;
    }

    Field() {}

    Field(int[] contents) throws WrongLengthException
    {
        setContents(contents);
    }

    public static int getIndexFromPoint(Point point) throws OutOfRangeException
    {
        return getIndexFromPoint(point.x, point.y);
    }

    public static int getIndexFromPoint(int x, int y) throws OutOfRangeException
    {
        if (x < 0 || x > 9 ||
            y < 0 || y > 9)
        {
            throw new OutOfRangeException(String.format("Coordinates out of range for (x, y): (%d, %d).", x, y));
        }
        return x + (y * 9);
    }

    public boolean isOccupied(Point point) throws OutOfRangeException
    {
        return isOccupied(point.x, point.y);
    }

    public boolean isOccupied(int x, int y) throws OutOfRangeException
    {
        return isOccupied(getIndexFromPoint(x, y));
    }
    
    public boolean isOccupied(int index) throws OutOfRangeException
    {
        if (index < 0 || index > 80)
        {
            throw new OutOfRangeException(String.format("Index out of range for value %d, must be between 0 and 80", index));
        }
        return contents[index] > 0;
    }

    public boolean validateField()
    {
        for (int i = 0; i < 9; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                try
                {
                    int index = getIndexFromPoint(j, i);
                    if (!verifyNumberAt(index, contents[index]))
                    {
                        return false;
                    }
                }
                catch (OutOfRangeException | IllegalNumberException e)
                {
                    System.out.println("Internal Exception");
                    e.printStackTrace();
                    return false;
                }
            }
        }
        return true;
    }

    protected static boolean listContains(int[] list, int value)
    {
        for (int i = 0; i < list.length; i++)
        {
            if (list[i] == value)
            {
                return true;
            }
        }
        return false;
    }

    protected static int findMissing(int[] list)
    {
        int zeroCount = 0;
        for (int i = 0; i < list.length; i++)
        {
            if (list[i] == 0)
            {
                zeroCount++;
                if (zeroCount > 1)
                {
                    return 0;
                }
            }
        }

        for (int i = 1; i <= 9; i++)
        {
            boolean hasValue = false;
            for (int j = 0; j < list.length; j++)
            {
                if (list[j] == i)
                {
                    hasValue = true;
                    break;
                }
            }
            if (!hasValue)
            {
                return i;
            }
        }
        return 0;
    }
}
