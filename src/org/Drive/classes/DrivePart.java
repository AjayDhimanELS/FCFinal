package org.Drive.classes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.Gmail.classes.MailCreation;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.Permission;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.script.Script;
import com.google.api.services.script.model.CreateProjectRequest;
import com.google.api.services.script.model.ExecutionRequest;
import com.google.api.services.script.model.Operation;
import com.google.api.services.script.model.Project;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

public class DrivePart {
	public static File checkDriveForSpreadSheet(Drive service, String userName, String excelFolder) throws IOException {
	
//|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||		
		//|||||||||||  Checking the drive for the file && returns the file if found, else null  ||||||||||||||||||||||||
//|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
		System.out.println("Calling CheckDriveForSpreadSheet!!");
	String pageToken = null;
	do {
	  FileList result = service.files().list()
	      .setQ("parents in '"+excelFolder+"' and name contains '"+userName+"' and trashed = false")
	      .setSpaces("drive")
	      .setFields("nextPageToken, files(id, name)")
	      .setPageToken(pageToken)
	      .execute();
	  
	  for (File file : result.getFiles()) {
	    if (file.getName().equalsIgnoreCase(userName)) 
    	{
	    	System.out.println("File exists already for "+userName);
	    	return file;
    	}
	  }
	  pageToken = result.getNextPageToken();
	} while (pageToken != null);
	System.out.println("File doesn't exist!!");
//|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
//|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
	return null;
}


