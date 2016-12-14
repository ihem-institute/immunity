package immunity;

import java.util.HashMap;

import org.COPASI.CCompartment;
import org.COPASI.CCopasiDataModel;
import org.COPASI.CCopasiMessage;
import org.COPASI.CCopasiMethod;
import org.COPASI.CCopasiObjectName;
import org.COPASI.CCopasiParameter;
import org.COPASI.CCopasiReportSeparator;
import org.COPASI.CCopasiRootContainer;
import org.COPASI.CCopasiStaticString;
import org.COPASI.CCopasiTask;
import org.COPASI.CMetab;
import org.COPASI.CModel;
import org.COPASI.CModelEntity;
import org.COPASI.CReaction;
import org.COPASI.CRegisteredObjectName;
import org.COPASI.CReportDefinition;
import org.COPASI.CReportDefinitionVector;
import org.COPASI.CTrajectoryMethod;
import org.COPASI.CTrajectoryProblem;
import org.COPASI.CTrajectoryTask;
import org.COPASI.ReportItemVector;

public class RabConversion {
	private static RabConversion instance = null;
	private CCopasiDataModel dataModel;
	private CModel model;
    private CReportDefinition report;
    private CTrajectoryTask trajectoryTask;
	private HashMap<String, CMetab> nameMetabs = new HashMap<String, CMetab>();
	
	public static RabConversion getInstance () {
		if (instance == null) {
			instance = new RabConversion();
		}
		
		return instance;
	}
	
	protected RabConversion() {
		System.out.println("Instantiation Once");
		
		// to defeat instantiation
		assert CCopasiRootContainer.getRoot() != null;
        // create a new datamodel
		dataModel = CCopasiRootContainer.addDatamodel();
        assert CCopasiRootContainer.getDatamodelList().size() == 1;
        
        String modelFileName = "rabs_conversion.cps";
        
        try
        {
            // load the model without progress report
            dataModel.loadModel(modelFileName);
        }
        catch (java.lang.Exception ex)
        {
            System.err.println("Error while loading the model from file named \"" + modelFileName + "\".");
            System.exit(1);
        }
        
        model = dataModel.getModel();
        assert model != null;
        System.out.println("Model statistics for model \"" + model.getObjectName() + "\".");
        
     // output number and names of all compartments
        int i, iMax = (int)model.getCompartments().size();
        System.out.println("Number of Compartments: " + (new Integer(iMax)).toString());
        System.out.println("Compartments: ");
        for (i = 0;i < iMax;++i)
        {
            CCompartment compartment = model.getCompartment(i);
            assert compartment != null;
            System.out.println("\t" + compartment.getObjectName());
        }

        // output number and names of all metabolites
        iMax = (int)model.getMetabolites().size();
        System.out.println("Number of Metabolites: " + (new Integer(iMax)).toString());
        System.out.println("Metabolites: ");
        for (i = 0;i < iMax;++i)
        {
            CMetab metab = model.getMetabolite(i);
            assert metab != null;
            nameMetabs.put(metab.getObjectName(), metab);
            System.out.println(metab.getObjectName());
        }
        
   //     setInitialConcentration("IL6", 4.0);
        
        for (String s : nameMetabs.keySet()) {
        	CMetab metab = nameMetabs.get(s);
        	System.out.println("\t" + metab.getObjectName() + "\t" + metab.getInitialConcentration() + "\t" + metab.getInitialValue());
        }

        // output number and names of all reactions
        iMax = (int)model.getReactions().size();
        System.out.println("Number of Reactions: " + (new Integer(iMax)).toString());
        System.out.println("Reactions: ");
        for (i = 0;i < iMax;++i)
        {
            CReaction reaction = model.getReaction(i);
            assert reaction != null;
            System.out.println("\t" + reaction.getObjectName());
        }
        
        setUpReport();
        setUpTask();
	}
	
	private void setUpReport() {
		// create a report with the correct filename and all the species against
        // time.
        CReportDefinitionVector reports = dataModel.getReportDefinitionList();
        // create a new report definition object
        report = reports.createReportDefinition("Report", "Output for timecourse");
        // set the task type for the report definition to timecourse
        report.setTaskType(CCopasiTask.timeCourse);
        // we don't want a table
        report.setIsTable(false);
        // the entries in the output should be seperated by a ", "
        report.setSeparator(new CCopasiReportSeparator(", "));
        
        // we need a handle to the header and the body
        // the header will display the ids of the metabolites and "time" for
        // the first column
        // the body will contain the actual timecourse data
        ReportItemVector header = report.getHeaderAddr();
        ReportItemVector body = report.getBodyAddr();
        
        body.add(new CRegisteredObjectName(model.getObject(new CCopasiObjectName("Reference=Time")).getCN().getString()));
        body.add(new CRegisteredObjectName(report.getSeparator().getCN().getString()));
        header.add(new CRegisteredObjectName(new CCopasiStaticString("time").getCN().getString()));
        header.add(new CRegisteredObjectName(report.getSeparator().getCN().getString()));

        int i, iMax =(int) model.getMetabolites().size();
        for (i = 0;i < iMax;++i)
        {
            CMetab metab = model.getMetabolite(i);
            assert metab != null;
            // we don't want output for FIXED metabolites right now
            if (metab.getStatus() != CModelEntity.FIXED)
            {
                // we want the concentration in the output
                // alternatively, we could use "Reference=Amount" to get the
                // particle number
                body.add(new CRegisteredObjectName(metab.getObject(new CCopasiObjectName("Reference=Concentration")).getCN().getString()));
                // add the corresponding id to the header
                header.add(new CRegisteredObjectName(new CCopasiStaticString(metab.getSBMLId()).getCN().getString()));
                // after each entry, we need a seperator
                if(i!=iMax-1)
                {
                  body.add(new CRegisteredObjectName(report.getSeparator().getCN().getString()));
                  header.add(new CRegisteredObjectName(report.getSeparator().getCN().getString()));
                }

            }

        }
	}
	
