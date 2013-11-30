package com.oropolito.opentsSample.GUI;

import info.monitorenter.gui.chart.Chart2D;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JPanel;

import sun.java2d.pipe.SolidTextRenderer;

import com.oropolito.opentsSample.GlobalData;

public class GUI_view implements Observer, MouseWheelListener {
	private GUI_model m;
	private double zoom = 1300;
	private JPanel c;

	//costruttore
	public GUI_view(GUI_model mm) {
		this.m = mm;
		m.addObserver(this);
		
		JFrame f = new JFrame();
	    f.setSize(1200, 800);
	    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    JPanel main_panel = new JPanel();
	    f.add(main_panel);
		
		c = new JPanel() {
			public void paint(Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				int w=getWidth();
			    int h=getHeight();
			    double z = (m.area_size*(zoom/1000));
			    g2.setPaint(Color.white);
			    g2.fill(new Rectangle2D.Double(0,0,w,h));
			    //g2.fill(new RoundRectangle2D.Double(10,10,w-20,h-20,10,10));
				g2.setPaint(Color.blue);
				//disegno i pallini dei customers
				double customers[][] = m.getCustomers();
				for (int i=0;i<GlobalData.numCustomers;i++) {
					g2.fill(new Ellipse2D.Double(customers[i][0]*w/z-3, customers[i][1]*h/z-3, 6, 6));
				}
				//disegno il percorso ottimale
				g2.setPaint(Color.green);
				if (m.getTour_optimal()!=null) {
					for (int i=0;i<GlobalData.numCustomers;i++) {
						int a = m.getTour_optimal()[(i==0)? GlobalData.numCustomers-1 : i-1];
						int b = m.getTour_optimal()[i];
						g2.draw(new Line2D.Double(customers[a][0]*w/z, customers[a][1]*h/z, customers[b][0]*w/z, customers[b][1]*h/z));
					}
				}
				//disegno il percorso trovato
				g2.setPaint(Color.black);
				if (m.getTour_current()!=null) {
					for (int i=0;i<GlobalData.numCustomers;i++) {
						//int a = m.getTour_current()[i-1];
						int a = m.getTour_current()[(i==0)? GlobalData.numCustomers-1 : i-1];
						int b = m.getTour_current()[i];
						g2.draw(new Line2D.Double(customers[a][0]*w/z, customers[a][1]*h/z+3, customers[b][0]*w/z, customers[b][1]*h/z+3));
					}
				}
			}
		};
		c.addMouseWheelListener(this);
		c.setPreferredSize(new Dimension(500,500));
		main_panel.add(c);
		
		//grafico:
		Chart2D chart = new Chart2D();
		chart.addTrace(m.trace);
		chart.setPreferredSize(new Dimension(500, 500));
		main_panel.add(chart);
		
		f.setVisible(true);
	}
	
	@Override
	public void update(Observable source, Object arg) {
		c.repaint();
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		this.zoom += e.getUnitsToScroll()*10;
		c.repaint();
	}

}
