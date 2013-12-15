package com.oropolito.opentsSample;

import com.oropolito.opentsSample.GUI.GUI_model;

public class GlobalData {
	public static int numCustomers=0;
	public static long random_seed=121;
	
	public static int notImprovingCounter = 0;
	public static int ImprovingCounter = 0;
	public static int iteration = 0;
	public static int nVicini = 10; //numero vicini da controllare

	public static GUI_model gui_model;
	public static double[][] customers;
}
