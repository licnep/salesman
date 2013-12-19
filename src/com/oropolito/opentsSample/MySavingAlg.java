 package com.oropolito.opentsSample;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;




import org.coinor.opents.ObjectiveFunction;

import com.oropolito.opentsSample.GUI.GUI_model;

//Parto da un nodo a caso.
//prendo gli N nodi piu' vicini e seleziono uno di quelli a caso
public class MySavingAlg extends MySolutionEdges {
	public MySavingAlg(double[][] customers,GUI_model gui_model, LK_ObjectiveFunction objFunc)
    {
		Random rand_generator = new Random(GlobalData.random_seed);
		int len = customers.length;
		int HubNode = 0;
		//creo vettore per sapere che arco eliminare--->if valore>2 then togli
        int [] check = new int[ len ];
        this.tour = new int[ len ];
        
      //creo lista con gli archi-------SOLUZIONE STELLA
        
        for( int i = 1; i < len; i++ ){
        	this.addEdge(new Edge(HubNode,(HubNode+i)%len));
        	check[HubNode]++;
        	check[(HubNode+i)%len]++;
        }
    
		try {
			//gui_model.setTour_current(tour);
			//gui_model.addColoredEdge(edges, Color.blue);
			//Thread.sleep(10000);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        //creo matrice lenxlen dei saving
        double[][] saving_matrix = new double[len][len];
        for( int i = 0; i < len; i++ ){	
        	for( int j = i+1; j < len; j++ ){
        		if(j!=HubNode&&i!=HubNode&&j!=i) {
        			saving_matrix[i][j]=objFunc.matrix[HubNode][i]
        					+objFunc.matrix[HubNode][j]
					     -objFunc.matrix[i][j];	
        		}
        	}
        }
        
        int[] max_arc= new int [2];
        double max = 0;
        
        while(check[HubNode]>2){
        	System.out.println(check[HubNode]);
	        //controllo il Saving MAX
	        for( int i = 0; i < len; i++ ){
	        	for( int j = i+1; j < len; j++ ){
	        		if(saving_matrix[i][j]>max){
	        			if(edges.contains(new Edge(HubNode,i)) && edges.contains(new Edge(HubNode,j))) {
		        			max=saving_matrix[i][j];
		        			max_arc[0]=i;
		        			max_arc[1]=j;
	        			}
	        		}
	        		
	        	}
	        }
	        
	        max=0.0;
	        saving_matrix[max_arc[0]][max_arc[1]]=0.0;
	        saving_matrix[max_arc[1]][max_arc[0]]=0.0;
	        check[max_arc[0]]++;
	        check[max_arc[1]]++;
	        
	        this.addEdge(new Edge(max_arc[0],max_arc[1]));
	        
	        if(check[max_arc[0]]>2){
	        	this.removeEdge(new Edge(HubNode,max_arc[0]));
	        	check[max_arc[0]]--;
	            check[HubNode]--;
	        }
	        if(check[max_arc[1]]>2){
	        	this.removeEdge(new Edge(HubNode,max_arc[1]));
	        	check[max_arc[1]]--;
	            check[HubNode]--;
	        }
	        ArrayList<Edge> asd = new ArrayList<Edge>();
	        asd.addAll(edges);
	        GlobalData.gui_model.resetColoredEdges();
	        GlobalData.gui_model.addColoredEdge(asd, Color.BLUE);
	        //try {Thread.sleep(100);}  catch (Exception e) {e.printStackTrace();}
        }
        
        //this.sincTourWithEdges();
        
		try {
			//gui_model.setTour_current(tour);
			Thread.sleep(10000);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    
        }
}
