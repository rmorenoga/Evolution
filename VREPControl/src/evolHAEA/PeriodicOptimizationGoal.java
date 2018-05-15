package evolHAEA;

import emst.evolution.search.multithread.MultithreadOptimizationGoal;
import unalcol.optimization.OptimizationFunction;

public class PeriodicOptimizationGoal<T> extends MultithreadOptimizationGoal<T> {

	public PeriodicOptimizationGoal(OptimizationFunction<T> function, boolean minimize, double goal_value) {
		super(function, minimize, goal_value);
		// TODO Auto-generated constructor stub
	}

	public PeriodicOptimizationGoal(OptimizationFunction<T> function, boolean minimize) {
		super(function, minimize);
		// TODO Auto-generated constructor stub
	}

	public PeriodicOptimizationGoal(OptimizationFunction<T> function) {
		super(function);
		// TODO Auto-generated constructor stub
	}
	
	public OptimizationFunction<T> getFunction(){
		return this.function;
	}

}
