
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
		setPopulation(0);
		setName(n);
	}
	
	private void sizeACs() {
		// put the right calc on the 1st line
		// Reference for BTU calculation based on space dimension and expected population: {insert reference}
		
		double btuPerPerson = 500;   // BTU/hr
		double numberOfPeople = this.getArea()*0.2;  // each person occupying 1m2, at 20% space capacity utilization
		
		double btu = this.getVolume()*6 + btuPerPerson*numberOfPeople;
		
		// split the capacity btw main and backUp, at 60:40
		mainAC = new CoolingUnit(0.6*btu);
		backupAC = new CoolingUnit(0.4*btu);
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
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	
	
}
