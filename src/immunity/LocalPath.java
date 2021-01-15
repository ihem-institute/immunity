
	//CAMBIO
	package immunity;

	import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.print.attribute.DateTimeSyntax;

	public class LocalPath {
		
	private String mypath;
	private String mypathOut;
	private String mypath1;
	private String mypath2;
	private String mypath3;
	private String mypath4;
	private String mypath5;

		
		File myDir = new File (".");
		{
	    try {
	      
	      mypath=myDir.getCanonicalPath().replace('\\','/');// for input
//	      FOR BATCH, THE PATH MUST BE ABSOLUTE BECAUSE THE BATCH RUNS FROM A
//	      TEMPORARY FOLDER THAT IS DELETED. SO IF RELATIVE, THE OUTPUT IS LOST
//	      SAME FOR INPUT, THE FILE MUST BE IN THE "data" FOLDER
//	      to get the results from the batch in different folders, the directory must be created
//	      Cannot stores de files in a non existing directory
	      String folderName = new SimpleDateFormat("yyyy-MM-dd-HH-mmss").format(new Date());
	      mypathOut="C:/Users/lmayo/workspace/immunity/output/"+folderName+"/";
	      Path path = Paths.get(mypathOut);
	      Files.createDirectory(path);
	      }
	    catch(Exception e) {
	      e.printStackTrace();
	      }

		}
		
		public String getPath(){ 
			return this.mypath; 
			} 
		
		public String getPathResultsIT(){ 
			
			mypath1=mypathOut+"/ResultsIntrTransp3.csv";
			return this.mypath1; 
			} 

		public String getPathResultsMarkers(){ 
			
			mypath2=mypathOut+"/ResultsMarker.csv";
			return this.mypath2; 
			} 
//		not used in BATCH

		public String getPathInputIT(){ 
			
			mypath3=mypath+"/data/inputIntrTransp3.csv";
			return this.mypath3; 
			} 
		
		public String getPathOutputFE(){ 
			
			mypath4=mypathOut+"/outputFrozenEndosomes.csv";
			return this.mypath4; 
			} 
			
		public String getPathTotalRabs(){ 
			
			mypath5=mypathOut+"/totalRabs.csv";
			return this.mypath5; 
			}
	}
