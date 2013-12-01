package com.oropolito.opentsSample;

import org.coinor.opents.*;

public class MyTabuList extends SimpleTabuList
{
    public int MAX_TENURE = GlobalData.numCustomers/2;

    public MyTabuList(int tabuTenure)
    {
    	super(tabuTenure);
    }
}
