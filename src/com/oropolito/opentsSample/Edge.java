package com.oropolito.opentsSample;

//edge che va da c1 a c2
public class Edge {
	public int c1;
	public int c2;
	
	public Edge(int c1, int c2) {
		this.c1 = c1;
		this.c2 = c2;
	}
	
	public int hashCode() {
		return c1*c2;
	}
	
	public boolean equals(Object o) {
		Edge e = (Edge)o;
		if (this.c1==e.c1&&this.c2==e.c2) return true;
		if (this.c2==e.c1&&this.c1==e.c2) return true;
		return false;
	}
	
	public Edge backwards() {
		return new Edge(this.c2,this.c1);
	}
 	
	public boolean isProper() {
		if (c1==c2) return false;
		else return true;
	}
}