	private void setUpTask() {
		// get the trajectory task object
        trajectoryTask = (CTrajectoryTask)dataModel.getTask("Time-Course");
        assert trajectoryTask != null;
        // if there isn't one
        if (trajectoryTask == null)
        {
            // create a new one
            trajectoryTask = new CTrajectoryTask();

            // add the new time course task to the task list
            // this method makes sure that the object is now owned 
            // by the list and that it does not get deleted by SWIG
            dataModel.getTaskList().addAndOwn(trajectoryTask);
        }

        // run a deterministic time course
        trajectoryTask.setMethodType(CCopasiMethod.deterministic);

        // pass a pointer of the model to the problem
        trajectoryTask.getProblem().setModel(dataModel.getModel());

        // actiavate the task so that it will be run when the model is saved
        // and passed to CopasiSE
        trajectoryTask.setScheduled(true);

        // set the report for the task
        trajectoryTask.getReport().setReportDefinition(report);
        // set the output filename
        trajectoryTask.getReport().setTarget("cd4TimeCourse.txt");
        // don't append output if the file exists, but overwrite the file
        trajectoryTask.getReport().setAppend(false);
        
        // get the problem for the task to set some parameters
        CTrajectoryProblem problem = (CTrajectoryProblem)trajectoryTask.getProblem();

        // simulate 600 steps
        problem.setStepNumber(4000);
        // start at time 0
        dataModel.getModel().setInitialTime(0.0);
        // simulate a duration of 60 time units
        problem.setDuration(4000);
        // tell the problem to actually generate time series data
        problem.setTimeSeriesRequested(true);

        // set some parameters for the LSODA method through the method
        CTrajectoryMethod method = (CTrajectoryMethod)trajectoryTask.getMethod();

        CCopasiParameter parameter = method.getParameter("Absolute Tolerance");
        assert parameter != null;
        assert parameter.getType() == CCopasiParameter.DOUBLE;
        parameter.setDblValue(1.0e-12);
	}
	
	public void setInitialConcentration(String name, double value) {
		if (!nameMetabs.containsKey(name)) {
			System.out.println(name + "\t does not exist as a metab");
		} else {
			CMetab m = nameMetabs.get(name);
			m.setInitialConcentration(value);
			m.refreshInitialValue();
		}
	}
	
	
	
	public void runTimeCourse() {
		// reapply the initial values
		model.applyInitialValues();
		
/*        System.out.println("Starting ...");
        
        for (int i = 0; i < (int) model.getMetabolites().size(); ++i)
        {
            CMetab metab = model.getMetabolite(i);
            assert metab != null;
            
            System.out.println(metab.getObjectName() + " : " + metab.getConcentration());
        }*/
		
		boolean result=true;
        String processError = "";
		String processWarning = "";
		try
        {
            // now we run the actual trajectory
        	System.out.println("trajectoryTask.process");
            result=trajectoryTask.process(true);
            processError = trajectoryTask.getProcessError();
            processWarning = trajectoryTask.getProcessWarning();
        }
        catch (java.lang.Exception ex)
        {
            System.err.println( "Error. Running the time course simulation failed." );
            System.out.println(processError);
            System.out.println(processWarning);
            // check if there are additional error messages
            if (CCopasiMessage.size() > 0)
            {
                // print the messages in chronological order
                System.err.println(CCopasiMessage.getAllMessageText(true));
            }
            System.exit(1);
        }
        if(result==false)
        {
            System.err.println( "An error occured while running the time course simulation." );
            System.out.println(processError);
            System.out.println(processWarning);
            // check if there are additional error messages
            if (CCopasiMessage.size() > 0)
            {
                // print the messages in chronological order
                System.err.println(CCopasiMessage.getAllMessageText(true));
            }
            System.exit(1);
        }

  /*      // look at the timeseries
        CTimeSeries timeSeries = trajectoryTask.getTimeSeries();
        // we simulated 100 steps, including the initial state, this should be
        // 101 step in the timeseries
        assert timeSeries.getRecordedSteps() == 101;
        System.out.println( "The time series consists of " + (new Long(timeSeries.getRecordedSteps())).toString() + "." );
        System.out.println( "Each step contains " + (new Long(timeSeries.getNumVariables())).toString() + " variables." );
        System.out.println( "The final state is: " );
        int iMax = (int)timeSeries.getNumVariables();
        int lastIndex = (int)timeSeries.getRecordedSteps() - 1;
        for (int i = 0; i < iMax; ++i)
        {
            // here we get the particle number (at least for the species)
            // the unit of the other variables may not be particle numbers
            // the concentration data can be acquired with getConcentrationData
            System.out.println(timeSeries.getTitle(i) + ": " + (new Double(timeSeries.getData(lastIndex, i))).toString() );
        }
        
        System.out.println("Ending ...");
        
        for (int i = 0; i < (int) model.getMetabolites().size(); ++i)
        {
            CMetab metab = model.getMetabolite(i);
            assert metab != null;
            
            System.out.println(metab.getObjectName() + " : " + metab.getValue());
        }*/
	}
	
	public double getConcentration(String name) {
		double d = 0.0;
		
		if (!nameMetabs.containsKey(name)) {
			System.out.println(name + "\t does not exist as a metab");
		} else {
			CMetab m = nameMetabs.get(name);
			d = m.getConcentration();
		}
		
		return d;
	}

}
