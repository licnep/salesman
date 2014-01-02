package com.oropolito.opentsSample;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import com.oropolito.opentsSample.GUI.GUI_model;

//Parto da un nodo a caso.
//prendo gli N nodi piu' vicini e seleziono uno di quelli a caso
public class MyRandomSolution2 extends MySolutionEdges {
	public MyRandomSolution2(double[][] customers,GUI_model gui_model)
    {
		Random rand_generator = new Random(G.random_seed);
		
        tour = new int[ customers.length ];
        ArrayList<Integer> avail = new ArrayList<>();
		for( int i = 0; i < customers.length; i++ )
            avail.add(i);
        
        //parto da un nodo a caso
        Double n_casuale =  Math.floor( rand_generator.nextInt(customers.length));
		this.tour[0] =  avail.remove( n_casuale.intValue() );
        
		//scelgo tra i 3 neighbout piu' vicini, a caso
		int N = 1; //1=pure greedy
		
        for( int i = 1; i < tour.length; i++ )
        {
        	//trovo gli N piu' vicini
        	LinkedList<Elemento> closest = new LinkedList<Elemento>();
        	
        	//scorro tutti i neighbour
        	for (int j=0;j<avail.size();j++) {
        		//elemento contiene il n. della citta' e' la distanza dalla citta' passata
        		Elemento cur = new Elemento(avail.get(j),norm(customers,avail.get(j),tour[i-1]));

        		//popolo la lista 5 neighbour + vicini
        		for (int k=0;k<N && k<closest.size()+1;k++) {
        			if (closest.size() < N) {
        				closest.add(cur); //la prima volta riempio l'array
        			}
        			else if (closest.get(k).distanza > cur.distanza) {
        				//ne ho trovato uno piu' lungo, inserisco il mio prime (la lista finale risulta ordinata)
        				closest.add(k, cur);
        				k=N;//l'ho aggiunto, voglio uscire dal ciclo
        			}
        		}
        	}
        	
        	//scelgo uno dei piu' vicini a caso e lo aggiungo al percorso
        	n_casuale =  Math.floor(rand_generator.nextInt(N)); //n casuale fra 0 e N-1
        	//System.out.println(closest);
        	Elemento chosen = closest.remove( n_casuale.intValue() );
			this.tour[i] = chosen.numero;
			avail.remove((Object)chosen.numero);
			//System.out.println(avail);
			try {
				gui_model.setTour_current(tour);
				//Thread.sleep(10);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        
        }   // end for

	}
	private double norm( double[][]matr, int a, int b )
	{
	    double xDiff = matr[b][0] - matr[a][0];
	    double yDiff = matr[b][1] - matr[a][1];
	    return Math.sqrt( xDiff*xDiff + yDiff*yDiff );
	}   // end norm
	
	class Elemento {
		public int numero;
		public double distanza;
		public Elemento(int n, double dist) {
			this.numero = n;
			this.distanza = dist;
		}
		public String toString() {
			return String.valueOf(this.distanza)/*+"-"+String.valueOf(this.distanza)*/;
		}
	}
}
