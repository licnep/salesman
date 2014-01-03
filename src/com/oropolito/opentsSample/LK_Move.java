package com.oropolito.opentsSample;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;

import org.coinor.opents.*;

public class LK_Move implements ComplexMove 
{
	public ArrayList<Edge> edgesX;
	public ArrayList<Edge> edgesY;
	public int customer1,customer2,customer3,customer4;
    
    
    public LK_Move( ArrayList<Edge> edgesX, ArrayList<Edge> edgesY )
    {   
    	this.edgesX = edgesX;
    	this.edgesY = edgesY;
    }   // end constructor
    
    
    public void operateOn( Solution soln )
    {
    	MySolutionEdges sol = (MySolutionEdges)soln;
    	
    	if(G.GUI) {
	    	G.gui_model.resetColoredEdges();
	    	G.gui_model.addColoredEdge(edgesX, Color.RED);
	    	G.gui_model.addColoredEdge(edgesY, Color.BLUE);
    	}
    	
    	//try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace();}
    	
    	/*
    	if(edgesX.size()==3) {
    		//try { Thread.sleep(100); } catch (InterruptedException e) { e.printStackTrace();}
    		if (edgesX.contains(edgesY.get(0))||edgesX.contains(edgesY.get(1))||edgesX.contains(edgesY.get(2))) {
        		System.out.println("IMPROPER!!:");
        		System.out.println(this);
        	}
    	}*/
        
    	//rimuovo tutti gli edge X
    	Iterator<Edge> i = edgesX.iterator();
    	while(i.hasNext()) {
    		Edge e = i.next();
    		G.activeNeighbourhoods[e.c1]=true;
    		G.activeNeighbourhoods[e.c2]=true;
    		sol.removeEdge(e);
    	}
    	
    	//aggiungo tutti gli edge Y
    	i = edgesY.iterator();
    	while(i.hasNext()) {
    		Edge e = i.next();
    		G.activeNeighbourhoods[e.c1]=true;
    		G.activeNeighbourhoods[e.c2]=true;
    		sol.addEdge(e);
    	}
    	
    	sol.sincTourWithEdges();
    	
    }   // end operateOn


	@Override
	public int[] attributes() {
		/**
		 * Mossa e' tabu se prova ad aggiungere un edge che era stato rimosso,
		 * o a rimuovere un edge che era stato aggiunto
		 */
		//MAI USATA IN TEORIA
		int attr[] = new int[4];
		attr[0] = edgesX.get(0).hashCode();
		attr[1] = edgesX.get(1).hashCode();
		
		return attr;
		//return null;
	}
	
	@Override
	public String toString() {
		   StringBuffer s = new StringBuffer();
	        
	        //s.append( "Solution value: " + getObjectiveValue()[0] );
	        s.append( "X: [ " );
	        Iterator<Edge> i = edgesX.iterator();
	        while(i.hasNext()) {
	        	Edge e = i.next();
	        	s.append(e.c1+"-"+e.c2+" ");
	        }
	        s.append( "] Y: [ " );
	        i = edgesY.iterator();
	        while(i.hasNext()) {
	        	Edge e = i.next();
	        	s.append(e.c1+"-"+e.c2+" ");
	        }
	        s.append("]");
	        return s.toString();
	}
    
}   // end class MySwapMove
