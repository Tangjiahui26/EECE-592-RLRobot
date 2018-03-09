package RLRobot;

//define the class of states
public class State
{
	  public static final int numHeading = 4; 
	  public static final int numTargetDistance = 4; 
	  public static final int numTargetBearing = 4; 
	  public static final int numXPosition = 8;
	  public static final int numYPosition = 6;
	  
	  public static final int numStates;
	  public static final int Mapping[][][][][];
	
	//Record the states of each execution
	  static {
	    Mapping = new int[numHeading][numTargetDistance][numTargetBearing][numXPosition][numYPosition];
	    int count = 0;
	    for (int a = 0; a < numHeading; a++)
	      for (int b = 0; b < numTargetDistance; b++)
	        for (int c = 0; c < numTargetBearing; c++)
	          for (int d = 0; d < numXPosition; d++)
		          for (int e = 0; e < numYPosition; e++)
	                 Mapping[a][b][c][d][e] = count++;
	    numStates = count;
	  }

	public static int getHeading(double arg){
	//4 
		int temp=0;
		if (arg>=0 && arg<(Math.PI/2)) temp=0;
		if (arg>=Math.PI/2 && arg<(Math.PI)) temp=1;
		if (arg>=Math.PI && arg<(Math.PI*3/2)) temp=2;
		if (arg>=(Math.PI*3/2)) temp =3;
		return temp;
	}
	public static int getTargetDistance(double arg){	
		//4 close, near, far, really far
		int temp=(int)(arg/100);
		if(temp>3) temp=3;
		return temp;
	}
	
	public static int getTargetBearing(double arg){
	//4 
		int temp=0;
		arg=arg+Math.PI;
		if (arg>=0 && arg<(Math.PI/2)) temp=0;
		if (arg>=Math.PI/2 && arg<(Math.PI)) temp=1;
		if (arg>=Math.PI && arg<(Math.PI*3/2)) temp=2;
		if (arg>=(Math.PI*3/2)) temp =3;
		return temp;
	}
	public static int getXPosition(double arg) {
		int temp=(int)(arg/100);
		if(temp >=0 && temp < 1) temp = 0;
		if(temp >=1 && temp < 2) temp = 1;
		if(temp >=2 && temp < 3) temp = 2;
		if(temp >=3 && temp < 4) temp = 3;
		if(temp >=4 && temp < 5) temp = 4;
		if(temp >=5 && temp < 6) temp = 5;
		if(temp >=6 && temp < 7) temp = 6;
		if(temp >=7 && temp <= 8) temp = 7;
		return temp;
	}
	public static int getYPosition(double arg) {
		int temp=(int)(arg/100);
		if(temp >=0 && temp < 1) temp = 0;
		if(temp >=1 && temp < 2) temp = 1;
		if(temp >=2 && temp < 3) temp = 2;
		if(temp >=3 && temp < 4) temp = 3;
		if(temp >=4 && temp < 5) temp = 4;
		if(temp >=5 && temp < 6) temp = 5;
		return temp;
	}

}