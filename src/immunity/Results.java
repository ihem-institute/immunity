

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


	ModelProperties cellProperties = ModelProperties.getInstance();

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
	static TreeMap<String, Double> contentDist = new TreeMap<String, Double>((String.CASE_INSENSITIVE_ORDER));

	static HashMap<String, Double> totalRabs = new HashMap<String, Double>();	
	static HashMap<String, Double> totalVolumeRabs = new HashMap<String, Double>();
	static HashMap<String, Double> initialTotalRabs = new HashMap<String, Double>();
	static HashMap<String, Double> initialTotalSolubleCargo = new HashMap<String, Double>();
	static HashMap<String, Double> initialTotalMembraneCargo = new HashMap<String, Double>();

	static HashMap<String, Double> cisternsArea = new HashMap<String, Double>();
	public HashMap<String, Double> singleEndosomeContent = new HashMap<String, Double>();
	
	
	static Results	instance = new Results(space, grid, totalRabs, initialTotalRabs);
	LocalPath mainpath=new LocalPath(); 
	String ITResultsPath = mainpath.getPathResultsIT(); 	
	String MarkerResultsPath =mainpath.getPathResultsMarkers();
	String TotalRabs = mainpath.getPathTotalRabs();
	String cisternsAreaPath = mainpath.getPathCisternsArea();
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
	
//	@ScheduledMethod(start = 1)
//	public void header(){
//		TreeMap<String, Double> header = new TreeMap<String, Double>(String.CASE_INSENSITIVE_ORDER);
//		header.putAll(content());
//		try {
//			writeToCsvHeader(header);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		TreeMap<String, Double> singleEndosomeHeader = new TreeMap<String, Double>(endosomeContent());
//		try {
//			writeToCsvHeadSingleEndosomeHeader(singleEndosomeHeader);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

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
		contentDistribution(totalRabs, initialTotalRabs, cisternsArea); 
		// Gets an hash map with all the 
		//possible combinations of contents and Rabs
		// a new line is added each 100 ticks
		TreeMap<String, Double> orderContDist = new TreeMap<String, Double>(contentDist);
		TreeMap<String, Double> orderTotalRabs = new TreeMap<String, Double>((String.CASE_INSENSITIVE_ORDER));
		orderTotalRabs.putAll(totalRabs);
		TreeMap<String, Double> orderCisternsArea = new TreeMap<String, Double>((String.CASE_INSENSITIVE_ORDER));
		orderCisternsArea.putAll(cisternsArea);
