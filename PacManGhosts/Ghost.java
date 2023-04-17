/**
 * @author Sullivan Fair
 */
package hw4;

import static api.Direction.DOWN;
import static api.Direction.LEFT;
import static api.Direction.RIGHT;
import static api.Direction.UP;

import api.Descriptor;
import api.Direction;
import api.Location;
import api.MazeMap;
import api.Mode;
import static api.Mode.*;

import java.util.Random;

public abstract class Ghost extends PacmanAndFriends
{
	//Variables
	/**
	 * The cell above the current location
	 */
	private Location nextCellUp;
	
	/**
	 * The cell to the left of the current location
	 */
	private Location nextCellLeft;
	
	/**
	 * The cell to the below the current location
	 */
	private Location nextCellDown;
	
	/**
	 * The cell to the right of the current location
	 */
	private Location nextCellRight;
	
	/**
	 * Distance between nextCellUp and the target cell
	 */
	private double distUpToTarg;
	
	/**
	 * Distance between nextCellLeft and the target cell
	 */
	private double distLeftToTarg;
	
	/**
	 * Distance between nextCellDown and the target cell
	 */
	private double distDownToTarg;
	
	/**
	 * Distance between nextCellRight and the target cell
	 */
	private double distRightToTarg;
	
	/**
	 * Stores the value of the "next" cell 
	 * that is the shortest distance of the target cell
	 */
	private double distShort;
	
	/**
	 * Target of the ghost
	 */
	private Location targetCell;
	
	/**
	 * Scatter location of the ghosts
	 */
	private Location scatterTarget;
	
	/**
	 * The next cell to be reached by the ghost
	 */
	private Location nextCell;
	
	/**
	 * The next direction the ghost will be in
	 */
	private Direction nextDir;
	
	/**
	 * Distance to the center of the current cell
	 */
	private double distCenter;
	
	/**
	 * Current mode of the game
	 */
	private Mode currentMode;
	
	/**
	 * Used as placeholder for the current speed in update()
	 */
	private double increment;
	
	/**
	 * Exact row of the current location
	 */
	private double curRowExact;
	
	/**
	 * Exact col of the current location
	 */
	private double curColExact;
	
	/**
	 * int value of the current row
	 */
	private int rowNum;
	
	/**
	 * int value of the current col
	 */
	private int colNum;
	
	/**
	 * Array of doubles that store the potential distShort among the "next" cells
	 */
	private double[] distances;
	
	/**
	 * Temporary placeholder for values in distances[], 
	 * assists in determining distShort
	 */
	private double distance;
	
	/**
	 * Current location of the player
	 */
	private Location playerLoc;
	
	/**
	 * Used to store Random rand from the constructor
	 */
	private Random rand;
	
	/**
	 * Random number from rand to determine 
	 * the next cell while in mode "FRIGHTENED"
	 */
	private int randNum;
	
	/**
	 * Universal constructor for ghost object 
	 * @param maze
	 * 		Maze configuration
	 * @param home
	 * 		Initial location
	 * @param baseSpeed
	 * 		Base speed increment
	 * @param homeDirection
	 * 		Initial direction
	 * @param scatterTarget
	 * 		Scatter location for ghost
	 * @param rand
	 * 		Pseudorandom generator used in frightened mode 
	 */
	public Ghost(MazeMap maze, Location home, double baseSpeed, 
			Direction homeDirection, Location scatterTarget, Random rand)
	{
		setMaze(maze);
		setHome(home);
		setBaseIncrement(baseSpeed);
		setHomeDirection(homeDirection);
		this.scatterTarget = scatterTarget;
		this.rand = rand;
		distances = new double[4];
		currentMode = INACTIVE;
	}
	
	//Get Methods
	/**
	 * 
	 * @return
	 * 		int value of current location row
	 */
	protected int getCurLocRow()
	{
		return (int) getCurrentLocation().row();
	}
	
