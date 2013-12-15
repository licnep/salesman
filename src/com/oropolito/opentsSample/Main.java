package com.oropolito.opentsSample;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main
{
	private enum ParamFile {
	    NONE, PARAMS, INSTANCES
	}
	
	//questi parametri vengono letti da readParams (param.txt)
	private static String P_baseDir ="./Data/TSP/";
	private static Integer P_TSMaxIter = 1000;

    public static void main (String args[]) 
    {
    	
    	readParams(args);
    	P_TSMaxIter = 1000;
    	
    	Tabu myTabu = new Tabu();
    	//myTabu.main(P_TSMaxIter,P_baseDir+"pr1002.tsp",P_baseDir+"pr1002.opt.tour");
    	
    	myTabu.main(P_TSMaxIter,P_baseDir+"berlin52.tsp",P_baseDir+"berlin52.opt.tour");
    	//myTabu.main(P_TSMaxIter,P_baseDir+"eil51.tsp",P_baseDir+"eil51.opt.tour");
    	//myTabu.main(P_TSMaxIter,P_baseDir+"eil76.tsp",P_baseDir+"eil76.opt.tour");

    	//myTabu.main(P_TSMaxIter,P_baseDir+"TSPLIB/a280.tsp",P_baseDir+"TSPLIB/a280.opt.tour");
    	//myTabu.main(P_TSMaxIter,P_baseDir+"TSPLIB/ch130.tsp",P_baseDir+"TSPLIB/ch130.opt.tour");
    	return;
	}
    
    
    public static void readParams(String args[])
	{
		ParamFile fileStatus = ParamFile.NONE;
		String myFileParam = "./Data/param.txt";
		if (args.length > 0)
		{
			myFileParam = args[0];
		}
		try
		{
			File input = new File(myFileParam);
			BufferedReader br = new BufferedReader(	new InputStreamReader(new FileInputStream(input)));
			String line;
			String token;
			StringTokenizer st;
			while ((line = br.readLine()) != null)
			{   					
				switch (fileStatus)
				{
				case NONE: 
    					st = new StringTokenizer(line);	
						token = st.nextToken();    							
    					switch (token.toUpperCase()) 
    					{
    						case "PARAMS":
    							fileStatus = ParamFile.PARAMS;
    							System.out.println("Reading the parameters");
    							break;
    						case "INSTANCES":
    							fileStatus = ParamFile.INSTANCES;
    							System.out.println("Reading the instances");
    							break;
    						case "EOF":
    							System.out.println("Close the file");
    							br.close();
    							return;
    						default:
    							break;
    					}
					break;
				case PARAMS:
					if (line.toUpperCase().contains("ENDPARAMS"))
					{
						fileStatus = ParamFile.NONE;
						System.out.println("End of parameters");
					}
					else
					{
						//Read the parameters
						st = new StringTokenizer(line);	
						token = st.nextToken();
						switch (token.toUpperCase()) {
							case "DATAFILEDIR":
								token = st.nextToken();
								P_baseDir = token + "/";
								break;
							case "TSMAXITER":
								token = st.nextToken();
								P_TSMaxIter = Integer.parseInt(token);
								break;
						}
						System.out.println(token);
					}
					break;
				case INSTANCES:
					if (line.toUpperCase().contains("ENDINSTANCES"))
					{
						System.out.println("End of instances");								
						fileStatus = ParamFile.NONE;
					}
					else
					{
						//Read the input files
					}
					break;
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error "+e.getMessage());
	        System.exit(-1);
		}    			
	}

}

