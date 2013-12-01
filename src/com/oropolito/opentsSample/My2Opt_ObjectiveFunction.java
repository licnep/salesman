package com.oropolito.opentsSample;
import org.coinor.opents.*;


public class My2Opt_ObjectiveFunction implements ObjectiveFunction
{
    public double[][] matrix;
    
    
    public My2Opt_ObjectiveFunction( double[][] customers ) 
    {   matrix = createMatrix( customers );
    }   // end constructor

    
    public double[] evaluate( Solution solution, Move proposed_move )
    {
        int[] tour = ((MySolution)solution).tour;
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
