package RLRobot;

import robocode.*;
import java.awt.*;
import java.awt.geom.*;
import java.io.*;


/**
 * TJHRobot - a robot by Jiahui Tang
 */
public class TJHRobot extends AdvancedRobot {
	/**
	 * run: TJHRobot's default behavior
	 */
	 // Initialization 
	  private RobotOpponent RobotOpponent; 
	  private QLearning qlearning;
	 // private static LUT table;
	  public static double reward;
	  private double firePower; 
	  public static int rounds;
	  public static int trueRounds;
	  public static int wins;
	  public static int i=0;
	  public static int totalNumOfEpochs[] = new int[2000];
	  public static double totalWinRate[] = new double[100];
	  public static double totalRewards[] = new double[2000];
	  public static double totalQError[] = new double[2000];
	  
	  public static int instantReward = 1; // if instantReward is 1, intermediate rewards; otherwise terminal rewards
	  public static int option = 0; //if option is 0, random selection; otherwise greedy
	  public static int off_policy = 0; // if 1, off-policy, otherwise on_policy
	  public static double total_rewards=0.0;
	  public static double epsilon=1.0; // if 1, then pure greedy; otherwise epsilon-greedy
	  
	  public double goodTerminalReward=0.5;
	  public double goodInstantReward=0.02;
	  public double badTerminalReward=-0.5;
	  public double badInstantReward=-0.02;
	 // private LUT table = new LUT();
	  
	  public void run()
	  { // initialize states in LUT
		qlearning = new QLearning(new LUT()); 
	    loadData(); 
	    RobotOpponent = new RobotOpponent(); 
	    RobotOpponent.distance = 1000; //initialize the RobotOpponent at far distance
	    // Robot initialization
	    setColors(Color.pink, Color.green, Color.red); 
	    setAdjustGunForRobotTurn(true); 
	    setAdjustRadarForGunTurn(true); 
	    turnRadarRightRadians(2 * Math.PI); 
	    
	    while (true) // each iteration
	    {   
    	  		int state = getState();
    	  		//select the action: epsilon-greedy
    	  		double randomNum = Math.random();
	    
	        System.out.println("epsilon:"+epsilon);
	        if (randomNum < epsilon){
	        	option = 0;
	        }
	        else {
	        	option = 1;
	        }
	     
	        int action = qlearning.selectAction(state, option); 
    	  		qlearning.Qlearn(state, action, reward, off_policy); 
    	  		reward = 0.0;
            // five actions are available to choose from
    	  		switch (action) 
    	  			{ 
    	  				case Action.ahead:
    	  					setAhead(Action.aheadDistance); 
    	  					break;
    	  				case Action.back:
    	  					setBack(Action.backDistance);
    	  					break;
    	  				case Action.turnLeft:
    	  					setTurnLeft(Action.turnDegree);
    	  					break;
    	  				case Action.turnRight:
    	  					setTurnRight(Action.turnDegree);
    	  					break;
    	  				case Action.fire:
    	  					fire(1);
    	  					break;
    	  		} 
	    		radarAction();
	    		gunAction(2);
	      //System.out.println(reward);
		    execute(); 	
		 }
	    
	  } 
      
	  private void radarAction() 
	  { 
	    double radarRotate; 
	    //if RobotOpponent has not been detected for longer than 4 seconds
	    if (getTime() - RobotOpponent.ctime > 4) { 
	    //rotate the radar one turn to find an RobotOpponent
	      radarRotate = 4*Math.PI;				
	    } 
	    else { 
	      //calculate the degrees of rotation
	      radarRotate = getRadarHeadingRadians() - (Math.PI/2 - Math.atan2(RobotOpponent.y - getY(),RobotOpponent.x - getX())); 
	      radarRotate = nomslizeBearing(radarRotate); 
	      if (radarRotate < 0) 
	        radarRotate -= Math.PI/10; 
	      else 
	        radarRotate += Math.PI/10; 
	    } 
	    //turn the radar 
	    setTurnRadarLeftRadians(radarRotate); 
	  } 
	 