	public static void drive(Drive service, Sheets sservice, Script scservice, List<List<Object>> header, String userName, Entry<String, List<Object>> val, String bCode, Gmail gservice, List<String> errorFiles, String sdate, String department, String bCode2) throws Exception {
		System.out.println("Calling Drive!!");
		String excelFolder = "10jr0yIgPXajMc_IWs2w_PeqHViBk7BCR";
		int lastRow = 0;
		File theFile = checkDriveForSpreadSheet(service,userName,excelFolder);
		if(theFile == null)
		{
			File fileMetadata = new File();
			fileMetadata.setName(userName);
			fileMetadata.setMimeType("application/vnd.google-apps.spreadsheet");
			fileMetadata.setParents(Collections.singletonList(excelFolder));			
			
			theFile = service.files().create(fileMetadata)
						.setFields("id")
						.execute();
			
			System.out.println("Created file ID: " + theFile.getId()+", and File Name: " + theFile.getName());
			
			//Writing headers into the newly created google sheet.
			List<Object> header_lists = new ArrayList<>();
			
  		  header_lists.add("Book Code");
  		  
  		  header_lists.add(header.get(0).get(40).toString()); //DateOfAssigning
  		  
  		  header_lists.add(header.get(0).get(41).toString()); //DateOfReceiving
  		  
  		  header_lists.add(header.get(0).get(0).toString()); //QuesCode
  		  
  		  header_lists.add(header.get(0).get(1).toString()); //Sol_ID
  		  
  		  header_lists.add(header.get(0).get(18).toString()); //Accuracy(L-1)
  		  
  		  header_lists.add(header.get(0).get(19).toString()); //Template(L-1)
  		  
  		  header_lists.add(header.get(0).get(20).toString()); //Compliance(L-1)
  		  
  		  header_lists.add(header.get(0).get(21).toString()); //L&C_Score
  		  
  		  header_lists.add(header.get(0).get(22).toString()); //S/F_Score
  		  
  		  header_lists.add(header.get(0).get(34).toString()); //G/S_Score
  		  
  		  header_lists.add("Date Of Report"); 
			  
			  List<List<Object>> header_write = new ArrayList<>();
			  header_write.add(header_lists);
//		     System.out.println("\n\nWriting Headers++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n\n");  
			 ValueRange body = new ValueRange()
		                .setValues(header_write);
		        UpdateValuesResponse result =
		                sservice.spreadsheets().values().update(theFile.getId(), "Sheet1!A1:L1", body)
		                        .setValueInputOption("RAW")
		                        .execute();
		        System.out.printf("%d cells updated with Headers", result.getUpdatedCells());
		        		
		}
		
//|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
			//||||||||||||||||||||||  We have Google SpreadSheet(existing or new) till here  ||||||||||||||||||||||||||||||
//|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
		
		//Reading Values here(for the lastRow)
		String theId= theFile.getId().toString();
		final String spreadsheetId = theId;
        final String range = "Sheet1!A1:L";
        int i = 0;
        ValueRange response = sservice.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        List<List<Object>> values = response.getValues();
        if (values == null || values.isEmpty()) {
            System.out.println("\nNo data found.");
            lastRow = 0;
        } 
        else 
        {
            for (List row : values) {
            	++lastRow;
            }
        }
//        System.out.println("The data we want: "+values+", The last row is : "+lastRow+" , so we should start at : "+(lastRow+1));
       
//|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
			//|||||||||||||||||||||||||||||||| Writing Data Values here ||||||||||||||||||||||||||||||||||||||||||||||||
//|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
    

//@@@@@@@@@@@@@@@@@@@@@@@@@@@@  The following prints all the data(A2:BH) perfectly  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//        Iterator iter = ((Iterable<String>) val).iterator(); //to iterate over the val's value.
//
//        int loopVar = 0;
//       
//        List<Object> lists = new ArrayList<>();
//       while(iter.hasNext())
//       {
//    	   String loopValue = iter.next().toString();
//    	   lists.add(loopValue.toString());
//    	   loopVar++;
//       }
//       List<List<Object>> write = new ArrayList<>();
//       write.add(lists);
//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
       
//@@@@@@@@@@@@@@@@@@@@@@@@@@@  The following prints the specific row-wise data perfectly  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//      ArrayList al1 = new ArrayList();
//		al1 = (ArrayList) val;
//		
//		List<Object> lists = new ArrayList<>();
//		
//		  lists.add(bCode);
//		  lists.add(al1.get(40).toString());
//		  lists.add(al1.get(41).toString());
//		  lists.add(al1.get(0).toString());
//		  lists.add(al1.get(1).toString());
//		  lists.add(al1.get(18).toString());
//		  lists.add(al1.get(19).toString());
//		  lists.add(al1.get(20).toString());
//		  lists.add(al1.get(21).toString());
//  		  lists.add(al1.get(22).toString());
//		  lists.add(al1.get(34).toString());
//		  lists.add(dateRange);
//		  
//		  List<List<Object>> write = new ArrayList<>();
//	       write.add(lists);
//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        	
        ArrayList al2 = new ArrayList();
		al2 = (ArrayList) val.getValue();
		
		  List<List<Object>> write = new ArrayList<>();
		  List<String> theURLArray = new ArrayList<>();
			
//		System.out.println("This is what we get from key-value pairs : "+al2);
	        System.out.println(errorFiles.size());
	        String url = null;
		for(int l=0; l <al2.size(); l++) 
		{
		
			ArrayList al1 = new ArrayList();
			al1 = (ArrayList) al2.get(l);

			for(String errorf : errorFiles)
			{
				if(al1.get(0).toString().equals(errorf))
				{
					System.out.println("\n---------Calling the Script task for file : "+errorf+"------------");
					url = theScriptTask(scservice,userName,sdate,errorf,department,bCode2);
					theURLArray.add(url);
				}
			}
			List<Object> lists = new ArrayList<>();
			
			  lists.add(bCode);
			  lists.add(al1.get(40).toString());
			  lists.add(al1.get(41).toString());
			  lists.add(al1.get(0).toString());
			  lists.add(al1.get(1).toString());
			  
			  //check if L-1 or L-2
			  
			  if(al1.get(24).toString().equalsIgnoreCase("Pass")) //L1
			  {
				  lists.add(al1.get(18).toString());
				  lists.add(al1.get(19).toString());
				  lists.add(al1.get(20).toString());
				  lists.add(al1.get(21).toString());
		  		  lists.add(al1.get(22).toString());
			  }
			  else //L2
			  {
				  lists.add(al1.get(27).toString());
				  lists.add(al1.get(28).toString());
				  lists.add(al1.get(29).toString());
				  lists.add(al1.get(30).toString());
				  lists.add(al1.get(31).toString());
			  }
			  
			  //check close
	  		  
	  		  lists.add(al1.get(34).toString());
			  lists.add(sdate);
		      
			  write.add(lists);//final value writing
			
		}

//		System.out.println("\\\\\\\\\\\\\\\\\\\\\\\\\\\\"+theURLArray);
       //-------------------------------------------------------------------------------------------------------------------------
       		//-------------------------- setting the Values Format to write into the sheet ----------------------------
       //-------------------------------------------------------------------------------------------------------------------------
        ValueRange body = new ValueRange()
                .setValues(write);
        UpdateValuesResponse result =
                sservice.spreadsheets().values().update(theId, "Sheet1!A"+(++lastRow)+":L", body)
                        .setValueInputOption("RAW")
                        .execute();
        System.out.printf("%d cells updated with data", result.getUpdatedCells());
        //------------------------------------------------------------------------------------------------------------------------
        //------------------------------------------------------------------------------------------------------------------------
        
        
//::::::::::  for Error Files (Storing all the files in a List<List<Object>> and checking if the row's qcode matches the file in drive)   :::::::::
       
        //setting permissions for the error files.
//        List<String> attachments = new ArrayList<>();
//        String pageToken = null;
//        String fileLink="";
//        do {
//        FileList filesInFolder = service.files().list()
//        	    		  .setQ("'"+folder+"' in parents and mimeType != 'application/vnd.google-apps.folder' and trashed = false")
//        	    		  .setSpaces("drive")
//        	    		  .setFields("nextPageToken, files(id, name, parents,webViewLink, mimeType)")
//        	    		  .setPageToken(pageToken)
//        	    		  .execute();
//        	    System.out.println("This is what we get : "+filesInFolder);
//        	    for (File fileinFolder: filesInFolder.getFiles())
//        	    {
//        	    	System.out.println("Yep Here!! First for");
//        	    	for(String row: errorFiles)
//        	    	{
//        	    		System.out.println("Yep Here!! Second for");
//        	    		System.out.println("Checking "+row+".docx  and "+fileinFolder.getName().toString());
//        	    		if((row+".docx").equalsIgnoreCase(fileinFolder.getName().toString()))
//	    				{
//        	    			System.out.println("Found an Error file : "+fileinFolder.getName().toString());
//	    					Permission userPermission = new Permission()
//            	    		            .setType("user")
//            	    		            .setRole("reader")
//            	    		            .setEmailAddress("ajay.dhiman@evelynlearning.com");
//            	    			service.permissions().create(fileinFolder.getId().toString(), userPermission)
//            	    		            .setFields("id")
//            	    		            .execute();
//                		            
//        	    			System.out.println("This is the webLink for error file : "+fileinFolder.getWebViewLink().toString());
//        	    			attachments.add(fileinFolder.getWebViewLink().toString()); 			
//	    				}
//        	    	}
//        	    }
//        	    pageToken = filesInFolder.getNextPageToken();
//        } while (pageToken != null);
        

//::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
              
        
        
    //------------------------------------------------------------------------------------------------------------------------
    //----------------------------  Creating Mails with the required cells  -----------------------------------
        
        String to = "ajay.dhiman@evelynlearning.com";  		//have to change this
//        String from = "ajay.dhiman@evelynlearning.com";		//have to change this
        
        String subject = "Feedback on text book solutions";
        String bodyText = "Dear "+userName.split("@")[0]+",<br/><br/>"
        		+ "We would like to share the feedback on some solutions done by you. <br/>"
        		+"<table style='border-collapse: collapse;width:0px;'>"
        		+ "<tbody style='vertical-align:middle;'><tr style='height:12px'>"	
        			+ "<tr style='height:12px'>"
		        		+ "<th style='border: 1px solid black;padding: 2px 3px;text-align:center;white-space: nowrap;'>Book Code</th>"
		        		+ "<th style='border: 1px solid black;padding: 2px 3px;text-align:center;white-space: nowrap;'>Date of Assigning</th>"
		        		+ "<th style='border: 1px solid black;padding: 2px 3px;text-align:center;white-space: nowrap;'>Date of Recieving</th>"
		        		+ "<th style='border: 1px solid black;padding: 2px 3px;text-align:center;white-space: nowrap;'>Ques Code</th>"
		        		+ "<th style='border: 1px solid black;padding: 2px 3px;text-align:center;white-space: nowrap;'>Solution Id</th>"
		        		+ "<th style='border: 1px solid black;padding: 2px 3px;text-align:center;white-space: nowrap;'>Accuracy</th>"
		        		+ "<th style='border: 1px solid black;padding: 2px 3px;text-align:center;white-space: nowrap;'>Template</th>"
		        		+ "<th style='border: 1px solid black;padding: 2px 3px;text-align:center;white-space: nowrap;'>Compliance</th>"
		        		+ "<th style='border: 1px solid black;padding: 2px 3px;text-align:center;white-space: nowrap;'>L&C_Score</tH>"
		        		+ "<th style='border: 1px solid black;padding: 2px 3px;text-align:center;white-space: nowrap;'>S/F_Score</th>"
		        		+ "<th style='border: 1px solid black;padding: 2px 3px;text-align:center;white-space: nowrap;'>G/S_Score</th>"
		        		+ "<th style='border: 1px solid black;padding: 2px 3px;text-align:center;white-space: nowrap;'>Date of Report</th>"
	        		+ "</tr>";
        		for(List row : write)
        		{	
        			bodyText+= "<tr style='height:12px;'>";
        			for(Object col: row)
        			{
        				bodyText+= "<td style='border: 1px solid black;padding: 2px 3px;text-align:center;white-space: nowrap;'>"+col.toString()+"</td>";
        			}
        			bodyText+= "</tr>";
        		}
        		bodyText+= "</tbody>"
        				+ "</table>"
        				+ "<br/><br/>";
        		if(!theURLArray.isEmpty())
        		{
        			bodyText += "Also, we have attached the files finalized by our team for these solutions, these files will give you an idea on our expected\r\n" + 
        					"output. <br/>";
        			for (String urls : theURLArray)
	        		{	
	        			if(urls!=null) 
		        		{
		        			bodyText += urls + "<br/>";
		        			System.out.println("The url : "+urls);
		    			}
	        		}
        		}
        		bodyText+="<br/>We hope this will help us to improve the quality of solutions.<br/>" + 
        				"Please get back to us if you have any query.<br/><br/>" + 
        				"Best Wishes,<br/>" + 
        				"Team Evelyn.";
        		System.out.println("Ending here for now!!");
        MailCreation.createEmail(gservice ,to,subject,bodyText);	
    //------------------------------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------------------------
	}
	
