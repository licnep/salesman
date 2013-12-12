package com.oropolito.opentsSample;
import java.util.ArrayList;

import org.coinor.opents.*;


public class Composite_MoveManager implements MoveManager
{
	
    public Move[] getAllMoves( Solution solution )
    {   
    	ArrayList<Move> l = new ArrayList<Move>();
    	
    	addVertexInsertionMoves(l,(MySolution)solution);
    	add2OptMoves(l,(MySolution)solution);
    	
    	
        Move[] moves = l.toArray(new Move[l.size()]);
        return moves;
        
    }   // end getAllMoves

	private void addVertexInsertionMoves(ArrayList<Move> l, MySolution solution) {
		//Mossa: prendere un customer c1 e metterlo in posizione c2 shiftando tutti a destra se necessario
    	//TODO: sarebbe buono avere liste degli N nearest neighbor per ogni citta' e controllare solo quelli
    	
        int[] tour = solution.tour;
        
        for (int c1=0; c1<tour.length; c1++) { //scelta di c1
        	for (int new_pos=0; new_pos<tour.length; new_pos++) { //scelta di new_pos
        		if (new_pos != c1 && c1%(tour.length-1)!=new_pos%(tour.length-1) ) 
        			l.add(new VertexInsertion_Move(c1,new_pos,tour[c1]));
        	}
        }
	}

	private void add2OptMoves(ArrayList<Move> l, MySolution solution) {
		int[] tour = solution.tour;
        
        for (int t1=0; t1<tour.length; t1++) { //scelta di t1
        	for (int t3=t1+2; t3<t1+tour.length-1; t3++) { //scelta di t3
        		int t3M = t3%tour.length;
        		l.add(new My2Opt_Move(t1,t3M,solution));
        	}
        }
	}
    
}   // end class MyMoveManager
