package evolHAEA;

import java.util.List;

import maze.Maze;
import simvrep.ShortChallengeSettings;
import simvrep.Simulation;

public class ShortChallengeEmP extends EmP{
	


	public ShortChallengeEmP(List<Simulation> simulators, int numberOfServers, double[] morphology, Maze maze,
			ShortChallengeSettings settings) {
		super(simulators, numberOfServers, morphology, maze, settings);
	}

	public ShortChallengeEmP(Simulation sim, double[] morphology, Maze maze, ShortChallengeSettings settings) {
		super(sim, morphology, maze, settings);
	}

	@Override
	public Double apply(double[] individual) {
		double fitness = -super.apply(individual);
		if (fitness == 0){
			ShortChallengeSettings newSetting = (ShortChallengeSettings) this.settings;
			newSetting.selectNextChallenge();
		}
		//System.out.println("Simulation Settings " + this.settings.maxTime + " ," +this.settings.environmentFraction);
		return fitness;
	}

	@Override
	public boolean isNonStationary() {
		return true;
	}
	
	
	
	
	

}