	/**
	 * 
	 * @return
	 * 		int value of current location col
	 */
	protected int getCurLocCol()
	{
		return (int) getCurrentLocation().col();
	}
	
	/**
	 * 
	 * @return
	 * 		Location of the ghost's scatter target
	 */
	protected Location getScatterTarget()
	{
		return scatterTarget;
	}
	
	/**
	 * 
	 * @return
	 * 		Next cell location
	 */
	public Location getNextCell()
	{
		return nextCell;
	}
	
	@Override
	public Mode getMode()
	{
		return currentMode;
	}
		
	//Set Methods
	/**
	 * Sets the location of the ghost's target cell
	 * @param l
	 * 		Given location
	 */
	protected void setTargetCell(Location l)
	{
		targetCell = l;
	}
	
	//Methods
	@Override
	public Location getCurrentLocation()
	{
		if(getRowExact() == 0 || getColExact() == 0)
		{
			return getHomeLocation();
		}
		
		return new Location((int) getRowExact(), (int) getColExact());
	}
	
	@Override
	public void reset()
	{
	    setRowExact(getHomeLocation().row() + 0.5);
	    setColExact(getHomeLocation().col() + 0.5);
	    setDirection(getHomeDirection());
	    setCurrentIncrement(getBaseIncrement());
	}
	
	@Override
	public void setMode(Mode mode, Descriptor desc)
	{
		currentMode = mode;
		
		if(currentMode == FRIGHTENED)
		{
			setCurrentIncrement((getBaseIncrement() * (2.0 / 3.0)));
		}
		else if(currentMode == DEAD)
		{
			setCurrentIncrement((getBaseIncrement() * 2));
		}
		else
		{
			setCurrentIncrement(getBaseIncrement());
		}
			
		calculateNextCell(desc);
	}
	
	@Override
	public void update(Descriptor desc)
	{
		 increment = getCurrentIncrement();
		 curRowExact = getRowExact();
		 curColExact = getColExact();
		 rowNum = (int) curRowExact;
		 colNum = (int) curColExact; 
		 distCenter = distanceToCenter();
		 distShort = 0;
		 
		
		 if (getCurrentDirection() == null)
		 {
		    return;
		 }
		 else if(currentMode == INACTIVE)
		 {
			 return;
		 }
		 
		 switch(getCurrentDirection())
		 {
		 	case LEFT:
		 	{	
		 		if (curColExact - increment - 0.5 < 0)
		        {
		 			curColExact = getMaze().getNumColumns() + (curColExact - increment - 0.5);
		        }
		        else
		        {
		          if (distCenter > -getERR() && distCenter < increment && getMaze().isWall(rowNum, colNum - 1)
		        		  || nextDir != LEFT)
		          {
		        	  increment = distCenter;
		          }
		          
		          curColExact -= increment;
		          
		          if(curColExact < (int) getColExact())
		 	      {
		        	  setColExact(curColExact);
		        	  calculateNextCell(desc);
		 	      }	
		          
		          if(curRowExact == ((int) getRowExact() + 0.5) && curColExact == ((int) getColExact() + 0.5))
		          {
			 			setDirection(nextDir);
		          }
		        }
		 		
		 		break;
		 	}
		 	
		 	case RIGHT:
		 	{		 		
		 		if (curColExact + increment + 0.5 >= getMaze().getNumColumns())
		        {
		          curColExact = curColExact + increment + 0.5 - getMaze().getNumColumns();
		        }
		 		else
		 		{
		 			if (distCenter > -getERR() && distCenter < increment && getMaze().isWall(rowNum, colNum + 1)
		 					|| nextDir != RIGHT)
		 	        {
		 				increment = distCenter;
		 	        }
		 			
		 	        curColExact += increment;
		 	          
		 	        if(curColExact > ((int) getColExact() + 1))
		 	        {
		 	        	setColExact(curColExact);
		 	        	calculateNextCell(desc);
		 	        }
		 	        
		 	       if(curRowExact == ((int) getRowExact() + 0.5) && curColExact == ((int) getColExact() + 0.5))
		 	       {
			 			setDirection(nextDir);
		 	       }
		 		}
		 		
		 		break;
		 	}

		 	case UP:
		 	{	
		 		if ((distCenter > -getERR() && distCenter < increment && getMaze().isWall(rowNum - 1, colNum))
		 				|| nextDir != UP)
		        {
		 			if(increment > distCenter)
		 			{
		 				increment = distCenter;
		 			}	
		        }
		 		
		 		curRowExact -= increment;
		 		
		 		if(curRowExact < (int) getRowExact())
		 		{
		 			setRowExact(curRowExact);
		 			calculateNextCell(desc);
		 		}
		 		
		 		if(curRowExact == ((int) getRowExact() + 0.5) && curColExact == ((int) getColExact() + 0.5))
		 		{
		 			setDirection(nextDir);
		 		}
		 		
			 	break;
		 	}

		 	case DOWN:
		 	{		 		
		 		 if (distCenter > -getERR() && distCenter < increment && getMaze().isWall(rowNum + 1, colNum)
		 				 || nextDir != DOWN)
		         {
		 			increment = distCenter;
		         }
		 		 
		         curRowExact += increment;
		         
		        if(curRowExact > ((int) getRowExact() + 1))
			 	{
		        	setRowExact(curRowExact);
			 		calculateNextCell(desc);
			 	}
		        
		        if(curRowExact == ((int) getRowExact() + 0.5) && curColExact == ((int) getColExact() + 0.5))
		 		{
		 			setDirection(nextDir);
		 		}
		         
		        break;
		 	}
		 }
		 
		 setRowExact(curRowExact);
		 setColExact(curColExact);
	}
	
