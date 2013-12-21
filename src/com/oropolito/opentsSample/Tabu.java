package com.oropolito.opentsSample;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
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
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;

public class Tabu {
	
	private TabuSearch tabuSearch;
	private Random4Opt_MoveManager random4opt;
	
	private enum ParamFile {
	    NONE, PARAMS, INSTANCES
	}
	

    public void main (int iterations, String tour_filename, String opt_tour_filename) 
    {
    	GUI_model gui_model = new GUI_model();
        GUI_view gui_view = new GUI_view(gui_model);
        GlobalData.gui_model = gui_model;
        LinkedList<Long> hashSoluzioni = new LinkedList<Long>();
    	
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
		gui_model.setCustomers(customers);
		GlobalData.numCustomers = numCustomers;
		GlobalData.customers = customers;
		GlobalData.MIN_TENURE = 15 + (int)((numCustomers)/5);
		GlobalData.MAX_TENURE = (int)numCustomers/2;
		
		//cerco la X massima per dimensionare il grafico:
		double maxX = 0;
		for (int i=0;i<numCustomers;i++)
			if (customers[i][0]>maxX) maxX = customers[i][0];
		gui_model.area_size = maxX;
		
        // Initialize our objects
        GlobalData.rand = new java.util.Random( GlobalData.random_seed );
        
        //ObjectiveFunction objFunc = new VertexInsertion_ObjectiveFunction( customers );
        ObjectiveFunction objFunc = new Composite_ObjectiveFunction( customers );
        Solution initialSolution  = new MyGreedyStartSolution( customers );
        MoveManager   moveManager = new Composite_MoveManager();
        //TabuList         tabuList = new Composite_TabuList( 10 );
        LK_TabuList			tabuList = new LK_TabuList(GlobalData.MIN_TENURE,4);
        LK_TabuList			tabuList2 = new LK_TabuList(GlobalData.MIN_TENURE,4);
        //TabuList         tabuList = new VertexInsertion_TabuList( 7 );
        //TabuList tabuList = new My2Opt_TabuList(7,4);
        LK_ObjectiveFunction lkObjFunc = new LK_ObjectiveFunction(customers);
        LK_MoveManager lkMoveManagerOld = new LK_MoveManager(lkObjFunc);
        random4opt = new Random4Opt_MoveManager(lkObjFunc,(LK_TabuList)tabuList);
        LK_MoveManagerPROPER lkMoveManager = new LK_MoveManagerPROPER(lkObjFunc,(LK_TabuList)tabuList);
        
        Solution soluzione_iniziale_random1 = new MyRandomSolution(numCustomers);
        Solution soluzione_iniziale_random2 = new MyRandomSolution2(customers,gui_model);
        //Solution soluzione_savings = new MySavingAlg(customers,gui_model,lkObjFunc);
        
        // Create Tabu Search object
        tabuSearch = new SingleThreadedTabuSearch(
                //initialSolution,
        		soluzione_iniziale_random2,
        		//soluzione_savings,
                lkMoveManagerOld,
                lkObjFunc,
              tabuList,
//              tabuList2,
                new BestEverAspirationCriteria(), // In OpenTS package
                false ); // maximizing = yes/no; false means minimizing
        
        //MyTSListener myListener = new MyTSListener();
        LK_Listener myListener = new LK_Listener();
        tabuSearch.addTabuSearchListener(myListener);

        // Carico la soluzione ottimale
        MySolutionEdges ottimale = new MySolutionEdges(customers);
        ottimale.tour = readTour(opt_tour_filename);
        double[] ottimal = objFunc.evaluate(ottimale, null);
        ottimale.setObjectiveValue(ottimal);
        gui_model.setTour_optimal(ottimale.tour);
        
        GlobalData.iteration = 0;
        GlobalData.nVicini = 50;
        
        int iterationiLocal = 100;
        GlobalData.iterazioni3Opt = 0;
        // Start solving
        for (;GlobalData.iteration<iterations;GlobalData.iteration++) 
        {
        	tabuSearch.setIterationsToGo( 1 );
            tabuSearch.startSolving();
            
            MySolutionEdges temp = (MySolutionEdges)tabuSearch.getCurrentSolution();
            gui_model.setTour_current(temp.tour);
            gui_model.update_current_optimality((temp.getObjectiveValue()[0]-ottimal[0])*100/ottimal[0]);

            MySolutionEdges cur_best = (MySolutionEdges)tabuSearch.getBestSolution();
            gui_model.update_best_optimality((cur_best.getObjectiveValue()[0]-ottimal[0])*100/ottimal[0]);
            
            if (GlobalData.iterazioni3Opt>0) {
            	GlobalData.nVicini = 25;
            	tabuSearch.setMoveManager(lkMoveManager);
            } else {
            	GlobalData.nVicini = 50;
            	tabuSearch.setMoveManager(lkMoveManagerOld);
            }
            GlobalData.iterazioni3Opt--;
            /*
            if(GlobalData.iteration==100||GlobalData.iteration==200) {
            	GlobalData.random_seed++;
            	tabuSearch.setCurrentSolution((MySolutionEdges)tabuSearch.getBestSolution().clone());
            	tabuList.setTenure(GlobalData.MIN_TENURE);
            	tabuSearch.setMoveManager(random4opt);
            }*/
            
            if(GlobalData.notImprovingCounter>15&&iterationiLocal<0||GlobalData.iteration==200) {
            	GlobalData.notImprovingCounter=0;
            	iterationiLocal=100;
            	GlobalData.random_seed++;
            	tabuSearch.setCurrentSolution((MySolutionEdges)tabuSearch.getBestSolution().clone());
            	tabuSearch.setMoveManager(random4opt);
            }
            /*if (GlobalData.iteration>100&&GlobalData.iteration<100) {
            	GlobalData.nVicini = Math.min(GlobalData.nVicini+1, 60);
            }*/
            //try { Thread.sleep(20); } catch (InterruptedException e) { e.printStackTrace();}
            
            long curHash = ((MySolutionEdges)tabuSearch.getCurrentSolution()).edges.hashCode();
            curHash = ((MySolutionEdges)tabuSearch.getCurrentSolution()).hashCode;
            if (hashSoluzioni.contains(curHash)) {
            	System.out.println("SOLUZIONE GIA VISTA!!!!");
        		//double_bridge_perturbation();
            	//tabuList.setTenure(tabuList.getTenure()+3);
            } else {
            	//tabuList.setTenure(Math.max(GlobalData.MIN_TENURE, tabuList.getTenure()-1));
            	hashSoluzioni.add(curHash);
            	if (hashSoluzioni.size()>50) {
            		hashSoluzioni.remove(0);
            	}
            }
            iterationiLocal--;
        }
        
        tabuList.setTenure(GlobalData.MIN_TENURE);
        GlobalData.nVicini = 25;

        tabuSearch = new SingleThreadedTabuSearch(
                //initialSolution,
        		tabuSearch.getBestSolution(),
                lkMoveManager,
                lkObjFunc,
                tabuList,
                new BestEverAspirationCriteria(), // In OpenTS package
                false );
        MyTSListener myListenerVertex = new MyTSListener();
        tabuSearch.addTabuSearchListener(myListenerVertex);
        
        for (;GlobalData.iteration<iterations+200;GlobalData.iteration++) 
        {
        	tabuSearch.setIterationsToGo( 1 );
            tabuSearch.startSolving();
            
            MySolutionEdges temp = (MySolutionEdges)tabuSearch.getCurrentSolution();
            gui_model.setTour_current(temp.tour);
            gui_model.update_current_optimality((temp.getObjectiveValue()[0]-ottimal[0])*100/ottimal[0]);

            MySolutionEdges cur_best = (MySolutionEdges)tabuSearch.getBestSolution();
            gui_model.update_best_optimality((cur_best.getObjectiveValue()[0]-ottimal[0])*100/ottimal[0]);
            //try { Thread.sleep(30); } catch (InterruptedException e) { e.printStackTrace();}
        } 
        //*/
        // Show solution
        MySolutionEdges best = (MySolutionEdges)tabuSearch.getBestSolution();
        System.out.println( "Best Solution:\n" + best );
        gui_model.setTour_current(best.tour);

        // Mostro la soluzione ottimale
        System.out.println( "Optimal Solution:");
        System.out.println(ottimale);
        double miaLunghezza = best.getObjectiveValue()[0];
        System.out.println("Optimality:"+(miaLunghezza-ottimal[0])*100/ottimal[0]);
        System.out.println("Lunghezza ottimale:"+ottimal[0]);
        
    }   // end main
    
    private void double_bridge_perturbation() {
    	GlobalData.notImprovingCounter=0;
    	GlobalData.random_seed++;
    	tabuSearch.setCurrentSolution((MySolutionEdges)tabuSearch.getBestSolution().clone());
    	tabuSearch.setMoveManager(random4opt);
	}

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
					if(line.trim().equals("TOUR_SECTION")) inTour=true;
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
