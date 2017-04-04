package evolHAEA;

import unalcol.optimization.real.mutation.PickComponents;


public class FavorFirst implements PickComponents{
	protected int d = 1;
	private int numberofparameters;
	private int numberofmessages;
	private int pack;
	private int numberofmodules;
	private boolean snake;
	

	
	public FavorFirst( int numberofmessages, int pack, boolean snake){
		this.numberofparameters = pack*numberofmessages;
		this.numberofmessages = numberofmessages;
		this.pack = pack;
		this.snake = snake;
	}
	
	public FavorFirst(){
		this(5,7,false);
	}
	
	public int[] get(int DIMENSION) {
		d = DIMENSION;
		numberofmodules = d/numberofparameters;
		int[] indices = new int[d];//number of messages 5
		int packsize = numberofmodules*pack;//Size of each pack of parameters for the number of modules
		
		if (!snake){
		for (int i=0;i<numberofmessages; i++){
			for(int j=0; j<numberofmodules; j++){
				indices[(i*numberofmodules*pack)+(j*pack)] = i+(j*numberofparameters);
				indices[(i*numberofmodules*pack)+(j*pack)+1] = i+5+(j*numberofparameters);
				indices[(i*numberofmodules*pack)+(j*pack)+2] = (2*numberofmessages)+(4*i)+(j*numberofparameters);
				indices[(i*numberofmodules*pack)+(j*pack)+3] = (2*numberofmessages)+(4*i)+(j*numberofparameters)+1;
				indices[(i*numberofmodules*pack)+(j*pack)+4] = (2*numberofmessages)+(4*i)+(j*numberofparameters)+2;
				indices[(i*numberofmodules*pack)+(j*pack)+5] = (2*numberofmessages)+(4*i)+(j*numberofparameters)+3;
				indices[(i*numberofmodules*pack)+(j*pack)+6] = i+(6*numberofmessages)+(j*numberofparameters);
			}
		}
		}else{
			for (int i=0;i<numberofmessages; i++){
				for(int j=0; j<numberofmodules; j++){
					for(int k=0; k<pack;k++){
						indices[(i*numberofmodules*pack)+(j*pack)+k] = i+(k*7)+(j*numberofparameters);
					}
				}
			}
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
