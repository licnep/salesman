package com.oropolito.opentsSample;
import org.coinor.opents.*;

public class VertexInsertion_Move implements ComplexMove 
{
	public int c1,new_pos,customerId;
    
    
    public VertexInsertion_Move( int c1, int new_pos,int customerId)
    {   
    	this.c1 = c1;
    	this.new_pos = new_pos;
    	this.customerId = customerId;
    }   // end constructor
    
    
    public void operateOn( Solution soln )
    {
    	System.out.println("c1:"+c1+ "newPos:"+new_pos);
        int[] tour = ((MySolution)soln).tour;
        //tolgo il customer dalla lista: 
        //TODO: (sarebbe molto piu' veloce usare liste che array)
        for (int i=c1;i<tour.length-1;i++) {
        	tour[i] = tour[i+1];
        }
        //reinserisco il customer dove richiesto
        int tmp = tour[new_pos]; 
        tour[new_pos] = customerId;
        for (int i=new_pos+1;i<tour.length;i++) {
        	int tmp2;
        	tmp2 = tour[i];
        	tour[i] = tmp;
        	tmp = tmp2;
        }
        
    }   // end operateOn


	@Override
	public int[] attributes() {
		/**
		 * Mossa e' tabu se prova a muovere vertice gia' mosso prima
		 */
		
		int attr[] = new int[1];
		attr[0] = customerId;
		return attr;
	}
    
}   // end class MySwapMove
