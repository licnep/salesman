package com.oropolito.opentsSample;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import com.oropolito.opentsSample.GUI.GUI_model;

//Parto da un nodo a caso.
//prendo gli N nodi piu' vicini e seleziono uno di quelli a caso
public class FarthestInsertion extends MySolutionEdges {
	public FarthestInsertion(double[][] customers,LK_ObjectiveFunction objFunc)
    {
		int len = customers.length;
		LinkedList<Integer> avail = new LinkedList<Integer>();
		for(int i=0;i<len;i++) avail.add(i);
		
		//Step1. trova i due nodi a distanza massima
		double max=0;
		Integer maxi=0,maxj=0;
		for (int i=0;i<len-1;i++) {
			for(int j=i+1;j<len;j++) {
				if (objFunc.matrix[i][j]>max) {
					max=objFunc.matrix[i][j];maxi=i;maxj=j;
				}
			}
		}
		//creo un tour iniziale circolare che passa solo per questi due nodi estremi:
		LinkedList<Integer> t = new LinkedList<Integer>();
		t.add(0, maxi);
		t.add(0, maxj);
		avail.remove((Object)maxi);
		avail.remove((Object)maxj);
		GlobalData.gui_model.addColoredEdge(maxi, maxj, Color.GREEN);
		
		//2. cerco il nodo piu' distante da quelli gia' nel tour
		//(scandisco solo righe nodi tour, e cerco max)
		while(avail.size()>0) {
			max=0; //maxi sara' in realta' mini, quello piu' vicino a lui
			for (Integer i : avail) {
				double mindist=Double.MAX_VALUE;
				int minj=0;
				for(Integer j: t) {
					if(objFunc.matrix[i][j]<mindist) {
						mindist=objFunc.matrix[i][j];
						minj=j;
					}
				}
				if (mindist>max) {
					max=mindist;
					maxi=i;
				}
			}
			//scelgo la posizione migliore dove inserirlo
			double minDelta=Double.MAX_VALUE;
			int insertpos=0;
			for (int i=0;i<t.size();i++) {
				//testo costo mettendolo dopo i
				double d = 0;
				int dopo_i = (i+1)%t.size();
				d -=objFunc.matrix[ t.get(i) ][ t.get(dopo_i) ];
				d +=objFunc.matrix[ t.get(i) ][ maxi ];
				d +=objFunc.matrix[ maxi ][ t.get(dopo_i) ];
				if (d<minDelta) {
					minDelta=d;insertpos=dopo_i;
				}
			}
			t.add(insertpos,maxi);
			avail.remove((Object)maxi);
			GlobalData.gui_model.resetColoredEdges();
			for (int i = 0;i<t.size();i++) {
				GlobalData.gui_model.addColoredEdge(t.get(i), t.get((i+1)%t.size()), Color.GREEN);
			}
			try { Thread.sleep(10); } catch (InterruptedException e) { e.printStackTrace();}
    	}
		this.tour = new int[t.size()];
		for (int i=0;i<tour.length;i++) tour[i]=t.get(i);
		//try { Thread.sleep(20000); } catch (InterruptedException e) { e.printStackTrace();}
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
