package com.oropolito.opentsSample;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;

import org.coinor.opents.*;

import com.oropolito.opentsSample.GUI.GUI_model;


public class LKDoubleBridge_MoveManager implements MoveManager
{
	public LK_ObjectiveFunction objFunc;
	public LK_TabuList tabu;
	
	ArrayList<Edge> edgesX;
	ArrayList<Edge> edgesY;
	
	public LKDoubleBridge_MoveManager(LK_ObjectiveFunction myObj,LK_TabuList tabu) {
		this.objFunc = myObj;
		this.tabu = tabu;
	}
	
    public Move[] getAllMoves( Solution solution )
    {   
    	MySolutionEdges sol = (MySolutionEdges)solution;
        
        ArrayList<LK_Move> l = new ArrayList<LK_Move>();
        
        double G_star = Integer.MAX_VALUE; //miglioramento migliore per adesso
        
        sol.sincEdgesWithTour(); //synchronize the edges with the tour
        edgesX = new ArrayList<Edge>();
        edgesY = new ArrayList<Edge>();
        
        LK_Move bestDoubleBridgeCandidate = new LK_Move(edgesX, edgesY);
        
        Iterator<Edge> ie = sol.edges.iterator();
        while(ie.hasNext()) {
        	edgesX.add(ie.next()); //setto x1
        	Edge[] vicini = objFunc.edgeVicini[edgesX.get(0).c2];
        	for (int i=0;i<GlobalData.nVicini;i++) {
        		if(!sol.contains(vicini[i])) {
        			edgesY.add(vicini[i]);
        			//mossa 2opt normale
            		edgesX.add(sol.getEdgeBefore(edgesY.get(0).c2));
            		edgesY.add(new Edge(edgesX.get(1).c2 , edgesX.get(0).c1));//ricollego
            		//testo tutti i vicini Y che non devono essere gia' nel tour
            		if(GlobalData.iteration>400) {
            			l.add(new LK_Move((ArrayList<Edge>)edgesX.clone(), (ArrayList<Edge>)edgesY.clone()));
            		}
            		edgesY.remove(1);
            		edgesX.remove(1);
            		
            		//testo anche mossa 2opt che genera 2 cicli, dalla migliore generero' una 4opt (double bridge)
            		edgesX.add(sol.getEdgeAfter(edgesY.get(0).c2));
            		edgesY.add(new Edge(edgesX.get(1).c2 , edgesX.get(0).c1));//ricollego
            		if (edgesY.get(1).isProper()) {
            			LK_Move m = new LK_Move((ArrayList<Edge>)edgesX.clone(), (ArrayList<Edge>)edgesY.clone());
            			if(!tabu.isTabu(sol, m)) {
	            			double g = calculateGain(edgesX, edgesY);
	                		if(g<G_star) {
	                			G_star=g;
	                			bestDoubleBridgeCandidate = m;
	                		}
            			}
            		}
    				edgesY.clear();
            		edgesX.remove(1);
        		}
        	}
        	edgesX.clear(); //testo un altro x1
        }

        
        //aggiungo la mosse migliori double bridge:
        //itero lungo uno a caso dei due cicli
        edgesX = bestDoubleBridgeCandidate.edgesX;
        edgesY = bestDoubleBridgeCandidate.edgesY;
        Edge y0=edgesY.get(0);
        Edge xx0 = y0;
        GlobalData.gui_model.resetColoredEdges();
        do {
        	xx0 = sol.getEdgeBefore(xx0.c2);
        	//testo altro edge nel secondo cerchio
        	Edge y1=edgesY.get(1);
        	Edge xx1=y1;
        	do {
        		xx1 = sol.getEdgeBefore(xx1.c2);
        		edgesY.add(new Edge(xx0.c1, xx1.c1));
        		edgesY.add(new Edge(xx0.c2, xx1.c2));
        		edgesX.add(xx0);
        		edgesX.add(xx1);
            	if (edgesY.get(2).isProper()&&edgesY.get(3).isProper()) {
            		l.add(new LK_Move((ArrayList<Edge>)edgesX.clone(), (ArrayList<Edge>)edgesY.clone()));
            	}
            	edgesY.remove(3);edgesY.remove(2);
            	edgesX.remove(3);edgesX.remove(2);
        	} while(y1.c1 != xx1.c2);
        	
        } while(y0.c1 != xx0.c2);//*/
        
        
        //try { Thread.sleep(1000); } catch (InterruptedException ex) { ex.printStackTrace();}
        LK_Move[] moves = l.toArray(new LK_Move[l.size()]);
        return moves;
    }   // end getAllMoves
    
