package com.oropolito.opentsSample;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import org.coinor.opents.*;

/**
 * <p>
 *  This implementation of a tabu list uses the {@link ComplexMove}'s <code>attributes()</code>
 *  method to determine the move's identity.
 *  <strong>It is imperative that you add the <code>attributes()</code>
 * and implement {@link ComplexMove} rather than just {@link Move}</strong>.
 * </p>
 * <p>
 *  A double <tt>int</tt> array (<tt>int[][]</tt>) is used to store
 *  the attributes values, and a double
 *  <code>for</code> loop checks for a move's presence
 *  when {@link #isTabu isTabu(...)}
 *  is called.
 * </p>
 * <p>
 *  You can resize the tabu list dynamically by calling
 *  {@link #setTenure setTenure(...)}. The data structure
 *  being used to record the tabu list grows if the requested
 *  tenure is larger than the array being used, but stays the 
 *  same size if the tenure is reduced. This is for performance
 *  reasons and insures that you can change the size of the
 *  tenure often without a performance degredation.
 * </p>
 *
 * <p><em>This code is licensed for public use under the Common Public License version 0.5.</em><br/>
 * The Common Public License, developed by IBM and modeled after their industry-friendly IBM Public License,
 * differs from other common open source licenses in several important ways:
 * <ul>
 *  <li>You may include this software with other software that uses a different (even non-open source) license.</li>
 *  <li>You may use this software to make for-profit software.</li>
 *  <li>Your patent rights, should you generate patents, are protected.</li>
 * </ul>
 * </p>
 * <p><em>Copyright © 2001 Robert Harder</em></p>
 *
 *
 * @author Robert Harder
 * @author rharder@usa.net
 * @see Move
 * @see Solution
 * @version 1.0-exp9
 * @since 1.0-exp9
 */
public class LK_TabuList implements TabuList
{
    /**
     * The value 10 will be used as the tenure if
     * the null constructor is used.
     *
     * @since 1.0-exp3
     */
    public final static int DEFAULT_TENURE   = 10;


    /**
    * The value 2 will be used as the number of attributes if
     * the null constructor is used.
     *
     * @since 1.0-exp9
     */
    public final static int DEFAULT_NUM_ATTR = 2;

    
    private int     tenure;       // Tabu list tenure
    private int[][] tabuList;     // Data structure used to store list
    
    private ArrayList<Edge> TabuListRimossi;
    
    
    private int     currentPos;   // Monotomically increasing counter
    private int     listLength;   // Always equals tabuList.length
    private int      numAttr;     // Number of attributes to track
    
    
    /**
     * When the int[] used for the tabu list needs to be
     * expanded, it will be set to the requested tenure
     * times this factor.
     *
     * @since 1.0-exp3
     */
    private final static double LIST_GROW_FACTOR = 2.0;
    
    
    /**
     * Constructs a <code>ComplexTabuList</code> with the
     * {@link #DEFAULT_TENURE} value of ten (10) and the
     * {@link #DEFAULT_NUM_ATTR} value of two (2).
     *
     * @since 1.0-exp3
     */
    public LK_TabuList()
    {   this( DEFAULT_TENURE, DEFAULT_NUM_ATTR );        
    }   // end SimpleTabuList
    
    
    /**
     * Constructs a <code>ComplexTabuList</code> with a given tenure
     * and number of attributes
     *
     * @param tenure the tabu list's tenure
     * @param numAttr the number of attributes in each move to store
     * @since 1.0-exp3
     */
    public LK_TabuList( int tenure, int numAttr )
    {
        super();
        
        this.TabuListRimossi = new ArrayList<Edge>();
        
        this.tenure     = tenure;
        this.numAttr    = numAttr;
        this.listLength = (int) (tenure * LIST_GROW_FACTOR);
        this.tabuList   = new int[ listLength ][ numAttr ];
        this.currentPos = 0;
        for( int i = 0; i < listLength; i++ )
            for( int j = 0; j < numAttr; j++ )
                this.tabuList[i][j] = Integer.MIN_VALUE;
    }   // end SimpleTabuList
    
    
    
