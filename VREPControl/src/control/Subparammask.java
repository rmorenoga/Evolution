package control;

/**
 * Helper interface for fixing specific sets of parameters in a ParamMask
 * @author golde_000
 *
 */
public interface Subparammask {
	
	public void setRawParameters(float[] parameters);
	
	public float[] getMaskedParameters();
	
	
}
