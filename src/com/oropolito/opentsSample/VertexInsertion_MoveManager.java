package com.oropolito.opentsSample;
import java.util.ArrayList;
import java.util.Arrays;

import org.coinor.opents.*;


public class VertexInsertion_MoveManager implements MoveManager
{
    public int[][] matrice;
    private int N = 10; //testiamo solo i 10 customer piu' vicini
    
    
    public VertexInsertion_MoveManager(double[][] matrix) {
    	System.out.println("CREAZIONE move manager");
    	/*int len = matrix.length;
    	//clono la matrice delle distanze:
    	double [][] matrix_clone = new double[matrix.length][];
    	for(int i = 0; i < matrix.length; i++)
    		matrix_clone [i] = matrix[i].clone();
    	
        int[][] matrice = new int[len][10];
        
        for( int i = 0; i < len; i++ ) {
        	int customer = (i+1)%len;
        	for (int k=0;k<10;k++) {
        		double min=Double.MAX_VALUE;
	        	for (int j=0;j<len;j++) {
	        		if (j!=i&&matrix_clone[i][j]<min) {
	        			customer = j;
	        			min = matrix_clone[i][j]; 
	        		}
	        	}
	        	//ho trovato il customer piu' vicino (customer), d=min
	        	matrice[i][k] = customer;
	        	matrix_clone[i][customer] = Double.MAX_VALUE; 
        	}
        	for (i=0;i<10;i++) {
        		int c = matrice[0][i];
        		//System.out.println(matrix[0][c]);
        	}
        }*/
	}
    
    /** Calculate distance between two points. */
    private double norm( double x1, double y1, double x2, double y2 )
    {   
        double xDiff = x2 - x1;
        double yDiff = y2 - y1;
        return Math.round(Math.sqrt( xDiff*xDiff + yDiff*yDiff ));
    }   // end norm
	
    
    public Move[] getAllMoves( Solution solution )
    {   
    	//Mossa: prendere un customer c1 e metterlo in posizione c2 shiftando tutti a destra se necessario
    	//TODO: sarebbe buono avere liste degli N nearest neighbor per ogni citta' e controllare solo quelli
    	
    	
        int[] tour = ((MySolution)solution).tour;
        
        ArrayList<VertexInsertion_Move> l = new ArrayList<VertexInsertion_Move>();
        
        for (int c1=0; c1<tour.length; c1++) { //scelta di c1
        	/*for (int j=0;j<10;j++) {
        		int c2 = matrice[c1][j];
        		int new_pos = 
        	}*/
        	int N = 10;
        	int len = tour.length;
        	//for (int new_pos=0; new_pos<tour.length; new_pos++) { //scelta di new_pos
        	for (int j=-N; j<N; j++) { //scelta di new_pos
        		int new_pos = (c1+j+len)%len;
        		if (new_pos != c1 && c1%(len-1)!=new_pos%(len-1) ) 
        			l.add(new VertexInsertion_Move(c1,new_pos,tour));
        	}
        }
        VertexInsertion_Move[] moves = l.toArray(new VertexInsertion_Move[l.size()]);
        return moves;
        /*
        Move[] buffer = new Move[ tour.length*tour.length ];
        int nextBufferPos = 0;
        int delta = (int)Math.round(GlobalData.numCustomers*0.75);      
        
        // Generate moves that move each customer
        // forward and back up to five spaces.
        for( int i = 1; i < tour.length; i++ )
//            for( int j = -5; j <= 5; j++ )
//          for( int j = -GlobalData.numCustomers/2; j <= GlobalData.numCustomers/2; j++ )
            for( int j = -delta; j <= delta; j++ )
                if( (i+j >= 1) && (i+j < tour.length) && (j != 0) )
                    buffer[nextBufferPos++] = new MySwapMove( tour[i], j, tour[i+j] );
         
        // Trim buffer
        Move[] moves = new Move[ nextBufferPos];
        System.arraycopy( buffer, 0, moves, 0, nextBufferPos );
        
        return moves;*/
    }   // end getAllMoves
    
}   // end class MyMoveManager
