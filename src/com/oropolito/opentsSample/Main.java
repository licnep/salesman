package com.oropolito.opentsSample;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main
{
	private enum ParamFile {
	    NONE, PARAMS, INSTANCES
	}
	
	//questi parametri vengono letti da readParams (param.txt)
	private static String P_baseDir;
	private static String[] InputName = new String[6];
	private static Integer P_TSMaxIter;
	private static Integer NumIstances = 0;

    public static void main (String args[]) 
    {
    	
    	readParams(args);
    	
    	Tabu myTabu = new Tabu();
    	
    	double TotalTime = 0;
    	
    	double startTime = 0;
    	
    	double timespent = 0;
    	
    	for(int n=0; n<NumIstances; n++){
    		
    		startTime = System.currentTimeMillis();
        	
        	myTabu.main(P_TSMaxIter,P_baseDir,InputName[n]);
        	
        	timespent = (System.currentTimeMillis()-startTime)/1000.0;
        	
        	System.out.println("Tempo impiegato: "+ timespent+ "s");
        	System.out.println("");
        	System.out.println("");
        	
        	G.TimeSpent[G.NumIstanza] = timespent;
        	G.NumIstanza++;
        	TotalTime += timespent;
    		
    	}
    	
    	//OUTPUT RESULTS
    	double MeanTime = 0;
    	double MeanOpt = 0;
    	for(int i=0; i<G.NumIstanza; i++){
        	MeanOpt = G.OptPercentage[i] + MeanOpt;
        	MeanTime = G.TimeSpent[i] + MeanTime;
        }
    	try {
            File file = new File("./Data/Output/"+"Results.csv");
            BufferedWriter output = new BufferedWriter(new FileWriter(file));
            output.write("Instance,Best sol,Best known,Optimality(%),Time(s)\n");
            for(int i=0; i<G.NumIstanza; i++){
            	output.write(G.NomeIstanza[i]+","+G.BestValue[i]+","+G.BestKnown[i]+","+G.OptPercentage[i]+","+G.TimeSpent[i]+"\n");
            }
            output.write("Mean, , ,"+(MeanOpt/G.NumIstOpt)+","+(MeanTime/G.NumIstanza)+"\n");
            output.write("Total, , , ,"+TotalTime+"\n");
            output.close();
          } catch ( IOException e ) {
             e.printStackTrace();
          }
    	
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
    							break;
    						case "INSTANCES":
    							fileStatus = ParamFile.INSTANCES;
    							break;
    						case "EOF":
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
							case "GUI":
								token = st.nextToken();
								G.GUI = Boolean.parseBoolean(token);
								break;
						}
					}
					break;
				case INSTANCES:
					if (line.toUpperCase().contains("ENDINSTANCES"))
					{								
						fileStatus = ParamFile.NONE;
					}
					else
					{
						//Read the input file
						st = new StringTokenizer(line);	
						token = st.nextToken();
						InputName[NumIstances] = token;
						InputName[NumIstances] = InputName[NumIstances].substring(0, InputName[NumIstances].length() - 4);
						NumIstances++;
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