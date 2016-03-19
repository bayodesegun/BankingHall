
package bankingHall;

/**
 * @author Bayode Aderinola
 *
 */
public class Space {
	
	private double length;
	private double width;
	private double height;
	private double area;
	private double volume;
	private int population;
	private String name;
	
	// Total BTU needed to cool
	private double totalBTU;
	
	private CoolingUnit mainAC;
	private CoolingUnit backupAC;
	
	private Sensor sensor;
	
	
	
	public Space (double l, double w, double h, String n)
	{
		length = l;
		width = w;
		height = h;
		area = l*w;
		volume = l*w*h;
		sizeACs();
		sensor = new Sensor();
		population = 0;
		name = n;
	}
	
	private void sizeACs() {
		// Reference for BTU calculation based on space dimension and expected population: {insert reference}
		
		double btuPerPerson = 450;   						// BTU/hr
		double numberOfPeople = this.getArea()*0.2;  		// each person occupying 1m2, at 20% space capacity utilization
		
		totalBTU = this.getVolume()*6 + btuPerPerson*numberOfPeople;
		
		// split the capacity btw main and backUp, at 60:40
		mainAC = new CoolingUnit(0.6*totalBTU);
		backupAC = new CoolingUnit(0.4*totalBTU);
	}

	public double getArea() {
		return area;
	}
	
	public double getVolume() {
		return volume;
	}

	/**
	 * @return the sensor
	 */
	public Sensor getSensor() {
		return sensor;
	}

	/**
	 * @return the population
	 */
	public int getPopulation() {
		return population;
	}

	/**
	 * @param population the population to set
	 */
	public void setPopulation(int population) {
		this.population = population;
	}
	
	public double getHeatFromPeople() {
		double heatPerPerson = 1250;
		return this.getPopulation()*heatPerPerson;
	}

	/**
	 * @return the mainAC
	 */
	public CoolingUnit getMainAC() {
		return mainAC;
	}

	
	
	/**
	 * @return the backupAC
	 */
	public CoolingUnit getBackupAC() {
		return backupAC;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return the totalBTU
	 */
	public double getTotalBTU() {
		return totalBTU;
	}

	

	
	
}
