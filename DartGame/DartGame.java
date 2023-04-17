package hw2;

import api.ThrowType;
import static api.ThrowType.*;

/**
 * This class models a standard game of darts, keeping track of the scores,
 * whose turn it is, and how many darts the current player has remaining.
 * The number of starting points and the number of darts used in 
 * a player's turn are configurable.
 */
public class DartGame
{  
	/**
	 * Starting number of points
	 */
	private int initialPoints;
	
	/**
	 * Score for player one
	 */
	private int scoreOne;
	
	/**
	 * Score for player two
	 */
	private int scoreTwo;
	
	/**
	 * The player that is currently throwing
	 */
	private int currentPlayer;
	
	/**
	 * Amount of darts a player currently has
	 */
	private int dartsLeft;
	
	/**
	 * Starting number of darts
	 */
	private int initialDarts;
	
	/**
	 * Determines if player one has "doubled in"
	 */
	private boolean doubledInOne;
	
	/**
	 * Determines if player two has "doubled in"
	 */
	private boolean doubledInTwo;
	
	/**
	 * Score on player 1's last turn
	 */
	private int previousScoreOne;
	
	/**
	 * Score on player 2's last turn
	 */
	private int previousScoreTwo;
	
	/**
	 * Constructs a DartGame with the given starting player, 
	 * initial points 301, 
	 * and three darts for each player's turn.
	 * @param startingPlayer
	 *   the starting player (0 or 1)
	 */
	public DartGame(int startingPlayer)
	{
		scoreOne = 301;
		scoreTwo = 301;
		currentPlayer = startingPlayer;
		dartsLeft = 3;
		initialDarts = 3;
		initialPoints = 301;
		previousScoreOne = 301;
		previousScoreTwo = 301;
		doubledInOne = false;
		doubledInTwo = false;
	}
	
	/**
	 * Constructs a DartGame with the given starting player, 
	 * initial number of points, 
	 * and number of darts.
	 * @param startingPlayer
	 * 	 the starting player (0 or 1)
	 * @param startingPoints
	 *   starting points for the game
	 * @param numDarts
	 *   number of darts for each player's turn
	 */
	public DartGame(int startingPlayer, int startingPoints, int numDarts)
	{
		scoreOne = startingPoints;
		scoreTwo = startingPoints;
		currentPlayer = startingPlayer;
		dartsLeft = numDarts;
		initialDarts = numDarts;
		initialPoints = startingPoints;
		previousScoreOne = startingPoints;
		previousScoreTwo = startingPoints;
		doubledInOne = false;
		doubledInTwo = false;
	}
	
	/**
	 * Simulates the action of the current players
	 * throwing one dart, adjusting the points as needed
	 * @param type
	 * 	 type of throw
	 * @param number
	 *   segment of the dartboard on which the
	 *   throw lands (ignored in case of MISS,
	 *   OUTER_BULLSEYE, or INNER_BULLSEYE
	 */
	public void throwDart(ThrowType type, int number)
	{
		if(isOver() == false)
		{
			storePreviousScore();

			if (type == MISS)
			{
				number = 0;
			}
			
			else if (type == SINGLE)
			{
				number = number * 1;
			}
			
			else if (type == DOUBLE)
			{
				number = number * 2;
			}
			
			else if (type == TRIPLE)
			{
				number = number * 3;
			}
			
			else if (type == OUTER_BULLSEYE)
			{
				number = 25;
			}
			
			else 
			{
				number = 50;
			}
			
			doubleinCheck(type);
			
			if(currentPlayer == 0 && doubledInOne == true)
			{
				adjustScore(number);
				bustCheck(type, scoreOne);	
			}
			
			else if(currentPlayer == 1 && doubledInTwo == true)
			{
				adjustScore(number);
				bustCheck(type, scoreTwo);
			}
			
			else
			{
				dartsLeft -= 1;
				if(dartsLeft == 0)
				{
					switchPlayer();
				}
			}
		}
	}
	
	/**
	 * Reduces the score for the current 
	 * player by the given amount
	 * @param amount
	 *   number of points to subtract
	 */
	private void adjustScore(int amount)
	{
		if (currentPlayer == 1)
		{
			scoreTwo -= amount;
			dartsLeft -= 1;
		}
		
		else if (currentPlayer == 0)
		{
			scoreOne -= amount;
			dartsLeft -= 1;
		}
		
		if(currentPlayer == 1 && dartsLeft == 0)
		{
			switchPlayer();
		}
		
		else if(currentPlayer == 0 && dartsLeft == 0)
		{
			switchPlayer();
		}
	}
	
	/**
	 * Stores the score of the each of the players'
	 * last turn
	 */
	private void storePreviousScore()
	{
		if(dartsLeft == initialDarts)
		{
			previousScoreOne = scoreOne;
			previousScoreTwo = scoreTwo;
		}
	}
	
