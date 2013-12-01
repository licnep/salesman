package com.oropolito.opentsSample;

import org.coinor.opents.*;

public class MyTSListener extends TabuSearchAdapter{

	public int MAX_TENURE = GlobalData.numCustomers;//*3;// /2;

    public void newBestSolutionFound( TabuSearchEvent evt )
    {   
    	TabuSearch theTS = (TabuSearch)evt.getSource();
    	Solution   best  = theTS.getBestSolution();
    	ComplexTabuList mytl;

    	mytl = (ComplexTabuList)theTS.getTabuList();
    	mytl.setTenure( Math.max( 7, (int)( 0.75 * mytl.getTenure() ) ) );
    	//mytl.setTenure( 7 );
        System.out.println("Decrease tenure to " + mytl.getTenure());

        final String msg = "New Best solution found at iteration " + theTS.getIterationsCompleted() + "\n Done. Best solution: " + best;
    	System.out.println(msg);
    }

    public void unimprovingMoveMade( TabuSearchEvent evt )
    {   // Increase tenure
    	TabuSearch theTS = (TabuSearch)evt.getSource();
    	ComplexTabuList mytl;
    	mytl = (ComplexTabuList)theTS.getTabuList();

    	//mytl.setTenure( Math.min( MAX_TENURE, mytl.getTenure() + 2 ));
    	mytl.setTenure( Math.min( MAX_TENURE, mytl.getTenure() + 2 ));
        System.out.println("Increase tenure to " + mytl.getTenure());
  }

    // We're not using these events
    public void newCurrentSolutionFound( TabuSearchEvent evt ){}
    public void tabuSearchStarted( TabuSearchEvent evt ){}
    public void tabuSearchStopped( TabuSearchEvent evt ){}
    public void noChangeInValueMoveMade( TabuSearchEvent evt ){
    	TabuSearch theTS = (TabuSearch)evt.getSource();
    	ComplexTabuList mytl;
    	mytl = (ComplexTabuList)theTS.getTabuList();

    	mytl.setTenure( Math.min( MAX_TENURE, mytl.getTenure() + 2));
        System.out.println("Increase tenure to " + mytl.getTenure());
    }
    public void improvingMoveMade( TabuSearchEvent evt ){
    	TabuSearch theTS = (TabuSearch)evt.getSource();
    	ComplexTabuList mytl;
    	mytl = (ComplexTabuList)theTS.getTabuList();

    	mytl.setTenure( Math.min( MAX_TENURE, mytl.getTenure() - 3 ));
    	System.out.println("Decrease tenure to " + mytl.getTenure());
    }
}
