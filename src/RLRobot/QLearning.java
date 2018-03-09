package RLRobot;

public class QLearning {

		  public static final double learningRate = 0.1; 
		  public static final double discountFactor = 0.9; 
		  private int previousState; 
		  private int previousAction; 
		  public LUT lookupQ; 
		  public double QError;
		  public QLearning(LUT lookupQ) 
		  { 
		    this.lookupQ = lookupQ; 
		  } 
		 
		  //Q-Learning algorithm is applied to update LUT, either off-policy or on-policy
		  public void Qlearn(int current_state, int current_action, double reward, int off_policy) 
		  { 
		    double last_Q = lookupQ.getQValue(previousState, previousAction); 
		    if (off_policy == 1){
		    		QError = learningRate * (reward + discountFactor * lookupQ.max_Q(current_state)-last_Q);
		    		double current_Q = last_Q + QError;
		    		lookupQ.setQValue(previousState, previousAction, current_Q); 
		    }
		    else {
		    		QError = learningRate * (reward + discountFactor * lookupQ.getQValue(current_state, current_action)-last_Q); 
		    		double current_Q = last_Q + QError;
		    		lookupQ.setQValue(previousState, previousAction, current_Q); 
		    }
		    previousState = current_state; 
		    previousAction = current_action; 
		  } 
		  
		  //epsilon-greedy action selection 
		  public int selectAction(int state, int option) 
		  {
		    if (option == 0) {
		    return 0 + (int)(Math.random()*5); //pure random
		    }
		    else{ 
		      return lookupQ.getBestAction(state); //pure greedy
		    }
		  } 
		  
		}

