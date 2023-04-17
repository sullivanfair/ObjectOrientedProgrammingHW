/**
 * @author Sullivan Fair
 */
package hw4;

import static api.Direction.DOWN;
import static api.Direction.LEFT;
import static api.Direction.UP;

import java.util.Random;

import api.Descriptor;
import api.Direction;
import api.Location;
import api.MazeMap;

public class Inky extends Ghost
{
	/**
	 * Cell two tiles in front of Pacman
	 */
	private Location tempCell;
	
	/**
	 * Exact location of the ghost Blinky
	 */
	private Location blinkyExact;
	
	/**
	 * Row of Inky's target cell
	 */
	private int targetRow;
	
	/**
	 * Col of Inky's target cell
	 */
	private int targetCol;
	
	/**
	 * Inherited constructor from Ghost
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
	public Inky(MazeMap maze, Location home, double baseSpeed, 
			Direction homeDirection, Location scatterTarget, Random rand)
	{
		super(maze, home, baseSpeed, homeDirection, scatterTarget, rand);
	}
	
	@Override
	protected void calcTargetCell(Descriptor desc)
	{
		if(desc.getPlayerDirection() == UP)
		{
			tempCell = new Location((int) desc.getPlayerLocation().row() - 2, (int) desc.getPlayerLocation().col());
		}
		else if(desc.getPlayerDirection() == LEFT)
		{
			tempCell = new Location((int) desc.getPlayerLocation().row(), (int) desc.getPlayerLocation().col() - 2);
		}
		else if(desc.getPlayerDirection() == DOWN)
		{
			tempCell = new Location((int) desc.getPlayerLocation().row() + 2, (int) desc.getPlayerLocation().col());
		}
		else
		{
			tempCell = new Location((int) desc.getPlayerLocation().row(), (int) desc.getPlayerLocation().col() + 2);
		}
		
		blinkyExact = new Location((int) desc.getBlinkyLocation().row(), (int) desc.getBlinkyLocation().col());
		
		targetRow = (tempCell.row() * 2) - (blinkyExact.row());
		targetCol = (tempCell.col() * 2) - (blinkyExact.col());
		
		setTargetCell(new Location(targetRow, targetCol));
	}
}
