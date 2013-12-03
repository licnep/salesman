package com.oropolito.opentsSample;

import org.coinor.opents.*;

public class VertexInsertion_TabuList extends ComplexTabuList
{
    public VertexInsertion_TabuList(int tabuTenure)
    {
    	super(tabuTenure, 1 /*n attributes*/);
    }
    
    @Override
    public boolean isTabu(Solution fromSolution, Move move) {    
    	VertexInsertion_Move mossa = (VertexInsertion_Move)move;
    	if (super.isTabu(fromSolution, mossa)) return true;
    	return false;
    }
}
