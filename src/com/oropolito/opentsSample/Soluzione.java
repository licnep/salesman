package com.oropolito.opentsSample;

public class Soluzione implements Comparable<Soluzione> {
	public int value;
	public double time;
	
	public Soluzione(int value, double time) {
		this.value = value;
		this.time = time;
	}
	
	@Override
	public int compareTo(Soluzione o) {
		if (this.value > o.value) return 1;
		if (this.value < o.value) return -1;
		return 0;
	}
		
}
