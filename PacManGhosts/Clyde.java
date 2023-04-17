/**
 * @author Sullivan Fair
 */
package hw4;

import java.util.Random;

import api.Descriptor;
import api.Direction;
import api.Location;
import api.MazeMap;

public class Clyde extends Ghost
{
	/**
	 * Stores the distance from Clyde to Pacman
	 */
	private double distToPac;
	
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
	public Clyde(MazeMap maze, Location home, double baseSpeed, 
			Direction homeDirection, Location scatterTarget, Random rand)
	{
		super(maze, home, baseSpeed, homeDirection, scatterTarget, rand);
	}
	
	@Override
	protected void calcTargetCell(Descriptor desc)
	{
		distToPac = calculateDistance(getCurrentLocation(), desc.getPlayerLocation());
		
		if(distToPac > 8.0)
		{
			setTargetCell(desc.getPlayerLocation());
		}
		else 
		{
			setTargetCell(getScatterTarget());
		}
		
	}
}
