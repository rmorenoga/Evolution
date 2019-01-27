package hardware;

import unalcol.optimization.OptimizationFunction;
import unalcol.optimization.OptimizationGoal;

public class IterOptimizationGoal<T> extends OptimizationGoal<T> {

	public IterOptimizationGoal(OptimizationFunction<T> function) {
		super(function);
		// TODO Auto-generated constructor stub
	}

	public IterOptimizationGoal(OptimizationFunction<T> function, boolean minimize, double goal_value) {
		super(function, minimize, goal_value);
		// TODO Auto-generated constructor stub
	}

	public IterOptimizationGoal(OptimizationFunction<T> function, boolean minimize) {
		super(function, minimize);
		// TODO Auto-generated constructor stub
	}
	
	public OptimizationFunction<T> getFunction(){
		return this.function;
	}

}
