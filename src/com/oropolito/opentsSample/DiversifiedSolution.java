package com.oropolito.opentsSample;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import com.oropolito.opentsSample.GUI.GUI_model;

public class DiversifiedSolution extends MySolution {
	public DiversifiedSolution(double[][] customers,GUI_model gui_model)
    {
		Random rand_generator = new Random(GlobalData.random_seed);
		
        tour = new int[ customers.length ];
        ArrayList<Integer> avail = new ArrayList<>();
		for( int i = 0; i < customers.length; i++ )
            avail.add(i);
        
        //parto da un nodo a caso
        Double n_casuale =  Math.floor( rand_generator.nextInt(customers.length));
		this.tour[0] =  avail.remove( n_casuale.intValue() );
        
		
        for( int i = 1; i < tour.length; i++ )
        {
        	int limit = 0; //inizialmente cerchiamo di inserire solo archi mai usati
        	int closest = -1;
            double min_dist = Double.MAX_VALUE;
            while (min_dist == Double.MAX_VALUE) {
	            for( int j = 0; j < avail.size(); j++ ) {
	            	if (GlobalData.objFunc.frequency_matrix[i-1][j]<=limit) {
		                if( (norm( customers, tour[i-1], j ) < min_dist) && (avail.get(j) >= 0) )
		                {   
		                    min_dist = norm( customers, tour[i-1], j );
		                    closest = j;
		                }
	            	}
	            }
	            limit++;
            }
            tour[i] = avail.get(closest);
            avail.remove(closest);
        	
			try {
				gui_model.setTour_current(tour);
				//Thread.sleep(10);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        
        }   // end for

	}
	private double norm( double[][]matr, int a, int b )
	{
	    double xDiff = matr[b][0] - matr[a][0];
	    double yDiff = matr[b][1] - matr[a][1];
	    return Math.sqrt( xDiff*xDiff + yDiff*yDiff );
	}   // end norm
	
	class Elemento {
		public int numero;
		public double distanza;
		public Elemento(int n, double dist) {
			this.numero = n;
			this.distanza = dist;
		}
		public String toString() {
			return String.valueOf(this.distanza)/*+"-"+String.valueOf(this.distanza)*/;
		}
	}
}
