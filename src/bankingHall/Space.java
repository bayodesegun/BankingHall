/**
 * 
 */
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
	
	private CoolingUnit mainAC;
	private CoolingUnit backupAC;
	
	private Sensor sensor;
	
	
	
	public Space (double l, double w, double h)
	{
		length = l;
		width = w;
		height = h;
		area = l*w;
		volume = l*w*h;
		sizeACs();
		sensor = new Sensor();
		setPopulation(0);
	}
	
	private void sizeACs() {
		// put the right calc on the 1st line
		double btu = this.getVolume()*100;
		
		// split the capacity btw main and backUp
		mainAC = new CoolingUnit(0.7*btu);
		backupAC = new CoolingUnit(0.3*btu);
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

	
}
