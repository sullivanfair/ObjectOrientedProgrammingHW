package hw3;

import java.util.ArrayList;
import api.GridCell;
import api.Line;
import api.Location;
import api.StringUtil;

/**
 * Game state for a Lines game.
 */
public class LinesGame
{
	/**
	 * Holds the current state of the game grid
	 */
	private GridCell[][] currentGrid;
	
	/**
	 * Holds the current state of the game lines
	 */
	private ArrayList<Line> lines;
	
	/**
	 * Holds the state of the current line
	 */
	private Line currentLine;
	
	/**
	 * Holds the value of a temporary line for the method isComplete()
	 */
	private Line tempLine;
	
	/**
	 * Stores the id of a given line
	 */
	private int idLine;
	
	/**
	 * Stores the value of the current location in a line
	 */
	private Location currentLocation;
	
	/**
	 * Stores a temporary location value for the second condition in startLine()
	 * and isAdjacent()
	 */
	private Location tempLoc;
	
	/**
	 * Stores value of current row for isAdjacent()
	 */
	private int rOld;
	
	/**
	 * Stores value of current col for isAdjacent()
	 */
	private int cOld;
	
	/**
	 * Stores row value of cell to be added for isAdjacent()
	 */
	private int rNew;
	
	/**
	 * Stores column value of cell to be added for isAdjacent()
	 */
	private int cNew;
	
	/**
	 * Stores the value of the current row location in a line
	 */
	private int currentRow;
	
	/**
	 * Stores the value of the current col location in a line
	 */
	private int currentCol;
	
	/**
	 * Stores the amount of moves taken by the player
	 */
	private int totalMoves;
	
	/**
	 * Stores list of cells in a line for the method startLine()
	 */
	private ArrayList<Location> cells;
	
  
  /**
   * Constructs a LinesGame from the given grid and Line list.
   * This constructor does not do any error-checking to ensure
   * that the grid and the Line array are consistent. Initially
   * the current line is null.
   * @param givenGrid
   *   a 2d array of GridCell
   * @param givenLines
   *   list of Line objects
   */
  public LinesGame(GridCell[][] givenGrid, ArrayList<Line> givenLines)
  {
	currentGrid = givenGrid;
    lines = givenLines;
    currentLine = null;
    tempLine = null;
    idLine = -1;
    currentLocation = null;
    tempLoc = null;
    rOld = 0;
    cOld = 0;
    rNew = 0;
    cNew = 0;
    currentRow = 0;
    currentCol = 0;
    totalMoves = 0;
    cells = null;
  }
  
  /**
   * Constructs a LinesGame from the given descriptor. Initially the
   * current line is null.
   * @param descriptor
   *   array of strings representing initial state
   */
  public LinesGame(String[] descriptor)
  {
    currentGrid = StringUtil.createGridFromStringArray(descriptor);
    lines = Util.createLinesFromGrid(currentGrid);
    currentLine = null;
    tempLine = null;
    idLine = -1;
    currentLocation = null;
    tempLoc = null;
    rOld = 0;
    cOld = 0;
    rNew = 0;
    cNew = 0;
    currentRow = 0;
    currentCol = 0;
    totalMoves = 0;
    cells = null;
  }
  
  /**
   * Returns the number of columns for this game.
   * @return
   *  width for this game
   */ 
  public int getWidth()
  {
    return currentGrid[0].length;
  }
  
  /**
   * Returns the number of rows for this game.
   * @return
   *   height for this game
   */ 
  public int getHeight()
  {
    return currentGrid.length;
  }
  
  /**
   * Returns the current cell for this game, possibly null.
   * The current cell is just the last location, if any, 
   * in the current line, if there is one. Returns null
   * if the current line is null or if the current line
   * has an empty list of locations.
   * @return
   *   current cell for this game, or null
   *   
   */
  public Location getCurrentLocation()
  {
    return currentLine.getLast();
  }
  
