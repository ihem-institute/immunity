
	//CAMBIO
	package immunity;

	import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import repast.simphony.engine.environment.RunEnvironment;

	public class LocalPath {
		
	private String mypath;
	private String mypath1;
	private String mypath2;
	private String mypath3;
	private String mypath4;
	private String mypath5;
	private String mypath6;
	private String mypathOut;
	private static LocalPath instance;
	public static LocalPath getInstance() {
		if( instance == null ) {
			instance = new  LocalPath();
		}
		return instance;
	}
	
		
	
	File myDir = new File (".");
	{
    try {
      
      mypath=myDir.getCanonicalPath().replace('\\','/');// for input
//      FOR BATCH, THE PATH MUST BE ABSOLUTE BECAUSE THE BATCH RUNS FROM A
//      TEMPORARY FOLDER THAT IS DELETED. SO IF RELATIVE, THE OUTPUT IS LOST
//      SAME FOR INPUT, THE FILE MUST BE IN THE "data" FOLDER
		if (RunEnvironment.getInstance().isBatch()) {
		 mypath="C:/Users/lmayo/Workspace-crossPresentation/immunity/";	
		}
//      to get the results from the batch in different folders, the directory must be created
//      Cannot stores de files in a non existing directory
      String folderName = new SimpleDateFormat("yyyy-MM-dd-HH-mmss").format(new Date());
      
      mypathOut=mypath+"/output/"+folderName+"/";
      Path path = Paths.get(mypathOut);
      Files.createDirectory(path);
      }
    catch(Exception e) {
      e.printStackTrace();
      }

	}
//	NOT USED BATCH
	public String getPath(){ 
		return this.mypath; 
		} 
	public String getMyPathOut(){ 
		return mypathOut; 
		} 
	
	public String getPathResultsIT(){ 
		
		mypath1=mypathOut+"/ResultsIntrTransp3.csv";
		return this.mypath1; 
		} 

	public String getPathResultsMarkers(){ 
		
		mypath2=mypathOut+"/ResultsMarker.csv";
		return this.mypath2; 
		} 
//	not used in BATCH

	public String getPathInputIT(){ 
		
		mypath3=mypath+"/data/inputIntrTransp3.csv";
		return this.mypath3; 
		}
		
		public String getPathOutputFE(){ 
//			mypath4=mypath+"/outputFrozenEndosomes.csv";		
			mypath4=mypathOut+"/outputFrozenEndosomes.csv";//batch
			return this.mypath4; 
			} 
			
		public String getPathTotalRabs(){ 
			
//			mypath5=mypath+"/totalRabs.csv";
			mypath5=mypathOut+"/totalRabs.csv";//batch
			return this.mypath5; 
			}

		public String getPathCisternsArea() {
			
//			mypath6 = mypath+"/cisternsArea.csv";			
			mypath6 = mypathOut+"/cisternsArea.csv";//batch
			return this.mypath6; 
		}
	}