	/**
	 * Determines whether a player has
	 * doubled in
	 */
	private void doubleinCheck(ThrowType type)
	{
		if(currentPlayer == 1 
				&& (type == DOUBLE || type == INNER_BULLSEYE) 
				&& scoreTwo == initialPoints)
		{
			doubledInTwo = true; 
		}
		
		else if(currentPlayer == 1
				&& scoreTwo != initialPoints)
		{
			doubledInTwo = true;
		}
		
		if(currentPlayer == 0
				&& (type == DOUBLE || type == INNER_BULLSEYE) 
				&& scoreOne == initialPoints)
		{
			doubledInOne = true; 
		}
		
		else if(currentPlayer == 0
				&& scoreOne != initialPoints)
		{
			doubledInOne = true;
		}
	}
	
	/**
	 * Determines whether a player has gone bust
	 */
	private void bustCheck(ThrowType type, int score)
	{
		if((scoreOne == 1) 
				|| (scoreOne < 0) 
				|| (scoreOne == 0 && type != DOUBLE && type != INNER_BULLSEYE))
		{
			scoreOne = previousScoreOne;
			switchPlayer();
		}
		
		if((scoreTwo == 1) 
				|| (scoreTwo < 0) 
				|| (scoreTwo == 0 && type != DOUBLE && type != INNER_BULLSEYE))
		{
			scoreTwo = previousScoreTwo;
			switchPlayer();
		}
	}
	
	/**
	 * @param type
	 * 	 type of throw
	 * @param number
	 *   segment of the dartboard on which the throw lands
	 *   (ignored in case of MISS, OUTER_BULLSEYE, or INNER_BULLSEYE
	 * @return
	 *   number of points for the given throw
	 */
	public static int calcPoints(ThrowType type, int number)
	{
		if (type == MISS)
		{
			number = 0;
		}
		
		if (type == SINGLE)
		{
			number = number * 1;
		}
		
		if (type == DOUBLE)
		{
			number = number * 2;
		}
		
		if (type == TRIPLE)
		{
			number = number * 3;
		}
		
		if (type == OUTER_BULLSEYE)
		{
			number = 25;
		}
		
		if (type == INNER_BULLSEYE)
		{
			number = 50;
		}
		
		return number;
	}
	
	/**
    * Returns the player whose turn it is.  (When the game is over,
    * this method always returns the winning player.)
    * @return
    *   current player (0 or 1)
    */
	public int getCurrentPlayer()
	{
		if(scoreOne == 0)
		{
			currentPlayer = 0;
		}
		
		else if (scoreTwo == 0)
		{
			currentPlayer = 1;
		}
		
		return currentPlayer;
	}
	
	private void switchPlayer()
	{
		if(currentPlayer == 1 && scoreTwo == 0) 
		{
			isOver();
		}
		
		else if(currentPlayer == 1)
		{
			currentPlayer = 0;
			dartsLeft = initialDarts;
		}
		
		else if(currentPlayer == 0 && scoreOne == 0)
		{
			isOver();
		}
		
		else if(currentPlayer == 0)
		{
			currentPlayer = 1;
			dartsLeft = initialDarts;
		}
	}
  
	/**
	 * Returns the score of the indicated player (0 or 1).  If
	 * the argument is any value other than 0 or 1, the method returns
	 * -1.
	 * @param which
	 *   indicator for which player (0 or 1)
	 * @return
	 *   score for the indicated player, or -1 if the argument is invalid
	 */
	public int getScore(int which)
	{
		if (which == 0)
		{
			return scoreOne;
		}
		
		else if (which == 1)
		{
			return scoreTwo;
		}
		
		else
		{
			return -1;
		}
	}
  
	/**
	 * Returns the number of darts left in the current player's turn.
	 * @return
	 *   the number of darts left in the current player's turn
	 */
	public int getDartCount()
	{
		return dartsLeft;
	}
  
	/**
	 * Returns a string representation of the current game state.
	 */
	public String toString()
	{
		String result = "Player 0: " + getScore(0) +
                   	"  Player 1: " + getScore(1) +
                   	"  Current: Player " + getCurrentPlayer() +
                    "  Darts: " + getDartCount();
		
		return result;
	}
	
	/**
	 * Returns true if one of the players has
	 * a score of zero, false otherwise
	 */
	public boolean isOver()
	{
		if(scoreOne == 0 || scoreTwo == 0)
		{
			return true;
		}
		
		else
		{
			return false;
		}
	}
	
	/**
	 * Returns the winner (0 or 1), or -1 if
	 * the game is not over
	 */
	public int whoWon()
	{
		if(scoreOne == 0)
		{
			return 0;
		}
		
		else if (scoreTwo == 0)
		{
			return 1;
		}
		
		else
		{
			return -1;
		}
	}
}


