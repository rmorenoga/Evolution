package mixed;

import emst.evolution.json.haea.JSONHaeaStepManager;
import unalcol.evolution.haea.HaeaOperators;
import unalcol.search.population.Population;

public class JSONMixedGenomeManager extends JSONHaeaStepManager<MixedGenome> {

	@Override
	public void addStep(HaeaOperators<MixedGenome> operators, Population<MixedGenome>... pops) {
		if(steps.size() == 0)
			steps.add(new JSONMixedGenome(pops[PARENTPOP], operators, pops[PARENTPOP]));
		steps.add(new JSONMixedGenome(pops[PARENTPOP], operators, pops[OFFSPRINGPOP]));
	}
	
	

}
