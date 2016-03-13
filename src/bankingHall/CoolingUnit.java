/**
 * 
 */
package bankingHall;

/**
 * @author Bayode Aderinola
 *
 */
public class CoolingUnit {

	/**
	 * 
	 */
	private boolean on;
	private float setTemp;
	
	private double capacityBTU;
	
	public CoolingUnit(double btu){
		setCapacityBTU(btu);
		on = false;
		setTemp=0;
	}
	
	public long consumeEnergy () {
		
		return 0;
	}

	/**
	 * @return the set temperature
	 */
	public double getSetTemp() {
		return setTemp;
	}

	/**
	 * @param setTemp the temperature to set
	 */
	public void setSetTemp(float setTemp) {
		this.setTemp = setTemp;
	}

	/**
	 * @return the "on" state
	 */
	public boolean isOn() {
		return on;
	}

	/**
	 * @param on the "on" state to set (true/false)
	 */
	public void setOn(boolean on) {
		this.on = on;
	}

	/**
	 * @return the capacity in BTU
	 */
	public double getCapacityBTU() {
		return capacityBTU;
	}

	/**
	 * @param btu the capacity in BTU to set
	 */
	public void setCapacityBTU(double btu) {
		this.capacityBTU = btu;
	}
	

}
