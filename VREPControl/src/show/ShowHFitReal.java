package show;

import evolHAEA.HEmP;
import simvrep.EvaluatorMT;
import simvrep.Simulation;
import util.ChromoConversion;

public class ShowHFitReal {

	public static void main(String[] args) {
		
		Simulation sim = new Simulation(0, 10);
		double fitness =1000;
		String morpho = "[(0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,0.0 , 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0]";
		// [( 'Type' 0.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 'Conn' 1.0, 3.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 'DadFace' 0.0, 3.0, 2.0, 1.0, 2.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 'Ori'0.0 , 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0]
		// Type: Only two types available 0 and 1, the root is always 0 and the rest are always 1
		// Conn: The number of modules connected to that module, the first value are modules connected to the root module and so on
		// DadFace: The face to which the module is connected in the parent, root module only has 0,1,2 faces the rest have 1,2,3 since 0 is always occupied by the parent
		// Ori: The orientation of the module, only two orientations available 0 and 1
		// Each position is evaluated after the other: For each module with a type, the number of modules is retrieved, then the children are retrieved and connected to the specified dadfaces, this continues until no more children are found
		//float[] parameters = new float[212];
		double[] parameters = new double[282];
		
//		char[][] subenvperm = new char[][] { { 's', 'l', 's', 'b', 's', 'r', 's' },
//			{ 's', 'l', 's', 's', 'r', 's', 'b' }, { 'b', 's', 'l', 's', 's', 'r', 's' },
//			{ 'b', 's', 'r', 's', 's', 'l', 's' }, { 's', 'r', 's', 's', 'l', 's', 'b' },
//			{ 's', 'r', 's', 'b', 's', 'l', 's' }, { 's', 's' } };
//			
//		float width = 0.7f;
		
		for (int i = 0;i<parameters.length;i++){
			parameters[i]=0;
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
		
		HEmP opfunc = new HEmP(0.7f,sim,morpho,2,true);
		fitness = opfunc.apply(parameters);
		
		System.out.println("Fitness: "+fitness);
		
		sim.Disconnect();
		
		
	}
	
}
