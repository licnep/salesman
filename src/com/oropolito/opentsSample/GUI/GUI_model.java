package com.oropolito.opentsSample.GUI;

import info.monitorenter.gui.chart.ITrace2D;
import info.monitorenter.gui.chart.traces.Trace2DLtd;
import info.monitorenter.gui.chart.traces.Trace2DSimple;

import java.util.Observable;

public class GUI_model extends Observable {
	private double[][] customers; //lista dei clienti, ogniuno 2 coordinate double X,Y
	private int[] tour_current,tour_optimal;
	public double area_size = 1000; //circa la massima x della citta piu' lontana
	private int current_iteration = 0;
	public ITrace2D trace; 
	
	public GUI_model() {
		//trace = new Trace2DLtd();
		trace = new Trace2DSimple("Optimality");
	}
	
	public void updateOptimality(double opt) {
		trace.addPoint(current_iteration, opt);
		current_iteration++;
	}
	
	//getters and setters:
	
	public double[][] getCustomers() {
		return customers;
	}

	public void setCustomers(double[][] customers) {
		this.customers = customers;
		this.setChanged();
		this.notifyObservers("setCustomers");
	}
	//new double[numCustomers][2];

	public int[] getTour_optimal() {
		return tour_optimal;
	}

	public void setTour_optimal(int[] tour_optimal) {
		this.tour_optimal = tour_optimal;
		this.setChanged();
		this.notifyObservers("setTour_optimal");
	}

	public int[] getTour_current() {
		return tour_current;
	}

	public void setTour_current(int[] tour_current) {
		this.tour_current = tour_current;
		this.setChanged();
		this.notifyObservers("setTour_current");
	}
}
