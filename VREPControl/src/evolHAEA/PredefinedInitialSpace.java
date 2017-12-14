package evolHAEA;

import unalcol.search.space.Space;

public abstract class PredefinedInitialSpace<T> extends Space<T> {

	T[] initialpop;
	int index;
	
	public PredefinedInitialSpace(T[] initialpop) {
		this.initialpop = initialpop;
		this.index = 0;
	}

	@Override
	public T pick() {
		if(index < initialpop.length)
			return initialpop[index++];
		return generate();
	}
	
	public abstract T generate();

}
