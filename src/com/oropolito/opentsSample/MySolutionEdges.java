package com.oropolito.opentsSample;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import org.coinor.opents.*;


public class MySolutionEdges extends SolutionAdapter 
{
    public int[] tour;
    public long hashCode = 0;
    
    public LinkedHashSet<Edge> edges;
    
    //queste vendono inizializzate in sincEdgesWithTour
    //e aggiornate con addEdge
    public int[] prima; //per ogni customer memorizzo il customer prima di lui
    public int[] dopo; //per ogni customer memorizzo il customer dopo di lui
    
    //TODO temp function, should be done automatically 
    public void sincEdgesWithTour() {
    	edges.clear();
    	for (int i=0;i<tour.length;i++) {
    		edges.add(new Edge(tour[i],tour[(i+1)%tour.length]));
    		dopo[tour[i]] = tour[(i+1)%tour.length];
    		prima[tour[(i+1)%tour.length]] = tour[i];
    	}
    }
    
    public void sincTourWithEdges() {
    	//TODO: sarebbe buono non dover scorrere tutti gli edge, ma poter trovare subito quello che parte con un dato n
    	tour = new int[tour.length];
    	tour[0] = 0;
    	LinkedHashSet<Edge> available = (LinkedHashSet<Edge>)edges.clone();
    	for(int i=1;i<tour.length;i++) {
    		Iterator<Edge> j = available.iterator();
    		while(j.hasNext()) {
    			Edge e = j.next();
    			if(e.c1==tour[i-1]) {
    				available.remove(e);
    				tour[i]=e.c2;
    				break;
    			}
    			if(e.c2==tour[i-1]) { //segmento invertito
    				available.remove(e);
    				tour[i]=e.c1;
    				break;
    			}
    		}
    	}
    	this.setHashCode();
    }
    
    public void addEdge(Edge e) {
    	edges.add(e);
    	//prima[e.c2] = e.c1;
    	//dopo[e.c1] = e.c2;
    }
    
    public void removeEdge(Edge e) {
    	edges.remove(e);
    	//questi due credo si possano togliere, per debug solo
    	//dopo[e.c1] = 0;
    	//prima[e.c2] = 0;
    }

    public boolean contains(Edge e) {
    	if(edges.contains(e)) return true;
    	return false;
    }
    
    public MySolutionEdges(){
    	edges = new LinkedHashSet<>();
    	prima = new int[ G.numCustomers ];
    	dopo = new int[ G.numCustomers ];
    } // Appease clone()
    
    Edge getEdgeAfter(int c) {
    	return new Edge(c, dopo[c]);
    }
    Edge getEdgeBefore(int c) {
    	//l'edge ritornato va in senso contrario al tour!
    	return new Edge(c,prima[c]);
    }
    
    public MySolutionEdges( double[][] customers )
    {
        // Crudely initialize solution
    	tour = new int[ customers.length ];
    	prima = new int[ customers.length ];
    	dopo = new int[ customers.length ];
        for( int i = 0; i < customers.length; i++ )
            tour[i] = i;
        edges = new LinkedHashSet<>();
    }   // end constructor
    
    
    public Object clone()
    {   
        MySolutionEdges copy = (MySolutionEdges)super.clone();
        copy.tour = (int[])this.tour.clone();
        copy.hashCode = this.hashCode;
        return copy;
    }   // end clone
    
    
    public String toString()
    {
        StringBuffer s = new StringBuffer();
        
        //s.append( "Solution value: " + getObjectiveValue()[0] );
        s.append( "Sequence: [ " );
        
        for( int i = 0; i < tour.length - 1; i++ )
            s.append( tour[i] ).append( ", " );
        
        s.append( tour[ tour.length - 1 ] );
        s.append( " ]" );
        
        return s.toString();
    }   // end toString
    
    private void setHashCode() {
    	this.hashCode = 0;
    	Iterator<Edge> i = edges.iterator();
    	while(i.hasNext()) {
    		this.hashCode += i.next().hashCode();
    	}
    }
    
}   // end class MySolution
