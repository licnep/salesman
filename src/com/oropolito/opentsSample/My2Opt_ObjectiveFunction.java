package com.oropolito.opentsSample;
import java.util.ArrayList;
import java.util.Arrays;

import org.coinor.opents.*;


public class My2Opt_ObjectiveFunction implements ObjectiveFunction
{
    public double[][] matrix;
    public int[][] vicini; //per ogni customer lista degli N piu' vicini
    public Edge[][] edgeVicini;
    
    
    public My2Opt_ObjectiveFunction( double[][] customers ) 
    {   matrix = createMatrix( customers );
    	createVicini();
    }   // end constructor

    public void createVicini() {
    	vicini = new int[matrix.length][GlobalData.nVicini];
    	edgeVicini = new Edge[matrix.length][GlobalData.nVicini];
    	for (int i=0;i<matrix.length;i++) {
    		double[] ordinati = matrix[i].clone();
    		Arrays.sort(ordinati); //ordinati contiene adesso le DISTANZE ordinate
    		for(int j=1;j<=GlobalData.nVicini;j++) { //il primo va saltato perche' e' sempre se stesso
    			for(int k=0;k<matrix.length;k++) {
    				if(matrix[i][k]==ordinati[j]) {
    					//solo se k non e' gia' stato inserito (puo' capitare se piu' distanze a parimerito
    					boolean giaInserito=false;
    					for (int m=0;m<j-1;m++) { //controllo che non sia gia' nell'array vicini
    						if (vicini[i][m]==k) giaInserito=true;
    					}
    					if ( !giaInserito ) {
    						vicini[i][j-1] = k;
    						edgeVicini[i][j-1] = new Edge(i,k);
    						break;
    					}
    				}
    			}
    		}
    		//qui ho l'elenco dei piu' vicini a i
    	}
    }
    
    public double[] evaluate( Solution solution, Move proposed_move )
    {
        int[] tour = ((MySolutionEdges)solution).tour;
        int len = tour.length;
        
        // If move is null, calculate distance from scratch
        if( proposed_move == null)
        {
            double dist = 0;
            for( int i = 0; i < len; i++ )
            	dist += matrix[ tour[i] ][ i+1 >= len ?  tour[0] : tour[i+1] ];
                
            return new double[]{ dist };
        }   // end if: move == null
        
        // Else calculate incrementally
        else
        {
            My2Opt_Move mv = (My2Opt_Move)proposed_move;
            
            // Prior objective value
            double dist = solution.getObjectiveValue()[0];
            //tolgo la lunghezza dei 2 segmenti rimossi
            dist -= matrix[ tour[mv.t1] ][ tour[mv.t2] ];
            dist -= matrix[ tour[mv.t3] ][ tour[mv.t4] ];
            //aggiungo quella dei 2 segmenti aggiunti:
            dist += matrix[ tour[mv.t1] ][ tour[mv.t3] ];
            dist += matrix[ tour[mv.t2] ][ tour[mv.t4] ];
            return new double[]{ dist };
        }
    }   // end evaluate
    
    
    /** Create symmetric matrix. */
    private double[][] createMatrix( double[][] customers )
    {
        int len = customers.length;
        double[][] matrix = new double[len][len];
        
        for( int i = 0; i < len; i++ )
            for( int j = i+1; j < len; j++ )
                matrix[i][j] = matrix[j][i] = norm(
                    customers[i][0], customers[i][1],
                    customers[j][0], customers[j][1] );
        return matrix;
    }   // end createMatrix
    
    
    /** Calculate distance between two points. */
    private double norm( double x1, double y1, double x2, double y2 )
    {   
        double xDiff = x2 - x1;
        double yDiff = y2 - y1;
        return Math.round(Math.sqrt( xDiff*xDiff + yDiff*yDiff ));
    }   // end norm
    
}   // end class MyObjectiveFunction
