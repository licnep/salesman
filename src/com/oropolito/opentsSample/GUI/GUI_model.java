package com.oropolito.opentsSample.GUI;

import java.util.Observable;

public class GUI_model extends Observable {
	private double[][] customers; //lista dei clienti, ogniuno 2 coordinate double X,Y
	private int[] tour_current,tour_optimal;
	
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
