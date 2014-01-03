package com.oropolito.opentsSample;

import java.util.Random;

import org.coinor.opents.MoveManager;

import com.oropolito.opentsSample.GUI.GUI_model;

public class G {
	public static int numCustomers=0;
	//public static long random_seed=10;
	public static Random rand;
	
	public static int Repetitions = 1;
	public static int iteration = 0;
	public static int nVicini = 30; //numero vicini da controllare
	public static int nViciniMax = 110;
	
	public static GUI_model gui_model;
	public static boolean GUI;
	public static double[][] customers;
		
	//Fast local search:
	public static int subNeighbourhoodSize = 10;
	public static boolean[] activeNeighbourhoods;
	
	//I/O
	/*
	public static String[] NomeIstanza = new String[20];
	public static double[] TimeSpent = new double[20];
	public static double[] OptPercentage = new double[20];
	public static int[] BestValue = new int[20];
	public static int[] BestKnown = new int[20];
	public static long[] BestTime = new long[20];
	*/ 
	public static int NumIstanza=0;
	public static int NumIstOpt=0;
}