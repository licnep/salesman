package com.oropolito.opentsSample;

import java.util.ArrayList;

//Totalmente casuale (fa schifo)
public class MyRandomSolution extends MySolution {
	public MyRandomSolution(int n_customers) {
		//genero un tour casuale
		this.tour = new int[n_customers];
		ArrayList<Integer> mazzoOrdinato = new ArrayList<>();
		for( int i = 0; i < n_customers; i++ )
            mazzoOrdinato.add(i);
		for (int i=n_customers;i>0;i--) {
			Double n_casuale =  Math.floor(Math.random()*i); //n casuale fra 0 e i-1
			this.tour[i-1] =  mazzoOrdinato.remove( n_casuale.intValue() );
		}
	}
}
