package com.oropolito.opentsSample;

import java.util.Random;

import org.coinor.opents.MoveManager;

import com.oropolito.opentsSample.GUI.GUI_model;

public class GlobalData {
	public static int numCustomers=0;
	public static long random_seed=10;
	
	public static int notImprovingCounter = 0;
	public static int ImprovingCounter = 0;
	public static int iteration = 0;
	public static int nVicini = 30; //numero vicini da controllare
	public static int nViciniMax = 60;
	
	public static GUI_model gui_model;
	public static boolean GUI = false;
	public static double[][] customers;
	
	public static Random rand;
	public static int iterazioni3Opt;
	
	public static boolean perturbate = false;
	
	//==
	public static int MAX_TENURE = 0;
	public static int MIN_TENURE = 0;
}
