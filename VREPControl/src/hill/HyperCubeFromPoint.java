package hill;

import unalcol.optimization.real.HyperCube;

public class HyperCubeFromPoint extends HyperCube{

	private double[][] referencePoints;
	private double[] radius;
	private int index;
	private boolean fixed;

	public HyperCubeFromPoint(double[] min, double[] max, double[][] referencePoints, double[] radius) {
		super(min, max);
		this.referencePoints = referencePoints;
		this.radius = radius;
		this.index = 0;
		this.fixed = true;
	}

	@Override
	public double[] get() {
		double[] result = super.get();
		if (referencePoints.length>0){
			for (int i = 0;i<result.length;i++)
				result [i] = (fixed) ? referencePoints[index][i] : referencePoints[index][i] + variate(radius[i]);
			index++;
			if (index==referencePoints.length) {
				index = 0;
				fixed = false;
			}
		}
		return repair(result);
	}
	
	public double variate(double radius){
		return 2 * (radius * Math.random()) - radius;
	}
	
	
	

}
