package evolHAEA;

import unalcol.search.selection.Selection;
import unalcol.search.solution.Solution;
import unalcol.random.util.Shuffle;

public class NUniqueIndividuals<T> implements Selection<T> {

	private Shuffle s = new Shuffle();
	
	public int[] apply(int n, Solution<T>[] x) {
		
		int[] shuffled = s.apply(x.length);
		
		int[] indices = new int[n];
		
		for (int i=0;i<n;i++){
			indices[i] = shuffled[i];
		}	
		
		return indices;
	}

	public int choose_one(Solution<T>[] arg0) {
		return 0;
	}
	
	

}