  /**
   * Returns the id for the current line, or -1
   * if the current line is null.
   * @return
   *   id for the current line
   */
  public int getCurrentId()
  {
	  if(currentLine == null)
	  {
		  return -1;
	  }
	  return currentLine.getId();
  }
  
  /**
   * Return this game's current line (which may be null).
   * @return
   *   current line for this game
   */
  public Line getCurrentLine()
  {
    return currentLine;
  }
  
  /**
   * Returns a reference to this game's grid.  Clients should
   * not modify the array.
   * @return
   *   the game grid
   */
  public GridCell[][] getGrid()
  {
    return currentGrid;
  }
  
  /**
   * Returns the grid cell at the given position.
   * @param row
   *   given row
   * @param col
   *   given column
   * @return
   *   grid cell at (row, col)
   */
  public GridCell getCell(int row, int col)
  {
    return currentGrid[row][col];
  }
  
  /**
   * Returns all Lines for this game.  Clients should not modify
   * the returned list or the Line objects.
   * @return
   *   list of lines for this game
   */ 
  public ArrayList<Line> getAllLines()
  {
    return lines;
  }
  
  /**
   * Returns the total number of moves.  A "move" means that a 
   * new Location was successfully added to the current line
   * in addCell.
   * @return
   *   total number of moves so far in this game
   */
  public int getMoveCount()
  {
    return totalMoves;
  }
  
  /**
   * Returns true if all lines are connected and all
   * cells are at their maximum count.
   * @return
   *   true if all lines are complete and all cells are at max
   */ 
  public boolean isComplete()
  {
    for(int i = 0; i < lines.size(); i++)
    {
    	tempLine = lines.get(i);
    	
    	if(!tempLine.isConnected())
    	{
    		return false;
    	}
    }
    
    for(int col = 0; col < currentGrid[0].length; col++)
    {
    	for(int row = 0; row < currentGrid.length; row++)
    	{
    		if(!currentGrid[row][col].maxedOut())
    		{
    			return false;
    		}
    	}
    }
    
    return true;
  }
  
  /**
   * Attempts to set the current line based on the given
   * row and column.  When using a GUI, this method is typically 
   * invoked when the mouse is pressed. If the current line is 
   * already non-null, this method does nothing.
   * There are two possibilities:
   * <ul>
   *   <li>Any endpoint can be selected.  Selecting an 
   *   endpoint clears the line associated with that endpoint's id,
   *   and all cells that were previously included in the line are decremented.
   *   The line then becomes the current line, and the endpoint is incremented
   *   and placed on the line's list of locations as its only element.
   *   <li>A non-endpoint cell can be selected if it is not a crossing
   *   and if it is the last cell in some line.  That line then becomes
   *   the current line.
   * </ul>
   * If neither of the above conditions is met, or if the
   * current line is non-null, this method does nothing.
   * 
   * @param row
   *   given row
   * @param col
   *   given column
   */
  public void startLine(int row, int col)
  {
	 idLine = currentGrid[row][col].getId();
	 
	 if(currentGrid[row][col].isEndpoint())
	 {
		for(int i = 0; i < lines.size(); i++)
		{
			currentLine = lines.get(i);
				 
			if(currentLine.getId() == idLine)
			{
				cells = currentLine.getCells();
					
				for(int j = 0; j < cells.size(); j++)
				{
					currentLocation = cells.get(j);
					currentRow = currentLocation.row();
					currentCol = currentLocation.col();
					currentGrid[currentRow][currentCol].decrement();
				}
					 
				currentLine.clear();
				currentLine.add(new Location(row, col));
				currentGrid[row][col].increment();
				break;
			}
		}
	 }
	 else if(!currentGrid[row][col].isCrossing())
	 { 
		 for (Line line : lines) 
		 {
			 tempLine = line;
			 currentLocation = tempLine.getLast();
			 tempLoc = new Location(row, col);
			 
			 if(currentLocation != null && currentLocation.equals(tempLoc))
			 {
				 currentLine = tempLine;
				 break;
			 }
		 }
	 }
  }
  
