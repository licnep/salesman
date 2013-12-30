package com.oropolito.opentsSample;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import org.coinor.opents.*;

import com.oropolito.opentsSample.GUI.GUI_model;


public class Random4Opt_MoveManager implements MoveManager
{
	public LK_ObjectiveFunction objFunc;
	
	ArrayList<Edge> edgesX;
	ArrayList<Edge> edgesY;
	
	public Random4Opt_MoveManager(LK_ObjectiveFunction myObj) {
		this.objFunc = myObj;
	}
	
	
	
	
	public Move[] getAllMovesNonRandom( Solution solution )
    {   
    	Edge x1,x2,x3,x4,y1,y2,y3,y4;
    	MySolutionEdges sol = (MySolutionEdges)solution;
    	sol.sincEdgesWithTour(); //synchronize the edges with the tour
    	ArrayList<LK_Move> l = new ArrayList<LK_Move>();
    	
    	edgesX = new ArrayList<Edge>();
    	edgesY = new ArrayList<Edge>();
        
    	ArrayList<Edge> available = new ArrayList<>(sol.edges);
    	
    	Random rand_generator = new Random(G.random_seed);
    	
    	double minG = Double.MAX_VALUE;
    	LK_Move bestIllegal2OptMove = new LK_Move(edgesX, edgesY);
    	Iterator<Edge> ie = sol.edges.iterator();
        while(ie.hasNext()) {
        	edgesX.add(ie.next()); //setto x1
        	
        	//scelgo y1
        	Edge[] vicini = objFunc.edgeVicini[edgesX.get(0).c2];
        	for (int i=0;i<G.nVicini;i++) {
        		if(!sol.contains(vicini[i])) {
        			edgesY.add(vicini[i]);
        			
        			if(edgesX.add(sol.getEdgeAfter(edgesY.get(0).c2))) {
        				if (edgesX.get(1).c2 != edgesX.get(0).c1) {
	        				//Y2 che ricollega a t1
	        				edgesY.add(new Edge(edgesX.get(1).c2 , edgesX.get(0).c1));
	        				double g = calculateGain(edgesX, edgesY);
	        				if (g<minG) {
	        					LK_Move m = new LK_Move((ArrayList<Edge>)edgesX.clone(), (ArrayList<Edge>)edgesY.clone());
        						minG = g;
        						bestIllegal2OptMove = m;
	        				}
	    					G.gui_model.resetColoredEdges();
	    					G.gui_model.addColoredEdge(edgesX, Color.RED);
	    					G.gui_model.addColoredEdge(edgesY, Color.GREEN);
	        				popEdgeY();
	                    	popEdgeX();
        				}
        			}
        			popEdgeY();
        		}
        	}
        	edgesX.clear();
        }
        
        //qui ho la migliore 2opt illegale non tabu
        
        edgesX = bestIllegal2OptMove.edgesX;
        edgesY = bestIllegal2OptMove.edgesY;
        
        G.gui_model.resetColoredEdges();
		G.gui_model.addColoredEdge(bestIllegal2OptMove.edgesX, Color.RED);
		G.gui_model.addColoredEdge(bestIllegal2OptMove.edgesY, Color.GREEN);
		//try { Thread.sleep(10000); } catch (InterruptedException e) { e.printStackTrace();}
    	
    	//itero lungo uno a caso dei due cicli
    	 x3 = bestIllegal2OptMove.edgesY.get(0);//  y1;
         do {
           x3 = sol.getEdgeBefore(x3.c2);
           G.gui_model.addColoredEdge(x3, Color.YELLOW);
           //try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace();}
           
           //testo altro edge nel secondo cerchio
           x4=bestIllegal2OptMove.edgesY.get(1);//y2;
           do {
        	 x4 = sol.getEdgeBefore(x4.c2);
             G.gui_model.addColoredEdge(x4, Color.GRAY);
             y3 = new Edge(x3.c1, x4.c2);
             y4 =new Edge(x3.c2, x4.c1);
             edgesY.add(y3);
             edgesY.add(y4);
             edgesX.add(x3);
             edgesX.add(x4);
             //try { Thread.sleep(100); } catch (InterruptedException e) { e.printStackTrace();}
               if (y3.isProper()&&y4.isProper()) {
                 l.add(new LK_Move((ArrayList<Edge>)edgesX.clone(), (ArrayList<Edge>)edgesY.clone()));
                 /*GlobalData.gui_model.resetColoredEdges();
                 GlobalData.gui_model.addColoredEdge(edgesX, Color.RED);
         		 GlobalData.gui_model.addColoredEdge(edgesY, Color.GREEN);*/
         		 //try { Thread.sleep(100); } catch (InterruptedException e) { e.printStackTrace();}
               }
               edgesY.remove(3);edgesY.remove(2);
               edgesX.remove(3);edgesX.remove(2);
           } while(bestIllegal2OptMove.edgesY.get(1).c1 != x4.c2);
         } while(bestIllegal2OptMove.edgesY.get(0).c1 != x3.c2);//*/

         LK_Move[] moves = l.toArray(new LK_Move[l.size()]);
         return moves;
    }   // end getAllMoves
	
	
	
	
	
