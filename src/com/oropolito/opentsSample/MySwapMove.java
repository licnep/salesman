package com.oropolito.opentsSample;
import org.coinor.opents.*;

public class MySwapMove implements ComplexMove 
{
	public int customer;
	public int customer2;
    public int movement;
    
    
    public MySwapMove( int customer, int movement, int customer2 )
    {   
    	this.customer = customer;
    	this.customer2 = customer2;
        this.movement = movement;
    }   // end constructor
    
    
    public void operateOn( Solution soln )
    {
        int[] tour = ((MySolution)soln).tour;
        int   pos1 = -1;
        int   pos2 = -1;
        
        // Find positions
        for( int i = 0; i < tour.length && pos1 < 0; i++ )
            if( tour[i] == customer ) 
                pos1 = i;
        pos2 = pos1 + movement;
        
        // Swap
        int cust2 = tour[pos2];
        tour[pos1] = cust2;
        tour[pos2] = customer;
        
    }   // end operateOn
    
    
    /** Identify a move for SimpleTabuList */
    public int hashCode()
    {   
    	//testButta: (e' sbagliato perche' vieta anche altre mosse)
        return customer;
    }   // end hashCode


	@Override
	public int[] attributes() {
		int attr[] = new int[2];
		attr[0] = customer;
		attr[1] = customer2;
		return attr;
		//return null;
	}
    
}   // end class MySwapMove
