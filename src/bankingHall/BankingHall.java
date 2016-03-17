/**
 * @author Bayode Aderinola
 *
 */
package bankingHall;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import controlP5.*;


public class BankingHall extends PApplet implements Runnable {
		
	// just to keep Eclipse from displaying a warning:
	private static final long serialVersionUID = 1L;
	
	private Space counter;
	private Space customerSP;
	private Space lobby;
	
	private ACController controlAC;
	
	private float ambientT;
	private int population;
	private static final int WORKINGHOURS = 8;
	
	private String currDir = null;
	private File inFile;
	private List<Float> inTemp;
	private List<Integer> inPop;
	
	private String event = "IDLE";
		
	private double rawEnergy = 0;
	private double manEnergy = 0;
	private double saveRatio=0;
	
	public ControlP5 cp5;
	ControlTimer time;
	
	private boolean start = false;
	
	PImage [] images = {loadImage("AC.png"),loadImage("control.png"),loadImage("thermoL.png"), loadImage("thermoP.png")};
	
	private final int GREY = color (128,128,128);
	private final int DARK_GREEN = color(0,100,0);
	private final int LIGHT_GREEN = color(0,150,0);
	private final int DARK_RED = color(155,0,0);
	private final int LIGHT_RED = color(255,0,0);
	private final int DARK_BLUE = color(0,0,155);
	private final int WHITE = color(255);
	private final int BLACK = color(0);
	
	private final int EER = 12;						// energy efficiency rating for ACs
	
	/*Runnable coolCounter = new Runnable() {
		public void run(){
			// do something
			float a = ambientT;
			distributePopulation();
		}
	};
	
	Runnable coolLobby = new Runnable() {
		public void run(){
			// do something
		}
	};
	
	Runnable coolCSP = new Runnable() {
		public void run(){
			// do something
		}
	};*/
	
