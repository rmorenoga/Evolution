package control;
public abstract class SubCPGHS implements Subparammask{

	private boolean samePhaseDiff;
	private boolean blockfrequency;
	
	public SubCPGHS(boolean samePhaseDiff, boolean blockfrequency){
		this.blockfrequency = blockfrequency;
		this.samePhaseDiff = samePhaseDiff;
	}

	public boolean isSamePhaseDiff() {
		return samePhaseDiff;
	}

	public void setSamePhaseDiff(boolean samePhaseDiff) {
		this.samePhaseDiff = samePhaseDiff;
	}

	public boolean isBlockfrequency() {
		return blockfrequency;
	}

	public void setBlockfrequency(boolean blockfrequency) {
		this.blockfrequency = blockfrequency;
	}
	
	
	
}
