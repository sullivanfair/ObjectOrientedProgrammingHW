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

public class Pinky extends Ghost
{	
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
	public Pinky(MazeMap maze, Location home, double baseSpeed, 
			Direction homeDirection, Location scatterTarget, Random rand)
	{
		super(maze, home, baseSpeed, homeDirection, scatterTarget, rand);
	}
	
	@Override
	protected void calcTargetCell(Descriptor desc)
	{
		if(desc.getPlayerDirection() == UP)
		{
			setTargetCell(new Location(desc.getPlayerLocation().row() - 4, desc.getPlayerLocation().col()));
		}
		else if(desc.getPlayerDirection() == LEFT)
		{
			setTargetCell(new Location(desc.getPlayerLocation().row(), desc.getPlayerLocation().col() - 4));
		}
		else if(desc.getPlayerDirection() ==  DOWN)
		{
			setTargetCell(new Location(desc.getPlayerLocation().row() + 4, desc.getPlayerLocation().col()));
		}
		else
		{
			setTargetCell(new Location(desc.getPlayerLocation().row(), desc.getPlayerLocation().col() + 4));
		}
	}
}