	  private void gunAction(double power) 
	  { 
	    long current_time; 
	    long next_time; 
	    Point2D.Double target_position; 
	    target_position = new Point2D.Double(RobotOpponent.x, RobotOpponent.y); 
	    for (int i = 0; i < 20; i++) 
	    { // time it takes for the bullet to reach the RobotOpponent, bullet velocity is 20-3*firepower
	      next_time = (int)Math.round((distance(getX(),getY(),target_position.x,target_position.y)/(20-(3*firePower)))); 
	      current_time = getTime() + next_time; 
	      target_position = RobotOpponent.position_guess(current_time); 
	    } 
	    //offsets the gun by the angle to the next shot based on linear guessing
	    double gunOffset = getGunHeadingRadians() - (Math.PI/2 - Math.atan2(target_position.y - getY(),target_position.x -  getX())); 
	    setTurnGunLeftRadians(nomslizeBearing(gunOffset));
	    if (getGunHeat() == 0) { 
		      setFire(power); 
		    }
	  } 
	  
	  private int getState() 
	  { 
		  int heading = State.getHeading(getHeading());
		    int targetDistance = State.getTargetDistance(RobotOpponent.distance);
		    int targetBearing = State.getTargetBearing(RobotOpponent.bearing);
			  int xPosition = State.getXPosition(getX());
			  int yPosition = State.getYPosition(getY());

		    int state = State.Mapping[heading][targetDistance][targetBearing][xPosition][yPosition];
		    return state;
		}
	   
	 
	  public void onScannedRobot(ScannedRobotEvent e) 
	  { 
		  
	    if ((e.getDistance() < RobotOpponent.distance)||(RobotOpponent.name == e.getName())) 
	    { 
	      //obtain the absolute bearing to the point where the RobotOpponent is 
	      double absbearing_rad = (getHeadingRadians()+e.getBearingRadians())%(2*Math.PI); 
	      //this section collects all the information about our RobotOpponent 
	      RobotOpponent.name = e.getName(); 
	      double h = nomslizeBearing(e.getHeadingRadians() - RobotOpponent.head); 
	      h = h/(getTime() - RobotOpponent.ctime); 
	      RobotOpponent.changeheading = h; 
	      RobotOpponent.x = getX()+Math.sin(absbearing_rad)*e.getDistance();
	      RobotOpponent.y = getY()+Math.cos(absbearing_rad)*e.getDistance();
	      RobotOpponent.bearing = e.getBearingRadians(); 
	      RobotOpponent.head = e.getHeadingRadians(); 
	      RobotOpponent.ctime = getTime();				//game time at which this scan was produced 
	      RobotOpponent.speed = e.getVelocity(); 
	      RobotOpponent.distance = e.getDistance(); 
	      RobotOpponent.energy = e.getEnergy(); 
	 
	    } 
	  } 
	  
	  double nomslizeBearing(double ang) { 
	    if (ang > Math.PI) 
	      ang -= 2*Math.PI; 
	    if (ang < -Math.PI) 
	      ang += 2*Math.PI; 
	    return ang; 
	  } 
	 
	  double nomalizeHeading(double ang) { 
	    if (ang > 2*Math.PI) 
	      ang -= 2*Math.PI; 
	    if (ang < 0) 
	      ang += 2*Math.PI; 
	    return ang; 
	  } 
	 
	  //compute the distance between two x,y coordinates 
	  public double distance( double x1,double y1, double x2,double y2 ) 
	  { 
	    double distance = Math.sqrt( (x2-x1)*(x2-x1) + (y2-y1)*(y2-y1) ); 
	    return distance; 
	  } 
	 
	  /* events */
	  public void onBulletHit(BulletHitEvent e) 
	  { 
		if (instantReward == 1){
		    if (RobotOpponent.name == e.getName()) 
	    { 
	      reward += goodInstantReward; 
	    }
		}
	  }
	 
	  public void onBulletMissed(BulletMissedEvent e) 
	  { 
		if (instantReward == 1){
	    reward += badInstantReward; 
	  }

	  }
	 
