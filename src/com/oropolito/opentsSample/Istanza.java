package com.oropolito.opentsSample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

public class Istanza {
	public int bestKnown=0;
	public String nomeIstanza = new String();
	
	ArrayList<Soluzione> solutions = new ArrayList<Soluzione>(G.Repetitions);
	
	public double getPercentFromOptimum() {
		return 100*(this.getBestValue()-this.bestKnown)/(double)this.bestKnown;
	}
	
	public int getBestValue() {
		return solutions.get(0).value;
	}
	
	public int getMinValue() {
		return solutions.get(0).value;
	}
	
	public int getMaxValue() {
		return solutions.get(solutions.size()-1).value;
	}
	
	public int getMeanValue() {
		return solutions.get((int)Math.floor(solutions.size()/2)).value;
	}
	
	public double getTimeBest() {
		return solutions.get(0).time;
	}
	
	public double getTimeMean() {
		double totTime=0;
		for (Soluzione s: solutions) {
			totTime+=s.time;
		}
		return totTime/solutions.size();
	}
	
	public void addSolution(Soluzione s) {
		solutions.add(s);
		Collections.sort(solutions);
	}
	
	public String getNomeIstanza() {
		return nomeIstanza;
	}
	
	private double optimality(int val) {
		return 100*(val-this.bestKnown)/(double)this.bestKnown;
	}
}
