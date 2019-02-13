package hardware;

import org.json.JSONObject;

import unalcol.evolution.haea.HaeaOperators;
import unalcol.evolution.haea.HaeaReplacement;
import unalcol.evolution.haea.HaeaStep;
import unalcol.evolution.haea.HaeaVariation;
import unalcol.search.Goal;
import unalcol.search.population.Population;
import unalcol.search.selection.Selection;
import unalcol.search.solution.Solution;
import unalcol.search.space.Space;

public class IterHAEAStep<T> extends HaeaStep<T>{
	
	int iteration = 0;
	public Population<T> lastPop = null;
	protected JSONObject resultLogger = null;

	public IterHAEAStep(int mu, HaeaVariation<T> variation, HaeaReplacement<T> replacement) {
		super(mu, variation, replacement);
		// TODO Auto-generated constructor stub
	}

	public IterHAEAStep(int mu, Selection<T> parent_selection, HaeaOperators<T> operators, boolean steady) {
		super(mu, parent_selection, operators, steady);
		// TODO Auto-generated constructor stub
	}

	public IterHAEAStep(int mu, Selection<T> parent_selection, HaeaOperators<T> operators,JSONObject resultLogger) {
		super(mu, new HaeaVariation<T>(parent_selection, operators),new CHaeaReplacement<T>( operators ));
		this.resultLogger = resultLogger;
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
		
		JSONObject popLog = new JSONObject(); 
		
		Solution<double[]>[] currentPop = new Solution[(int)(lastPop.size())]; 
		for(int i = 0; i < currentPop.length; i++)
    		currentPop[i] = (Solution)lastPop.get(i);
		
		double[][] currentPopD = new double[currentPop.length][];
		double[] currentPopFitness = new double[currentPop.length];
		for(int j = 0; j < currentPop.length; j++){
			currentPopD[j] = currentPop[j].object();
			currentPopFitness[j] = (double) currentPop[j].info(Goal.GOAL_TEST);
		}
		
		popLog.put("currentPop", currentPopD);
		popLog.put("currentPopFitness", currentPopFitness);
		
		resultLogger.put("Pop"+(iteration-1), popLog);		
		
		return lastPop;
	}
	
	@Override
	public Population<T> init(Space<T> space, Goal<T, Double> goal) {
		lastPop = super.init(space, goal);			
		return lastPop;
	}
	
	
}
