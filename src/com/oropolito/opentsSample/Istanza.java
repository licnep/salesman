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
		return (this.getBestValue()-this.bestKnown)/this.bestKnown;
	}
	
	public double getBestValue() {
		return optimality(solutions.get(0).value);
	}
	
	public double getMinValue() {
		return optimality(solutions.get(0).value);
	}
	
	public double getMaxValue() {
		return optimality(solutions.get(solutions.size()-1).value);
	}
	
	public double getMeanValue() {
		return optimality(solutions.get((int)Math.floor(solutions.size()/2)).value);
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
