package com.oropolito.opentsSample;
import org.coinor.opents.*;

public class VertexInsertion_Move implements ComplexMove 
{
	public int c1,new_pos,customerId,t1,t2,t3,t4;
	
	/**
	 *  ---t1---c1---t2-----------------t3----new_pos---t4--------
	 */
    
    
    public VertexInsertion_Move( int c1, int new_pos,int[] tour)
    {   
    	this.c1 = c1;
    	this.t1 = tour[(c1-1+tour.length)%tour.length];
    	this.t2 = tour[(c1+1+tour.length)%tour.length];
    	this.t3 = tour[(new_pos-1+tour.length)%tour.length];
    	this.t4 = tour[new_pos];
    	this.new_pos = new_pos;
    	this.customerId = tour[c1];
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
		//memorizzo uno dei segmenti rimossi, questo non potra' essere reinserito dopo
		int attr[] = new int[5];
		attr[0] = customerId;
		attr[1] = t1;
		attr[2] = t2;
		attr[3] = t3;
		attr[4] = t4;
		return attr;
	}
    
}   // end class MySwapMove
