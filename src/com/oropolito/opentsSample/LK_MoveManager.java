package com.oropolito.opentsSample;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;

import org.coinor.opents.*;

import com.oropolito.opentsSample.GUI.GUI_model;


public class LK_MoveManager implements MoveManager
{
	public LK_ObjectiveFunction objFunc;
	public LK_MoveManager(LK_ObjectiveFunction myObj) {
		this.objFunc = myObj;
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
        double G_star = 0; //miglioramento migliore per adesso
        
        sol.sincEdgesWithTour(); //synchronize the edges with the tour
        ArrayList<Edge> edgesX = new ArrayList<Edge>();
        ArrayList<Edge> edgesY = new ArrayList<Edge>();
        
        Iterator<Edge> ie = sol.edges.iterator();
        while(ie.hasNext()) {
        	edgesX.add(ie.next()); //setto x1
        	Edge[] vicini = objFunc.edgeVicini[edgesX.get(0).c2];
        	for (int i=0;i<GlobalData.nVicini;i++) {
        		//testo tutti i vicini Y che non devono essere gia' nel tour
        		if(!sol.contains(vicini[i])) {
        			edgesY.add(vicini[i]);        			
        			double g = calculateGain(edgesX, edgesY);
        			
                	//X2 e' obbligato una volta scelto Y1
        			edgesX.add( sol.getEdgeBefore(edgesY.get(0).c2) );
        			edgesY.add( new Edge(edgesX.get(1).c2 , edgesX.get(0).c1) ); //Y2 che ricollega a t1
                	//l.add(new My2Opt_Move(edgesX.get(0).c1, edgesX.get(1).c2, sol));
        			l.add(new LK_Move((ArrayList<Edge>)edgesX.clone(), (ArrayList<Edge>)edgesY.clone(), sol));
                	
        			edgesY.remove(1);
        			edgesY.remove(0);
	            	edgesX.remove(1);
        		}
        	}
        	
        	edgesX.clear(); //testo un altro x1
        }
        
        /*
        for(int i=0;i<len;i++) { //per tutti i possibili x1
        	G_star=0;
        	edges.add(tour[i]); //t1=i
        	edges.add(tour[(i+1)%len]); //t2
        	int[] vicini = objFunc.vicini[edges.get(1)]; //vicini a t2
        	for(int j=0;j<GlobalData.nVicini;j++) { //scelgo t3 tra i piu' vicini a t2
        		//TODO: condizione, gli y non devono essere nel tour attuale
        		
        		//edge(t2,vicini[j]) non deve essere in current tour
        		boolean giaNelTour = false;
        		for(int p=0;p<len;p++) {
        			if(vicini[j]==tour[p]) {
        				if(tour[(p+1)%len]==edges.get(1)||tour[(p-1+len)%len]==edges.get(1)) {
        					giaNelTour=true;
        				}
        			}
        		}
        			
        		if (!giaNelTour) {
	        		edges.add(vicini[j]);
	        		//non conosco la posizione nel tour dato un customer, e mi serve per sapere il t4 dell'edge da rimuovere
	        		//quindi scorro tutto il tour ogni volta... (BAD)
	        		//TODO: serve un modo che dato un customer mi dia rapidamente il prossimo nel tour
	        		for (int p=0;p<tour.length;p++) if (tour[p]==vicini[j]) 
	        			edges.add(tour[(p-1+len)%len] );
	        		
	        		//temp: li mette tutti non solo il migliore (= a 2 opt)
	        		if (edges.get(2)!=edges.get(0)&&edges.get(2)!=edges.get(1) &&
        				edges.get(3)!=edges.get(0)&&edges.get(3)!=edges.get(1))
	        		l.add(new My2Opt_Move(edges.get(0), edges.get(3), sol));
	        		/*
	        		GlobalData.gui_model.resetColoredEdges();
	            	GlobalData.gui_model.addColoredEdge(edges.get(0),edges.get(1), Color.RED);
	            	GlobalData.gui_model.addColoredEdge(edges.get(1),edges.get(2), Color.BLUE);
	            	GlobalData.gui_model.addColoredEdge(edges.get(2),edges.get(3), Color.RED);
	            	GlobalData.gui_model.addColoredEdge(edges.get(3),edges.get(0), Color.BLUE);
	            	//try { Thread.sleep(100); } catch (InterruptedException e) { e.printStackTrace();}
	            	
	        		//testo per vedere quant'e' il guadagno
	        		double tmpG = calculateGain(edges);
	        		if (tmpG>G_star) {
	        			G_star = tmpG;
	        			bestEdges.clear();
	        			for (int e: edges) bestEdges.add(e);
	        		}
	        		edges.remove(3);
	        		edges.remove(2);
        		}
        	}
        	edges.remove(1);
        	edges.remove(0);
        	if (bestEdges.size()>2) { //ha volte puo' non averne trovato nessuno migliore
        		//l.add(new My2Opt_Move(bestEdges.get(0), bestEdges.get(3), solution));
        	}
        }*/
        
        LK_Move[] moves = l.toArray(new LK_Move[l.size()]);
        return moves;
    }   // end getAllMoves
    
    private double calculateGain(ArrayList<Edge> edgesX,ArrayList<Edge> edgesY) {
    	double g=0;
    	//aggiungo gli X, tolgo gli Y 
    	Iterator<Edge> i = edgesX.iterator();
    	while(i.hasNext()) {
    		Edge e = i.next();
    		g+=objFunc.matrix[e.c1][e.c2];
    	}
    	i = edgesY.iterator();
    	while(i.hasNext()) {
    		Edge e = i.next();
    		g-=objFunc.matrix[e.c1][e.c2];
    	}
    	return g;
    }
    
}   // end class MyMoveManager