	public void setup()
	{
		// size of applet
		size(900, 700);
		 		 
		// set background color of applet 
		background(GREY);               
		
		PFont labelFont = createFont("arial", 13);    // label font
		PFont textFont = createFont("arial", 15);     // text font
		
		
		

		// Create a new ControlP5 controller
	    cp5 = new ControlP5(this);
	    
	    // set the font for the label on all controllers
	    cp5.setFont(labelFont);
				  
		// draw the temperature indicator rects
			
	    // temp at the counter 		
		cp5.addNumberbox("counterTemp")
		   .setPosition(5,645)
	       .setSize(90,35)
	       .setColorBackground(DARK_GREEN)
	       .setDecimalPrecision(2)
	       .setLabel("  COUNTER")
	       .setLock(true)
		   ;
		
		// temp at the lobby
		cp5.addNumberbox("lobbyTemp")
		   .setPosition(105,645)
	       .setSize(90,35)
	       .setColorBackground(DARK_GREEN)
	       .setDecimalPrecision(2)
	       .setLabel("     LOBBY")
	       .setLock(true)
		   ;
		
		// temp at the customer service point
		cp5.addNumberbox("cspTemp")
		   .setPosition(205,645)
	       .setSize(90,35)
	       .setColorBackground(DARK_GREEN)
	       .setDecimalPrecision(2)
	       .setLabel(" CUST SERV")
	       .setLock(true)
		   ;
		
		// temp outside (ambient)
		cp5.addNumberbox("ambientTemp")
		   .setPosition(305,645)
	       .setSize(90,35)
	       .setColorBackground(DARK_GREEN)
	       .setDecimalPrecision(2)
	       .setLabel(" A M B I E N T")
	       .setLock(true)
		   ;
		
	    // add the Timer
	   // ControlTimer time = new ControlTimer();
	    
	    cp5.addTextfield("currentTime")
		   .setPosition(5,570)
	       .setSize(90,35)
	       .setColorBackground(DARK_BLUE)
	       .setDecimalPrecision(0)
	       .setLabel("       TIME")
	       .setLock(true)
	       .setFont(textFont)
	       //.setValue("00:00:00")
		   ;
	    
	    // add the "raw" energy used so far
	    cp5.addNumberbox("rawEnergy")
		   .setPosition(105,570)
	       .setSize(90,35)
	       .setColorBackground(DARK_BLUE)
	       .setDecimalPrecision(2)
	       .setLabel("ENERGY-RAW")
	       .setLock(true)
	       ;
	    
	 // add the "managed" energy used so far
	    cp5.addNumberbox("manEnergy")
		   .setPosition(205,570)
	       .setSize(90,35)
	       .setColorBackground(DARK_BLUE)
	       .setDecimalPrecision(2)
	       .setLabel("ENERGY-MAN")
	       .setLock(true)
	       ;
	    
	 // add energy savings ratio
	    cp5.addNumberbox("saveRatio")
		   .setPosition(305,570)
	       .setSize(90,35)
	       .setColorBackground(DARK_BLUE)
	       .setDecimalPrecision(2)
	       .setLabel("   MAN : RAW")
	       .setLock(true)
	       ;

	// Add labels
	    cp5.addTextlabel("lblDim")
		   .setPosition(405,570)
	       .setSize(150,15)
	       .setColorBackground(DARK_BLUE)
	       .setLabel("")
	       .setLock(true)
	       .setValue("DIM. (metres):        L            B            H")
		   ;
	    
	    cp5.addTextlabel("lblCounter")
		   .setPosition(405,590)
	       .setSize(90,35)
	       .setColorBackground(DARK_BLUE)
	       .setLabel("")
	       .setLock(true)
	       .setValue("Counter:")
		   ;
	    
	    cp5.addTextlabel("lblLobby")
		   .setPosition(405,629)
	       .setSize(90,35)
	       .setColorBackground(DARK_BLUE)
	       .setLabel("")
	       .setLock(true)
	       .setValue("Lobby:")
		   ;
	    
	    cp5.addTextlabel("lblCsp")
		   .setPosition(405,663)
	       .setSize(90,35)
	       .setColorBackground(DARK_BLUE)
	       .setLabel("")
	       .setLock(true)
	       .setValue("Cust Serv:")
		   ;
	    
	    // Add the text boxes
	    cp5.addTextfield("counterL")
		   .setPosition(496,589)
	       .setSize(50,35)
	       .setColorBackground(WHITE)
	       .setColorCursor(BLACK)
	       .setColorActive(LIGHT_RED)
	       .setColor(BLACK)
	       .setFont(textFont)
	       .setLabel("")
		   ;
	    
	    cp5.addTextfield("counterB")
		   .setPosition(550,589)
	       .setSize(50,35)
	       .setColorBackground(WHITE)
	       .setColorCursor(BLACK)
	       .setColorActive(LIGHT_RED)
	       .setColor(BLACK)
	       .setFont(textFont)
	       .setLabel("")
		   ;
	    
	    cp5.addTextfield("counterH")
		   .setPosition(604,589)
	       .setSize(50,35)
	       .setColorBackground(WHITE)
	       .setColorCursor(BLACK)
	       .setColorActive(LIGHT_RED)
	       .setColor(BLACK)
	       .setFont(textFont)
	       .setLabel("")
		   ;
	    
	    cp5.addTextfield("lobbyL")
		   .setPosition(496,626)
	       .setSize(50,35)
	       .setColorBackground(WHITE)
	       .setColorCursor(BLACK)
	       .setColorActive(LIGHT_RED)
	       .setColor(BLACK)
	       .setFont(textFont)
	       .setLabel("")
		   ;
	    
	    cp5.addTextfield("lobbyB")
		   .setPosition(550,626)
	       .setSize(50,35)
	       .setColorBackground(WHITE)
	       .setColorCursor(BLACK)
	       .setColorActive(LIGHT_RED)
	       .setColor(BLACK)
	       .setFont(textFont)
	       .setLabel("")
		   ;
	    
	    cp5.addTextfield("lobbyH")
		   .setPosition(604,626)
	       .setSize(50,35)
	       .setColorBackground(WHITE)
	       .setColorCursor(BLACK)
	       .setColorActive(LIGHT_RED)
	       .setColor(BLACK)
	       .setFont(textFont)
	       .setLabel("")
		   ;
	    
	    cp5.addTextfield("cspL")
		   .setPosition(496,663)
	       .setSize(50,35)
	       .setColorBackground(WHITE)
	       .setColorCursor(BLACK)
	       .setColorActive(LIGHT_RED)
	       .setColor(BLACK)
	       .setFont(textFont)
	       .setLabel("")
		   ;
	    
	    cp5.addTextfield("cspB")
		   .setPosition(550,663)
	       .setSize(50,35)
	       .setColorBackground(WHITE)
	       .setColorCursor(BLACK)
	       .setColorActive(LIGHT_RED)
	       .setColor(BLACK)
	       .setFont(textFont)
	       .setLabel("")
		   ;
	    
	    cp5.addTextfield("cspH")
		   .setPosition(604,663)
	       .setSize(50,35)
	       .setColorBackground(WHITE)
	       .setColorCursor(BLACK)
	       .setColorActive(LIGHT_RED)
	       .setColor(BLACK)
	       .setFont(textFont)
	       .setLabel("")
	       ;
	    
	    cp5.addTextlabel("lblSetTemp")
		   .setPosition(660,590)
	       .setSize(90,35)
	       .setColorBackground(DARK_BLUE)
	       .setLabel("")
	       .setLock(true)
	       .setValue("Set Temp (deg.C):")
		   ;
	    
	    // boxes for set temp
	    
	    // the label...
	    cp5.addTextlabel("lblSetTempUp")
		   .setPosition(780,570)
	       .setSize(90,35)
	       .setColorBackground(DARK_BLUE)
	       .setLabel("")
	       .setLock(true)
	       .setValue("   Min           Max")
		   ;
	    cp5.addTextfield("minTemp")
		   .setPosition(780,590)
	       .setSize(50,35)
	       .setColorBackground(WHITE)
	       .setColorCursor(BLACK)
	       .setColorActive(LIGHT_RED)
	       .setColor(BLACK)
	       .setFont(textFont)
	       .setLabel("")
		   ;
	    
	    cp5.addTextfield("maxTemp")
		   .setPosition(845,590)
	       .setSize(50,35)
	       .setColorBackground(WHITE)
	       .setColorCursor(BLACK)
	       .setColorActive(LIGHT_RED)
	       .setColor(BLACK)
	       .setFont(textFont)
	       .setLabel("")
		   ;
	   // the Data Source
	    cp5.addTextlabel("lblDataSource")
		   .setPosition(660,629)
	       .setSize(90,35)
	       .setColorBackground(DARK_BLUE)
	       .setLabel("")
	       .setLock(true)
	       .setValue("Data Source:")
		   ;
	    
	    // add the radio button for options
	    cp5.addRadioButton("dataSource")
            .setPosition(780,629)
            .setSize(20,30)
            .setColorBackground(WHITE)
            .setColorForeground(BLACK)
            .setColorActive(BLACK)
            .setItemsPerRow(2)
            .setSpacingColumn(45)
            .addItem("Real",1)
            .addItem("Sim",2)
            ;
	    
	    // file path if real data
	    cp5.addTextlabel("lblFilePath")
		   .setPosition(660,663)
	       .setSize(90,35)
	       .setColorBackground(DARK_BLUE)
	       .setLabel("")
	       .setLock(true)
	       .setValue("File Path:")
		   ;
	    
	    cp5.addTextfield("filePath")
		   .setPosition(780,663)
	       .setSize(115,35)
	       .setColorBackground(WHITE)
	       .setColorCursor(BLACK)
	       .setColorActive(LIGHT_RED)
	       .setColor(BLACK)
	       .setFont(textFont)
	       .setLock(true)
	       .setLabel("")
		   ;
	    
	    // Add radio buttons for selecting simulation speed
	    // label...
	    cp5.addTextlabel("lblSimSpeed")
		   .setPosition(410,520)
	       .setSize(200,40)
	       .setLabel("")
	       .setFont(labelFont)
	       .setValue("Simulation Speed (1h = ? sec):")
		   ;
	    // buttons...
	      cp5.addRadioButton("simSpeed")
           .setPosition(600,510)
           .setSize(20,40)
           .setColorBackground(WHITE)
           .setColorForeground(BLACK)
           .setColorActive(BLACK)
           .setItemsPerRow(4)
           .setSpacingColumn(30)
           .setLabel("")
	       .addItem("60", 60)
	       .addItem("30", 120)
	       .addItem("15", 240)
	       .addItem("10", 360)
		   ;
	   
	    // Textfield for displaying error messages    
	      cp5.addTextfield("errorMsg")
		   .setPosition(1,510)
	       .setSize(400,40)
	       .setColorBackground(GREY)
	       .setColor(DARK_RED)
	       .setLabel("")
	       .setLock(true)
	       .setFont(textFont)
	       
	       ;
	      
	      
		// Finally, add the start button
		  
		 cp5.addButton("Start")
		 .setPosition(800,510)
		 .setSize(100,40)
		 .setColorBackground(DARK_GREEN)
         .setColorForeground(color(LIGHT_GREEN))
         .setLabel("Start")
         ;
		  
		
		}
		
	
	public void draw()
	{
	// things that should run when start is enabled
    if (start)	
    {    
    	cp5.get(Textfield.class, "currentTime").setText(time.toString());
    	if (time.hour()==WORKINGHOURS) {
    		stopSimulation();
    		displayMsg("EVENT: Simulation ended due to end of working hours.", DARK_GREEN);
    		event ="DAY END";
      	}
    	//update temperature and population data from file every hour
    	//if it has changed
    	if (time.hour() < 8) {
    		int hour = time.hour();
    		changePopAndTemp(hour);
    		
    		
    		
    	}
    	
    	  	
    	// Update all bound fields depending on start...
    	//1. update the Space temperatures
    	cp5.get(Numberbox.class, "ambientTemp").setValue(ambientT);
    	cp5.get(Numberbox.class, "counterTemp").setValue((float) counter.getSensor().getTemp());
    	cp5.get(Numberbox.class, "lobbyTemp").setValue((float) lobby.getSensor().getTemp());
    	cp5.get(Numberbox.class, "cspTemp").setValue((float) customerSP.getSensor().getTemp());
    	updateSpaceTempBoxColors();
    	
    	//2. update the Energy data
    	cp5.get(Numberbox.class, "rawEnergy").setValue((float) rawEnergy);
    	cp5.get(Numberbox.class, "manEnergy").setValue((float) manEnergy);
    	if (rawEnergy != 0) saveRatio = manEnergy/rawEnergy;
		cp5.get(Numberbox.class, "saveRatio").setValue((float) saveRatio);
		
		// Update rawEnergy continuously
		double totalBTU = counter.getMainAC().getCapacityBTU()+ counter.getBackupAC().getCapacityBTU()+
				lobby.getMainAC().getCapacityBTU()+ lobby.getBackupAC().getCapacityBTU() +
				customerSP.getMainAC().getCapacityBTU()+ customerSP.getBackupAC().getCapacityBTU();
		
		double totalKW = totalBTU/(EER*1000);
				// float speed = cp5.get(RadioButton.class, "simSpeed").getValue();
				// double elapsedTime = time.time()* (3600/speed);
				// println("Elapsed in millisec:" + elapsedTime);
		
		rawEnergy = (time.hour()+1) * totalKW;               // time goes from 0 to 7 :)
    	   	
    		
    }
		
	
	// load the images
	image (images[0], 225, 1);
	
	// draw enclosure and legend For temperatures
	    fill(GREY);
	    rect (0,625,400,75);
	    textSize(15);
	    fill(BLACK);
	    text("TEMPERATURES (DEG. C)", 112, 640);
	    
	 // draw enclosure and legend For simulation status
	    fill(GREY);
	    rect (0,550,400,75);
	    textSize(15);
	    fill(BLACK);
	    text("SIMULATION RESULTS (ALL ENERGY in kWh)", 45, 565);
	    
	 // draw enclosure and legend For setup area
	    fill(GREY);
	    rect (400,550,500,150);
	    textSize(15);
	    fill(BLACK);
	    text("SETUP", 625, 565);	
	    
	// Draw the banking hall sections
	   fill(GREY);
	   rect(0,0,900,220);       //COUNTER
	   rect(0,220,500,288);		//LOBBY
	   rect(500,220,400,288);	//CUST. SERV.
	   
	   fill(WHITE);
	   text("COUNTER", 410, 115);
	   text("LOBBY", 240, 360);
	   text("CUST SERV", 680, 360);
	   
	// load the images, if we have started!
	if (start) {
		// 1. load the AC images
	    images[0].resize(50, 50);
	    
		image (images[0], 200, 0);
		image (images[0], 625, 0);
		
		image (images[0], 0, 235);
		image (images[0], 0, 450);
		
		image (images[0], 850, 235);
		image (images[0], 850, 450);
		
		//2. load the sensor image
			
		image (images[2], 420, 5);
		image (images[3], 5, 345);
		image (images[3], 880, 345);
		
		//3. load the controller image
		images[1].resize(100, 100);
		image(images[1], 450, 170);
		}
	
	}
	

private void evaluateACsNeeded() {
	
		Space [] spaces = {counter, lobby, customerSP};
		 println ("Hour: " + time.hour());
		for (Space space : spaces) {
			
			// The temperatures to cool to considered
			float target = controlAC.getMinTemp();
			
			// Get population
			int pop = space.getPopulation();
			
			// declare constants
			double heatPerPerson = 130;  // Watts
			float density = 1.205f;  // kg/m3 -- air density
			float specificHeat = 1.006f; // kJ/kg.K --air specific heat capacity
									
			// calc variables
			double volumeFlowRate = (space.getVolume()* 0.2)/3600;   // m3/s
			float dT = ambientT - target;
			double e4cooling = 0;
			 
			 // 1. Determine energy e required to cool = energy to cool empty space to minTemp + heat energy from population
			 double e2coolKj = density*volumeFlowRate*specificHeat*dT + heatPerPerson*pop;               // kJ
			 // @FIXME: 1b if e > 0 goto 2, else goto 1
		     
			 // 2. Determine the ACs required for cooling based on 1 - either main, back-up or both
			 double e2coolBtu = e2coolKj * 1.055;				// 1 btu = 1.055kJ
			 
			 //3. we need AC to supply e2coolBtu in time2cool minutes, so
			 double requiredBtuPerHr = e2coolBtu; // * (60/maxTime2Cool);
			
			 //4. check which AC(s) has this 'power' and start it/them
			 if (space.getBackupAC().getCapacityBTU() >= requiredBtuPerHr) {
				space.getBackupAC().setOn(true);
				e4cooling = space.getBackupAC().getCapacityBTU();
				println(space.getName() + ": Backup chosen." + " Temp: " + ambientT + ", Population: " + pop);
			 }
			 else if (space.getMainAC().getCapacityBTU() >= requiredBtuPerHr) {
				 space.getMainAC().setOn(true);
				 e4cooling = space.getMainAC().getCapacityBTU();
				 println(space.getName() + ": Main chosen." + " Temp: " + ambientT + ", Population: " + pop);
			 }
			 else if (space.getBackupAC().getCapacityBTU()+ space.getMainAC().getCapacityBTU() >= requiredBtuPerHr) {
				 space.getBackupAC().setOn(true);
				 space.getMainAC().setOn(true);
				 e4cooling = space.getBackupAC().getCapacityBTU() + space.getMainAC().getCapacityBTU();
				 println(space.getName() + ": Both chosen." + " Temp: " + ambientT + ", Population: " + pop);
			 }
			 else {
				 displayMsg("ERROR: Insufficient capacity in AC for: " + space.getName(), DARK_RED);
				 println(space.getName() + ": Required: " + e2coolBtu + ", Available: " + 
				 (space.getBackupAC().getCapacityBTU() + space.getMainAC().getCapacityBTU())
				 + ". Temp: " + ambientT + ", Population: " + pop);
				 e4cooling = space.getBackupAC().getCapacityBTU() + space.getMainAC().getCapacityBTU();
				 //stopSimulation();
				 //return;
			 }
			 
			 // 5. Calculate energy consumed by AC for running for time t, based on the AC capacity
			 double eConsumed = (e4cooling/(EER*1000));                // in kWh, energy consumed for this hour
			 manEnergy += eConsumed;
			
			 
			 
			 
		}
			
		
	  
			
	}



private void changePopAndTemp(int hr) {
		// update population and temp data if it has changed
		if (inPop.get(hr)!= population) {
			population = inPop.get(hr);
			distributePopulation();
			ambientT = inTemp.get(hr);
			evaluateACsNeeded();
		}	
		
	}


private void distributePopulation() {
		// reset space population data and 
		// randomly distribute population among the three areas
		counter.setPopulation(0);
		lobby.setPopulation(0);
		customerSP.setPopulation(0);
		Random rand = new Random();
		for (int i=0; i < population; i++) {
			int next = rand.nextInt(3);  //generates 0, 1, or 2.
			if (next==0) lobby.setPopulation(counter.getPopulation()+1);
			else if (next==1) counter.setPopulation(lobby.getPopulation()+1);
			else customerSP.setPopulation(customerSP.getPopulation()+1);
		}
		
	}


private void updateSpaceTempBoxColors() {
	// a = ambient
	int rgb_a[] = calcRelativeColor(ambientT);
	int color_a = color(rgb_a[0], rgb_a[1], rgb_a[2]);
	cp5.get(Numberbox.class, "ambientTemp").setColorBackground(color_a);
	
	// c = counter
	int rgb_c[] = calcRelativeColor(counter.getSensor().getTemp());
	int color_c = color(rgb_c[0], rgb_c[1], rgb_c[2]);
	cp5.get(Numberbox.class, "counterTemp").setColorBackground(color_c);
	
	// l = lobby
	int rgb_l[] = calcRelativeColor(lobby.getSensor().getTemp());
	int color_l = color(rgb_l[0], rgb_l[1], rgb_l[2]);
	cp5.get(Numberbox.class, "lobbyTemp").setColorBackground(color_l);
	
	// cs = customer service
	int rgb_cs[] = calcRelativeColor(customerSP.getSensor().getTemp());
	int color_cs = color(rgb_cs[0], rgb_cs[1], rgb_cs[2]);
	cp5.get(Numberbox.class, "cspTemp").setColorBackground(color_cs);
	
	}

private int[] calcRelativeColor(float temp) {
	
		int[] rgb = new int[3];
		
		// Min and Max as specified in setup and used to initialize the controller by now
		float min = controlAC.getMinTemp();
		float max = controlAC.getMaxTemp();
		
		float avg = (max+min)/2;
		float ratio = 255/(max-min);
		
		rgb[0] = (int)(0 - (ratio*(avg-temp)));
		rgb[1] = (int)(255 + (ratio*(avg-temp)));
		rgb[2] = 0;
		
		// println("Temp: " + temp + ", R" + rgb[0] + " G" + rgb[1] );
		return rgb;
		
			
}



private void displayMsg(String msg, int color) {
	Textfield errorBox = cp5.get(Textfield.class, "errorMsg");
	errorBox.setText(msg);
	errorBox.setColor(color);
		
	}




