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
    	
    	GlobalData.gui_model.resetColoredEdges();
    	GlobalData.gui_model.addColoredEdge(edgesX, Color.RED);
    	GlobalData.gui_model.addColoredEdge(edgesY, Color.BLUE);
    	
    	if(edgesX.size()==4) {
    		//try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace();}
    	}
        
    	//rimuovo tutti gli edge X
    	Iterator<Edge> i = edgesX.iterator();
    	while(i.hasNext()) sol.removeEdge(i.next());
    	
    	//aggiungo tutti gli edge Y
    	i = edgesY.iterator();
    	while(i.hasNext()) sol.addEdge(i.next());
    	
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
    
}   // end class MySwapMove
