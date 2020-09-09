package immunity;

import java.io.File;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

import repast.simphony.engine.schedule.ScheduledMethod;

public class UpdateParameters {

	private static UpdateParameters instance;
	public static UpdateParameters getInstance() {
		if( instance == null ) {
			instance = new  UpdateParameters();
		}
		return instance;
	}
	
	//CAMBIO
	LocalPath mainpath=new LocalPath(); 
	String InputPath = mainpath.getPathInputIT();

	private String oldFile="";
	
	public String getOldFile() {
		return oldFile;
	}
	
	public void setOldFile(String oldFile) {
		this.oldFile = oldFile;
	}
		
	public UpdateParameters() {
		
//		this.space = sp;
//		this.grid = gr;				
	}

	@ScheduledMethod(start = 1, interval = 100)
	public void step(){
			try {
				testNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	public String getFileAtt() throws IOException { // para q quede mas ordenado
		File file = new File(InputPath);
		Path filePath = file.toPath();		
		BasicFileAttributes attr = Files.readAttributes(filePath, BasicFileAttributes.class);
		String newFile = attr.lastModifiedTime().toString();
		return newFile;
	}
	
	public void testNewFile() throws IOException {
		//CAMBIO
		String newFile=getFileAtt();
		if (oldFile==""){              // para que no cargue dos veces todo al iniciar el modelo
				setOldFile(newFile);
		}
		
		if (newFile.equals(oldFile)){return;}
		else{System.out.println(1d);
			try {
				CellProperties cellProperties = CellProperties.getInstance();
				CellProperties.loadFromCsv(cellProperties);     // para no repetir el codigo de la misma funcion
				
				// The CellProperties are changed, but for parameters that are actualized only at the  				
				//	beginning, I need to re-load values.  This is the case of initial rabs content in the 
				//	Cell. This maight be useful for knocking down a Rab in the middle of an experiment
				
				Cell cell = Cell.getInstance();
				
				cell.setRabCell(cellProperties.getInitRabCell());
				cell.setCellArea(cellProperties.getCellAgentProperties().get("cellArea"));// agregue estos 3 para q actualicen valores
				cell.setCellVolume(cellProperties.getCellAgentProperties().get("cellVolume"));//
				cell.setSolubleCell(cellProperties.getSolubleCell());//
								
				try {
					Thread.sleep(4000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
//				System.out.println(InitialOrganelles.getInstance()
//						.getInitRabContent());
//				System.out.println(InitialOrganelles.getInstance()
//						.getInitMembraneContent());
//				System.out.println(InitialOrganelles.getInstance()
//						.getInitSolubleContent());

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			setOldFile(newFile);
		}

		
		
	}	
	
}