	/**
	 * Determines the next cell to be traveled to by the ghost 	
	 * @param desc
	 * 		Given descriptor
	 */
	public void calculateNextCell(Descriptor desc)
	{
		nextCellUp = new Location(getCurLocRow() - 1, getCurLocCol());
		
		if(getCurLocCol() == 0)
		{
			nextCellLeft = new Location(getCurLocRow(), getMaze().getNumColumns() - 2);
		}
		else
		{
			nextCellLeft = new Location(getCurLocRow(), getCurLocCol() - 1);
		}
		
		nextCellDown = new Location(getCurLocRow() + 1, getCurLocCol());
		
		if(getCurLocCol() == getMaze().getNumColumns() - 1)
		{
			nextCellRight = new Location(getCurLocRow(), 1);
		}
		else
		{
			nextCellRight = new Location(getCurLocRow(), getCurLocCol() + 1);
		}
		
		if(currentMode == INACTIVE)
		{
			return;
		}
		else if(currentMode == SCATTER)
		{
			calcScatter(desc);
		}
		else if(currentMode == CHASE)
		{
			calcChase(desc);
		}
		else if(currentMode == DEAD)
		{	
			calcDead(desc);
		}
		else
		{
			calcFright(desc);
		}
	}

	/**
	 * Helper method used to determine the next cell while in SCATTER mode
	 * @param desc
	 * 		Given descriptor
	 */
	private void calcScatter(Descriptor desc) 
	{	
		resetDistances();
		
		if(getCurrentDirection() != DOWN && !getMaze().isWall(nextCellUp.row(), nextCellUp.col()))
		{
			distUpToTarg = calculateDistance(nextCellUp, scatterTarget);
			distances[0] = distUpToTarg;
		}
		if(getCurrentDirection() != RIGHT && !getMaze().isWall(nextCellLeft.row(), nextCellLeft.col()))
		{
			distLeftToTarg = calculateDistance(nextCellLeft, scatterTarget);
			distances[1] = distLeftToTarg;
		}
		if(getCurrentDirection() != UP && !getMaze().isWall(nextCellDown.row(), nextCellDown.col()))
		{
			distDownToTarg = calculateDistance(nextCellDown, scatterTarget);
			distances[2] = distDownToTarg;
		}
		if(getCurrentDirection() != LEFT && !getMaze().isWall(nextCellRight.row(), nextCellRight.col()))
		{
			distRightToTarg = calculateDistance(nextCellRight, scatterTarget);
			distances[3] = distRightToTarg;			
		}
		
		calcCell(scatterTarget);
	}
	
	
	/**
	 * Helper method used to determine the next cell while in CHASE mode
	 * @param desc
	 * 		Given descriptor
	 */
	private void calcChase(Descriptor desc)
	{
		resetDistances();
		
		calcTargetCell(desc);
		
		if(getCurrentDirection() != DOWN && !getMaze().isWall(nextCellUp.row(), nextCellUp.col()))
		{
			distUpToTarg = calculateDistance(nextCellUp, targetCell);
			distances[0] = distUpToTarg;
		}
		if(getCurrentDirection() != RIGHT && !getMaze().isWall(nextCellLeft.row(), nextCellLeft.col()))
		{
			distLeftToTarg =calculateDistance(nextCellLeft, targetCell);
			distances[1] = distLeftToTarg;
		}
		if(getCurrentDirection() != UP && !getMaze().isWall(nextCellDown.row(), nextCellDown.col()))
		{
			distDownToTarg =calculateDistance(nextCellDown, targetCell);
			distances[2] = distDownToTarg;
		}
		if(getCurrentDirection() != LEFT && !getMaze().isWall(nextCellRight.row(), nextCellRight.col()))
		{
			distRightToTarg =calculateDistance(nextCellRight, targetCell);
			distances[3] = distRightToTarg;
		}
		
		calcCell(targetCell);
	}
	
	
	/**
	 * Helper method used to determine the next cell while in DEAD mode
	 * @param desc
	 * 		Given descriptor
	 */		
	private void calcDead(Descriptor desc)
	{
		resetDistances();
		
		if(getCurrentDirection() != DOWN && !getMaze().isWall(nextCellUp.row(), nextCellUp.col()))
		{
			if(calcCheck())
			{
				return;
			}
			distUpToTarg = calculateDistance(nextCellUp, getHomeLocation());
			distances[0] = distUpToTarg;
		}
		if(getCurrentDirection() != RIGHT && !getMaze().isWall(nextCellLeft.row(), nextCellLeft.col()))
		{
			if(calcCheck())
			{
				return;
			}
			distLeftToTarg = calculateDistance(nextCellLeft, getHomeLocation());
			distances[1] = distLeftToTarg;
		}
		if(getCurrentDirection() != UP && !getMaze().isWall(nextCellDown.row(), nextCellDown.col()))
		{
			if(calcCheck())
			{
				return;
			}
			distDownToTarg =calculateDistance(nextCellDown, getHomeLocation());
			distances[2] = distDownToTarg;
		}
		if(getCurrentDirection() != LEFT && !getMaze().isWall(nextCellRight.row(), nextCellRight.col()))
		{
			if(calcCheck())
			{
				return;
			}
			distRightToTarg = calculateDistance(nextCellRight, getHomeLocation());
			distances[3] = distRightToTarg;
		}
		
		calcCell(playerLoc);
	}
	
	
	/**
	 * Helper method used to determine the next cell while in FRIGHTENED mode
	 * @param desc
	 * 		Given descriptor
	 */
	private void calcFright(Descriptor desc)
	{
		for(int i = 0; i < 4; i++)
		{
			randNum = rand.nextInt(4);
			
			if(randNum == 0 && !getMaze().isWall(nextCellUp.row(), nextCellUp.col()))
			{
				if(calcCheck())
				{
					return;
				}
				nextCell = nextCellUp;
				nextDir = UP;
			}
			else if(randNum == 1 && !getMaze().isWall(nextCellLeft.row(), nextCellLeft.col()))
			{
				if(calcCheck())
				{
					return;
				}
				nextCell = nextCellLeft;
				nextDir = LEFT;
			}
			else if(randNum == 2 && !getMaze().isWall(nextCellDown.row(), nextCellDown.col()))
			{
				if(calcCheck())
				{
					return;
				}
				nextCell = nextCellDown;
				nextDir = DOWN;
			
				break;
			}
			else if(randNum == 3 && !getMaze().isWall(nextCellRight.row(), nextCellRight.col()))
			{
				if(calcCheck())
				{
					return;
				}
				nextCell = nextCellRight;
				nextDir = RIGHT;	
			}
		}
	}
	
	
	/**
	 * Determines the target cell to be traveled to by the ghost
	 * @param desc
	 * 		Given descriptor
	 */
	protected abstract void calcTargetCell(Descriptor desc);
	
