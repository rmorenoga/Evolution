package evolHAEA;

import unalcol.clone.Clone;
import unalcol.random.integer.IntUniform;
import unalcol.random.real.DoubleGenerator;

public class DEXOver extends RealArityFive {

	protected IntUniform g;
	private double F;
	private double CR;
	protected DoubleGenerator r;
	private double[] CRF;

	public DEXOver(double F, double CR, DoubleGenerator r, int DIM) {
		this.F = F;
		this.CR = CR;
		this.r = r;
		g = new IntUniform(1, DIM);
	}

	 public DEXOver(double F, double[] CRF, DoubleGenerator r, int DIM){
	//public DEXOver(double F, DoubleGenerator r, int DIM) {
		this.F = F;
		 this.CRF = CRF;
		//this.CRF = new double[DIM];
//		for (int i = 0; i < CRF.length; i++) {
//			CRF[i] = Math.exp(-i * 0.1);
//		}
		this.r = r;
		g = new IntUniform(1, DIM);
	}

	public double[] apply(double[] c1, double[] c2, double[] c3, double[] c4, double[] c5) {
		
		double[][] otherc = new double[][]{c2,c3,c4,c5};
		boolean inThere = false;
		int where = -1;
		for (int i = 0;i<otherc.length;i++){
			if(c1 == otherc[i]){
				inThere = true;
				where = i;
			}
		}
		if(inThere){
			double[] otherccopy = otherc[where];
			otherc[where] = otherc[otherc.length-1];
			otherc[otherc.length-1] = otherccopy;
		}
		
		
		if (CRF == null) {
			try {
				int index = g.generate();
				double random;

				double[] y = new double[c1.length];

				for (int i = 0; i < c1.length; i++) {
					random = r.generate();
					if (random < CR || index == i) {
						//y[i] = c2[i] + F * (c3[i] - c4[i]);
						y[i] = otherc[0][i] + F * (otherc[1][i] - otherc[2][i]);
					} else {
						y[i] = c1[i];
					}
				}

				return y;
			} catch (Exception e) {
			}
			return null;

		} else {
			try {
				int index = g.generate();
				double random;

				double[] y = new double[c1.length];

				for (int i = 0; i < c1.length; i++) {
					random = r.generate();
					//System.out.println("CRF value: " + CRF[i]);
					if (random < CRF[i] || index == i) {
						//y[i] = c2[i] + F * (c3[i] - c4[i]);
						y[i] = otherc[0][i] + F * (otherc[1][i] - otherc[2][i]);
					} else {
						y[i] = c1[i];
					}
				}

				return y;
			} catch (Exception e) {
			}
			return null;
		}
	}

}
