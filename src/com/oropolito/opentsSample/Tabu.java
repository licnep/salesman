package com.oropolito.opentsSample;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

import org.coinor.opents.BestEverAspirationCriteria;
import org.coinor.opents.Move;
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
	private GUI_model gui_model; 
	private double ottimal[];
	
	private enum ParamFile {
	    NONE, PARAMS, INSTANCES
	}
	

    public void main (int iterations, String base_dir, String filename, Istanza currentInstance)  
    {
    	long startTime = System.currentTimeMillis();
    	
    	gui_model = new GUI_model();
    	if(G.GUI) {
    		GUI_view gui_view = new GUI_view(gui_model);
    	}
        G.gui_model = gui_model;
    	
    	int numCustomers=0;
       	
        double[][] customers = new double[numCustomers][2];
        
        String tour_filename = new String(base_dir+filename+".tsp");
    	String opt_tour_filename = new String(base_dir+filename+".opt.tour");
    	File opt_tour = new File(opt_tour_filename);
    	
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
		if (G.GUI) gui_model.setCustomers(customers);
		G.numCustomers = numCustomers;
		G.customers = customers;
		
		//if(numCustomers>850)iterations=iterations*2;
		
		//cerco la X massima per dimensionare il grafico:
		if(G.GUI) {
			double maxX = 0;
			for (int i=0;i<numCustomers;i++)
				if (customers[i][0]>maxX) maxX = customers[i][0];
			gui_model.area_size = maxX;
		}
		
		LK_ObjectiveFunction lkObjFunc = new LK_ObjectiveFunction(customers);
		MySolutionEdges soluzione_iniziale_nearest = new MyRandomSolution2(customers,gui_model);
		random4opt = new Random4Opt_MoveManager(lkObjFunc);
		
		double[] val = lkObjFunc.evaluate( soluzione_iniziale_nearest, null );
        soluzione_iniziale_nearest.setObjectiveValue( val );
		
		// Carico la soluzione ottimale
        MySolutionEdges ottimale = new MySolutionEdges(customers);
        if(opt_tour.exists()){
            ottimale.tour = readTour(opt_tour_filename);
            ottimal = lkObjFunc.evaluate(ottimale, null);
            ottimale.setObjectiveValue(ottimal);
            if(G.GUI)gui_model.setTour_optimal(ottimale.tour);
        }
		
        //inizializzio bit subneighbourhood attivi tutti a uno
        G.activeNeighbourhoods = new boolean[numCustomers];
        Arrays.fill(G.activeNeighbourhoods, true);
        
        MySolutionEdges bestSol = (MySolutionEdges)soluzione_iniziale_nearest.clone();
        double bestVal = soluzione_iniziale_nearest.getObjectiveValue()[0]; 
        long timeBest = 0;
        iterations = iterations*G.numCustomers;
        for (G.iteration=0;G.iteration<iterations;G.iteration++) {
        	//prendiamo tutte le mosse possibili dalla soluzione attuale
        	//scegliamo quella migliore, se e' migliorativa esegui e continua loop, altrimenti ritorna
        	localSearch2Opt(soluzione_iniziale_nearest, lkObjFunc);
        	
            if(G.GUI) gui_model.setTour_current(soluzione_iniziale_nearest.tour);

            lkObjFunc.localMinimumReached_UpdatePenalty(soluzione_iniziale_nearest);
            val = lkObjFunc.evaluate( soluzione_iniziale_nearest, null );
            soluzione_iniziale_nearest.setObjectiveValue( val );
            if(G.GUI) gui_model.update_current_optimality((soluzione_iniziale_nearest.getObjectiveValue()[0]-ottimal[0])*100/ottimal[0]);
            if (soluzione_iniziale_nearest.getObjectiveValue()[0]<bestVal) {
            	bestSol = (MySolutionEdges)soluzione_iniziale_nearest.clone();
            	bestVal = soluzione_iniziale_nearest.getObjectiveValue()[0];
            	timeBest = System.currentTimeMillis();
            	if(G.GUI) gui_model.update_best_optimality((bestVal-ottimal[0])*100/ottimal[0]);
            }
            //if(G.iteration%50==0) perturbateSolution(soluzione_iniziale_nearest, lkObjFunc);
        }
        
        // Show solution
        MySolutionEdges best = bestSol;
        System.out.println("ISTANZA: "+filename);
        System.out.println( "Best Solution:\n" + best );
        if(G.GUI)gui_model.setTour_current(best.tour);

        // Mostro la soluzione ottimale
        double miaLunghezza = best.getObjectiveValue()[0];
        if(opt_tour.exists()){
            System.out.println( "Optimal Solution:");
            System.out.println(ottimale);
            System.out.println("Optimality:"+(miaLunghezza-ottimal[0])*100/ottimal[0]);
            System.out.println("Lunghezza ottimale:"+ottimal[0]);
        }
        System.out.println("Lunghezza trovata:"+best.getObjectiveValue()[0]);
        
        /*
        if(opt_tour.exists())G.BestKnown[G.NumIstanza] = (int)ottimale.getObjectiveValue()[0];
        G.BestValue[G.NumIstanza] = (int)best.getObjectiveValue()[0];
        G.BestTime[G.NumIstanza] = timeBest-startTime;
        if(opt_tour.exists())G.OptPercentage[G.NumIstanza] =  (100*(miaLunghezza-ottimal[0])/ottimal[0]);
        G.NomeIstanza[G.NumIstanza] = filename;*/
        
        if(opt_tour.exists()) currentInstance.bestKnown = (int)ottimale.getObjectiveValue()[0];
        int bestValue = (int)best.getObjectiveValue()[0];
        double bestTime = (timeBest-startTime)/1000.0;
        currentInstance.addSolution(new Soluzione(bestValue,bestTime));
        currentInstance.nomeIstanza = filename;
        
        if(opt_tour.exists())G.NumIstOpt++;
        
        System.out.println("Tempo per trovare la best: "+(timeBest-startTime)/1000.0+"s");
        System.out.println("Tempo totale: "+(System.currentTimeMillis()-startTime)/1000.0+"s");
        
        //OUTPUT FILE
        try {
            File file = new File("./Data/Output/"+filename+".tour");
            BufferedWriter output = new BufferedWriter(new FileWriter(file));
            output.write("NAME: "+filename+"\n");
            output.write("TYPE: TOUR\n");
            output.write("DIMENSION: "+G.numCustomers+"\n");
            output.write("TOUR_SECTION\n");
            for(int i=0; i<G.numCustomers;i++){
            	output.write(best.tour[i]+"\n");
            }
            output.write("-1\nEOF");
            output.close();
          } catch ( IOException e ) {
             e.printStackTrace();
          }

        return;
    }   // end main
    
    private void double_bridge_perturbation() {
    	G.random_seed++;
    	tabuSearch.setCurrentSolution((MySolutionEdges)tabuSearch.getBestSolution().clone());
    	tabuSearch.setMoveManager(random4opt);
    	//((LK_TabuList)tabuSearch.getTabuList()).setTenure(0);
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
	
	public void localSearch2Opt(MySolutionEdges sol,LK_ObjectiveFunction objFunc) {
		//prendiamo tutte le mosse possibili dalla soluzione attuale
    	//scegliamo quella migliore, se e' migliorativa esegui e continua loop, altrimenti ritorna
		Move bestMove = new Move() {
			public void operateOn(Solution soln) {}
		};
		double delta = 0;
		do {
			bestMove = getBest2optMove(sol,objFunc);
			delta = objFunc.evaluate( sol, bestMove )[0]-sol.getObjectiveValue()[0];
			//evitare errori arrotondamento che fanno looppare (2 mosse che cambiano l'obj val di pochissimo vengono
			//System.out.println(delta);
			if (Math.abs(delta)<0.000000001) delta=0;
			if(delta<0) { //la mossa migliore e' migliorativa, la eseguo
				bestMove.operateOn(sol);
				sol.setObjectiveValue(new double[]{sol.getObjectiveValue()[0]+delta});
				if(G.GUI) gui_model.setTour_current(sol.tour);
			}
		} while(delta<0);
	}
	
	private Move getBest2optMove(MySolutionEdges sol,LK_ObjectiveFunction obj) {
		sol.sincEdgesWithTour(); //synchronize the edges with the tour
		Iterator<Edge> ie = sol.edges.iterator();
		
		LK_Move bestMove = new LK_Move(new ArrayList<Edge>(),new ArrayList<Edge>());
		double bestDelta=0;
		
		Edge x1,x2,x3,y1,y2,y3;
		
		while(ie.hasNext()) {
        	x1 = ie.next();
        	if(G.activeNeighbourhoods[x1.c1]||G.activeNeighbourhoods[x1.c2]) {
        		//"disattivo" il neighbourhood, in realta' se trovo almeno una mossa migliorativa lo riattivo 
        		G.activeNeighbourhoods[x1.c1]=false;
    			G.activeNeighbourhoods[x1.c2]=false;
	        	for (int i=0;i<G.nVicini;i++) {
	        		y1 = obj.edgeVicini[x1.c2][i];
	        		//X2 e' obbligato una volta scelto Y1
	        		x2 = sol.getEdgeBefore(y1.c2);
	        		//Y2 che ricollega a t1
	        		y2 = new Edge(x2.c2 , x1.c1);
	        		if (y2.isProper()) {
		        		//valuto la mossa e' controllo se e' migliorativa:
		        		LK_Move mossa = new LK_Move(new ArrayList<Edge>(Arrays.asList(x1,x2)), new ArrayList<Edge>(Arrays.asList(y1,y2)));
		        		double cur = obj.evaluate( sol, mossa )[0]-sol.getObjectiveValue()[0];
		        		if (cur<bestDelta) {
		        			bestDelta = cur;
		        			bestMove = mossa;
		        		}
		        		if (cur<0) {
		        			G.activeNeighbourhoods[x1.c1]=true;
		        			G.activeNeighbourhoods[x1.c2]=true;
		        		}
	        		}
	        		//testiamo anche la vertex insertion: (invece di ricollegare a x1, dobbiamo rimuovere anche l'edge dopo x1, e fare collegamenti diversi
	        		//x1,c2 e' il vertice che viene 'tolto e reinserito'
	        		//insertion la testo solo coi 10 piu' vicini
	        		if (i<10 && x2.c2!=x1.c2) {
		        		y2 = new Edge(x2.c2,x1.c2);
		        		x3 = sol.getEdgeAfter(x1.c2);
		        		y3 = new Edge(x1.c1,x3.c2);
		        		LK_Move mossa = new LK_Move(new ArrayList<Edge>(Arrays.asList(x1,x2,x3)), new ArrayList<Edge>(Arrays.asList(y1,y2,y3)));
		        		double cur = obj.evaluate( sol, mossa )[0]-sol.getObjectiveValue()[0];
		        		if (cur<bestDelta) {
		        			bestDelta = cur;
		        			bestMove = mossa;
		        		}
		        		if (cur<0) {
		        			G.activeNeighbourhoods[x1.c1]=true;
		        			G.activeNeighbourhoods[x1.c2]=true;
		        		}
	        		}
	        	}
        	}
        }
        return bestMove;
	}
	
	
	
	private void perturbateSolution(MySolutionEdges sol, LK_ObjectiveFunction obj) {
		//generate a bunch of random 4 opt moves
		G.random_seed++;
    	Move[] mosse = random4opt.getAllMoves(sol);
    	Move bestMove = new Move() {public void operateOn(Solution soln) {}};
    	double min = Double.MAX_VALUE;
    	for (int i=0;i<mosse.length;i++) {
    		double v = obj.evaluate(sol, mosse[i])[0];
    		if (v<min) {
    			min=v;
    			bestMove = mosse[i];
    		}
    	}
    	bestMove.operateOn(sol);
    	//try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace();}
    	sol.setObjectiveValue(new double[]{min});
	}
}
