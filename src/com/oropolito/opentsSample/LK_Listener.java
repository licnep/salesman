package com.oropolito.opentsSample;

import org.coinor.opents.*;

import sun.org.mozilla.javascript.tools.shell.Global;

public class LK_Listener extends TabuSearchAdapter{
	
	private float k = G.numCustomers;
	
	private double tenure = 0;
	private LK_TabuList ts;
	
	public int MAX_TENURE =  Math.round(k);//*3;// /2;
	public int MIN_TENURE = 27;//GlobalData.MIN_TENURE;//Math.max(7, Math.round(k/6) );

	public LK_Listener(LK_TabuList ts) {
		this.ts = ts;
		this.tenure = ts.getTenure();
	}
	
    public void newBestSolutionFound( TabuSearchEvent evt )
    {   
    	TabuSearch theTS = (TabuSearch)evt.getSource();
    	Solution   best  = theTS.getBestSolution();
    	LK_TabuList mytl;

    	mytl = (LK_TabuList)theTS.getTabuList();
    	//mytl.setTenure( Math.max( MIN_TENURE, (int)( 0.75 * mytl.getTenure() ) ) );
    	this.setTenure(tenure*0.5);
    	//mytl.setTenure( Math.max( GlobalData.MIN_TENURE, (int)( 0.50 * mytl.getTenure() ) ) );
    	//mytl.setTenure( Math.min( GlobalData.MAX_TENURE, mytl.getTenure() + 3) );
    	G.iterazioni3Opt = 20;
    	//mytl.setTenure( 7 );
        //System.out.println("Decrease tenure to " + mytl.getTenure());

        final String msg = "New Best solution found at iteration " + theTS.getIterationsCompleted() + "\n Done. Best solution: " + best;
    	System.out.println(msg);
    }

    public void unimprovingMoveMade( TabuSearchEvent evt )
    {   
    	G.ImprovingCounter=0;
    	G.notImprovingCounter++;
    	// Increase tenure
    	TabuSearch theTS = (TabuSearch)evt.getSource();
    	LK_TabuList mytl;
    	mytl = (LK_TabuList)theTS.getTabuList();

    	//mytl.setTenure( Math.min( GlobalData.MAX_TENURE, mytl.getTenure() + 2 ));
    	this.setTenure(tenure+2);
    	//mytl.setTenure( Math.max( GlobalData.MIN_TENURE, mytl.getTenure() - 2 ));
        //System.out.println("Increase tenure to " + mytl.getTenure());
    	if (G.notImprovingCounter>1) {
    		G.perturbate = true;
    	}
  }

    // We're not using these events
    public void newCurrentSolutionFound( TabuSearchEvent evt ){}
    public void tabuSearchStarted( TabuSearchEvent evt ){}
    public void tabuSearchStopped( TabuSearchEvent evt ){}
    public void noChangeInValueMoveMade( TabuSearchEvent evt ){
    	G.ImprovingCounter=0;
    	G.notImprovingCounter++;
    	
    	// Increase tenure
    	TabuSearch theTS = (TabuSearch)evt.getSource();
    	LK_TabuList mytl;
    	mytl = (LK_TabuList)theTS.getTabuList();
    	//mytl.setTenure( Math.min( GlobalData.MAX_TENURE, mytl.getTenure() + 1 ));
    	this.setTenure(tenure+1);
    	//mytl.setTenure( Math.max( MIN_TENURE, mytl.getTenure() - 1 ));
    	//System.out.println("Increase tenure to " + mytl.getTenure());

    }
    public void improvingMoveMade( TabuSearchEvent evt ){
    	G.ImprovingCounter++;
    	
    	TabuSearch theTS = (TabuSearch)evt.getSource();
    	LK_TabuList mytl;
    	mytl = (LK_TabuList)theTS.getTabuList();
    	G.notImprovingCounter = 0;
    	if (G.ImprovingCounter>1) {
    		G.notImprovingCounter = 0;
        	//mytl.setTenure( Math.max( MIN_TENURE, mytl.getTenure() - 8 ));
        	System.out.println("Decrease tenure to " + mytl.getTenure());
    	}
    	
    	this.setTenure(Math.max( G.MIN_TENURE, tenure-8) );
    	//mytl.setTenure( Math.min( GlobalData.MAX_TENURE, mytl.getTenure() + 2 ));
    	System.out.println("Decrease tenure to " + mytl.getTenure());
    }
    
    private void setTenure(double t) {
    	this.tenure = t;
    	ts.setTenure((int)Math.round(t));
    }
}