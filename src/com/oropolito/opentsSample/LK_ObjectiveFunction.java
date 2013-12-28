package com.oropolito.opentsSample;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.coinor.opents.*;


public class LK_ObjectiveFunction implements ObjectiveFunction
{
    public double[][] matrix;
    public int[][] vicini; //per ogni customer lista degli N piu' vicini
    public Edge[][] edgeVicini;
    public double lambda = 1;
    
    public int[][] penalty; //penalita' per ogni edge possibile, inizializzata a 0
    
    public LK_ObjectiveFunction( double[][] customers ) 
    {   matrix = createMatrix( customers );
    	createVicini();
    }   // end constructor

    public void createVicini() {
    	int N = Math.min(60, GlobalData.numCustomers-1); //mai + vicini di numCustomers-1
    	GlobalData.nViciniMax = N;
    	//GlobalData.nVicini = 10;
    	
    	
    	vicini = new int[matrix.length][N];
    	edgeVicini = new Edge[matrix.length][N];
    	for (int i=0;i<matrix.length;i++) {
    		double[] ordinati = matrix[i].clone();
    		Arrays.sort(ordinati); //ordinati contiene adesso le DISTANZE ordinate
    		for(int j=1;j<=N;j++) { //il primo va saltato perche' e' sempre se stesso
    			for(int k=0;k<matrix.length;k++) {
    				if(matrix[i][k]==ordinati[j]) {
    					//solo se k non e' gia' stato inserito (puo' capitare se piu' distanze a parimerito
    					boolean giaInserito=false;
    					for (int m=0;m<j-1;m++) { //controllo che non sia gia' nell'array vicini
    						if (vicini[i][m]==k) giaInserito=true;
    					}
    					if ( !giaInserito ) {
    						vicini[i][j-1] = k;
    						edgeVicini[i][j-1] = new Edge(i,k);
    						break;
    					}
    				}
    			}
    		}
    		//qui ho l'elenco dei piu' vicini a i
    	}
    }
    
    public double[] evaluate( Solution solution, Move proposed_move )
    {
        int[] tour = ((MySolutionEdges)solution).tour;
        int len = tour.length;
        
        // If move is null, calculate distance from scratch
        if( proposed_move == null)
        {
            double dist = 0;
            for( int i = 0; i < len; i++ )
            	dist += matrix[ tour[i] ][ i+1 >= len ?  tour[0] : tour[i+1] ];
                
            return new double[]{ dist };
        }   // end if: move == null
        
        // Else calculate incrementally
        else
        {
            LK_Move mv = (LK_Move)proposed_move;
            
            // Prior objective value
            double dist = solution.getObjectiveValue()[0];
            //tolgo la lunghezza dei 2 segmenti rimossi
            Iterator<Edge> i = mv.edgesX.iterator();
            while(i.hasNext()) {
            	Edge e = i.next();
            	dist -= matrix[e.c1][e.c2] ;
            	dist -= lambda*penalty[e.c1][e.c2];
            }
            i = mv.edgesY.iterator();
            while(i.hasNext()) {
            	Edge e = i.next();
            	dist += matrix[e.c1][e.c2] ;
            	dist += lambda*penalty[e.c1][e.c2];
            }
            return new double[]{ dist };
        }
    }   // end evaluate
    
    
    /** Create symmetric matrix. */
    private double[][] createMatrix( double[][] customers )
    {
        int len = customers.length;
        double[][] matrix = new double[len][len];
        penalty = new int[len][len]; //inizializzata a 0
        
        for( int i = 0; i < len; i++ )
            for( int j = i+1; j < len; j++ )
                matrix[i][j] = matrix[j][i] = norm(
                    customers[i][0], customers[i][1],
                    customers[j][0], customers[j][1] );
        return matrix;
    }   // end createMatrix
    
    public void localMinimumReached_UpdatePenalty(MySolutionEdges sol) {
    	//suggetito 0.3*lunghezza media edge
    	this.lambda = 0.1;//*sol.getObjectiveValue()[0]/GlobalData.numCustomers;
    	Collection<Edge> edges = sol.edges;
    	//dobbiamo aggiornare le penalita', viene aumentata solo per l'edge a costo massimo
    	//dove il costo e' costo_edge/(1+penalita' edge)
    	Iterator<Edge> i = edges.iterator();
    	double maxCost = 0;
    	Edge maxEdge = new Edge(0,0);
    	while(i.hasNext()) {
    		Edge e = i.next();
    		double cost = matrix[e.c1][e.c2]/(1+penalty[e.c1][e.c2]);
    		if (cost>maxCost) {
    			maxEdge = e;
    			maxCost = cost;
    		}
    	}
    	//incremento la penalita' dell'edge a costo massimo
    	penalty[maxEdge.c1][maxEdge.c2]++;
    	penalty[maxEdge.c2][maxEdge.c1]++; //matrice sempre simmetrica
    	
    	GlobalData.gui_model.resetColoredEdges();
    	GlobalData.gui_model.addColoredEdge(maxEdge, Color.MAGENTA);
    	//try { Thread.sleep(2000); } catch (InterruptedException e) { e.printStackTrace();}
    }
    
    
    /** Calculate distance between two points. */
    private double norm( double x1, double y1, double x2, double y2 )
    {   
        double xDiff = x2 - x1;
        double yDiff = y2 - y1;
        return Math.round(Math.sqrt( xDiff*xDiff + yDiff*yDiff ));
    }   // end norm
    
}   // end class MyObjectiveFunction
