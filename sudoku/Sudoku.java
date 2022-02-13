package sudokusolver.sudoku;

public class Sudoku
{
    private Field field;
    private boolean solved;

    public boolean isSolved()
    {
        return solved;
    }

    public Field getField()
    {
        return field;
    }

    public void setField(Field field)
    {
        this.field = field;
    }

    public void printSudoku()
    {
        int[] contents = field.getContents();
        for (int y = 0; y < 9; y++)
        {
            for (int x = 0; x < 9; x++)
            {
                try
                {
                    int n = Field.getIndexFromPoint(x, y);
                    if (contents[n] == 0)
                    {
                        System.out.print(' ');
                    }
                    else
                    {
                        System.out.print(contents[n]);
                    }
                }
                catch (OutOfRangeException e)
                {
                    System.out.println("Internal Exception");
                    e.printStackTrace();
                    return;
                }
                if ((x + 1) % 3 == 0 && x != 8)
                {
                    System.out.print('|');
                }
            }
            System.out.println();
            if ((y + 1) % 3 == 0 && y != 8)
            {
                System.out.println("---+---+---");
            }
        }
    }

    Sudoku()
    {
        field = new Field();
    }
    
    Sudoku(Field field)
    {
        this.field = field;
    }

    public boolean solve()
    {

        for (int y = 0; y < 3; y++)
        {
            for (int x = 0; x < 3; x++)
            {
                int[][] possibleNumbersInBlock = new int[9][];
                Point block_base = new Point(x, y);
                int n = 0;
                for (int j = 0; j < 3; j++)
                {
                    for (int i = 0; i < 3; i++)
                    {
                        try
                        {
                            Point point = new Point(block_base.x * 3 + i, block_base.y * 3 + j);
                            int[] possibleNumbers;
                            if (field.isOccupied(point))
                            {
                                possibleNumbers = new int[] {};
                            }
                            else
                            {
                                possibleNumbers = field.getPossibleNumbersAt(point);
                            }
                            possibleNumbersInBlock[n] = possibleNumbers;
                        }
                        catch (OutOfRangeException e)
                        {
                            System.out.println("Internal Exception");
                            e.printStackTrace();
                            return false;
                        }
                        finally
                        {
                            n++;
                        }
                    }
                }

                // Happens for each block; Point block_base is coordinates of top-left cell in block
                for (int i = 1; i <= 9; i++)
                {
                    int occurrence_index = -1;
                    for (int j = 0; j < 9; j++)
                    {
                        if (Field.listContains(possibleNumbersInBlock[j], i))
                        {
                            if (occurrence_index != -1)
                            {
                                if (occurrence_index >= 0)
                                {
                                    occurrence_index = -1;
                                }
                                occurrence_index--;
                                continue;
                            }
                            occurrence_index = j;
                        }
                    }
                    if (occurrence_index >= 0)
                    {
                        try
                        {
                            Point point = new Point(block_base.x * 3 + occurrence_index % 3, block_base.y * 3 + occurrence_index / 3);
                            
                            if (field.verifyNumberAt(Field.getIndexFromPoint(point), i))
                            {
                                System.out.println("Set a number at " + point + " to " + i);
                                field.setNumberAt(point, i);
                            }
                            else
                            {
                                System.out.format("Something went wrong:\n\tTried putting %d in (%d, %d),\n\tbut number is illegal to put down.\n", i, point.x, point.y);
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

                int[] block;
                try
                {
                    block = field.getBlock(x, y);
                }
                catch (OutOfRangeException e)
                {
                    System.out.println("Internal Exception");
                    e.printStackTrace();
                    return false;
                }
                int missing = Field.findMissing(block);
                if (missing > 0)
                {
                    boolean set = false;
                    for (int i = 0; i < 3; i++)
                    {
                        if (set)
                        {
                            continue;
                        }
                        for (int j = 0; j < 3; j++)
                        {
                            Point point = new Point(block_base.x * 3 + i, block_base.y * 3 + j);
                            try
                            {
                                if (field.getNumberAt(point) == 0)
                                {
                                    System.out.println("Set a number at " + point + " to " + missing);
                                    
                                    if (field.verifyNumberAt(Field.getIndexFromPoint(point), missing))
                                    {
                                        field.setNumberAt(point, missing);
                                        set = true;
                                    }
                                    break;
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
                }
            }
        }
        if (!Field.listContains(field.getContents(), 0))
        {
            solved = true;
        }
        return solved;
    }
}
