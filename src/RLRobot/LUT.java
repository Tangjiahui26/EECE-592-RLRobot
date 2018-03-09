package RLRobot;

import robocode.*;
import java.awt.*;
import java.awt.geom.*;
import java.io.*;

public class LUT implements LUTInterface {
		  
	public double[][] table; 
		 
	public LUT() 
		  { 
		    table = new double[State.numStates][Action.numActions]; 
		    initialiseLUT(); // set all function values to be zero
		  } 
		 
	 private void initialiseLUT()
		  { 
		    for (int i = 0; i < State.numStates; i++) 
		      for (int j = 0; j < Action.numActions; j++) 
		        table[i][j] = 0.0; 
		  } 
		 
		  // return the maximum value in the LUT
		  
	public double max_Q(int state) 
		  { 
		    double maxinum = Double.NEGATIVE_INFINITY; 
		    for (int i = 0; i < table[state].length; i++) 
		    { 
		      if (table[state][i] > maxinum) 
		        maxinum = table[state][i]; 
		    } 
		    return maxinum; 
		  }
	
	@Override
	 public int outputFor(int [] X) {
		 return 2;
	 }
	
	@Override	 
	public void train(int[] X, int argCurrentAction, double argReward) {
		
	 }
	@Override
	 public int indexFor(int [] X){
		 return 2;
	 }
		  
	public int getBestAction(int state) 
		  { 
		    double maxinum = Double.NEGATIVE_INFINITY; 
		    int bestAction = 0; 
		    for (int i = 0; i < table[state].length; i++) 
		    { 
		      double qValue = table[state][i];  
		      if (qValue > maxinum) 
		      { 
		        maxinum = qValue; 
		        bestAction = i; 
		      } 
		    } 
		    return bestAction; 
		  } 
		 
		  
	public double getQValue(int state, int action) 
		  { 
		    return table[state][action]; 
		  } 
		 
	public void setQValue(int state, int action, double value) 
		  { 
		    table[state][action] = value; 
		  } 
		 
		  
	@Override
	public void load(File file) 
		  { 
		    BufferedReader read = null; 
		    try 
		    { 
		      read = new BufferedReader(new FileReader(file)); 
		      for (int i = 0; i < State.numStates; i++) 
		        for (int j = 0; j < Action.numActions; j++) 
		          table[i][j] = Double.parseDouble(read.readLine()); 
		    } 
		    catch (IOException e) 
		    { 
		    //  System.out.println("IOException trying to open reader: " + e); 
		      initialiseLUT(); 
		    } 
		    catch (NumberFormatException e) 
		    { 
		      initialiseLUT(); 
		    } 
		    finally 
		    { 
		      try 
		      { 
		        if (read != null) 
		          read.close(); 
		      } 
		      catch (IOException e) 
		      { 
		     //   System.out.println("IOException trying to close reader: " + e); 
		      } 
		    } 
		  } 
		 
	@Override
	public void save(File file) 
		  { 
		    PrintStream write = null; 
		    try 
		    { 
		      write = new PrintStream(new RobocodeFileOutputStream(file)); 
		      for (int i = 0; i < State.numStates; i++) 
		        for (int j = 0; j < Action.numActions; j++) 
		          write.println(new Double(table[i][j])); 
		 
		      if (write.checkError()) 
		        System.out.println("Could not save the data!"); 
		      write.close(); 
		    } 
		    catch (IOException e) 
		    { 
		   //   System.out.println("IOException trying to write: " + e); 
		    } 
		    finally 
		    { 
		      try 
		      { 
		        if (write != null) 
		          write.close(); 
		      } 
		      catch (Exception e) 
		      { 
		  //      System.out.println("Exception trying to close witer: " + e); 
		      } 
		    } 
		  }  
}