	/**
	 * Determines if the ghost is in the center of the "non" nextcell and
	 * is there allowed to calculateNextCell()
	 * @return
	 * 		Boolean that determines if ghost can calculateNextCell()
	 */
	private boolean calcCheck()
	{
		if(getRowExact() != ((int) getRowExact() + 0.5) && !nextCell.equals(getCurrentLocation()))
		{
			return true;
		}
		return false;
	}
	
	
	/**
	 * Helper method used to calculate the next cell based
	 * on the target cell
	 * @param target
	 * 		Target cell of the ghost
	 */
	private void calcCell(Location target)
	{	
		for(int i = 0; i < 4; i++)
		{
			distance = distances[i];
			
			if(distShort == 0 && distance != 0)
			{
				distShort = distance;
			}
			else if(distance != 0 && distance < distShort)
			{
				distShort = distance;
			}
		}
		
		//Check to see if next equals the target cell, safeguards against distance
		//being equal to zero
		if(nextCellUp.equals(target))
		{
			if(calcCheck())
			{
				return;
			}
			nextCell = nextCellUp;
			nextDir = UP;
			return;
		}
		else if(nextCellLeft.equals(target))
		{
			if(calcCheck())
			{
				return;
			}
			nextCell = nextCellLeft;
			nextDir = LEFT;
			return;
		}
		else if(nextCellDown.equals(target))
		{
			if(calcCheck())
			{
				return;
			}
			nextCell = nextCellDown;
			nextDir = DOWN;
			return;
		}
		else if(nextCellRight.equals(target))
		{
			if(calcCheck())
			{
				return;
			}
			nextCell = nextCellRight;
			nextDir = RIGHT;
			return;
		}
		
		//Find next cell as normal
		if(Math.abs(distUpToTarg - distShort) < getERR())
		{
			if(calcCheck())
			{
				return;
			}
			nextCell = nextCellUp;
			nextDir = UP;
		}
		else if(Math.abs(distLeftToTarg - distShort) < getERR())
		{
			if(calcCheck())
			{
				return;
			}
			nextCell = nextCellLeft;
			nextDir = LEFT;
		}
		else if(Math.abs(distDownToTarg - distShort) < getERR())
		{
			if(calcCheck())
			{
				return;
			}
			nextCell = nextCellDown;
			nextDir = DOWN;
		}
		else if(Math.abs(distRightToTarg - distShort) < getERR())
		{
			if(calcCheck())
			{
				return;
			}
			nextCell = nextCellRight;
			nextDir = RIGHT;
		}
	}	

	/**
	 * Resets the distances[]
	 */
	private void resetDistances()
	{
		for(int i = 0; i < 4; i++)
		{
			distances[i] = 0;
		}
	}	
	
	/**
	 * Calculates the distance between two location
	 * @param loc1
	 * 		Given location
	 * @param loc2
	 * 		Given location
	 * @return
	 * 		Distance between loc1 and loc2
	 */
	protected double calculateDistance(Location loc1, Location loc2)
	{
		return Math.sqrt((Math.pow((loc1.row() - loc2.row()), 2)) + (Math.pow((loc1.col() - loc2.col()), 2)));
	}
}

