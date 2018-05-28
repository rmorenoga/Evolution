package evolHAEA;

import java.util.List;
import maze.SelectableMaze;
import simvrep.ShortChallengeSettings;
import simvrep.Simulation;
import simvrep.SimulationSettings;

public class GenerationalEnvEmP extends EmP{
	
	protected int iteration = 0;
	protected int numberOfGenerations;
	protected boolean updateEnvInst = true;

	public GenerationalEnvEmP(List<Simulation> simulators, int numberOfServers, double[] morphology, SelectableMaze maze,
			SimulationSettings settings,int numberOfGenerations) {
		super(simulators, numberOfServers, morphology, maze, settings);
		this.numberOfGenerations = numberOfGenerations;
	}

	public GenerationalEnvEmP(Simulation sim, double[] morphology, SelectableMaze maze, SimulationSettings settings, int numberOfGenerations) {
		super(sim, morphology, maze, settings);
		this.numberOfGenerations = numberOfGenerations;
	}
	
	@Override
	public void update(int k) {
		super.update(k);
		this.iteration = k;
	}
	
	@Override
	public boolean isNonStationary() {
		return true;
	}
	
	@Override
	public Double apply(double[] individual) {
		updateEnvironment();
		double fitness = super.apply(individual);
		//System.out.println("Simulation Settings " + this.settings.maxTime + " ," +this.settings.environmentFraction);
		return fitness;
	}
	
	private void updateEnvironment(){
		if (iteration != 0) {
			if (iteration % numberOfGenerations == 0) {
				if (updateEnvInst) {
					SelectableMaze newMaze = (SelectableMaze) this.maze;
					newMaze.selectNextMaze(); 
					updateEnvInst = false;
				}
			}else{
				updateEnvInst = true;
			}
		}
	}
	
	
	

}