    /**
     * Determines if the {@link ComplexMove} is on the tabu list and ignores the
     * {@link Solution} that is passed to it. The move's identity is determined
     * by its <code>attributes()</code> method.
     *
     * @param move A move
     * @param solution The solution before the move operation - ignored.
     * @return whether or not the tabu list permits the move.
     * @throw IllegalArgumentException if move is not of type {@link ComplexMove}
     * @throw IllegalArgumentException if move's <tt>attributes()</tt> method
     *        returns the wrong number of elements in the array.
     * @see Move
     * @see ComplexMove
     * @see Solution
     * @since 1.0-exp3
     */
    public boolean isTabu(Solution fromSolution, Move move) 
    {
    	// Make sure it's a "LK_Move"
        if( ! (move instanceof LK_Move) )
            throw new IllegalArgumentException( "Move is not of type ComplexMove" );
        LK_Move mossa = (LK_Move)move;
        
        Iterator<Edge> i = mossa.edgesY.iterator();
        while (i.hasNext()) {
	        if(TabuListRimossi.contains(i.next())) {
	        	return true;
	        }
        }
        
        
        return false;
        /*
     // See if attributes are tabu
        for( int i = 1; i <= tenure; i++ )
            if ( currentPos - i < 0 )
                return false;
            else {
            	//Edge rimossi: t1-t2,  t3-t4
            	//Edge aggiunti: t1-t3, t2-t4
            	int c1 = attrs[0], c2 = attrs[1], c3 = attrs[2], c4 = attrs[3];
            	int old_c1 = tabuList[ (currentPos-i) % listLength ][0];
            	int old_c2 = tabuList[ (currentPos-i) % listLength ][1];
            	int old_c3 = tabuList[ (currentPos-i) % listLength ][2];
            	int old_c4 = tabuList[ (currentPos-i) % listLength ][3];
            	if (isSameEdge(c1, c3, old_c1, old_c2)) return true;
            	if (isSameEdge(c1, c3, old_c3, old_c4)) return true;
            	if (isSameEdge(c2, c4, old_c1, old_c2)) return true;
            	if (isSameEdge(c2, c4, old_c3, old_c4)) return true;
            }
        
    	return false;*/
    }   // end isTabu 
    
    private boolean isSameEdge(int a1, int a2, int b1, int b2) {
    	if (a1==b1 && a2==b2) return true;
    	if (a1==b2 && a2==b1) return true;
    	return false;
    }
    
    /**
     * This method accepts a {@link ComplexMove} and {@link Solution} as
     * arguments and updates the tabu list as necessary.
     * <P>
     * Although the tabu list may not use both of the passed
     * arguments, both must be included in the definition.
     *
     * Records a {@link ComplexMove} on the tabu list by calling the move's
     * <code>attributes()</code> method.
     *
     * @param move The {@link ComplexMove} to register
     * @param solution The {@link Solution} before the move operation - ignored.
     * @throw IllegalArgumentException if move is not of type {@link ComplexMove}
     * @throw IllegalArgumentException if move's <tt>attributes()</tt> method
     *        returns the wrong number of elements in the array.
     * @see Move
     * @see ComplexMove
     * @see Solution
     * @since 1.0-exp3
     */
    public void setTabu(Solution fromSolution, Move move)
    {
        // Make sure it's a "LK_Move"
        if( ! (move instanceof LK_Move) )
            throw new IllegalArgumentException( "Move is not of type ComplexMove" );
        LK_Move cMove = (LK_Move)move;
        
        Iterator<Edge> i = cMove.edgesX.iterator();
        TabuListRimossi.addAll(cMove.edgesX);
        
        while(TabuListRimossi.size()>tenure) {
        	TabuListRimossi.remove(0); //rimuovo i piu' vecchi fino a dim tenure
        }
        
        // Record tabu
        //for( int j = 0; j < this.numAttr; j++ )
        //    tabuList[ (currentPos) % listLength ][j] = attrs[j];
        currentPos++;
    }   // end setTabu


    /**
     * Returns the number of attributes in each move
     * being tracked by this tabu list.
     *
     * @return number of attributes
     * @since 1.0-exp9
     
    public int getNumberOfAttributes()
    {   return numAttr;
    }   // end getNumberOfAttributes
    
*/

    
    /**
     * Returns the tenure being used by this tabu list.
     *
     * @return tabu list's tenure
     * @since 1.0-exp3
     */
    public int getTenure()
    {   return tenure;
    }   // end getTenure
    
    
    /**
     * Sets the tenure used by the tabu list. The data structure
     * being used to record the tabu list grows if the requested
     * tenure is larger than the array being used, but stays the 
     * same size if the tenure is reduced. This is for performance
     * reasons and insures that you can change the size of the
     * tenure often without a performance degredation.
     * A negative value will be ignored.
     *
     * @param tenure the tabu list's new tenure
     * @since 1.0-exp3
     */
    public void setTenure( int tenure )
    {
        if( tenure < 0 )
            return;
        
        if( tenure > this.tenure && tenure > tabuList.length )
        {
            listLength        = (int) (tenure * LIST_GROW_FACTOR);
            int[][] newTabuList = new int[ listLength ][ this.numAttr ];
            for( int i = 0; i < tabuList.length; i++ )
                newTabuList[i] = tabuList[i];
            
            tabuList   = newTabuList;
        }   // end if: grow tabu list
        
        this.tenure = tenure;
    }   // end setTenure
    
    
}   // end class SimpleTabuList
