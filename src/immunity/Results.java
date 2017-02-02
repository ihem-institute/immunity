package immunity;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



import java.util.TreeMap;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

public class Results {
//	private static Results instance;
	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
//
//	public static Results getInstance() {
//		if (instance == null) {
//			instance = new Results();
//		}
//		return instance;
//	}

	CellProperties cellProperties = CellProperties.getInstance();

	public HashMap<String, Double> cellK = cellProperties.getCellK();
	public List<String> solubleMet = cellProperties.getSolubleMet();
	public List<String> membraneMet = cellProperties.getMembraneMet();
	public List<String> rabSet = cellProperties.getRabSet();
	public List<String> allMet = new ArrayList<String>();
	public HashMap<String, Double> initRabCell = new HashMap<String, Double>();
	public HashMap<String, Double> rabCompatibility = new HashMap<String, Double>();
	public HashMap<String, Double> tubuleTropism = new HashMap<String, Double>();
	public HashMap<String, List<String>> rabTropism = new HashMap<String, List<String>>();
	public HashMap<String, Double> mtTropism = new HashMap<String, Double>();
	public HashMap<String, Double> contentDist = new HashMap<String, Double>();
	
	public Results(ContinuousSpace<Object> sp, Grid<Object> gr) {
		this.space = sp;
		this.grid = gr;
		TreeMap<String, Double> header = new TreeMap<String, Double>(content());
		try {
			writeToCsvHeader(header);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//TreeMap<String, Double> header = new TreeMap<String, Double>(content());
	//writeToCsvHeader(header);
	

	//Results results = new Results(space, grid);

	@ScheduledMethod(start = 1, interval = 100)
	public void step() {
		contentDistribution();
		//System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
		TreeMap<String, Double> orderContDist = new TreeMap<String, Double>(contentDist);
		System.out.println(orderContDist);
		try {
			writeToCsv(orderContDist);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void writeToCsv(TreeMap<String, Double> orderContDist) throws IOException {
		String line = "";
		for (String key : orderContDist.keySet()) {
            line = line+ Math.round(orderContDist.get(key)*100)/100 + ";";
		}
		line = line + "\n";
		Writer output;
		output = new BufferedWriter(new FileWriter("C:/users/lmayorga/desktop/ResultsIntrTransp3.csv", true));
		output.append(line);
		output.close();

		
	}
	private void writeToCsvHeader(TreeMap<String, Double> orderContDist) throws IOException {
		String line = "";
		for (String key : orderContDist.keySet()) {
            line = line+ key + ";";
		}
		line = line + "\n";
		Writer output;
		output = new BufferedWriter(new FileWriter("C:/users/lmayorga/desktop/ResultsIntrTransp3.csv", false));
		output.append(line);
		output.close();	
	}

	public void contentDistribution() {
		List<Endosome> allEndosomes = new ArrayList<Endosome>();
		for (Object obj : grid.getObjects()) {
			if (obj instanceof Endosome) {
				allEndosomes.add((Endosome) obj);
			}
		}
		content();
		//System.out.println(contentDist);

		for (Endosome endosome : allEndosomes) {
			Double area = endosome.area;
			HashMap<String, Double> rabContent = endosome.getRabContent();
			HashMap<String, Double> membraneContent = endosome
					.getMembraneContent();
			HashMap<String, Double> solubleContent = endosome
					.getSolubleContent();

			for (String rab : rabContent.keySet()) {
				for (String sol : solubleContent.keySet()) {
					//System.out.println(" soluble "+ sol + " Rab " +rab);
					double value = contentDist.get(sol + rab)
							+ solubleContent.get(sol) * rabContent.get(rab)
							/ area;
					contentDist.put(sol + rab, value);
					//System.out.println("SOLUBLE"+sol + "Rab" +rab);
				}
			for (String mem : membraneContent.keySet()) {
				//System.out.println(" membrane "+mem + " Rab " +rab);
					double value = contentDist.get(mem + rab)
							+ membraneContent.get(mem) * rabContent.get(rab)
							/ area;
					contentDist.put(mem + rab, value);
				}

			}
		}
		//System.out.println("ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ"+contentDist);

	}

	public HashMap<String, Double> content() {
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

	public HashMap<String, Double> getCellK() {
		return cellK;
	}

}
