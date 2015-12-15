package PrimeFactorAttack;


public class Grid
{
  public static final int GRID_PIXELS = 50;
  public static final boolean EMPTY = false;
  public static final boolean FILLED = true;
  
  private int columnCount, rowCount;
  private int highestRow = rowCount-1;
  private int lastHighestRow = rowCount-1;
  
  private boolean[][] grid;
  
  
  public Grid(int totalPixelX, int totalPixelY)
  { 
    
    columnCount = totalPixelX/GRID_PIXELS;
    rowCount = totalPixelY/GRID_PIXELS;
    
    System.out.println("Grid("+totalPixelX + ", " + totalPixelY +
        ") Constructor: rows="+rowCount+", columns="+columnCount);
    
    if (totalPixelX != columnCount*GRID_PIXELS)
    { throw new IllegalArgumentException("Grid() bad totalPixelX="+totalPixelX);
    }
    if (totalPixelY != rowCount*GRID_PIXELS)
    { throw new IllegalArgumentException("Grid() bad totalPixelY="+totalPixelY);
    }
    
    grid = new boolean[columnCount][rowCount];
    
    
  }
  
  public void clear()
  { highestRow=rowCount-1;
    System.out.println("Grid.clear()");
    for (int x=0; x<columnCount; x++)
    { for (int y=0; y<rowCount; y++)
      { grid[x][y] = EMPTY;
      }
    }
  }
  
  public int getRowCount()
  { return rowCount;
  }
  
  public int getColumnCount()
  { return columnCount;
  }
  
  public int getPixelLeft(int column)
  { return GRID_PIXELS*column;
  }

  public boolean isEmpty(int x, int y)
  { if (grid[x][y] == EMPTY) return true; 
    return false;
  }
  
  public void setFilled(int x, int y)
  { grid[x][y] = FILLED;
    if (y< highestRow)
    {
      lastHighestRow = highestRow;
      highestRow = y;
    }
  }
  public void setEmpty(int x, int y)
  {
    grid[x][y] = EMPTY;
  }
  public void resetHighestRow()
  {
    highestRow = rowCount - 1;
  }
  public void revertToLastHighest()
  {
    highestRow = lastHighestRow;
  }
  public int getHighestRow()
  {
    return highestRow;
  }
  
  /*
  public int getGap(int colWidth)
  { 
    int largestGap = 0;
    int largestLeft = 0;
    int largestRight = 0;
    
    int gapLeft = 0;
    int gapRight = 0;
    while(gapLeft < columnCount)
    {
      
      while(grid[gapLeft][highestRow] == FILLED) 
      { gapLeft++;
        if (gapLeft == columnCount) break;
      }
      
      gapRight = gapLeft+1;
      while((gapRight<columnCount) && (grid[gapRight][highestRow] == EMPTY)) gapRight++;
      
      if (gapRight - gapLeft > largestGap)
      { largestGap = gapRight - gapLeft;
        largestLeft = gapLeft;
        largestRight = gapRight;
      }
      
      gapLeft = gapRight;
    
    }
    
    if (largestGap == 0) return -1;
    
    int colLeft = 0;
    if (largestGap<=colWidth)
    {
      int mid = largestLeft+(largestRight-largestLeft)/2;
      colLeft = mid - colWidth/2;
    }
    else
    { colLeft = largestLeft+Game.rand.nextInt(largestGap-colWidth);
    }
    if (colLeft < 0) colLeft = 0;
    else if (colLeft+colWidth > columnCount)
    { colLeft = columnCount - colWidth;
    }
    //System.out.println("Grid.getGap("+colWidth+"): highestRow="+highestRow+ ", Left="+largestLeft+
    //    ", Right="+largestRight+
    //    ", colLeft="+colLeft);
    return colLeft;
    
  }
  */
  /*
  public int getDepth(int col)
  { if (col<0) return -1;
    if (col>=columnCount) return -1;
    int depth = 0;
    while((depth < rowCount) && (grid[col][depth] != FILLED))
    { depth++;
    }
    return depth;
  }
  */
  /*
  public int getTroughCenter()
  { 
    
    int deepestDepth = 0;
    int deepestLeft = 0;
    int deepestRight = columnCount-1;
    boolean inTrough = false;
    boolean inSecondTrough = false;
    
    int col = 0;
    while(col < columnCount)
    {
      int depth = getDepth(col);
      if (depth>deepestDepth)
      { deepestDepth = depth;
        deepestLeft = col;
        deepestRight = col;
        inTrough = true;
      }
      else if (depth==deepestDepth)
      { if (inTrough)
        { deepestRight = col;
        }
        else if (!inSecondTrough)
        { if (Game.rand.nextBoolean())
          { deepestLeft = col;
            deepestRight = col;
            inTrough = true;
          }
          else
          { inSecondTrough = true;
          }
        }
      }
      else
      { inTrough = false;
        inSecondTrough = false;
      }
      col++;
    }
    
    int center = (deepestLeft + deepestRight)/2;
    //System.out.println("Grid.getTroughCenter() = "+center);
    
    return center;
    
  }
  */

  public int pixelToRow(int y)
  {
    if(y<Grid.GRID_PIXELS) y = 0;

    int row = y/Grid.GRID_PIXELS;
    return row;
  }
  
  public int getPixelTop(int row)
  { 
    return row*GRID_PIXELS;
  }
  
  

  
}
