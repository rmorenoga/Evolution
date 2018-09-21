package mixed;

import unalcol.search.variation.Variation_2_2;
import unalcol.types.collection.bitarray.BitArray;

public class MixedXOver extends Variation_2_2<MixedGenome>{
	
	private Variation_2_2<BitArray> binaryOp;
	private Variation_2_2<double[]> realOp;
	
	
	public MixedXOver(Variation_2_2<BitArray> binaryOp, Variation_2_2<double[]> realOp) {
		this.binaryOp = binaryOp;
		this.realOp = realOp;
	}


	@Override
	public MixedGenome[] apply(MixedGenome parent1, MixedGenome parent2) {
		
		BitArray[] newBitGenomes = binaryOp.apply(parent1.sensors,parent2.sensors);
		double[][] newDoubleGenomes = realOp.apply(parent1.annWeights,parent2.annWeights);
		
		MixedGenome[] newGenomes = new MixedGenome[]{
				new MixedGenome(newBitGenomes[0],newDoubleGenomes[0]),
				new MixedGenome(newBitGenomes[1],newDoubleGenomes[1])};

		return newGenomes;
	}
	
	

}
