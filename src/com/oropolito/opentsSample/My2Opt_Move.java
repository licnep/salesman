package com.oropolito.opentsSample;
import org.coinor.opents.*;

public class My2Opt_Move implements ComplexMove 
{
	public int t1,t2,t3,t4;
	public int customer1,customer2,customer3,customer4;
    
    
    public My2Opt_Move( int t1, int t3, Solution soln )
    {   
    	this.t1 = t1;
    	this.t2 = (t1+1)%GlobalData.numCustomers;
    	this.t3 = t3;
    	this.t4 = (t3+1)%GlobalData.numCustomers;
    	int[] tour = ((MySolution)soln).tour;
        customer1 = tour[t1];
        customer2 = tour[t2];
        customer3 = tour[t3];
        customer4 = tour[t4];
    	
    }   // end constructor
    
    
    public void operateOn( Solution soln )
    {
        int[] tour = ((MySolution)soln).tour;
        
        try {
        	//System.out.println("t1="+t1 + "  t2="+t2 + " t3="+t3+" t4="+t4);
			//inverto la sezione da t2 a t3, il loop e fino a meta' della lunghezza, il resto e' gia' stato swappato
			
        	if (t3>t2) {
	        	for (int i=0;i<=Math.floor((t3-t2)/2);i++) {
					int tmp = tour[t2+i];
					tour[t2+i] = tour[t3-i];
					tour[t3-i] = tmp;
				}
        	} else {
        		//in questo caso vuol dire che i due segmenti sono a cavallo dell'inizio dell array
        		//(t1-t2 verso la fine dell'array, e t3-t4 verso l'inizio)
        		for (int i=0;i<=Math.floor((t3+(tour.length-t2))/2);i++) {
					int tmp = tour[(t2+i)%tour.length];
					tour[(t2+i)%tour.length] = tour[(t3-i+ tour.length)%tour.length ];
					tour[(t3-i+ tour.length)%tour.length ] = tmp;
				}
        	}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
        
        
    }   // end operateOn


	@Override
	public int[] attributes() {
		/**
		 * Mossa e' tabu se prova ad aggiungere un edge che era stato rimosso,
		 * o a rimuovere un edge che era stato aggiunto
		 */
		
		int attr[] = new int[4];
		attr[0] = customer1;
		attr[1] = customer2;
		attr[2] = customer3;
		attr[3] = customer4;
		return attr;
		//return null;
	}
    
}   // end class MySwapMove