	  public void onHitByBullet(HitByBulletEvent e) 
	  { 
		if (instantReward == 1){
	    if (RobotOpponent.name == e.getName()) 
	    { 
	      reward += badInstantReward; 
	    } 
	  }
	  }
	 
	  public void onHitWall(HitWallEvent e) 
	  { 
	    if (instantReward == 1){
	    reward += badInstantReward/2;
	    }
	  } 
	 
	  public void onRobotDeath(RobotDeathEvent e) 
	  { 
	 
	    if (e.getName() == RobotOpponent.name) 
	      RobotOpponent.distance = 1000; 
	  } 
	 
	  public void loadData() 
	  { 
	    try 
	    { 
	      qlearning.lookupQ.load(getDataFile("LUT.dat")); 
	    } 
	    catch (Exception e) 
	    { 
	    } 
	  } 
	 
	  public void saveData() 
	  { 
	    try 
	    { 
	      qlearning.lookupQ.save(getDataFile("LUT.dat")); 
	    } 
	    catch (Exception e) 
	    { 
	      out.println("Exception trying to write: " + e); 
	    } 
	  } 
	  
	  // save results 
	  public void onWin(WinEvent event) 
	  { 
	    saveData(); 
	    reward += goodTerminalReward;
	    wins +=1;
	    rounds +=1;
	    trueRounds += 1;
	    total_rewards +=reward;
	    if (rounds  == 99) {
	  	 totalNumOfEpochs[i]=i;
	  	  totalWinRate[i]=(double)(wins+1)/100;
	  	//  totalRewards[i]=total_rewards;
		//  totalQError[i] = Math.abs(qlearning.QError);
	  	  total_rewards = 0;
	    	  i += 1;
	  	 // System.out.println(i);
	  	  wins=0;
	  	  rounds=0;  
	    }
	    if (trueRounds == 1999) {
	      	PrintStream k = null; 
	        try 
	        { 
	       
	          k = new PrintStream(new RobocodeFileOutputStream(getDataFile("win_rates.dat"))); 
	          for (int i = 0; i < 20; i++) 
	              k.println(new Double(totalWinRate[i])); 
	          if (k.checkError()) 
	            System.out.println("Could not save the data!"); 
	          k.close(); 
	          
	        } 
	        catch (IOException e) 
	        { 
	          System.out.println("IOException trying to write: " + e); 
	        } 
	        finally 
	        { 
	          try 
	          { 
	            if (k != null) 
	              k.close(); 
	          } 
	          catch (Exception e) 
	          { 
	            System.out.println("Exception trying to close witer: " + e); 
	          } 
	        }
	    }
	    
	  } 
	 
	  public void onDeath(DeathEvent event) 
	  { 
	    saveData(); 
	    reward += badTerminalReward;
	    rounds += 1;
	    trueRounds += 1;
	    total_rewards +=reward;
	    if (rounds == 99) {
	    	  totalNumOfEpochs[i]=i;
	    	  totalWinRate[i]=(double)(wins)/100;
	    	 // totalRewards[i]=total_rewards;
	    //	  totalQError[i] = Math.abs(qlearning.QError);
	    	  total_rewards = 0;
	      i += 1;
	    	  wins=0;
	    	  rounds=0;  
	      }
	      if (trueRounds == 1999) {
	        	PrintStream k = null; 
	          try 
	          { 
	        	 
	            k = new PrintStream(new RobocodeFileOutputStream(getDataFile("win_rates.dat"))); 
	            for (int i = 0; i < 20; i++) 
	                k.println(new Double(totalWinRate[i])); 
	            if (k.checkError()) 
	              System.out.println("Could not save the data!"); 
	            k.close();  	  
	       
	          } 
	          catch (IOException e) 
	          { 
	            System.out.println("IOException trying to write: " + e); 
	          } 
	          finally 
	          { 
	            try 
	            { 
	              if (k != null) 
	                k.close(); 
	            } 
	            catch (Exception e) 
	            { 
	              System.out.println("Exception trying to close witer: " + e); 
	            } 
	          }
	      }
	  } 
	 

	}
