
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
	
	
	

}
