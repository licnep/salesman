package com.oropolito.opentsSample;
import java.util.ArrayList;

import org.coinor.opents.*;


public class VertexInsertion_MoveManager implements MoveManager
{
    
    public Move[] getAllMoves( Solution solution )
    {   
    	//Mossa: prendere un customer c1 e metterlo in posizione c2 shiftando tutti a destra se necessario
    	//TODO: sarebbe buono avere liste degli N nearest neighbor per ogni citta' e controllare solo quelli
    	
    	
        int[] tour = ((MySolution)solution).tour;
        
        ArrayList<VertexInsertion_Move> l = new ArrayList<VertexInsertion_Move>();
        
        for (int c1=0; c1<tour.length; c1++) { //scelta di c1
        	for (int new_pos=0; new_pos<tour.length; new_pos++) { //scelta di new_pos
        		if (new_pos != c1 && c1%(tour.length-1)!=new_pos%(tour.length-1) ) 
        			l.add(new VertexInsertion_Move(c1,new_pos,tour[c1]));
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
