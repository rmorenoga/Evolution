package control;

public class CPGHANN extends ParameterMask{
	
	private boolean individual;
	
	public CPGHANN(int numberofParameters, boolean individual){
		this.numberofParameters = 208;
		this.individual = individual;
		//this.numberofParameters = numberofParameters;
	}
	
	
	public void growParam(int numberofModules) {
		
//		float[] grownparam = new float[numberofModules*numberofParameters];
//		
//		float[] individual = new float[]{3.7031880237361f,-5.1820857538735f,3.5284722615823f,7.5991052079333f,1.99567117686f,0.0067916145044883f,2.029819228183f,0.39400266017765f,-7.9233205156134f,0.10114106211706f,-0.67121761225486f,1.5811749855056f,2.3427921775728f,-3.0773759717976f,-3.7813482970676f,-3.1339531910578f,3.5251460618683f,-1.8171455972769f,-2.0519315089041f,-2.4337173029194f,-2.9603466913104f,0.26013738451878f,2.498754863258f,-2.9640368161076f,1.7256013741717f,1.5553430495784f,0.13463249917696f,1.702608981763f,0.21844506731106f,2.6456150175253f,-0.35269590960733f,0.2248871128083f,-3.6278856703929f,-1.6072324505076f,2.2571119127186f,0.59249884233954f,-0.41404701301829f,2.9397290116176f,1.4328445829451f,4.5323892639144f,-4.149957704121f,4.9166635652462f,1.1047370716296f,-1.8132987136144f,-0.1415272878606f,2.4656989126168f,-1.3824661150575f,-3.1263127509993f,-0.018338225554331f,-0.42316831436748f,2.1205908358097f,2.1957736993209f,1.8542810431173f,1.3353725816429f,-4.4690987269077f,2.0569926549321f,-0.37661783974822f,2.2429315036143f,-0.49708555733972f,-2.1907681077719f,-0.21123788594747f,0.061790144513587f,0.0013608704378629f,-1.8738026563078f,-0.87850607838482f,-3.485989383528f,-1.1428688454485f,-3.0979939978765f,-2.4540788824934f,-2.7385986316458f,1.9799118927239f,-3.0158423189757f,-2.44439278543f,-1.9523201668208f,2.3945188088478f,6.151575827233f,-2.3449598997831f,2.9778150748461f,0.46199694928873f,3.3130297000656f,1.0072263549876f,3.4035053568783f,2.1815083713426f,-0.3678521693529f,1.240060481398f,2.1007075142115f,-2.9617709811875f,1.1072367896478f,0.4240434702011f,1.6250089611858f,-2.944431594573f,-2.5702644009635f,6.9898747049965f,-1.9423911112004f,-2.040450571635f,-1.9443284064384f,-3.296971925147f,-6.5994954210158f,-1.2541743507609f,0.14330233122916f,-2.5570626123656f,8.0666524572069f,2.1520442795008f,-1.250427740626f,-1.7215988464563f,-3.9019626995131f,1.0979264664446f,1.7030097199683f,0.061167456412728f,6.5893035925488f,1.0486786686471f,-8.1674874609995f,-6.7288535026462f,-0.49230402490319f,2.0919403438506f,8.2637518690908f,-0.018826403220291f,0.13363266616764f,-1.7140724646149f,0.020907039261609f,0.12838544090791f,0.013974824809526f,-0.16935299755544f,-0.41614542402388f,1.6274974287835f,-0.016246435065239f,-0.37220701492428f,-0.0091937635358005f,0.12838544090791f,0.013974824809526f,-0.16935299755544f,-0.41614542402388f,1.6274974287835f,-0.016246435065239f,-0.37220701492428f,-0.0091937635358006f,0.12838544090791f,0.013974824809526f,-0.16935299755544f,-0.41614542402388f,1.6274974287835f,-0.016246435065239f,-0.37220701492428f,-0.0091937635358005f,0.12838544090791f,0.013974824809526f,-0.16935299755544f,-0.41614542402388f,1.6274974287835f,-0.016246435065239f,-0.37220701492428f,-0.0091937635358006f};
//		
//		for(int i=0;i<grownparam.length;i=i+numberofParameters){
//			for (int j=0;j<individual.length;j++){
//				grownparam[i+j] = individual[j];
//			}
//			
//		}
//		
		float[] grownparam;
		if (getParameters() == null) {
			System.err.println("CPGHANN");
			System.err.println("Parameters have not been set yet");
			System.exit(-1);
		}
		grownparam = organize(numberofModules,getParameters());
		maskedparameters = grownparam;
		
	}
	
	float[] organize(int numberofModules, float[] parameters) {
		
		float[] grownparam = new float[numberofModules*numberofParameters];
		
		System.out.println("grownparam: "+grownparam.length);
		System.out.println("numberofmodules: "+numberofModules);
		System.out.println("numberofParameters: "+numberofParameters);
		
		if(!individual){
		
		for(int i=0;i<grownparam.length;i=i+numberofParameters){ //The same for all
			for (int j=0;j<parameters.length;j++){  
				grownparam[i+j] = parameters[j];
			}
			
		}
		}else{
		
		for (int i = 0;i<grownparam.length;i++){  //Individual parameters per module
			grownparam[i] = parameters[i]*10;
		}
		}
		
		return grownparam;
			
	}
	
	
	
	
	
	

}
