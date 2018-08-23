package incremental;

import unalcol.algorithm.iterative.ForLoopCondition;
import unalcol.search.Goal;
import unalcol.search.RealQualityGoal;
import unalcol.search.population.Population;
import unalcol.sort.Order;

public class ForLoopFitnessCondition<T> extends ForLoopCondition<Population<T>> {

	double maxFitness;
	
	public ForLoopFitnessCondition(int iters, double maxFitness) {
		super(iters);
		this.maxFitness = maxFitness;
	}

	@Override
	public boolean evaluate(Population<T> pop) {
		return super.evaluate(pop) || evaluateFitness(pop);
	}

	private boolean evaluateFitness(Population<T> pop) {
		String gName = Goal.class.getName();
    	@SuppressWarnings("unchecked")
		RealQualityGoal<T> goal = (RealQualityGoal<T>)pop.data(gName);
    	Order<Double> order = goal.order();
    	for(Double fitness: goal.apply(pop.object()))
    		if( order.compare(maxFitness, fitness) <= 0)
    			return true;
		return false;
	}
	
	

}