    public Move[] getAllMoves( Solution solution )
    {   
    	Edge x1,x2,x3,x4,y1,y2,y3,y4;
    	MySolutionEdges sol = (MySolutionEdges)solution;
    	sol.sincEdgesWithTour(); //synchronize the edges with the tour
    	ArrayList<LK_Move> l = new ArrayList<LK_Move>();
    	
    	ArrayList<Edge> edgesX = new ArrayList<Edge>();
    	ArrayList<Edge> edgesY = new ArrayList<Edge>();
        
    	ArrayList<Edge> available = new ArrayList<>(sol.edges);
    	
    	Random rand_generator = new Random(G.random_seed);
    	
    	//edge x1 preso a caso
    	x1 = available.remove(rand_generator.nextInt(available.size()));
    	//scelgo y1
    	int i=0;
    	y1=x1;
    	do {
    		//y1 = new Edge(x1.c2,rand_generator.nextInt(GlobalData.numCustomers));
    		y1 = new Edge(x1.c2,objFunc.vicini[x1.c2][rand_generator.nextInt(G.nVicini)]);
    		//prendo x2 (obbligato)
        	x2 = sol.getEdgeAfter(y1.c2);
        	//ricollego a x1
        	y2 = new Edge(x2.c2, x1.c1);
    	} while (!y1.isProper()||!y2.isProper()||y1.equals(x1));

    	//ho la prima 2opt illegale che genera 2 cicli
    	edgesX.add(x1);edgesX.add(x2);
        edgesY.add(y1);edgesY.add(y2);
    	
    	//itero lungo uno a caso dei due cicli
    	 x3 = y1;
    	 G.gui_model.resetColoredEdges();
         do {
           x3 = sol.getEdgeBefore(x3.c2);
           //GlobalData.gui_model.addColoredEdge(x3, Color.YELLOW);
           //testo altro edge nel secondo cerchio
           x4=y2;
           do {
             x4 = sol.getEdgeBefore(x4.c2);
             //GlobalData.gui_model.addColoredEdge(x4, Color.GRAY);
             y3 = new Edge(x3.c1, x4.c1);
             y4 =new Edge(x3.c2, x4.c2);
             edgesY.add(y3);
             edgesY.add(y4);
             edgesX.add(x3);
             edgesX.add(x4);
               if (y3.isProper()&&y4.isProper()) {
                 l.add(new LK_Move((ArrayList<Edge>)edgesX.clone(), (ArrayList<Edge>)edgesY.clone()));
               }
               edgesY.remove(3);edgesY.remove(2);
               edgesX.remove(3);edgesX.remove(2);
           } while(y2.c1 != x4.c2);
         } while(y1.c1 != x3.c2);//
    	
         //prendo una a caso:
         //LK_Move a_caso = l.get(rand_generator.nextInt(l.size()));
         
        /*GlobalData.gui_model.resetColoredEdges();
     	GlobalData.gui_model.addColoredEdge(edgesX, Color.RED);
     	GlobalData.gui_model.addColoredEdge(edgesY, Color.BLUE);
     	try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace();}
     	*/
     	//Move[] moves = new Move[1];
     	//moves[0] = a_caso; //new LK_Move((ArrayList<Edge>)edgesX.clone(), (ArrayList<Edge>)edgesY.clone());
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
    	if (edgesX.contains(e)) return false;
    	if (edgesY.contains(e)) return false;
    	/*Iterator i = edgesX.iterator();
    	while(i.hasNext()) {
    		if(i.next().equals(e)) return false;
    	}
    	i = edgesY.iterator();
    	while(i.hasNext()) {
    		if(i.next().equals(e)) return false;
    	}*/
    	
    	edgesX.add(e);
    	return true;
    }
    
    private boolean pushEdgeY(Edge e,MySolutionEdges sol) {
    	if (!e.isProper()) return false; //<- per evitare che vengano usati edge improper es (1,1)
    	
    	//non deve appartenere al tour attuale
    	if(sol.contains(e)) return false;
    	
    	//non deve appartenere agli edges in gioco:
    	//TODO: usare struttura migliore per controllare appartenenza senza cicli
    	if (edgesX.contains(e)) return false;
    	if (edgesY.contains(e)) return false;
    	/*Iterator i = edgesX.iterator();
    	while(i.hasNext()) {
    		if(i.next().equals(e)) return false;
    	}
    	i = edgesY.iterator();
    	while(i.hasNext()) {
    		if(i.next().equals(e)) return false;
    	}*/
    	
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
    	
    	if (n==G.numCustomers) return true;
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
