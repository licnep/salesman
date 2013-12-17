package com.oropolito.opentsSample;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;

import org.coinor.opents.*;

import com.oropolito.opentsSample.GUI.GUI_model;


public class LK_MoveManagerPROPER implements MoveManager
{
	public LK_ObjectiveFunction objFunc;
	public LK_TabuList tabu;
	
	ArrayList<Edge> edgesX;
	ArrayList<Edge> edgesY;
	
	public LK_MoveManagerPROPER(LK_ObjectiveFunction myObj,LK_TabuList tabu) {
		this.objFunc = myObj;
		this.tabu = tabu;
	}
	
    public Move[] getAllMoves( Solution solution )
    {   
    	MySolutionEdges sol = (MySolutionEdges)solution;
    	double[][] customers = GlobalData.customers;
        int[] tour = sol.tour;
        int len = tour.length;
        
        ArrayList<LK_Move> l = new ArrayList<LK_Move>();
        
        
        //edges contiene: [t1,t2,t3,t4...] 
        // dove x1=(t1,t2), y1=(t2,t3)... 
        ArrayList<Integer> edges = new ArrayList<Integer>();
        ArrayList<Integer> bestEdges = new ArrayList<Integer>();
        double G_star = Integer.MAX_VALUE; //miglioramento migliore per adesso
        
        sol.sincEdgesWithTour(); //synchronize the edges with the tour
        edgesX = new ArrayList<Edge>();
        edgesY = new ArrayList<Edge>();
        
        Iterator<Edge> ie = sol.edges.iterator();
        while(ie.hasNext()) {
        	edgesX.add(ie.next()); //setto x1
        	Edge[] vicini = objFunc.edgeVicini[edgesX.get(0).c2];
        	for (int i=0;i<GlobalData.nVicini;i++) {
        		//testo tutti i vicini Y che non devono essere gia' nel tour
        		if(pushEdgeY(vicini[i], sol)) {
        			//X2 e' obbligato una volta scelto Y1
        			if(pushEdgeX(sol.getEdgeBefore(edgesY.get(0).c2))) {
        				//scelta Y2
            			Edge[] vicini2 = objFunc.edgeVicini[edgesX.get(1).c2];
            			for(int j=0;j<GlobalData.nVicini;j++) {
            				
            				//mossa 2-opt
            				if(pushEdgeY(new Edge(edgesX.get(1).c2 , edgesX.get(0).c1), sol)) {
            					if(calculateGain(edgesX, edgesY)<G_star) {
            						if(!tabu.isTabu(sol, new LK_Move(edgesX, edgesY))&&generatesFeasibleTour(sol)) {
            							G_star = calculateGain(edgesX, edgesY);
        								l.add(new LK_Move((ArrayList<Edge>)edgesX.clone(), (ArrayList<Edge>)edgesY.clone()));
        							}
            					}
    							popEdgeY();
    						}
            				
            				if(pushEdgeY(vicini2[j], sol)) {
            					//X3:
            					if(pushEdgeX(sol.getEdgeBefore(edgesY.get(1).c2))) {
            						//ricollego all'inizio:
            						if(pushEdgeY(new Edge(edgesX.get(2).c2 , edgesX.get(0).c1), sol)) {
            							if(calculateGain(edgesX, edgesY)<G_star) {
	            							if(!tabu.isTabu(sol, new LK_Move(edgesX, edgesY))&&generatesFeasibleTour(sol)) {
	            								G_star = calculateGain(edgesX, edgesY);
	            								l.add(new LK_Move((ArrayList<Edge>)edgesX.clone(), (ArrayList<Edge>)edgesY.clone()));
	            							}
            							}
            							popEdgeY();
            						}
            						popEdgeX();
            					}
            					popEdgeY();
            				}            				
            			}
            			popEdgeX();
        			}
        			popEdgeY();
        		}
        			/*
        			edgesY.add( new Edge(edgesX.get(1).c2 , edgesX.get(0).c1) ); //Y2 che ricollega a t1
        			LK_Move curMov = new LK_Move((ArrayList<Edge>)edgesX.clone(), (ArrayList<Edge>)edgesY.clone(), sol);
        			
        			double g = calculateGain(edgesX, edgesY);
        			if (g<G_star) {
        				if(!tabu.isTabu(solution, curMov)) {
        					System.out.println(g);
        					G_star=g;
            				l.add(new LK_Move((ArrayList<Edge>)edgesX.clone(), (ArrayList<Edge>)edgesY.clone(), sol));
            			}
        			}
        		}*/
        	}
        	
        	edgesX.clear(); //testo un altro x1
        }
        
        LK_Move[] moves = l.toArray(new LK_Move[l.size()]);
        return moves;
    }   // end getAllMoves
    
