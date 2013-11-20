package com.oropolito.opentsSample;
import java.beans.Customizer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.lang.String;
import java.lang.Integer;
import java.util.Collections;
import java.util.Comparator;

import org.coinor.opents.*;

import com.oropolito.opentsSample.GUI.GUI_model;
import com.oropolito.opentsSample.GUI.GUI_view;

public class Main
{
	private enum ParamFile {
	    NONE, PARAMS, INSTANCES
	}
	

    public static void main (String args[]) 
    {
    	Tabu myTabu = new Tabu();
    	myTabu.main(args);
    	return;
	}    
}

