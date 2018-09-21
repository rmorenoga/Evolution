package evolHAEA;

import emst.evolution.haea.ModifiedHaeaReplacement;
import emst.evolution.haea.ModifiedHaeaStep;
import emst.evolution.haea.ModifiedHaeaVariation;
import unalcol.evolution.haea.HaeaOperators;
import unalcol.optimization.OptimizationFunction;
import unalcol.search.Goal;
import unalcol.search.population.Population;
import unalcol.search.selection.Selection;
import unalcol.search.space.Space;

public class PeriodicHAEAStep<T> extends ModifiedHaeaStep<T> {
	int iteration = 0;
	public Population<T> lastPop = null;

	public PeriodicHAEAStep(int mu, ModifiedHaeaVariation<T> variation, ModifiedHaeaReplacement<T> replacement) {
		super(mu, variation, replacement);
		// TODO Auto-generated constructor stub
	}

	public PeriodicHAEAStep(int mu, Selection<T> parent_selection, HaeaOperators<T> operators, boolean steady) {
		super(mu, parent_selection, operators, steady);
		// TODO Auto-generated constructor stub
	}

	public PeriodicHAEAStep(int mu, Selection<T> parent_selection, HaeaOperators<T> operators) {
		super(mu, parent_selection, operators);
		// TODO Auto-generated constructor stub
	}

	public PeriodicHAEAStep(int mu, Selection<T> parent_selection, ModifiedHaeaReplacement<T> replacement) {
		super(mu, parent_selection, replacement);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Population<T> apply(Population<T> pop, Space<T> space) {
		String gName = Goal.class.getName();
		Goal goal = (Goal) pop.data(gName);
		//goal.
		((PeriodicOptimizationGoal)goal).getFunction().update(iteration++);
		lastPop = super.apply(pop, space);
		return lastPop;
	}

	@Override
	public Population<T> init(Space<T> space, Goal<T, Double> goal) {
		lastPop = super.init(space, goal);
		return lastPop;
	}
	
	
	

}