  /**
   * Sets the current line to null. When using a GUI, this method is 
   * typically invoked when the mouse is released.
   */
  public void endLine()
  {
    currentLine = null;
  }
  
  /**
   * Attempts to add a new cell to the current line.  
   * When using a GUI, this method is typically invoked when the mouse is 
   * dragged.  In order to add a cell, the following conditions must be satisfied.
   * Here the "current cell" is the last cell in the current line, and "new cell"
   * is the cell at the given row and column:
   * :
   * <ol>
   *   <li>The current line is non-null
   *   <li>The current line is not connected
   *   <li>The given row and column are adjacent to the location of the current cell
   *       (horizontally, vertically, or diagonally) and not the same as the current cell
   *   <li>The count for the new cell is less than its max count
   *   <li>If the new cell is a MIDDLE or ENDPOINT, then its id matches
   *   the id for the current line
   *   <li>Adding the new cell will not cause the line to re-trace any
   *   existing line (according to the result of Util.checkForLineSegment)
   *   <li>Adding the new cell to the line would not cross any existing line
   *   (according to the result of Util.checkForPotentialCrossing)
   * </ol>
   * If the above conditions are met, a new Location at (row, col) is added
   * to the current line and the cell count is incremented.  Otherwise, the 
   * method does nothing.  If a new location
   * is added to the current line, the move counter is increased by 1.
   * @param row
   *   given row for the new cell
   * @param col
   *   given column for the new cell
   */
  public void addCell(int row, int col)
  {
	if(currentLine != null)
	{
		if(!currentLine.isConnected())
		{
			if(isAdjacent(new Location(row, col)))
			{
				if(!currentGrid[row][col].maxedOut())
				{
					if(!Util.checkForLineSegment(lines, currentLine.getLast(), new Location(row, col)))
					{
						if(!Util.checkForPotentialCrossing(lines, currentLine.getLast(), new Location(row, col)))
						{
							if(currentGrid[row][col].isMiddle() || currentGrid[row][col].isEndpoint())
							{
								if(currentLine.getId() == currentGrid[row][col].getId())
								{
									currentLine.add(new Location(row, col));
									currentGrid[row][col].increment();
									totalMoves += 1;
								}
								
								return;
							}
							
							currentLine.add(new Location(row, col));
							currentGrid[row][col].increment();
							totalMoves += 1;
						}
					}
				}
			}
		}
	}	
  }
  
  /**
   * Used to determine whether given cell is adjacent to the current cell
   * @param cell
   * 	Cell to be added to current line
   * @return
   * 	Whether given cell is adjacent to the current cell
   */
  private boolean isAdjacent(Location cell)
  {
	  tempLoc = currentLine.getLast();
	  rOld = tempLoc.row();
	  cOld = tempLoc.col();
	  rNew = cell.row();
	  cNew = cell.col();
	  
	  if((Math.abs(rOld - rNew) + Math.abs(cOld - cNew) == 1)
			  || Math.abs(rOld - rNew) + Math.abs(cOld - cNew) == 2)
	  {
		  if(Math.sqrt((Math.pow(rNew - rOld, 2) + Math.pow(cNew - cOld, 2))) != 2)
		  {
			  return true;
		  }
	  }
	  
	  return false;
  }

  /**
   * Returns a string representation of this game.
   */
  public String toString()
  {
    String result = "";
    result += "-----\n";
    result += StringUtil.originalGridToString(getGrid());
    result += "-----\n";
    result += StringUtil.currentGridToString(getGrid(), getAllLines());
    result += "-----\n";
    result += StringUtil.allLinesToString(getAllLines());
    Line ln = getCurrentLine();
    if (ln != null)
    {
      result += "Current line: " + ln.getId() + "\n";
    }
    else
    {
      result += "Current line: null\n";
    }
    return result;
  }

}
