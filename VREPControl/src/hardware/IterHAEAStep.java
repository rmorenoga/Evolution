package hardware;

import unalcol.evolution.haea.HaeaOperators;
import unalcol.evolution.haea.HaeaReplacement;
import unalcol.evolution.haea.HaeaStep;
import unalcol.evolution.haea.HaeaVariation;
import unalcol.search.Goal;
import unalcol.search.population.Population;
import unalcol.search.selection.Selection;
import unalcol.search.space.Space;

public class IterHAEAStep<T> extends HaeaStep<T>{
	
	int iteration = 0;
	public Population<T> lastPop = null;

	public IterHAEAStep(int mu, HaeaVariation<T> variation, HaeaReplacement<T> replacement) {
		super(mu, variation, replacement);
		// TODO Auto-generated constructor stub
	}

	public IterHAEAStep(int mu, Selection<T> parent_selection, HaeaOperators<T> operators, boolean steady) {
		super(mu, parent_selection, operators, steady);
		// TODO Auto-generated constructor stub
	}

	public IterHAEAStep(int mu, Selection<T> parent_selection, HaeaOperators<T> operators) {
		super(mu, parent_selection, operators);
		// TODO Auto-generated constructor stub
	}

	public IterHAEAStep(int mu, Selection<T> parent_selection, HaeaReplacement<T> replacement) {
		super(mu, parent_selection, replacement);
		// TODO Auto-generated constructor stub
	}
	
	public Population<T> apply(Population<T> pop, Space<T> space) {
		String gName = Goal.class.getName();
		Goal goal = (Goal) pop.data(gName);
		//goal.
		((IterOptimizationGoal)goal).getFunction().update(iteration++);
		lastPop = super.apply(pop, space);
		return lastPop;
	}
	
	@Override
	public Population<T> init(Space<T> space, Goal<T, Double> goal) {
		lastPop = super.init(space, goal);
		return lastPop;
	}
	
	
}
