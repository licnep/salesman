package com.oropolito.opentsSample;

import org.coinor.opents.*;

public class LK_Listener extends TabuSearchAdapter{
	
	private float k = GlobalData.numCustomers;
	
	public int MAX_TENURE =  Math.round(k);//*3;// /2;
	public int MIN_TENURE = 27;//GlobalData.MIN_TENURE;//Math.max(7, Math.round(k/6) );

    public void newBestSolutionFound( TabuSearchEvent evt )
    {   
    	TabuSearch theTS = (TabuSearch)evt.getSource();
    	Solution   best  = theTS.getBestSolution();
    	LK_TabuList mytl;

    	mytl = (LK_TabuList)theTS.getTabuList();
    	//mytl.setTenure( Math.max( MIN_TENURE, (int)( 0.75 * mytl.getTenure() ) ) );
    	//mytl.setTenure( Math.max( GlobalData.MIN_TENURE, (int)( 0.50 * mytl.getTenure() ) ) );
    	mytl.setTenure( Math.min( GlobalData.MAX_TENURE, mytl.getTenure() + 3) );
    	//mytl.setTenure( 7 );
        //System.out.println("Decrease tenure to " + mytl.getTenure());

        final String msg = "New Best solution found at iteration " + theTS.getIterationsCompleted() + "\n Done. Best solution: " + best;
    	System.out.println(msg);
    }

    public void unimprovingMoveMade( TabuSearchEvent evt )
    {   
    	GlobalData.ImprovingCounter=0;
    	GlobalData.notImprovingCounter++;
    	// Increase tenure
    	TabuSearch theTS = (TabuSearch)evt.getSource();
    	LK_TabuList mytl;
    	mytl = (LK_TabuList)theTS.getTabuList();

    	//mytl.setTenure( Math.min( MAX_TENURE, mytl.getTenure() + 1 ));
    	mytl.setTenure( Math.max( GlobalData.MIN_TENURE, mytl.getTenure() - 2 ));
        //System.out.println("Increase tenure to " + mytl.getTenure());
  }

    // We're not using these events
    public void newCurrentSolutionFound( TabuSearchEvent evt ){}
    public void tabuSearchStarted( TabuSearchEvent evt ){}
    public void tabuSearchStopped( TabuSearchEvent evt ){}
    public void noChangeInValueMoveMade( TabuSearchEvent evt ){
    	GlobalData.ImprovingCounter=0;
    	GlobalData.notImprovingCounter++;
    	
    	// Increase tenure
    	TabuSearch theTS = (TabuSearch)evt.getSource();
    	LK_TabuList mytl;
    	mytl = (LK_TabuList)theTS.getTabuList();
    	//mytl.setTenure( Math.min( MAX_TENURE, mytl.getTenure() + 1 ));
    	//mytl.setTenure( Math.max( MIN_TENURE, mytl.getTenure() - 1 ));
    	//System.out.println("Increase tenure to " + mytl.getTenure());

    }
    public void improvingMoveMade( TabuSearchEvent evt ){
    	GlobalData.ImprovingCounter++;
    	
    	TabuSearch theTS = (TabuSearch)evt.getSource();
    	LK_TabuList mytl;
    	mytl = (LK_TabuList)theTS.getTabuList();
    	GlobalData.notImprovingCounter = 0;
    	if (GlobalData.ImprovingCounter>1) {
    		GlobalData.notImprovingCounter = 0;
        	//mytl.setTenure( Math.max( MIN_TENURE, mytl.getTenure() - 8 ));
        	System.out.println("Decrease tenure to " + mytl.getTenure());
    	}
    	//mytl.setTenure( Math.max( GlobalData.MIN_TENURE, mytl.getTenure() - 1 ));
    	mytl.setTenure( Math.min( GlobalData.MAX_TENURE, mytl.getTenure() + 2 ));
    	System.out.println("Decrease tenure to " + mytl.getTenure());
    }
}