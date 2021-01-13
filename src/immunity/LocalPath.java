
	//CAMBIO
	package immunity;

	import java.io.File;

	public class LocalPath {
		
	private String mypath;
	private String mypath1;
	private String mypath2;
	private String mypath3;
	private String mypath4;
	private String mypath5;

		
		File myDir = new File (".");
		{
	    try {
	      
	      mypath=myDir.getCanonicalPath().replace('\\','/');
//	      FOR BATCH, THE PATH MUST BE ABSOLUTE BECAUSE THE BATCH IS RUN FROM A
//	      TEMPORARY FOLDER THAT IS DELETED. SO IF RELATIVE, THE OUTPUT IS LOST
//	      SAME FOR INPUT, THE FILE MUST BE IN THE "data" FOLDER
	      mypath="C:/Users/lmayo/workspace/immunity/output";
	      
	      }
	    catch(Exception e) {
	      e.printStackTrace();
	      }

		}
		
		public String getPath(){ 
			return this.mypath; 
			} 
		
		public String getPathResultsIT(){ 
			
			mypath1=mypath+"/ResultsIntrTransp3.csv";
			return this.mypath1; 
			} 

		public String getPathResultsMarkers(){ 
			
			mypath2=mypath+"/ResultsMarker.csv";
			return this.mypath2; 
			} 

		public String getPathInputIT(){ 
			
			mypath3=mypath+"/inputIntrTransp3.csv";
			return this.mypath3; 
			} 
		
		public String getPathOutputFE(){ 
			
			mypath4=mypath+"/outputFrozenEndosomes.csv";
			return this.mypath4; 
			} 
			
		public String getPathTotalRabs(){ 
			
			mypath5=mypath+"/totalRabs.csv";
			return this.mypath5; 
			}
	}
