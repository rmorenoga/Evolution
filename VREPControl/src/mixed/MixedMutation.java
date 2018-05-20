package mixed;

import unalcol.search.variation.Variation_1_1;
import unalcol.types.collection.bitarray.BitArray;

public class MixedMutation extends Variation_1_1<MixedGenome> {
	
	private Variation_1_1<BitArray> binaryOp;
	private Variation_1_1<double[]> realOp;
	
	

	public MixedMutation(Variation_1_1<BitArray> binaryOp, Variation_1_1<double[]> realOp) {
		this.binaryOp = binaryOp;
		this.realOp = realOp;
	}


	@Override
	public MixedGenome apply(MixedGenome genome) {
		
		BitArray newBitGenome = binaryOp.apply(genome.sensors);
		double[] newDoubleGenome = realOp.apply(genome.annWeights);
			
		return new MixedGenome(newBitGenome,newDoubleGenome);
	}
	
}
