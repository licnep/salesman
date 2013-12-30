package com.oropolito.opentsSample;

import java.util.Random;

import org.coinor.opents.MoveManager;

import com.oropolito.opentsSample.GUI.GUI_model;

public class G {
	public static int numCustomers=0;
	public static long random_seed=10;
	
	public static int notImprovingCounter = 0;
	public static int ImprovingCounter = 0;
	public static int iteration = 0;
	public static int nVicini = 30; //numero vicini da controllare
	public static int nViciniMax = 110;
	
	public static GUI_model gui_model;
	public static boolean GUI = true;
	public static double[][] customers;
	
	public static Random rand;
	public static int iterazioni3Opt;
	
	public static boolean perturbate = false;
	
	//Fast local search:
	public static int subNeighbourhoodSize = 10;
	public static boolean[] activeNeighbourhoods;
	
	//==
	public static int MAX_TENURE = 0;
	public static int MIN_TENURE = 0;
}
