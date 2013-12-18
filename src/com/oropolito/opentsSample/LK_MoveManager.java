package com.oropolito.opentsSample;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import org.coinor.opents.*;

import com.oropolito.opentsSample.GUI.GUI_model;


public class LK_MoveManager implements MoveManager
{
	public LK_ObjectiveFunction objFunc;
	ArrayList<Edge> edgesX;
	ArrayList<Edge> edgesY;
	
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
        edgesX = new ArrayList<Edge>();
        edgesY = new ArrayList<Edge>();
        
        Iterator<Edge> ie = sol.edges.iterator();
        while(ie.hasNext()) {
        	edgesX.add(ie.next()); //setto x1
        	Edge[] vicini = objFunc.edgeVicini[edgesX.get(0).c2];
        	for (int i=0;i<GlobalData.nVicini;i++) {
        		if(pushEdgeY(vicini[i], sol)) {
        			double g = calculateGain(edgesX, edgesY);
        			
                	//X2 e' obbligato una volta scelto Y1
        			if(pushEdgeX(sol.getEdgeBefore(edgesY.get(0).c2))) {
        				//Y2 che ricollega a t1
        				if(pushEdgeY(new Edge(edgesX.get(1).c2 , edgesX.get(0).c1), sol)) {
        					l.add(new LK_Move((ArrayList<Edge>)edgesX.clone(), (ArrayList<Edge>)edgesY.clone()));
        					popEdgeY();
        				}
                    	popEdgeX();
        			}
        			popEdgeY();
        		}
        		/*
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
        		*/
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
    	
    	//non deve appartenere agli edges in gioco:
    	//TODO: usare struttura migliore per controllare appartenenza senza cicli
    	Iterator i = edgesX.iterator();
    	while(i.hasNext()) {
    		if(i.next().equals(e)) return false;
    	}
    	i = edgesY.iterator();
    	while(i.hasNext()) {
    		if(i.next().equals(e)) return false;
    	}
    	
    	edgesX.add(e);
    	return true;
    }
    
    private boolean pushEdgeY(Edge e,MySolutionEdges sol) {
    	if (!e.isProper()) return false; //<- per evitare che vengano usati edge improper es (1,1)
    	
    	//non deve appartenere al tour attuale
    	if(sol.contains(e)) return false;
    	
    	//non deve appartenere agli edges in gioco:
    	//TODO: usare struttura migliore per controllare appartenenza senza cicli
    	Iterator i = edgesX.iterator();
    	while(i.hasNext()) {
    		if(i.next().equals(e)) return false;
    	}
    	i = edgesY.iterator();
    	while(i.hasNext()) {
    		if(i.next().equals(e)) return false;
    	}
    	
    	edgesY.add(e);
    	return true;
    }
    
    private void popEdgeX() {
    	edgesX.remove(edgesX.size()-1);
    }
    
    private void popEdgeY() {
    	edgesY.remove(edgesY.size()-1);
    }
    
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
