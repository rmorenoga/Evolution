package hardware;

import unalcol.evolution.haea.HaeaOperators;
import unalcol.evolution.haea.HaeaReplacement;
import unalcol.search.Goal;
import unalcol.search.population.Population;
import unalcol.sort.Order;
import unalcol.search.RealQualityGoal;
import unalcol.search.solution.Solution;

public class CHaeaReplacement<T> extends HaeaReplacement<T> {

	public CHaeaReplacement(HaeaOperators<T> operators, boolean steady) {
		super(operators, steady);
		// TODO Auto-generated constructor stub
	}

	public CHaeaReplacement(HaeaOperators<T> operators) {
		super(operators);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Population<T> apply(Population<T> current, Population<T> next) {
		String gName = Goal.class.getName();
    	@SuppressWarnings("unchecked")
		RealQualityGoal<T> goal = (RealQualityGoal<T>)current.data(gName);
    	//next.set(gName,goal);
    	Order<Double> order = goal.order();
        int k=0;
		Solution<T>[] buffer = (Solution<T>[])tagged_array(current.size());
        for( int i=0; i<current.size(); i++){
            //@TODO: Change the elitism here
            int sel = k; 
            double qs = (Double)next.get(sel).info(gName);
            k++;
            for(int h=1; h<operators.getSizeOffspring(i); h++){
                double qk = (Double)next.get(k).info(gName);
                if( order.compare(qk, qs) > 0 ){
                    sel = k;
                    qs = qk;
                }
                k++;
            }
            double qi = (Double)current.get(i).info(gName);
            if( order.compare(qi, qs) < 0)
                operators.reward(i);
            else
                operators.punish(i);
            
            if( !steady || order.compare(qi, qs) <= 0)
                buffer[i] = next.get(sel);
            else
                buffer[i] = current.get(i);
            
        }
        return new Population<T>(buffer,goal);
	}
	
	

}
