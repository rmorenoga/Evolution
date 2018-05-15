package mixed;

import unalcol.optimization.binary.BinarySpace;
import unalcol.optimization.real.HyperCube;
import unalcol.search.space.Space;

public class MixedSpace extends Space<MixedGenome>{
	
	private HyperCube realSp;
	private BinarySpace binSp;
	private boolean fixed;
	private boolean value;
	
	public MixedSpace(HyperCube realSp,BinarySpace binSp) {
		this.realSp = realSp;
		this.binSp = binSp;
		this.fixed = false;
	}
	
	public MixedSpace(HyperCube realSp,BinarySpace binSp, boolean value) {
		this.realSp = realSp;
		this.binSp = binSp;
		this.fixed = true;
		this.value = value;
	}

	@Override
	public double feasibility(MixedGenome arg0) {
		return 1.0;
	}

	@Override
	public boolean feasible(MixedGenome arg0) {
		return true;
	}

	@Override
	public MixedGenome pick() {
		return new MixedGenome(binSp.pick(),realSp.pick());
	}

	@Override
	public MixedGenome repair(MixedGenome gen) {
		if(fixed) {
			gen.sensors.zero();
			if(value)
				gen.sensors.not();
		}
		return gen;
	}
	
	

}
