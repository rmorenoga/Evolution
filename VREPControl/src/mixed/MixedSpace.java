package mixed;

import unalcol.optimization.binary.BinarySpace;
import unalcol.optimization.real.HyperCube;
import unalcol.search.space.Space;

public class MixedSpace extends Space<MixedGenome>{
	
	private HyperCube realSp;
	private BinarySpace binSp;
	
	public MixedSpace(HyperCube realSp,BinarySpace binSp) {
		this.realSp = realSp;
		this.binSp = binSp;
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
		return gen;
	}
	
	

}
