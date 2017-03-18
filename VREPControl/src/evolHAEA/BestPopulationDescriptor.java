package evolHAEA;

import unalcol.descriptors.Descriptors;
import unalcol.search.Goal;
import unalcol.search.population.Population;
import unalcol.types.real.array.DoubleArray;

public class BestPopulationDescriptor<T> extends Descriptors<Population<T>> {

	public double[] descriptors(Population<T> pop){
		String gName = Goal.class.getName();
		double best = Double.MAX_VALUE;
		int index = -1;
		double[] quality = new double[pop.size()];
		for(int i = 0;i<quality.length;i++){
			quality[i] = (Double) pop.get(i).info(gName);
			if(best>quality[i]){
				best = quality[i];
				index = i;
			}
		}
		double[] statistics = DoubleArray.statistics_with_median(quality).get();
		double[] individual = (double[]) pop.get(index).object();
		return join(statistics,individual);
		
	}
	
	double[] join(double[] x1, double[] x2){
		double[] result = new double[x1.length+x2.length];
		for(int i = 0; i<x1.length;i++ ){
			result[i] = x1[i];
		}
		for(int i = 0; i < x2.length;i++)
			result[x1.length+ i] = x2[i];
		return result;
	}
	
}
