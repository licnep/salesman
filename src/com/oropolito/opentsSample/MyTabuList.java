package com.oropolito.opentsSample;

import org.coinor.opents.*;

public class MyTabuList extends SimpleTabuList
 implements TabuSearchListener
{
    public int MAX_TENURE = GlobalData.numCustomers/2;

    public MyTabuList(int tabuTenure)
    {
    	super(tabuTenure);
    }
    
    public void newBestSolutionFound( TabuSearchEvent evt )
    {   // Decrease tenure
        setTenure( Math.max( 1, (int)( 0.75 * getTenure() ) ) );
        System.out.println("Decrease tenure");
    }

    public void unimprovingMoveMade( TabuSearchEvent evt )
    {   // Increase tenure
        setTenure( Math.min( MAX_TENURE, getTenure() + 2 ));
        System.out.println("Increase tenure");
    }

    
    
   
    // We're not using these events
    public void noChangeInValueMoveMade( TabuSearchEvent evt ){}
    public void newCurrentSolutionFound( TabuSearchEvent evt ){}
    public void tabuSearchStarted( TabuSearchEvent evt ){}
    public void tabuSearchStopped( TabuSearchEvent evt ){}
    public void improvingMoveMade( TabuSearchEvent evt ){}
}
