package evolHAEA;

import hill.HillMAS;
import unalcol.descriptors.Descriptors;
import unalcol.descriptors.WriteDescriptors;
import unalcol.evolution.haea.HAEA;
import unalcol.evolution.haea.HaeaOperators;
import unalcol.evolution.haea.SimpleHaeaOperators;
import unalcol.io.Write;
import unalcol.optimization.OptimizationFunction;
import unalcol.optimization.OptimizationGoal;
import unalcol.optimization.real.HyperCube;
import unalcol.optimization.real.mutation.AdaptMutationIntensity;
import unalcol.optimization.real.mutation.GaussianMutation;
import unalcol.optimization.real.mutation.IntensityMutation;
import unalcol.optimization.real.mutation.OneFifthRule;
import unalcol.optimization.real.xover.LinearXOver;
import unalcol.search.Goal;
import unalcol.search.Solution;
import unalcol.search.SolutionDescriptors;
import unalcol.search.population.variation.ArityTwo;
import unalcol.search.population.variation.Operator;
import unalcol.search.selection.Tournament;
import unalcol.search.space.Space;
import unalcol.tracer.ConsoleTracer;
import unalcol.tracer.Tracer;
import unalcol.types.real.array.DoubleArray;
import unalcol.types.real.array.DoubleArrayPlainWrite;

public class HAEAParallel {
	
	public static void main(String[] args) {
		
		//Search Space Definition 
				int DIM = 3;
				double[] min = DoubleArray.create(DIM,-1);
				double[] max = DoubleArray.create(DIM,1);
				
			Space<double[]> space = new HyperCube(min,max);
			
			//Optimization function
			OptimizationFunction<double[]> function = new HMAS(2);
			Goal<double[]> goal = new OptimizationGoal<double[]>(function);
			goal.setMaxThreads(5);
			
			//Variation Definition
			AdaptMutationIntensity adapt = new OneFifthRule(20,0.9);
			IntensityMutation variation = new GaussianMutation(0.1,null,adapt);
			ArityTwo<double[]> xover = new LinearXOver();
			
			int POPSIZE = 10;
			int MAXITERS = 2;
			Operator<double[]>[] opers = (Operator<double[]>[])new Operator[2];
			opers[0] = variation;
			opers[1] = xover;
			
			HaeaOperators<double[]> operators = new SimpleHaeaOperators<double[]>(opers);
			HAEA<double[]> search = new HAEA<double[]>(POPSIZE,operators, new Tournament<double[]>(4), MAXITERS);
			
			//Track Individuals and Goal Evaluations
			SolutionDescriptors<double[]> desc = new SolutionDescriptors<double[]>();
			Descriptors.set(Space.class,desc);
			DoubleArrayPlainWrite write = new DoubleArrayPlainWrite(',');
			Write.set(double[].class, write);
			WriteDescriptors w_desc = new WriteDescriptors();
			Write.set(Space.class, w_desc);
			
			ConsoleTracer tracer = new ConsoleTracer();
			Tracer.addTracer(search, tracer);
			
			Solution<double[]> solution = search.apply(space, goal);
			
			System.out.println(solution.quality());
			
			
			
	}

}
