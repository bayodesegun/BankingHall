/**
 * 
 */
package bankingHall;

/**
 * @author Bayode Aderinola
 *
 */
public class ACController {

	/**
	 * 
	 */
	private float minTemp, maxTemp;
	
	public void coolSpaces2ReqTemp () {
		////STEPS TO COOLING
		// For each Space
		
			/*
			 * 1. Determine energy e required to cool = energy to cool empty space to minTemp + heat energy from population
			 * 1b if e > 0 goto 2, else goto 1
			 * 2. Determine the ACs required for cooling based on 1 - either main, back-up or both
			 * 3. Calculate the time, t, needed for the AC to effect cooling to lower val of acceptable temp range (minTemp)
			 * 4  Start AC(s)
			 * 4b. Simulate the cooling over time t, i.e, progressive set Space temp so that after time t it is at minTemp
			 * 5. Stop AC
			 * 5b. Calculate energy consumed by AC for running for time t, based on the AC capacity
			 * 6. Calculate time, t2, required for temp to go from minTemp to maxTemp+1, heat from people and outside air temp
			 * 7. Simulate "heating" over time t2, such that Space temperature increases progressive to maxTemp+1
			 * 8. goto 1.
			 */
		
	}
	public ACController(float min, float max) {
		minTemp = min;
		maxTemp = max;
	}
	/**
	 * @return the minTemp
	 */
	public float getMinTemp() {
		return minTemp;
	}
	
	/**
	 * @return the maxTemp
	 */
	public float getMaxTemp() {
		return maxTemp;
	}
	
	public void checkCooling() {
		// TODO Auto-generated method stub
		
	}
	

}
