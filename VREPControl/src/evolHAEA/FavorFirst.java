package evolHAEA;

import unalcol.optimization.real.mutation.PickComponents;


public class FavorFirst implements PickComponents{
	protected int d = 1;
	private int numberofparameters;
	private int numberofmessages;
	private int pack;
	private int numberofmodules;
	private boolean snake;
	private int extraparam;
	

	
	public FavorFirst(int numberofmessages, int pack, boolean snake, int extraparam){
		this.numberofparameters = pack*numberofmessages;
		this.numberofmessages = numberofmessages;
		this.pack = pack;
		this.snake = snake;
		this.extraparam = extraparam;
	}
	
	public FavorFirst(){
		this(5,7,false,0);
	}
	
	public int[] get(int DIMENSION) {
		d = DIMENSION-extraparam;
		if(d%numberofparameters!=0){
			System.err.println("Warning: Genotype dimension does not match the number of modules");
		}
		numberofmodules = d/numberofparameters;
		int[] indices1 = new int[d];//number of messages 5
		int packsize = numberofmodules*pack;//Size of each pack of parameters for the number of modules
		
		if (!snake){
		for (int i=0;i<numberofmessages; i++){
			for(int j=0; j<numberofmodules; j++){
				indices1[(i*numberofmodules*pack)+(j*pack)] = i+(j*numberofparameters)+extraparam;
				indices1[(i*numberofmodules*pack)+(j*pack)+1] = i+5+(j*numberofparameters)+extraparam;
				indices1[(i*numberofmodules*pack)+(j*pack)+2] = (2*numberofmessages)+(4*i)+(j*numberofparameters)+extraparam;
				indices1[(i*numberofmodules*pack)+(j*pack)+3] = (2*numberofmessages)+(4*i)+(j*numberofparameters)+1+extraparam;
				indices1[(i*numberofmodules*pack)+(j*pack)+4] = (2*numberofmessages)+(4*i)+(j*numberofparameters)+2+extraparam;
				indices1[(i*numberofmodules*pack)+(j*pack)+5] = (2*numberofmessages)+(4*i)+(j*numberofparameters)+3+extraparam;
				indices1[(i*numberofmodules*pack)+(j*pack)+6] = i+(6*numberofmessages)+(j*numberofparameters)+extraparam;
			}
		}
		}else{
			for (int i=0;i<numberofmessages; i++){
				for(int j=0; j<numberofmodules; j++){
					for(int k=0; k<pack;k++){
						indices1[(i*numberofmodules*pack)+(j*pack)+k] = i+(k*7)+(j*numberofparameters)+extraparam;
					}
				}
			}
		}
		
		int[] indices = new int[extraparam+indices1.length];
		
		for(int i=0;i<extraparam;i++){
			indices[i]=i;
		}
		for(int i=0;i<indices1.length;i++){
			indices[i+extraparam]=indices1[i];
		}

		return indices;
	}
	
	

	/*public int[] get(int DIMENSION) {
		d = DIMENSION;
		numberofmodules = d/numberofparameters;
		int[] indices = new int[d]; 
		int packsize = numberofmodules*pack;//Size of each pack of parameters times the number of modules
		
		for (int i=0;i<indices.length;i++){
			indices[i]=0;
		}	
		
		for (int j = 0; j < numberofmessages;j++ ){			
			for (int i = 0; i < numberofmodules; i++) {
				indices[i+(numberofmodules*j)] = (pack * i) + (packsize*j);
			}
		}
		
		
		return indices;
	}*/

	

	
	

}
