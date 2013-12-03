package com.oropolito.opentsSample;
import org.coinor.opents.*;


public class Composite_ObjectiveFunction implements ObjectiveFunction
{
	My2Opt_ObjectiveFunction my2Opt_obj;
    VertexInsertion_ObjectiveFunction VertexInsertion_obj;
    
    public Composite_ObjectiveFunction( double[][] customers ) 
    {   
    	my2Opt_obj = new My2Opt_ObjectiveFunction(customers);
    	VertexInsertion_obj = new VertexInsertion_ObjectiveFunction(customers);
    }   // end constructor

    
    public double[] evaluate( Solution solution, Move proposed_move )
    {
        if (proposed_move instanceof My2Opt_Move) {
        	return my2Opt_obj.evaluate(solution, proposed_move);
        } else {
        	return VertexInsertion_obj.evaluate(solution, proposed_move);
        }
    }   // end evaluate
    
}   // end class MyObjectiveFunction
