/**
 * @author Sullivan Fair
 */
package hw4;

import api.Descriptor;
import api.Direction;
import api.Location;
import api.MazeMap;

import java.util.Random;

/**
 *   
 * I used three classes to implement pacman and the ghosts.  The first class, PacmanAndFriends, implements
 * Actor and contains the code shared by pacman and the ghosts.  The second class, Ghosts, contains 
 * all of the common code among the ghosts.  The only major difference among the ghosts is how they calculate
 * there target cell while in CHASE mode, all of the other code is shared.  So Blinky, Pinky, Inky, and Clyde only
 * needed a constructor and a method to calculate their CHASE target cell.
 *
 */

public class Blinky extends Ghost
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
	public Blinky(MazeMap maze, Location home, double baseSpeed, 
			Direction homeDirection, Location scatterTarget, Random rand)
	{
		super(maze, home, baseSpeed, homeDirection, scatterTarget, rand);
	}

	@Override
	protected void calcTargetCell(Descriptor desc)
	{
		setTargetCell(desc.getPlayerLocation());
	}
}
