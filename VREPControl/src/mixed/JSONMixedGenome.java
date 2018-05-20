package mixed;

import org.json.simple.JSONObject;

import emst.evolution.json.haea.JSONHaeaStep;
import unalcol.evolution.haea.HaeaOperators;
import unalcol.search.population.Population;

public class JSONMixedGenome extends JSONHaeaStep<MixedGenome>{

	public JSONMixedGenome(Population<MixedGenome> population, HaeaOperators<MixedGenome> operators,
			Population<MixedGenome> fitnesseval) {
		super(population, operators, fitnesseval);
		// TODO Auto-generated constructor stub
	}

	@Override
	public JSONObject encode() {
		JSONObject obj = new JSONObject();
		addStatistics(obj);
		JSONObject best = new JSONObject();
		JSONObject genome = new JSONObject();
		genome.put("sensors", bestIndividual.sensors.toString());
		genome.put("weights", bestIndividual.annWeights);
		best.put("genome", genome);
		obj.put("best_individual", best);
		return obj;
	}
	
	

}
