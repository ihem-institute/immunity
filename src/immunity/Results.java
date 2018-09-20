

package immunity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JPanel;
import javax.swing.table.TableModel;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.environment.RunState;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.ui.table.AgentTableFactory;
import repast.simphony.ui.table.SpreadsheetUtils;
import repast.simphony.ui.table.TablePanel;

public class Results {

	private static ContinuousSpace<Object> space;
	private static Grid<Object> grid;


	CellProperties cellProperties = CellProperties.getInstance();

	public HashMap<String, Double> cellK = cellProperties.getCellK();
	public Set<String> solubleMet = cellProperties.getSolubleMet();
	public Set<String> membraneMet = cellProperties.getMembraneMet();
	public Set<String> rabSet = cellProperties.getRabSet();
//	public List<String> allMet = new ArrayList<String>();
//	public HashMap<String, Double> initRabCell = new HashMap<String, Double>();
//	public HashMap<String, Double> rabCompatibility = new HashMap<String, Double>();
//	public HashMap<String, Double> tubuleTropism = new HashMap<String, Double>();
//	public HashMap<String, List<String>> rabTropism = new HashMap<String, List<String>>();
//	public HashMap<String, Double> mtTropism = new HashMap<String, Double>();
	static HashMap<String, Double> contentDist = new HashMap<String, Double>();

	static HashMap<String, Double> totalRabs = new HashMap<String, Double>();	
	static HashMap<String, Double> totalVolumeRabs = new HashMap<String, Double>();
	static HashMap<String, Double> initialTotalRabs = new HashMap<String, Double>();
	public HashMap<String, Double> singleEndosomeContent = new HashMap<String, Double>();
	
	
	static Results	instance = new Results(space, grid, totalRabs, initialTotalRabs);
//	
	public static Results getInstance() {
		return instance;
	}
	
	//Constructor.  It is called once from CellBuilder
	public Results(ContinuousSpace<Object> sp, Grid<Object> gr, HashMap<String, Double> totalRabs, HashMap<String, Double> initialTotalRabs) {
		this.space = sp;
		this.grid = gr;
		// Generate a file with the header of the variables that are going to be followed
		//along the simulation.  Up to now= content distribution according to rabs contents.

	}
	
