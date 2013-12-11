package com.oropolito.opentsSample;

import org.coinor.opents.*;

public class MyTSListener extends TabuSearchAdapter{

	public int MAX_TENURE =  GlobalData.numCustomers*3;//*3;// /2;
	public int MIN_TENURE = Math.max(7,GlobalData.numCustomers/13);

    public void newBestSolutionFound( TabuSearchEvent evt )
    {   
    	TabuSearch theTS = (TabuSearch)evt.getSource();
    	Solution   best  = theTS.getBestSolution();
    	My2Opt_TabuList mytl;

    	mytl = (My2Opt_TabuList)theTS.getTabuList();
    	mytl.setTenure( Math.max( MIN_TENURE, (int)( 0.75 * mytl.getTenure() ) ) );
    	//mytl.setTenure( 7 );
        System.out.println("Decrease tenure to " + mytl.getTenure());

        final String msg = "New Best solution found at iteration " + theTS.getIterationsCompleted() + "\n Done. Best solution: " + best;
    	System.out.println(msg);
    }

    public void unimprovingMoveMade( TabuSearchEvent evt )
    {   // Increase tenure
    	TabuSearch theTS = (TabuSearch)evt.getSource();
    	My2Opt_TabuList mytl;
    	mytl = (My2Opt_TabuList)theTS.getTabuList();

    	//mytl.setTenure( Math.min( MAX_TENURE, mytl.getTenure() + 2 ));
    	mytl.setTenure( Math.min( MAX_TENURE, mytl.getTenure() + 2 ));
        System.out.println("Increase tenure to " + mytl.getTenure());
  }

    // We're not using these events
    public void newCurrentSolutionFound( TabuSearchEvent evt ){}
    public void tabuSearchStarted( TabuSearchEvent evt ){}
    public void tabuSearchStopped( TabuSearchEvent evt ){}
    public void noChangeInValueMoveMade( TabuSearchEvent evt ){}
    public void improvingMoveMade( TabuSearchEvent evt ){
    	TabuSearch theTS = (TabuSearch)evt.getSource();
    	My2Opt_TabuList mytl;
    	mytl = (My2Opt_TabuList)theTS.getTabuList();

    	mytl.setTenure( Math.max( MIN_TENURE, mytl.getTenure() - 4 ));
    	System.out.println("Decrease tenure to " + mytl.getTenure());
    }
}