	private static String theScriptTask(Script scservice, String userName, String sdate, String errorf,String department, String bCode2) throws IOException {
//----------------   Calling ScriptsAPI for the errorFile task  ---------------------------------
        
//      String scriptId = "MvIeZ8KMJBfO1yYcpza91NgrbAaJpZHU8";
      String scriptId = "1FB1ZuzE6YS7hbmDssUnQ7XUaRDQUrKTP40ZZpoapcbijbAq9PvDHxkD_";
        
//        for(String error: errorFiles)
//        {
//        	Object p = errorf;
        	List<Object> param = new ArrayList<>();
        	param.add(errorf);
        	param.add(sdate);
        	param.add(department);
        	param.add(bCode2);
        	ExecutionRequest request = new ExecutionRequest()
            		.setParameters(param)
                    .setFunction("myFunction");

         // Make the API request.
            Operation op = scservice.scripts()
            				.run(scriptId, request)
            				.execute();

            // Print results of request.
            if (op.getError() != null) {
                // The API executed, but the script returned an error.
                System.out.println(getScriptError(op));
            } 
            else {
            	System.out.println("\n\nthis is what we get from the call : "+op.getResponse().get("result")+"for user : "+userName+"\n\n\n\n");
            	String openingURL = (String) op.getResponse().get("result");
            	return openingURL;
            }
			return null;
            	
//            }
        
        //-------------------------------------------------------------------------------------

	}

	public static String getScriptError(Operation op) {
	    if (op.getError() == null) {
	        return null;
	    }

	    // Extract the first (and only) set of error details and cast as a Map.
	    // The values of this map are the script's 'errorMessage' and
	    // 'errorType', and an array of stack trace elements (which also need to
	    // be cast as Maps).
	    Map<String, Object> detail = op.getError().getDetails().get(0);
	    List<Map<String, Object>> stacktrace =
	            (List<Map<String, Object>>)detail.get("scriptStackTraceElements");

	    java.lang.StringBuilder sb =
	            new StringBuilder("\nScript error message: ");
	    sb.append(detail.get("errorMessage"));
	    sb.append("\nScript error type: ");
	    sb.append(detail.get("errorType"));

	    if (stacktrace != null) {
	        // There may not be a stacktrace if the script didn't start
	        // executing.
	        sb.append("\nScript error stacktrace:");
	        for (Map<String, Object> elem : stacktrace) {
	            sb.append("\n  ");
	            sb.append(elem.get("function"));
	            sb.append(":");
	            sb.append(elem.get("lineNumber"));
	        }
	    }
	    sb.append("\n");
	    return sb.toString();
	}
}

