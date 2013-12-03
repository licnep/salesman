package com.oropolito.opentsSample;

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
public class Composite_TabuList implements TabuList
{
	private My2Opt_TabuList my2Opt_TabuList;
	private VertexInsertion_TabuList vertexInsertion_TabuList;
    
	private int     currentPos;
	private int     tenure;
    
    /**
     * Constructs a <code>ComplexTabuList</code> with a given tenure
     * and number of attributes
     *
     * @param tenure the tabu list's tenure
     * @param numAttr the number of attributes in each move to store
     * @since 1.0-exp3
     */
    public Composite_TabuList( int tenure )
    {
    	this.my2Opt_TabuList = new My2Opt_TabuList(tenure, 4);
    	this.vertexInsertion_TabuList = new VertexInsertion_TabuList(tenure);
    	this.currentPos = 0;
    	this.tenure     = tenure;
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
    	if( (move instanceof My2Opt_Move) )
        	return this.my2Opt_TabuList.isTabu(fromSolution, move);
    	if( (move instanceof VertexInsertion_Move) )
        	return this.vertexInsertion_TabuList.isTabu(fromSolution, move);
    	
    	return false;
    }   // end isTabu 
    
    
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
    	if( (move instanceof My2Opt_Move) )
    		this.my2Opt_TabuList.setTabu(fromSolution, move);
    	if( (move instanceof VertexInsertion_Move) )
        	this.vertexInsertion_TabuList.setTabu(fromSolution, move);
        currentPos++;
    }   // end setTabu


    /**
     * Returns the number of attributes in each move
     * being tracked by this tabu list.
     *
     * @return number of attributes
     * @since 1.0-exp9
     */
    public int getNumberOfAttributes()
    {   return my2Opt_TabuList.getNumberOfAttributes();
    }   // end getNumberOfAttributes
    


    
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

        my2Opt_TabuList.setTenure(tenure);
        vertexInsertion_TabuList.setTenure(tenure);
        
        this.tenure = tenure;
    }   // end setTenure
    
    
}   // end class SimpleTabuList
