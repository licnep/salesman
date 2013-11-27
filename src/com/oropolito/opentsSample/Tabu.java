package com.oropolito.opentsSample;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.coinor.opents.BestEverAspirationCriteria;
import org.coinor.opents.MoveManager;
import org.coinor.opents.ObjectiveFunction;
import org.coinor.opents.SimpleTabuList;
import org.coinor.opents.SingleThreadedTabuSearch;
import org.coinor.opents.Solution;
import org.coinor.opents.TabuList;
import org.coinor.opents.TabuSearch;

import com.oropolito.opentsSample.GUI.GUI_model;
import com.oropolito.opentsSample.GUI.GUI_view;

public class Tabu {
	private enum ParamFile {
	    NONE, PARAMS, INSTANCES
	}
	

    public static void main (int iterations, String tour_filename, String opt_tour_filename) 
    {
    	int numCustomers=0;
    	int[][] customersPoints;

    	//String myFile = new String("./Data/TSP/berlin52.tsp");
    	
       	//readParams(args);
        double[][] customers = new double[numCustomers][2];
		try
		{
			File input = new File(tour_filename);
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
        tabuSearch.setIterationsToGo( iterations );
        tabuSearch.startSolving();
        
        // Show solution
        MySolution best = (MySolution)tabuSearch.getBestSolution();
        System.out.println( "Best Solution:\n" + best );

        // Mostro la soluzione ottimale
        MySolution ottimale = new MySolution(customers);
        //ottimale.tour = readTour("./Data/TSP/berlin52.opt.tour");
        ottimale.tour = readTour(opt_tour_filename);
        double[] ottimal = objFunc.evaluate(ottimale, null);
        ottimale.setObjectiveValue(ottimal);
        System.out.println( "Optimal Solution:");
        System.out.println(ottimale);
        double miaLunghezza = best.getObjectiveValue()[0];
        System.out.println("Optimality:"+ottimal[0]/miaLunghezza);
        
        GUI_model model = new GUI_model();
        GUI_view view = new GUI_view(model);
        model.setCustomers(customers);
        model.setTour_optimal(ottimale.tour);
        model.setTour_current(best.tour);
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
					String tokens[] = line.split("\\s");
					for (int i=0;i<tokens.length;i++) {
						try {
							lista.add(Integer.parseInt(tokens[i])-1); //meno 1 perche' nel file parte da 1 invece che da 0
						} catch (Exception e) {
							//non e' un numero, "" non faccio niente
						}
					}
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
}
