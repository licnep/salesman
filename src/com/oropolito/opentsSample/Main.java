package com.oropolito.opentsSample;
import java.beans.Customizer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.lang.String;
import java.lang.Integer;
import java.util.Collections;
import java.util.Comparator;

import org.coinor.opents.*;

public class Main
{
	private enum ParamFile {
	    NONE, PARAMS, INSTANCES
	}
	

    public static void main (String args[]) 
    {
    	int numCustomers=0;
    	int[][] customersPoints;
//    		String myFile = new String("C:/Users/Guido/EclipseWorkspace/OpenTS/Data/TSP/eil51.tsp");       	
//    		String myFile = new String("C:/Users/Guido/EclipseWorkspace/OpenTS/Data/TSP/eil76.tsp");
//    		String myFile = new String("C:/Users/Guido/EclipseWorkspace/OpenTS/Data/TSP/pr152.tsp");    		
//       	String myFile = new String("C:/Users/Guido/EclipseWorkspace/OpenTS/Data/TSP/pr1002.tsp");
//    		String myFile = new String("C:/Users/Guido/EclipseWorkspace/OpenTS/Data/TSP/rat195.tsp");
    	String myFile = new String("./Data/TSP/berlin52.tsp");
    	//String myFile = new String("/home/alox/Programmazione/Optimization/example/Data/TSP/pr1002.tsp");
    	
       	readParams(args);
        double[][] customers = new double[numCustomers][2];
		try
		{
			File input = new File(myFile);
			BufferedReader br = new BufferedReader(	new InputStreamReader(new FileInputStream(input)));
			String line;
			String token;
			line = br.readLine();	
			line = br.readLine();	
			line = br.readLine();	
			line = br.readLine();
			StringTokenizer st = new StringTokenizer(line);	
			token = st.nextToken();
			token = st.nextToken();		
			numCustomers = Integer.parseInt(token); 
	        customers = new double[numCustomers][2];
			line = br.readLine();
			line = br.readLine();
	        for( int i = 0; i < numCustomers; i++ )
	        {
				line = br.readLine();        	
				st = new StringTokenizer(line);	
				token = st.nextToken();
				token = st.nextToken();
				customers[i][0] = Double.parseDouble(token); 
				token = st.nextToken();
				customers[i][1] = Double.parseDouble(token); 
	        }
			line = br.readLine();						
			if (!line.toUpperCase().contains("EOF"))
			{
				throw new IllegalArgumentException("Error while reading the input file: EOF Section");
			}

		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error "+e.getMessage());
	        System.exit(-1);
		}
		GlobalData.numCustomers = numCustomers;
        // Initialize our objects
        java.util.Random r = new java.util.Random( 12345 );
        
        ObjectiveFunction objFunc = new MyObjectiveFunction( customers );
//        Solution initialSolution  = new MySolution( customers );
        Solution initialSolution  = new MyGreedyStartSolution( customers );
        MoveManager   moveManager = new MyMoveManager();
        TabuList         tabuList = new SimpleTabuList( 7 ); // In OpenTS package
        TabuList         tabuList2 = new MyTabuList( 7 );
        
        // Create Tabu Search object
        TabuSearch tabuSearch = new SingleThreadedTabuSearch(
                initialSolution,
                moveManager,
                objFunc,
              tabuList,
//              tabuList2,
                new BestEverAspirationCriteria(), // In OpenTS package
                false ); // maximizing = yes/no; false means minimizing
        
        MyTSListener myListener = new MyTSListener();
        tabuSearch.addTabuSearchListener(myListener);

        
        // Start solving
        tabuSearch.setIterationsToGo( 1000 );
        tabuSearch.startSolving();
        
        // Show solution
        MySolution best = (MySolution)tabuSearch.getBestSolution();
        System.out.println( "Best Solution:\n" + best );

        // Mostro la soluzione ottimale
        MySolution ottimale = new MySolution(customers);
        ottimale.tour = readTour("./Data/TSP/berlin52.opt.tour");
        //ottimale.tour = readTour("/home/alox/Programmazione/Optimization/example/Data/TSP/pr1002.opt.tour");
        double[] ottimal = objFunc.evaluate(ottimale, null);
        ottimale.setObjectiveValue(ottimal);
        System.out.println( "Optimal Solution:");
        System.out.println(ottimale);
        double miaLunghezza = best.getObjectiveValue()[0];
        System.out.println("Optimality:"+ottimal[0]/miaLunghezza);
        
        int[] tour = best.tour;
//        for( int i = 0; i < tour.length; i++ )
//            System.out.println( 
//             customers[tour[i]][0] + "\t" + customers[tour[i]][1] );
    }   // end main
    
    public static int[] readTour(String filename)
    {
    	List<Integer> lista = new ArrayList<Integer>();
    	File input = new File(filename);
    	try {
			BufferedReader br = new BufferedReader(	new InputStreamReader(new FileInputStream(input)));
			String line;
			boolean inTour = false;
			while ((line = br.readLine()) != null) 
			{
				if (!inTour) {
					if(line.equals("TOUR_SECTION")) inTour=true;
				} else {
					if(line.equals("-1")) {
						inTour=false;
						break;
					}
					lista.add(Integer.parseInt(line)-1); //meno 1 perche' nel file parte da 1 invece che da 0
				}
			}
    	} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error "+e.getMessage());
	        System.exit(-1);
		}    
    	int[] ret = new int[lista.size()];
        int i = 0;
        for (Integer e : lista) ret[i++] = e.intValue();
        return ret;
    }

    public static void readParams(String args[])
    		{
    			ParamFile fileStatus = ParamFile.NONE;
    			//String myFileParam = "C:/Users/Guido/EclipseWorkspace/OpenTS/Data/TSP/param.txt";
    			String myFileParam = "/home/alox/Programmazione/Optimization/example/Data/param.txt";
    			if (args.length > 0)
    			{
    				myFileParam = args[0];
    			}
    			try
    			{
    				File input = new File(myFileParam);
    				BufferedReader br = new BufferedReader(	new InputStreamReader(new FileInputStream(input)));
    				String line;
    				String token;
    				StringTokenizer st;
    				while ((line = br.readLine()) != null)
    				{   					
    					switch (fileStatus)
    					{
    					case NONE: 
    	    					st = new StringTokenizer(line);	
    							token = st.nextToken();    							
    	    					switch (token.toUpperCase()) 
    	    					{
    	    						case "PARAMS":
    	    							fileStatus = ParamFile.PARAMS;
    	    							System.out.println("Reading the parameters");
    	    							break;
    	    						case "INSTANCES":
    	    							fileStatus = ParamFile.INSTANCES;
    	    							System.out.println("Reading the instances");
    	    							break;
    	    						case "EOF":
    	    							System.out.println("Close the file");
    	    							br.close();
    	    							return;
    	    						default:
    	    							break;
    	    					}
    						break;
    					case PARAMS:
							if (line.toUpperCase().contains("ENDPARAMS"))
							{
								fileStatus = ParamFile.NONE;
    							System.out.println("End of parameters");
							}
							else
							{
								//Read the parameters
							}
    						break;
    					case INSTANCES:
							if (line.toUpperCase().contains("ENDINSTANCES"))
							{
    							System.out.println("End of instances");								
								fileStatus = ParamFile.NONE;
							}
							else
							{
								//Read the input files
							}
    						break;
    					}
    				}
    			}
    			catch (Exception e) {
    				e.printStackTrace();
    				System.out.println("Error "+e.getMessage());
    		        System.exit(-1);
    			}    			
    		}
    
}   // end class Main