    /**
     * 
     * @param e
     * @return true se e' permesso inserirlo, false se non e' permesso
     */
    private boolean pushEdgeX(Edge e) {
    	if (!e.isProper()) return false; //<- per evitare che vengano usati edge improper es (1,1)
    	
    	//l'ultimo Xi aggiunto non deve mai andare verso l'origine t1, altrimenti chiudendo, si creano 2 cicli
    	
    	
    	//non deve appartenere agli edges in gioco:
    	//TODO: usare struttura migliore per controllare appartenenza senza cicli
    	Iterator i = edgesX.iterator();
    	while(i.hasNext()) {
    		if(i.next().equals(e)) return false;
    	}
    	i = edgesY.iterator();
    	while(i.hasNext()) {
    		if(i.next().equals(e)) return false;
    	}
    	
    	edgesX.add(e);
    	return true;
    }
    
    private boolean pushEdgeY(Edge e,MySolutionEdges sol) {
    	if (!e.isProper()) return false; //<- per evitare che vengano usati edge improper es (1,1)
    	
    	//non deve appartenere al tour attuale
    	if(sol.contains(e)) return false;
    	
    	//non deve appartenere agli edges in gioco:
    	//TODO: usare struttura migliore per controllare appartenenza senza cicli
    	Iterator i = edgesX.iterator();
    	while(i.hasNext()) {
    		if(i.next().equals(e)) return false;
    	}
    	i = edgesY.iterator();
    	while(i.hasNext()) {
    		if(i.next().equals(e)) return false;
    	}
    	
    	edgesY.add(e);
    	return true;
    }
    
    private boolean generatesFeasibleTour(MySolutionEdges sol) {
    	//testa edgesX e edgesY
    	
    	//provo a percorrere tutto il 'nuovo tour'
    	//deve essere lungo nCustomers, altrimenti vuol dire che ho generato un sotto-tour
    	int[] tour = sol.tour;
    	int n=0;
    	int c = 0;
    	boolean avanti=true;
    	
    	do {
	    	//il prossimo e' 
	    	Edge nextE = avanti? sol.getEdgeAfter(c) : sol.getEdgeBefore(c);
	    	if (edgesX.contains(nextE)) {
	    		//l'edge e' stato rimosso
	    		//devo cercare fra quelli inseriti quello che tocca c
	    		Iterator<Edge> i = edgesY.iterator();
	    		while(i.hasNext()) {
	    			Edge e = i.next();
	    			if (e.c1==c||e.c2==c) {
	    				if (e.c1==c) c=e.c2;
	    				else c=e.c1;
	    				n++;
	    				//devo capire la nuova direzione di percorrenza, a seconda di quale edge attaccato a c e' stato rimosso
	    				avanti=true;
	    				Edge testE = sol.getEdgeAfter(c);
	    				Iterator<Edge> j = edgesX.iterator();
	    				while(j.hasNext()) {
	    					e = j.next();
	    					if(testE.equals(e)) {
	    						//l'edge di percorrenza in avanti e' stato eliminato, dobiamo percorrere all'indietro
	    						avanti=false;
	    					}
	    				}
	    			}
	    		}
	    	} else {
	    		c = nextE.c2; 
				n++;
	    	}
    	} while (c!=0);
    	
    	if (n==GlobalData.numCustomers) return true;
    	else return false;
    }
    
    private void popEdgeX() {
    	edgesX.remove(edgesX.size()-1);
    }
    
    private void popEdgeY() {
    	edgesY.remove(edgesY.size()-1);
    }
    
    private double calculateGain(ArrayList<Edge> edgesX,ArrayList<Edge> edgesY) {
    	double g=0;
    	Iterator<Edge> i = edgesX.iterator();
    	while(i.hasNext()) {
    		Edge e = i.next();
    		g-=objFunc.matrix[e.c1][e.c2];
    	}
    	i = edgesY.iterator();
    	while(i.hasNext()) {
    		Edge e = i.next();
    		g+=objFunc.matrix[e.c1][e.c2];
    	}
    	return g;
    }
    
}   // end class MyMoveManager