    /**
     * 
     * @param e
     * @return true se e' permesso inserirlo, false se non e' permesso
     */
    private boolean pushEdgeX(Edge e) {
    	if (!e.isProper()) return false; //<- per evitare che vengano usati edge improper es (1,1)
    	
    	//l'ultimo Xi aggiunto non deve mai andare verso l'origine t1, altrimenti chiudendo, si creano 2 cicli
    	
    	
    	//non deve appartenere agli edges in gioco:
    	//TODO: usare struttura migliore per controllare appartenenza senza cicli
    	if (edgesX.contains(e)) return false;
    	if (edgesY.contains(e)) return false;
    	/*Iterator i = edgesX.iterator();
    	while(i.hasNext()) {
    		if(i.next().equals(e)) return false;
    	}
    	i = edgesY.iterator();
    	while(i.hasNext()) {
    		if(i.next().equals(e)) return false;
    	}*/
    	
    	edgesX.add(e);
    	return true;
    }
    
    private boolean pushEdgeY(Edge e,MySolutionEdges sol) {
    	if (!e.isProper()) return false; //<- per evitare che vengano usati edge improper es (1,1)
    	
    	//non deve appartenere al tour attuale
    	if(sol.contains(e)) return false;
    	
    	//non deve appartenere agli edges in gioco:
    	//TODO: usare struttura migliore per controllare appartenenza senza cicli
    	if (edgesX.contains(e)) return false;
    	if (edgesY.contains(e)) return false;
    	/*Iterator i = edgesX.iterator();
    	while(i.hasNext()) {
    		if(i.next().equals(e)) return false;
    	}
    	i = edgesY.iterator();
    	while(i.hasNext()) {
    		if(i.next().equals(e)) return false;
    	}*/
    	
    	edgesY.add(e);
    	return true;
    }
    
    private boolean generatesFeasibleTour(MySolutionEdges sol) {
    	//testa edgesX e edgesY
    	
    	//provo a percorrere tutto il 'nuovo tour'
    	//deve essere lungo nCustomers, altrimenti vuol dire che ho generato un sotto-tour
    	int[] tour = sol.tour;
    	int n=0;
    	int c = 0;
    	boolean avanti=true;
    	
    	do {
	    	//il prossimo e' 
	    	Edge nextE = avanti? sol.getEdgeAfter(c) : sol.getEdgeBefore(c);
	    	if (edgesX.contains(nextE)) {
	    		//l'edge e' stato rimosso
	    		//devo cercare fra quelli inseriti quello che tocca c
	    		Iterator<Edge> i = edgesY.iterator();
	    		while(i.hasNext()) {
	    			Edge e = i.next();
	    			if (e.c1==c||e.c2==c) {
	    				if (e.c1==c) c=e.c2;
	    				else c=e.c1;
	    				n++;
	    				//devo capire la nuova direzione di percorrenza, a seconda di quale edge attaccato a c e' stato rimosso
	    				avanti=true;
	    				Edge testE = sol.getEdgeAfter(c);
	    				Iterator<Edge> j = edgesX.iterator();
	    				while(j.hasNext()) {
	    					e = j.next();
	    					if(testE.equals(e)) {
	    						//l'edge di percorrenza in avanti e' stato eliminato, dobiamo percorrere all'indietro
	    						avanti=false;
	    					}
	    				}
	    			}
	    		}
	    	} else {
	    		c = nextE.c2; 
				n++;
	    	}
    	} while (c!=0);
    	
    	if (n==GlobalData.numCustomers) return true;
    	else return false;
    }
    
    private void popEdgeX() {
    	edgesX.remove(edgesX.size()-1);
    }
    
    private void popEdgeY() {
    	edgesY.remove(edgesY.size()-1);
    }
    
    private double calculateGain(ArrayList<Edge> edgesX,ArrayList<Edge> edgesY) {
    	double g=0;
    	Iterator<Edge> i = edgesX.iterator();
    	while(i.hasNext()) {
    		Edge e = i.next();
    		g-=objFunc.matrix[e.c1][e.c2];
    	}
    	i = edgesY.iterator();
    	while(i.hasNext()) {
    		Edge e = i.next();
    		g+=objFunc.matrix[e.c1][e.c2];
    	}
    	return g;
    }
    
}   // end class MyMoveManager