//	
		/*
		 * System.out.println(orderContDist); System.out.println(orderTotalRabs);
		 * System.out.println(orderCisternsArea);
		 */
		try {
			writeToCsv(orderContDist);
			writeToCsvTotalRabs(orderTotalRabs);
			writeToCsvCisternsArea(orderCisternsArea);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	// to load the file
	private void writeToCsv(TreeMap<String, Double> orderContDist) throws IOException {
		double tick = RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		if (tick <10){// HEADER
		String line = "";
		for (String key : orderContDist.keySet()) {
            line = line+ key + ",";
		}
		line = line + "\n";
		Writer output;
		//CAMBIO
		output = new BufferedWriter(new FileWriter(ITResultsPath, false));		
//		output = new BufferedWriter(new FileWriter("C:/Users/lmayo/workspace/immunity/ResultsIntrTransp3.csv", false));
		output.append(line);
		output.close();	
	}
		String line = "";
		for (String key : orderContDist.keySet()) {
            line = line+ Math.round(orderContDist.get(key)*100000d)/100000d + ",";
		}
		line = line + "\n";
		Writer output;
		//CAMBIO
		output = new BufferedWriter(new FileWriter(ITResultsPath, true));
//		output = new BufferedWriter(new FileWriter("C:/Users/lmayo/workspace/immunity/ResultsIntrTransp3.csv", true));
		output.append(line);
		output.close();
	}
	// to generate the file and add the headers in the first row
//	private void writeToCsvHeader(TreeMap<String, Double> orderContDist) throws IOException {
//		
//		String line = "";
//		for (String key : orderContDist.keySet()) {
//            line = line+ key + ",";
//		}
//		line = line + "\n";
//		Writer output;
//		//CAMBIO
//		output = new BufferedWriter(new FileWriter(ITResultsPath, false));		
////		output = new BufferedWriter(new FileWriter("C:/Users/lmayo/workspace/immunity/ResultsIntrTransp3.csv", false));
//		output.append(line);
//		output.close();	
//	}
	private void writeToCsvHeadSingleEndosomeHeader (TreeMap<String, Double> orderSingleEndHead) throws IOException {
		
		String line = "";
		for (String key : orderSingleEndHead.keySet()) {
            line = line+ key + ",";
		}
		line = line + "\n";
		Writer output;
		//CAMBIO
		output = new BufferedWriter(new FileWriter(MarkerResultsPath, false));
//		output = new BufferedWriter(new FileWriter("C:/Users/lmayo/workspace/immunity/ResultsMarker.csv", false));
		output.append(line);
		output.close();	
		// TODO Auto-generated method stub
		
	}
	private void writeToCsvTotalRabs(TreeMap<String, Double> totalRabs2) throws IOException {
	double tick = RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
	if (tick <10){// HEADER
		String line = "";
		for (String key : totalRabs2.keySet()) {
            line = line+ key + ",";
		}
		line = line + "\n";
		Writer output;
		//CAMBIO
		output = new BufferedWriter(new FileWriter(TotalRabs, false));		
//		output = new BufferedWriter(new FileWriter("C:/Users/lmayo/workspace/immunity/ResultsIntrTransp3.csv", false));
		output.append(line);
		output.close();	
		
	}
		String line = "";
		for (String key : totalRabs2.keySet()) {
            line = line+ Math.round(totalRabs2.get(key)*100000d)/100000d + ",";
		}
		line = line + "\n";
		Writer output;
		//CAMBIO
		output = new BufferedWriter(new FileWriter(TotalRabs, true));
//		output = new BufferedWriter(new FileWriter("C:/Users/lmayo/workspace/immunity/ResultsIntrTransp3.csv", true));
		output.append(line);
		output.close();
	}
	
	private void writeToCsvCisternsArea(TreeMap<String, Double> orderCisternsArea) throws IOException {
		double tick = RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		if (tick <10){// HEADER
			String line = "";
			for (String key : orderCisternsArea.keySet()) {
	            line = line+ key + ",";
			}
			line = line + "\n";
			Writer output;
			//CAMBIO
			output = new BufferedWriter(new FileWriter(cisternsAreaPath, false));		
//			output = new BufferedWriter(new FileWriter("C:/Users/lmayo/workspace/immunity/ResultsIntrTransp3.csv", false));
			output.append(line);
			output.close();	
			
		}
			String line = "";
			for (String key : orderCisternsArea.keySet()) {
	            line = line+ Math.round(orderCisternsArea.get(key)*100000d)/100000d + ",";
			}
			line = line + "\n";
			Writer output;
			//CAMBIO
			output = new BufferedWriter(new FileWriter(cisternsAreaPath, true));
//			output = new BufferedWriter(new FileWriter("C:/Users/lmayo/workspace/immunity/ResultsIntrTransp3.csv", true));
			output.append(line);
			output.close();
		}

	
	
	
	// sum the content in all endosomes weighted by the rab content of each endosome
	public void contentDistribution(HashMap<String, Double> totalRabs, HashMap<String, Double> initialTotalRabs, HashMap<String, Double> cisternsArea) {
		List<Endosome> allEndosomes = new ArrayList<Endosome>();
		int tick = (int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		if (tick == 1) {
			for (String mem : ModelProperties.getInstance().getMembraneMet()) {
			initialTotalMembraneCargo.put(mem, 0d);	
			}
			for (String sol : ModelProperties.getInstance().getSolubleMet()) {
			initialTotalSolubleCargo.put(sol, 0d);	
			}
			for (Object obj : grid.getObjects()) {
				if (obj instanceof Endosome) {
					allEndosomes.add((Endosome) obj);
				}
			}
			for (Endosome end : allEndosomes) {
				for (String mem : end.membraneContent.keySet()) {
					double value = initialTotalMembraneCargo.get(mem) + end.membraneContent.get(mem);
					initialTotalMembraneCargo.put(mem, value);
				}
				for (String sol : end.solubleContent.keySet()) {
					double value = initialTotalSolubleCargo.get(sol) + end.solubleContent.get(sol);
					initialTotalSolubleCargo.put(sol, value);
				}
			}
		}
		content();// initialize all contents to zero
// include in the contentDistribution all the recycled components, soluble and membrane
		HashMap<String, Double> solubleRecycle = PlasmaMembrane.getInstance().getSolubleRecycle();
		HashMap<String, Double> membraneRecycle = PlasmaMembrane.getInstance().getMembraneRecycle();
// include in the contentDistribution all the recycled components, soluble and membrane		
		HashMap<String, Double> solubleSecretion = EndoplasmicReticulum.getInstance().getSolubleRecycle();
		HashMap<String, Double> membraneSecretion = EndoplasmicReticulum.getInstance().getMembraneRecycle();
		HashMap<String, Double> solubleCell = Cell.getInstance().getSolubleCell();
		for (String sol : solubleRecycle.keySet()) {
//			System.out.println(" soluble "+ sol);
			double value = solubleRecycle.get(sol);
			contentDist.put(sol+"Pm", value);
//			System.out.println("SOLUBLE  PM"+ sol + value );
		}
		for (String mem : membraneRecycle.keySet()) {
			//System.out.println(" soluble "+ sol + " Rab " +rab);
			double value = membraneRecycle.get(mem);
			contentDist.put(mem+"Pm" , value);
//			System.out.println("MEMBRANE PM  "+ mem + value);
		}			
		for (String sol : solubleSecretion.keySet()) {
//			System.out.println(" soluble "+ sol);
			double value = solubleSecretion.get(sol);
			contentDist.put(sol+"Er", value);
//			System.out.println("SOLUBLE  ER "+ sol + value );
		}
		for (String mem : membraneSecretion.keySet()) {
			//System.out.println(" soluble "+ sol + " Rab " +rab);
			double value = membraneSecretion.get(mem);
			contentDist.put(mem+"Er" , value);
			System.out.println("MEMBRANE ER  "+ mem + value);
		}
		for (String sol : solubleCell.keySet()) {
//			System.out.println(" soluble "+ sol);
			double value = solubleCell.get(sol);
			contentDist.put(sol+"Cy", value);
//			System.out.println("SOLUBLE CELL  "+ sol + value );
		}
		
//		for the set of all endosomes, calculate the content distribution among the different
//		rab-containing compartments.  This is calculated as the sum of all the soluble and
//		membrane components multiplied by the area ratio corresponding to the endosome
//		this is: content*RabX/(area of the endosome).
//		In addition, the total amount of each Rab present in all organelles
//		in the system is calculated.  And the total volume that correspond to the Rab domain adding
//		all volumes proportional to the rab area of each organelle
		allEndosomes.clear();
		for (Object obj : grid.getObjects()) {
			if (obj instanceof Endosome) {
				allEndosomes.add((Endosome) obj);
			}
		}

		for (String rab: rabSet){
			totalRabs.put(rab,  0.0);
			totalVolumeRabs.put(rab, 0.0);
			cisternsArea.put(rab, 0.0);
		}
		double totalIndividualEntropy = 0d;
		double totalArea = 0d;
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
			// for all endosomes calculate a individual entropy for the rab distribution
			double individualEntropy = 0d;
			for (String rab : rabContent.keySet()){
				double sum = totalRabs.get(rab)+ rabContent.get(rab);
				totalRabs.put(rab, sum);
				individualEntropy = individualEntropy - rabContent.get(rab)/area * Math.log(rabContent.get(rab)/area + 1E-30);
			}
			
			totalArea = totalArea + area;
			totalIndividualEntropy = totalIndividualEntropy + individualEntropy* area;
//			System.out.println("INDIVIDUAL ENTROPY " + totalIndividualEntropy);
// Sum all the organelle volume surrounded by a rab domain
		for (String rab : rabContent.keySet()){
			double sum = totalVolumeRabs.get(rab)+ volume*rabContent.get(rab)/area;
			totalVolumeRabs.put(rab, sum);
		}
//
// If the endosome contains a MARKER, print info in a Results file		
//		int tick = (int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
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
		
//		double totalCisternsArea = 0d;
//		for (String rab : cisternsArea.keySet()) {
//			totalCisternsArea = totalCisternsArea + cisternsArea.get(rab);
//		}
//		HashMap<String, Double> relativeCisternsArea = new HashMap<String, Double>();
//
//		double entropy = 0d;
//		for (String rab : cisternsArea.keySet()) {
//			double value = cisternsArea.get(rab)/totalCisternsArea;
//			relativeCisternsArea.put(rab, value);
//			entropy = entropy - value * Math.log(value + 1E-30);
//		}
		cisternsArea.clear();
//		cisternsArea.putAll(relativeCisternsArea);
//		cisternsArea.put("entropy", entropy);
		cisternsArea.put("entropyInd", totalIndividualEntropy/totalArea);
		int endosomeNumber = allEndosomes.size();	
//		int tick = (int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		if (tick == 1) initialTotalRabs.putAll(totalRabs);
		
		
//		sum in cytosol
		System.out.println(" TOTAL INDIVIDUAL ENTROPY      " + cisternsArea.get("entropyInd"));


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
		//CAMBIO
		output = new BufferedWriter(new FileWriter(MarkerResultsPath, true));
//		output = new BufferedWriter(new FileWriter("C:/Users/lmayo/workspace/immunity/ResultsMarker.csv", true));
		output.append(line);
		output.close();
	}
	


	// generate the set of all combinations between contents 
	//(soluble or membrane) with all Rabs and sets the initial values to zero
	public TreeMap<String, Double> content() {
		for (String sol : solubleMet) {
			contentDist.put(sol+"Pm", 0d);
		} 
		for (String mem : membraneMet) {
			contentDist.put(mem+"Pm", 0d);
			
		}
		for (String sol : solubleMet) {
			contentDist.put(sol+"Er", 0d);
		} 
		for (String mem : membraneMet) {
			contentDist.put(mem+"Er", 0d);
			
		}
		for (String sol : Cell.getInstance().getSolubleCell().keySet()) {
			contentDist.put(sol+"Cy", 0d);
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

	public final TreeMap<String, Double> getContentDist() {
		return contentDist;
	}

	public final HashMap<String, Double> getTotalVolumeRabs() {
		return totalVolumeRabs;
	}
}
