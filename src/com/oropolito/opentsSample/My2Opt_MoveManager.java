package com.oropolito.opentsSample;
import java.util.ArrayList;

import org.coinor.opents.*;


public class My2Opt_MoveManager implements MoveManager
{
    
    public Move[] getAllMoves( Solution solution )
    {   
        int[] tour = ((MySolution)solution).tour;
        
        ArrayList<My2Opt_Move> l = new ArrayList<My2Opt_Move>();
        
        for (int t1=0; t1<tour.length; t1++) { //scelta di t1
        	for (int t3=t1+2; t3<t1+tour.length-1; t3++) { //scelta di t3
        		int t3M = t3%tour.length;
        		l.add(new My2Opt_Move(t1,t3M,solution));
        	}
        }
        My2Opt_Move[] moves = l.toArray(new My2Opt_Move[l.size()]);
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
