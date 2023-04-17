/**
 * @author Sullivan Fair
 */
package hw4;

import api.Actor;
import api.Direction;
import api.Location;
import api.MazeMap;

public abstract class PacmanAndFriends implements Actor
{
	//Variables
	/**
	 * Margin of error for comparing exact position to centerline
	 * of cell.
	 */
	private static final double ERR = .001;
	
	/**
	 * Maze configuration.
	 */
	private MazeMap maze;
	
	/**
	 * Initial location on reset().
	 */
	private Location home;
	
	/**
	 * Initial direction on reset().
	 */
	private Direction homeDirection;
	
	/**
	 * Current direction of travel.
	 */
	private Direction currentDirection;

	/**
	 * Basic speed increment, used to determine currentIncrement.
	 */
	private double baseIncrement;
	
	/**
	 * Current speed increment, added in direction of travel each frame.
	 */
	private double currentIncrement;
	
	/**
	 * Row (y) coordinate, in units of cells.  The row number for the
	 * currently occupied cell is always the int portion of this value.
	 */
	private double rowExact;
	
	/**
	 * Column (x) coordinate, in units of cells.  The column number for the
	 * currently occupied cell is always the int portion of this value.
	 */
	private double colExact;
	
	/**
	 * Position of row used in distanceToCenter()
	 */
	private double rowPos;
	
	/**
	 * Position of col used in distanceToCenter()
	 */
	private double colPos;
	
	//Get Methods
	/**
	 * Accessor forERR
	 * @return
	 * 		Value of ERR
	 */
	protected double getERR()
	{
		return ERR;
	}
	
	/**
	 * Accessor for maze configuration
	 * @return
	 * 		Maze of the game
	 */
	protected MazeMap getMaze()
	{
		return maze;
	}
	
	public Location getHomeLocation()
	{
		return home;
	}
	
	public Direction getHomeDirection()
	{
		return homeDirection;
	}
	
	public Direction getCurrentDirection()
	{
		return currentDirection;
	}
	
	public double getBaseIncrement()
	{
		return baseIncrement;
	}
	
	public double getCurrentIncrement()
	{
		return currentIncrement;
	}
	
	public double getRowExact()
	{
		return rowExact;
	}
	
	public double getColExact()
	{
		return colExact;
	}
	
	
	//Set Methods
	/**
	 * Set method for maze variable
	 * @param m
	 * 		Given MazeMap
	 */
	protected void setMaze(MazeMap m)
	{
		maze = m;
	}
	
	/**
	 * Set method for home location
	 * @param h
	 * 		Given Location
	 */
	protected void setHome(Location h)
	{
		home = h;
	}
	
	/**
	 * Set method for inital direction
	 * @param hd
	 * 		Given direction
	 */
	protected void setHomeDirection(Direction hd)
	{
		homeDirection = hd;
	}
	
	public void setDirection(Direction dir)
	{
		currentDirection = dir;
	}
	
	/**
	 * Sets the base speed
	 * @param i
	 * 		Given double value
	 */
	protected void setBaseIncrement(double i)
	{
		baseIncrement = i;
	}
	
	/**
	 * Sets current speed
	 * @param i
	 * 		Given double value
	 */
	protected void setCurrentIncrement(double i)
	{
		currentIncrement = i;
	}
	
	public void setRowExact(double r)
	{
		rowExact = r;
	}
	
	public void setColExact(double c)
	{
		colExact = c;
	}
	
	//Methods
	public abstract Location getCurrentLocation();
	
	/**
	 * Determines the difference between current position and center of 
	 * current cell, in the direction of travel.
	 */
 	protected double distanceToCenter()
	{
		rowPos = getRowExact();
		colPos = getColExact();
		
		switch (getCurrentDirection())
		{
			case LEFT:
				return colPos - ((int) colPos) - 0.5;
			case RIGHT:
				return 0.5 - (colPos - ((int) colPos));
			case UP:
				return rowPos - ((int) rowPos) - 0.5;
			case DOWN:
				return 0.5 - (rowPos - ((int) rowPos));
	    }  
		return 0;
	}	
}