	@ScheduledMethod(start = 1)
	public void header(){
		TreeMap<String, Double> header = new TreeMap<String, Double>(content());
		try {
			writeToCsvHeader(header);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TreeMap<String, Double> singleEndosomeHeader = new TreeMap<String, Double>(endosomeContent());
		try {
			writeToCsvHeadSingleEndosomeHeader(singleEndosomeHeader);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@ScheduledMethod(start = 1, interval = 5000)
	public void stepTable() {
		log();
//		freeze endosome set
		FreezeDryEndosomes.getInstance();
		try {
			FreezeDryEndosomes.getInstance().writeToCsv();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void log(){
	    double tick = RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
	    Context<Object> context = RunState.getInstance().getMasterContext();

	    Map<String,TableModel> models = new HashMap<String,TableModel>();

	    // Create a tab panel for each agent layer
	    for (Object agentType : context.getAgentTypes()){
	        Class agentClass = (Class)agentType;

	        JPanel agentPanel = AgentTableFactory.createAgentTablePanel(context.getAgentLayer(agentClass), agentClass.getSimpleName());

	        if (agentPanel instanceof TablePanel){
	            TableModel model = ((TablePanel)agentPanel).getTable().getModel();
	            models.put(agentClass.getSimpleName(), model);

	        }
	    }

	    SpreadsheetUtils.saveTablesAsExcel(models, new File("out-"+tick+".xlsx"));
	}

	@ScheduledMethod(start = 1, interval = 100)
	public void step() {
		contentDistribution(totalRabs, initialTotalRabs); // Gets an hash map with all the 
		//possible combinations of contents and Rabs
		// a new line is added each 100 ticks
		TreeMap<String, Double> orderContDist = new TreeMap<String, Double>(contentDist);
//		System.out.println(orderContDist);
		try {
			writeToCsv(orderContDist);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	// to load the file
	private void writeToCsv(TreeMap<String, Double> orderContDist) throws IOException {
		String line = "";
		for (String key : orderContDist.keySet()) {
            line = line+ Math.round(orderContDist.get(key)*100d)/100d + ",";
		}
		line = line + "\n";
		Writer output;
		output = new BufferedWriter(new FileWriter("C:/Users/lmayo/workspace/immunity/ResultsIntrTransp3.csv", true));
		output.append(line);
		output.close();
	}
	// to generate the file and add the headers in the first row
	private void writeToCsvHeader(TreeMap<String, Double> orderContDist) throws IOException {
		
		String line = "";
		for (String key : orderContDist.keySet()) {
            line = line+ key + ",";
		}
		line = line + "\n";
		Writer output;
		output = new BufferedWriter(new FileWriter("C:/Users/lmayo/workspace/immunity/ResultsIntrTransp3.csv", false));
		output.append(line);
		output.close();	
	}
	private void writeToCsvHeadSingleEndosomeHeader (TreeMap<String, Double> orderSingleEndHead) throws IOException {
		
		String line = "";
		for (String key : orderSingleEndHead.keySet()) {
            line = line+ key + ",";
		}
		line = line + "\n";
		Writer output;
		output = new BufferedWriter(new FileWriter("C:/Users/lmayo/workspace/immunity/ResultsMarker.csv", false));
		output.append(line);
		output.close();	
		// TODO Auto-generated method stub
		
	}
	// sum the content in all endosomes weighted by the rab content of each endosome
	public void contentDistribution(HashMap<String, Double> totalRabs, HashMap<String, Double> initialTotalRabs) {
		content();// initialize all contents to zero
		HashMap<String, Double> solubleRecycle = PlasmaMembrane.getInstance().getSolubleRecycle();
		// include in the contentDistribution all the recycled components, soluble and membrane
		HashMap<String, Double> membraneRecycle = PlasmaMembrane.getInstance().getMembraneRecycle();
		for (String sol : solubleRecycle.keySet()) {
//			System.out.println(" soluble "+ sol);
			double value = solubleRecycle.get(sol);
			contentDist.put(sol, value);
			//System.out.println("SOLUBLE"+sol + "Rab" +rab);
		}
		for (String mem : membraneRecycle.keySet()) {
			//System.out.println(" soluble "+ sol + " Rab " +rab);
			double value = membraneRecycle.get(mem);
			contentDist.put(mem, value);
			//System.out.println("SOLUBLE"+sol + "Rab" +rab);
		}			
		
		List<Endosome> allEndosomes = new ArrayList<Endosome>();
		for (Object obj : grid.getObjects()) {
			if (obj instanceof Endosome) {
				allEndosomes.add((Endosome) obj);
			}
		}
//		for the set of all endosomes, calculate the content distribution among the different
//		rab-containing compartments.  This is calculated as the sum of all the soluble and
//		membrane components multiplied by the area ratio corresponding to the endosome
//		this is: content*RabX/(area of the endosome).
//		In addition, the total amount of each Rab present in all organelles
//		in the system is calculated.  And the total volume that correspond to the Rab domain adding
//		all volumes proportional to the rab area of each organelle

		for (String rab: rabSet){
			totalRabs.put(rab,  0.0);
			totalVolumeRabs.put(rab, 0.0);
		}
		for (Endosome endosome : allEndosomes) {
			Double area = endosome.area;
			Double volume = endosome.volume;
			HashMap<String, Double> rabContent = endosome.getRabContent();
			HashMap<String, Double> membraneContent = endosome
					.getMembraneContent();
			HashMap<String, Double> solubleContent = endosome
					.getSolubleContent();
			

			for (String rab : rabContent.keySet()) {
				for (String sol : solubleContent.keySet()) {
//					System.out.println(" soluble "+ sol + " Rab " +rab);
//					System.out.println(" FALTA " + contentDist.get(sol + rab));
					double value = contentDist.get(sol + rab)
							+ solubleContent.get(sol) * rabContent.get(rab)
							/ area;
					contentDist.put(sol + rab, value);
					//System.out.println("SOLUBLE"+sol + "Rab" +rab);
				}
			for (String mem : membraneContent.keySet()) {
//				System.out.println(" membrane "+mem + " Rab " +rab);
					double value = contentDist.get(mem + rab)
							+ membraneContent.get(mem) * rabContent.get(rab)
							/ area;
					contentDist.put(mem + rab, value);
				}

			}
// CONTROL OF RAB LOST
// sum Rabs in all endosomes
		for (String rab : rabContent.keySet()){
			double sum = totalRabs.get(rab)+ rabContent.get(rab);
			totalRabs.put(rab, sum);
		}
// Sum all the organelle volume surrounded by a rab domain
		for (String rab : rabContent.keySet()){
			double sum = totalVolumeRabs.get(rab)+ volume*rabContent.get(rab)/area;
			totalVolumeRabs.put(rab, sum);
		}
//
// If the endosome contains a MARKER, print info in a Results file		
		int tick = (int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		if (tick == 1) initialTotalRabs.putAll(totalRabs);
		
		if((endosome.getMembraneContent().containsKey("membraneMarker") && 
				endosome.getMembraneContent().get("membraneMarker") > 0.9)
				||
			(endosome.getSolubleContent().containsKey("solubleMarker") && 
				endosome.getSolubleContent().get("solubleMarker") > 0.9))
		{
			try {
				printEndosome(endosome);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		}
//		sum in cytosol
		System.out.println(" TOTAL RABS      "+totalRabs);


	}
// Send information about the endosome that contains a membrane or a soluble MARKER
	private void printEndosome(Endosome endosome) throws IOException {
		singleEndosomeContent.put("area", endosome.getArea());
		singleEndosomeContent.put("volume", endosome.getVolume());
		for (String rab : rabSet)
			if (endosome.getRabContent().containsKey(rab)){
				singleEndosomeContent.put(rab, endosome.getRabContent().get(rab));
			}
		for (String sol : solubleMet)
			if (endosome.getSolubleContent().containsKey(sol)){
				singleEndosomeContent.put(sol, endosome.getSolubleContent().get(sol));
			}
		for (String mem : membraneMet)
			if (endosome.getMembraneContent().containsKey(mem)){
				singleEndosomeContent.put(mem, endosome.getMembraneContent().get(mem));
			}
		TreeMap<String, Double> orderSingleEndosome = new TreeMap<String, Double>(singleEndosomeContent);
		String line = "";
		for (String key : orderSingleEndosome.keySet()) {
            line = line+ Math.round(orderSingleEndosome.get(key)*100d)/100d + ",";
		}
		line = line + "\n";
		Writer output;
		output = new BufferedWriter(new FileWriter("C:/Users/lmayo/workspace/immunity/ResultsMarker.csv", true));
		output.append(line);
		output.close();
	}
	


	// generate the set of all combinations between contents 
	//(soluble or membrane) with all Rabs and sets the initial values to zero
	public HashMap<String, Double> content() {
		for (String sol : solubleMet) {
			contentDist.put(sol, 0d);
		} 
		for (String mem : membraneMet) {
			contentDist.put(mem, 0d);
		}
		for (String rab : rabSet) {
			for (String sol : solubleMet) {
				contentDist.put(sol + rab, 0d);
			}
			for (String mem : membraneMet) {
				contentDist.put(mem + rab, 0d);
			}
		}
		return contentDist;
	}
	public HashMap<String, Double> endosomeContent() {
		singleEndosomeContent.put("area", 0d);
		singleEndosomeContent.put("volume", 0d);
		
		for (String sol : rabSet) {
			singleEndosomeContent.put(sol, 0d);
		}
		for (String sol : solubleMet) {
			singleEndosomeContent.put(sol, 0d);
		}
		for (String mem : membraneMet) {
			singleEndosomeContent.put(mem, 0d);
		}	
		return singleEndosomeContent;
	}
	
	public HashMap<String, Double> getCellK() {
		return cellK;
	}
	
	public HashMap<String, Double> getTotalRabs() {
		return totalRabs;
	}
	public HashMap<String, Double> getInitialTotalRabs() {
		return initialTotalRabs;
	}

	public final HashMap<String, Double> getContentDist() {
		return contentDist;
	}

	public final HashMap<String, Double> getTotalVolumeRabs() {
		return totalVolumeRabs;
	}
}
