package mixed;

import unalcol.types.collection.bitarray.BitArray;

public class MixedGenome {
	
	public BitArray sensors;
	public double[] annWeights;

	
	public MixedGenome(BitArray sensors, double[] annWeights) {
		this.sensors = sensors;
		this.annWeights = annWeights;
	}
	
	public MixedGenome(double[] annWeights){
		this.annWeights = annWeights;
		this.sensors = new BitArray(12,false);
		this.sensors.not();
	}
	
	public float[] getAnnWeightsAsFloatArray(){
		float[] floatAnnWeights = new float[annWeights.length];

		for (int i = 0; i < annWeights.length; i++) {
			floatAnnWeights[i] = (float) annWeights[i];
		}
		
		return floatAnnWeights;
	}
	

}
