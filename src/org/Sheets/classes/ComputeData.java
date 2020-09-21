package org.Sheets.classes;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.Drive.classes.DrivePart;

import com.google.api.services.drive.Drive;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.script.Script;
import com.google.api.services.sheets.v4.Sheets;

public class ComputeData {

	public static void computeDataForCT(Drive dservice, Gmail gservice, Sheets sservice, Script scservice, List<String> reqBookCodes, String id, String bCode, String sdate, String department, String bCode2) throws Exception {
			
		//dateArray is the data from the sheet.
		List<List<Object>> dataArray = new ArrayList<>();
//		System.out.println("Calling For Data!!");
		dataArray = GetData.forData(sservice,id,bCode);
		
		//header is the Header cells of the Sheet.
		List<List<Object>> header = new ArrayList<>();
//		System.out.println("Calling For Headers!!");
		header = GetData.forHeaders(sservice,id,bCode);

	    List<Map<String,List<Object>>> list = new ArrayList<Map<String,List<Object>>>();//This is the final list we need
		Map<String, List<Object>> map1 = new HashMap<String, List<Object>>();//This is one instance of the  map we want to store in the above list.
	    List<String> errorFiles = new ArrayList<>();
		
	    for(String har : reqBookCodes) 
	    {
//	    	System.out.println("we are in with "+har);
		    for (int i = 0; i < dataArray.size(); i++) 
			{
//		    	System.out.println("we are in-in with "+har+" and "+dataArray.get(i).get(0));
		    	if(dataArray.get(i).get(0).toString().equalsIgnoreCase(har))
		    	{
//					System.out.println("We are in for loop and the cordinator is : "+dataArray.get(i).get(42)+"---------------------------------------------------");
					if(!dataArray.get(i).get(42).equals(""))//coordinator email-id
					{	
//						System.out.println("the coordinator email is not empty ---------------------------------------------------");
		
						if(dataArray.get(i).get(8).toString().equalsIgnoreCase("CT")) //8-CT/IHT 
						{
//							System.out.println("We are in second 'pass' condition---------------------------------------------------");
		
//							if(dataArray.get(i).get(17).toString()=="")continue;
							if(dataArray.get(i).get(24).toString().equalsIgnoreCase("Pass") || dataArray.get(i).get(33).toString().equalsIgnoreCase("Pass"))   //24- L-1,Status  33- L-2,Status
							{ 
								String name = dataArray.get(i).get(42).toString();
		//						System.out.println("We are here!!---------------------------------------------------");
		//						System.out.println("We are checking for the name: "+name);
								//--------------------------------
								String level = "";
								level = dataArray.get(i).get(24).toString().equalsIgnoreCase("Pass") ? "L1" : "L2" ;
								System.out.println("Sending data for "+level+" for ques code: "+dataArray.get(i).get(0).toString());
						        //--------------------------------
								if (!map1.containsKey(name)) {
								    List<Object> arraylist1 = new ArrayList<Object>();
		//				        	System.out.println("Name key not found : "+name);
						        	arraylist1.add(dataArray.get(i));
//						        	arraylist1.add(level);
						        	map1.put(name,arraylist1);
						        	list.add(map1);
		//				        	System.out.println("Name key just added for : "+name+", now: "+map1.get(name));
						        }
						        else 
						        {
		//				        	System.out.println("Name key already found : "+map1.get(name));
						        	map1.get(name).add(dataArray.get(i));
//						        	map1.get(name).add(level);
		//				        	System.out.println("Another value just added for : "+name+", now: "+map1.get(name));
						        }
								//error checking 
						        if(level=="L1")
								{
						        	if(dataArray.get(i).get(23).toString().equalsIgnoreCase("Poor")) {  //L-1 Rating and L-2 Rating						        {
						        		errorFiles.add(dataArray.get(i).get(0).toString());
						        	}
								}
						        else if(level=="L2") 
						        {
							        if(dataArray.get(i).get(32).toString().equalsIgnoreCase("Poor"))
						        	{
						        		errorFiles.add(dataArray.get(i).get(0).toString());
						        	}
						        }
								
							}//L1 or L2 check-if
						}
					}//last-if	
				}
			}//for
	    }
	    
//	    System.out.println("The list is here : "+list);
		
		File yourFile =  null;
		for (Map<String, List<Object>> map : list) {
		    for (Entry<String, List<Object>> entry : map.entrySet()) {
		        String userName = entry.getKey();
		        List<Object> value = entry.getValue();
//		        System.out.println("\nUser : "+userName);
//		         for(Object val: value) {
//		        	System.out.println("\n\n"+val);
	        	    System.out.println("\n\n----------------------Calling creatSheet("+userName+","+entry+")--------------------\n\n");
					DrivePart.drive(dservice,sservice,scservice, header, userName.toString(),entry,bCode,gservice,errorFiles,sdate,department,bCode2);
//	            }    
	    	}
		    break;
		    }
		System.out.println("This is the list of errorFiles : "+errorFiles);
	    
	}
}
