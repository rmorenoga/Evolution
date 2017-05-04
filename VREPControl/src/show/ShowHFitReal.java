package show;

import evolHAEA.HEmP;
import simvrep.EvaluatorMT;
import simvrep.Simulation;
import util.ChromoConversion;

public class ShowHFitReal {

	public static void main(String[] args) {
		
		Simulation sim = new Simulation(0, 30);
		double fitness =1000;
		String morpho = "[(0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 3.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,0.0, 3.0, 2.0, 1.0, 2.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,0.0 , 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0]";
		
		//float[] parameters = new float[212];
		double[] parameters = new double[212];
		
//		char[][] subenvperm = new char[][] { { 's', 'l', 's', 'b', 's', 'r', 's' },
//			{ 's', 'l', 's', 's', 'r', 's', 'b' }, { 'b', 's', 'l', 's', 's', 'r', 's' },
//			{ 'b', 's', 'r', 's', 's', 'l', 's' }, { 's', 'r', 's', 's', 'l', 's', 'b' },
//			{ 's', 'r', 's', 'b', 's', 'l', 's' }, { 's', 's' } };
//			
//		float width = 0.7f;
		
		for (int i = 0;i<parameters.length;i++){
			parameters[i]=0.5;
		}
		
		for (int i = 0; i < 5; i++) {
			if (sim.Connect()) {
				break;
			} else {
				// No connection could be established
				System.out.println("Failed connecting to remote API server");
				System.exit(-1);
			}
		}
		
		
//		if (morpho != null && !morpho.equals("")) {
//			double[] morphoDouble = ChromoConversion.str2double(morpho);
//			EvaluatorMT evaluator = new EvaluatorMT(morphoDouble, "defaultmh.ttt", parameters, sim,0.7f,2,subenvperm[6],width);
//			fitness = evaluator.evaluate();
//		}
		
		HEmP opfunc = new HEmP(0.7f,sim,morpho,2);
		fitness = opfunc.apply(parameters);
		
		System.out.println("Fitness: "+fitness);
		
		sim.Disconnect();
		
		
	}
	
}
