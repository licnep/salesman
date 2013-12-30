package com.oropolito.opentsSample;

import org.coinor.opents.*;

public class MyTabuList extends ComplexTabuList
{
    public int MAX_TENURE = G.numCustomers/2;

    public MyTabuList(int tabuTenure)
    {
    	super(tabuTenure, 2 /*n attributes*/);
    }
    
    @Override
    public boolean isTabu(Solution fromSolution, Move move) {    
    	MySwapMove mossa = (MySwapMove)move;
    	MySwapMove duale = new MySwapMove(mossa.customer2, -mossa.movement, mossa.customer2);
    	boolean isTB = false;
    	if (super.isTabu(fromSolution, mossa)) isTB=true;
    	if (super.isTabu(fromSolution, duale)) isTB=true;
    	return isTB;
    }
}
