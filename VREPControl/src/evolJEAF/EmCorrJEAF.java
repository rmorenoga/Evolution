package evolJEAF;

import es.udc.gii.common.eaf.problem.objective.ObjectiveFunction;
import simvrep.Evaluator;
import util.ChromoConversion;

public class EmCorrJEAF extends ObjectiveFunction{

	public double evaluate(double[] values) {
		String morpho = "[(0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 3.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,0.0, 3.0, 2.0, 1.0, 2.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,0.0 , 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0]";
		float[] parameters = new float[values.length];
		double fitness = 0;
		for (int i = 0;i<values.length;i++){
			parameters[i] = (float)values[i];
		}
		if (morpho != null && !morpho.equals("")) {
			double[] morphoDouble = ChromoConversion.str2double(morpho);
			Evaluator evaluator = new Evaluator(morphoDouble,parameters);
			fitness = evaluator.evaluate();
		}
		return fitness;
	}
	
	public void reset() {

	}
}
