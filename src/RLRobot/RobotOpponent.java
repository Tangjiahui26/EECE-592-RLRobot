package RLRobot;

import java.awt.geom.*; 

public class RobotOpponent {
	
	  String name; 
	  public double bearing; 
	  public double head; 
	  public long ctime; 
	  public double speed; 
	  public double x, y; 
	  public double distance; 
	  public double changeheading; 
	  public double energy; 
	 
	  public Point2D.Double position_guess(long when) 
	  { 
	    double diff = when - ctime; 
	    double newY, newX; 
	 
	    /**if the change in heading is significant, use circular targeting**/ 
	    if (Math.abs(changeheading) > 0.00001) 
	    { 
	      double radius = speed/changeheading; 
	      double tothead = diff * changeheading; 
	      newY = y + (Math.sin(head + tothead) * radius) - (Math.sin(head) * radius); 
	      newX = x + (Math.cos(head) * radius) - (Math.cos(head + tothead) * radius); 
	    } 
	    /**If the change in heading is insignificant, use linear**/ 
	    else { 
	      newY = y + Math.cos(head) * speed * diff; 
	      newX = x + Math.sin(head) * speed * diff; 
	    } 
	    return new Point2D.Double(newX, newY); 
	  } 
}
