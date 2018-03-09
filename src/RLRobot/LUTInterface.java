package RLRobot;

/**
4 * Interface for the Look Up Table (LUT) Class
5 * You should 'implement' this interface
6 * @date 20 June 012
7 * @author sarbjit
8 *
9 */
public interface LUTInterface extends CommonInterface {

/**
13 * Constructor. (You will need to define one in your implementation)
14 * @param argNumInputs The number of inputs in your input vector
15 * @param argVariableFloor An array specifying the lowest value of each variable in the input vector.
16 * @param argVariableCeiling An array specifying the highest value of each of the variables in the input vector.
17 * The order must match the order as referred to in argVariableFloor.
18 *
19 public LUT (
20 int argNumInputs,
21 int [] argVariableFloor,
22 int [] argVariableCeiling );
23 */

 /**
26 * Initialise the look up table to all zeros.
27 */
//private void initialiseLUT();

 /**
31 * A helper method that translates a vector being used to index the look up table
32 * into an ordinal that can then be used to access the associated look up table element.
33 * @param X The state action vector used to index the LUT
34 * @return The index where this vector maps to
35 */
 public int indexFor(int [] X);

 } // End of public interface LUT