	public void controlEvent(ControlEvent theEvent) {
		  
		String event = theEvent.getName();
		if (event.equals("dataSource")) {
	    	 int value = (int) cp5.get(RadioButton.class, "dataSource").getValue();
	    	  if (value == 1) chooseFile();
	    	  else if (value == 2) generateRandomData();
	    	  
	      }
		
		
	}
	
	private void generateRandomData() {
		// TODO Auto-generated method stub
		
	}



	private void chooseFile() {
		//Create a file chooser
		final JFileChooser fc = new JFileChooser(currDir);
		
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
		        "CSV Files", "csv");
		
		fc.setAcceptAllFileFilterUsed(false);
		fc.setFileFilter(filter);
				
		//In response to a button click:
		int returnVal = fc.showOpenDialog(this);
		
		
		if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            cp5.get(Textfield.class, "filePath").setText(file.getAbsolutePath());
            currDir = file.getPath();
            inFile = new File(file.getAbsolutePath());
            // getStringFromFile(file);
        } 
		else {
           
			displayMsg("WARNING: File selection was cancelled.", DARK_RED);
			cp5.get(RadioButton.class, "dataSource").setValue(0);
        }     
		
		
	}



	public void Start(int theValue) throws InterruptedException {
		String value = cp5.get(Button.class, "Start").getLabel().toUpperCase();
		boolean allOK = false;
		if (value.equals("START")) {
			//1st, check that everything is OK
			allOK = allInputOK();
			
			//If so, initialize objects with data supplied
			if (allOK) allOK = objectsInitialized();
			
			//If all goes well, we can go ahead and start simulation
			if (allOK) startSimulation();
			
					
		}
		else if (value.equals("STOP")) {
			// stop the simulation
			displayMsg ("EVENT: Simulation manually terminated via STOP button.", DARK_RED);
			event = "STOPPED";
			stopSimulation();
					
			
		}
			
		
		  
		 
		}



	private void stopSimulation() {
		// stop all code depending on start
		start = false;
		
		// Re-enable applicable controls to allow modifications
		//1. open the Radio buttons
		cp5.get(RadioButton.class, "simSpeed").open();
		cp5.get(RadioButton.class, "dataSource").open();
		
		//2. unlock all text fields to allow editing
		String [] fields = {"counterL", "counterB", "counterH", "lobbyL", "lobbyB", "lobbyH", "cspL", "cspB", "cspH", "minTemp", "maxTemp"};
			
		for (String cField: fields)                    	//cField = current field
		{							
			// get each field and...unlock it!
			cp5.get(Textfield.class, cField).unlock();
		}
		
		// change button label to "Start"
		cp5.get(Button.class, "Start").setLabel("Start")
		.setColorBackground(DARK_GREEN)
        .setColorForeground(LIGHT_GREEN)
         ;
		
		
		
	}



	private void startSimulation() {
		
		// Disable applicable controls to prevent in-run modifications
		//1. close the Radio buttons
		cp5.get(RadioButton.class, "simSpeed").close();
		cp5.get(RadioButton.class, "dataSource").close();
		
		//2. lock all text fields to prevent further entry, in a loop
		String [] fields = {"counterL", "counterB", "counterH", "lobbyL", "lobbyB", "lobbyH", "cspL", "cspB", "cspH", "minTemp", "maxTemp"};
		
		for (String cField: fields)                    	//cField = current field
		{							
			// get each field and...lock it!
			cp5.get(Textfield.class, cField).lock();
		}
		
			
		// change button label to "Stop"
		cp5.get(Button.class, "Start").setLabel("Stop")
		.setColorBackground(DARK_RED)
         .setColorForeground(LIGHT_RED)
		;
		// start simulation: reset time, set simulation speed
		start = true;
		time.reset();
		
		float speed = cp5.get(RadioButton.class, "simSpeed").getValue();
		time.setSpeedOfTime(speed);
		
		// set current event
		event = "STARTED";
		
		// evaluate ACs needed for first time
		evaluateACsNeeded();
		
	}



	private boolean objectsInitialized() {
		event = "INITIALIZING...";
		boolean dataFileOK = true;
		// Initialize temperature and population data from inFile, if not null
		// returns true is the initialization was successful, false otherwise
		if (inFile != null) {
			dataFileOK = fileDataReadOK(inFile);
			if (!dataFileOK) return false;
		}
		
		// initialize ACController
		float min = Float.parseFloat(cp5.get(Textfield.class, "minTemp").getText());
		float max = Float.parseFloat(cp5.get(Textfield.class, "maxTemp").getText());
		controlAC = new ACController(min, max);
		
		// initialize Space Objects
		double counterL = Double.parseDouble(cp5.get(Textfield.class, "counterL").getText());
		double counterB = Double.parseDouble(cp5.get(Textfield.class, "counterB").getText());
		double counterH = Double.parseDouble(cp5.get(Textfield.class, "counterH").getText());
		counter = new Space(counterL, counterB, counterH, "Counter");
		
		double lobbyL = Double.parseDouble(cp5.get(Textfield.class, "lobbyL").getText());
		double lobbyB = Double.parseDouble(cp5.get(Textfield.class, "lobbyB").getText());
		double lobbyH = Double.parseDouble(cp5.get(Textfield.class, "lobbyH").getText());
		lobby = new Space(lobbyL, lobbyB, lobbyH, "Lobby");
		
		double cspL = Double.parseDouble(cp5.get(Textfield.class, "cspL").getText());	
		double cspB = Double.parseDouble(cp5.get(Textfield.class, "cspB").getText());	
		double cspH = Double.parseDouble(cp5.get(Textfield.class, "cspH").getText());
		customerSP = new Space (cspL, cspB, cspH, "CustomerSP");
		
		// initialize the time
		time  = new ControlTimer();
		
		// Initialize population and temperature data
		population = inPop.get(0);
		ambientT = inTemp.get(0);
		
		// Default all space temperatures to ambientT
		counter.getSensor().setTemp(min);
		lobby.getSensor().setTemp(min);
		customerSP.getSensor().setTemp(min);
		
		// Distribute population randomly among the Spaces
		distributePopulation();
		
		return true;
	}



	



	private boolean allInputOK() {
		boolean ret = true;
		event = "VALIDATING...";
		// clear the message box
		displayMsg("", DARK_RED);
		// check if simulation speed has been selected:
		float speed = cp5.get(RadioButton.class, "simSpeed").getValue();
		if (speed <= 0.0){   												// none selected
			displayMsg("ERROR: Please choose a simulation speed.", DARK_RED);
			return false;
		}
		
		// check if data source has been selected:
		String path = cp5.get(Textfield.class, "filePath").getText();
		if (!path.equals("")) cp5.get(RadioButton.class, "dataSource").setValue(1);
		
	
		float source = cp5.get(RadioButton.class, "dataSource").getValue();
		if (source <= 0.0){													// none selected
			displayMsg("ERROR: Please choose a data source.",DARK_RED);
			return false;
		}
		
		// Now we check all text fields for proper entry, in a loop
		String [] fields = {"counterL", "counterB", "counterH", "lobbyL", "lobbyB", "lobbyH", "cspL", "cspB", "cspH", "minTemp", "maxTemp"};
		
		// get and check the content (text) of each field...
		for (String cField: fields)                    	//cField = current field
		{							
			Textfield field = cp5.get(Textfield.class, cField);
			float value = 0;
			
			// add call callback event to restore background to white on mouse "click" on the fields
			// ...in case field is flagged and background changed to red
			field.addCallback(new CallbackListener() {
			    public void controlEvent(CallbackEvent theEvent) {
			      if (theEvent.getAction()==ControlP5.ACTION_ENTER) {
			    	  field.setColorBackground(WHITE);
			      }
			    }
			  }
			  );
			
			// try converting the field to a float value
			try 
			{
				value = Float.parseFloat(field.getText());
			// if error, then a numeric value was not entered
			} catch (NumberFormatException e) 
			{
				// display warning and set background to red
				displayMsg("ERROR: Please enter numeric value(s) in the red box(es).", DARK_RED);
				field.setColorBackground(LIGHT_RED);
				ret = false;									// we need to return false
				
			}
			// We are not accepting negative values!
			if (value < 0){
				displayMsg("ERROR: Please enter positive value(s) in the red box(es).", DARK_RED);
				field.setColorBackground(LIGHT_RED);
				ret = false;
			}
		}
		// if any of the field has issues then abort and return false!
		if (!ret) return false;
		
		// Now, we check that min temp <= max temp (by now we are sure the values are numeric)
		float min = Float.parseFloat(cp5.get(Textfield.class, "minTemp").getText());
		float max = Float.parseFloat(cp5.get(Textfield.class, "maxTemp").getText());
		if (min > max) {
			displayMsg("ERROR: Min temp cannot be greater than max temp.", DARK_RED);
			return false;
		}
		
		return true;
	}
	private boolean fileDataReadOK(File file) {
		try {
			BufferedReader buf = new BufferedReader(new FileReader(file));
			String str, error;
			String [] splitStr;
			inTemp = new ArrayList<Float> ();
			inPop = new ArrayList<Integer> ();
			for (int loop = 0; loop <= WORKINGHOURS; loop++) {
				str = buf.readLine();
				// println(str);
				if (loop > 0) {
					splitStr = str.split(",");
					try {
						inTemp.add(Float.parseFloat(splitStr[1]));
					} catch (NumberFormatException e) {
						error = "ERROR: Invalid temperature data in input file for hour: " + loop;
						displayMsg(error, DARK_RED);
						buf.close();
						return false;
					}
					catch (ArrayIndexOutOfBoundsException e) {
						error = "ERROR: Missing temperature data in input file for hour: " + loop;
						displayMsg(error, DARK_RED);
						buf.close();
						return false;
					}
					try {
						inPop.add(Integer.parseInt(splitStr[2]));
					} catch (NumberFormatException e) {
						error = "ERROR: Invalid population data in input file for hour: " + loop;
						displayMsg(error, DARK_RED);
						buf.close();
						return false;
					}
					catch (ArrayIndexOutOfBoundsException e) {
						error = "ERROR: Missing population data in input file for hour: " + loop;
						displayMsg(error, DARK_RED);
						buf.close();
						return false;
					}
										
				}
				
			}
			
			buf.close();
			// println("Temp: " + inTemp + " Pop: " + inPop);
		
			return true;
		}
		catch(FileNotFoundException ex) {
			// file not found
			String error = "ERROR: File not found!";
			displayMsg(error, DARK_RED);
			return false;
			
		}
		catch(IOException ex) {
			String error = "ERROR: unknown I/O error while reading file!";
			displayMsg(error, DARK_RED);
			return false;
		}
	}
	public static void main (String[] args) {
		//Add main method for running as application
		PApplet.main(new String[] {"--present", "bankingHall.BankingHall"});
	}	
}
