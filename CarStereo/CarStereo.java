/**
 * @author Sullivan Fair
 */
package hw1;

public class CarStereo 
{
	/**
	 * Amount the volume changes for each louder() or quieter() operation
	 */
	public static final double VOLUME_STEP = 0.16;
	
	/**
	 * What level the volume is at
	 */
	private double volume;
	
	/**
	 * What frequency the tuner is set
	 */
	private double tuner;
	
	/**
	 * Equals the given minimum frequency
	 */
	private double minFrequency;
	
	/**
	 * Equals the given maximum frequency
	 */
	private double maxFrequency;
	
	/**
	 * Equals the number of given stations starting at 0 instead of 1
	 */
	private double numStations;
	
	/**
	 * Distance between max frequency and min frequency
	 */
	private double bandwidth;
	
	/**
	 * Equals the bandwidth divided by the number of stations
	 * Used to determine where the station numbers are located on the tuner
	 */
	private double interval;
	
	/**
	 * Equals whatever station number is saved by the user
	 */
	private int preset;
	
	
	/**
	 * 
	 * @param givenMinFrequency
	 * @param givenMaxFrequency
	 * @param givenNumStations
	 * Constructs a new radio with the given
	 * band and number of stations
	 * Initially: Volume is 0.5, tuner is at
	 * min frequency, and preset station is 0
	 */
	public CarStereo(double givenMinFrequency, double givenMaxFrequency, int givenNumStations) 
	{
		volume = 0.5;
		tuner = givenMinFrequency;
		minFrequency = givenMinFrequency;
		maxFrequency = givenMaxFrequency;
		numStations = givenNumStations - 1;
		bandwidth = givenMaxFrequency - givenMinFrequency;
		interval = (givenMaxFrequency - givenMinFrequency) / givenNumStations;
		preset = 0;
	}
	
	/**
	 * 
	 * @return the current volume
	 */
	public double getVolume()
	{
		return volume;
	}
	
	/**
	 * Increases the volume by VOLUME_STEP,
	 * but not going above 1
	 */
	public void louder() 
	{
		volume += VOLUME_STEP;
		volume = Math.min(volume, 1);
	}
	
	/**
	 * Decreases the volume by VOLUME_STEP,
	 * but not going below 0
	 */
	public void quieter() 
	{
		volume -= VOLUME_STEP;
		volume = Math.max(volume,0);
	}
	
	/**
	 * 
	 * @return the frequency to which this 
	 * radio is currently tuned
	 */
	public double getTuner()
	{
		return tuner;
	}
	
	/**
	 * 
	 * @param givenFrequency
	 * Sets tuner to the given frequency if possible
	 * Set to max is given value is above max
	 * Set to min if given value is below min
	 */
	public void setTuner(double givenFrequency) 
	{
		tuner = givenFrequency;
		tuner = Math.min(tuner, maxFrequency);
		tuner = Math.max(tuner, minFrequency);
	}
	
	/**
	 * 
	 * @param degrees
	 * Adjusts the current tuner frequency by given 
	 * amount specified as degrees of 
	 * rotation (positive or negative)
	 */
	public void turnDial(double degrees) 
	{
		degrees = degrees / 360;
		tuner += bandwidth * degrees;
		tuner = Math.min(tuner, maxFrequency);
		tuner = Math.max(tuner, minFrequency);
	}
	
	/**
	 * 
	 * @param stationNumber
	 * Sets tuner to broadcast frequency of given 
	 * station if possible
	 */
	public void setTunerFromStationNumber(int stationNumber) 
	{
		tuner = minFrequency + (stationNumber * interval + (interval / 2));
		tuner = Math.min(tuner, minFrequency + (numStations * interval + (interval /2)));
		tuner = Math.max(tuner, minFrequency + (0 * interval + (interval /2)));
	}
	
	/**
	 * 
	 * @return current station number
	 * Determines the station number that is 
	 * closest to the radio's current tuner
	 */
	public int findStationNumber() 
	{
		tuner = (tuner - minFrequency - (interval / 2)) / interval;
		tuner = Math.min(numStations, tuner);
		return (int) Math.round(tuner);
	}
	
	/**
	 * Sets tuner to the broadcast frequency of the
	 * next station below the current one
	 */
	public void seekUp()
	{
		tuner = (findStationNumber() + 1) % (numStations + 1);
		tuner = minFrequency + (tuner * interval + (interval / 2));
		tuner = Math.min(tuner, minFrequency + (numStations * interval + (interval / 2)));
		tuner = Math.max(tuner, minFrequency + (0 * interval + (interval / 2)));
	}
	
	/**
	 * Sets tuner to the broadcast frequency of the
	 * next station above the current one
	 */
	public void seekDown()
	{
		tuner = (findStationNumber() + (numStations + 1) - 1) % (numStations + 1);
		tuner = minFrequency + (tuner * interval + (interval / 2));
		tuner = Math.min(tuner, minFrequency + (numStations * interval + (interval / 2)));
		tuner = Math.max(tuner, minFrequency + (0 * interval + (interval / 2)));
	}
	
	/**
	 * Stores the current station number as
	 * the preset
	 */
	public void savePreset()
	{
		preset = findStationNumber();
	}
	
	/**
	 * Sets the frequency to be the broadcast 
	 * frequency of the current preset station
	 */
	public void goToPreset()
	{
		tuner = minFrequency + (preset * interval + (interval / 2));
	}
}
