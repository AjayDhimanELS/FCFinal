package org.Servlets;


import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.Drive.classes.DriveServiceUtil;
import org.Gmail.classes.GmailServiceUtil;
import org.Scripts.classes.ScriptsServiceUtil;
import org.Sheets.classes.ComputeData;
import org.Sheets.classes.GetData;
import org.Sheets.classes.SheetsServiceUtil;

import com.google.api.services.drive.Drive;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.script.Script;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.Spreadsheet;

/**
 * Servlet implementation class ConnectionOnButtonClick
 */
//@WebServlet("/ConnectionOnButtonClick")
public class ConnectionOnButtonClick extends HttpServlet {
	private static final long serialVersionUID = 1L;

   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String[][] credentials = new String[11][3];
		
		credentials[0][0] = "accounting@evelynlearning.com";
		credentials[0][1] = "accounting@EL#";
		credentials[0][2] = "Accounting";
		credentials[1][0] = "biology@evelynlearning.com";
		credentials[1][1] = "biology@EL#";
		credentials[1][2] = "Biology";
		credentials[2][0] = "business@evelynlearning.com";
		credentials[2][1] = "business@EL#";
		credentials[2][2] = "Business";
		credentials[3][0] = "chemistry@evelynlearning.com";
		credentials[3][1] = "chemistry@EL#";
		credentials[3][2] = "Chemistry";
		credentials[4][0] = "computerscience@evelynlearning.com";
		credentials[4][1] = "computerscience@EL#";
		credentials[4][2] = "Computer Science";
		credentials[5][0] = "economics@evelynlearning.com";
		credentials[5][1] = "economics@EL#";
		credentials[5][2] = "Economics";
		credentials[6][0] = "electricalengineering@evelynlearning.com";
		credentials[6][1] = "electricalengineering@EL#";
		credentials[6][2] = "Electrical Engineering";
		credentials[7][0] = "health@evelynlearning.com";
		credentials[7][1] = "health@EL#";
		credentials[7][2] = "Health";
		credentials[8][0] = "math@evelynlearning.com";
		credentials[8][1] = "math@EL#";
		credentials[8][2] = "Math";
		credentials[9][0] = "psychology@evelynlearning.com";
		credentials[9][1] = "psychology@EL#";
		credentials[9][2] = "Psychology";
		credentials[10][0] = "statistics@evelynlearning.com";
		credentials[10][1] = "statistics@EL#";
		credentials[10][2] = "Statistics";
		
		//--------getting the values of URL and dates-------
		String sdate = request.getParameter("date");
		String department = request.getParameter("department");
		String sheetURL = request.getParameter("sheetURL");
		String subURL = request.getParameter("submissionURL");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		//---------------------------------------------
		System.out.println("Entered Department : "+department);
		System.out.println("Entered Email : "+email);
		System.out.println("Entered Password : "+password);
		
		if(email.isEmpty() || password.isEmpty())
		{
			System.out.println("Credentials Empty");
			String error = "<div class=\"alert alert-danger\" role=\"alert\">\r\n" + 
					"  <h6 class=\"alert-heading text-center\">Credentials Empty! Try Again.</h6>\r\n" +
					"</div>";
			request.setAttribute("message", error);
			request.getRequestDispatcher("/index.jsp").forward(request, response);
			return;
		}
		else if(sdate.isEmpty() || department.isEmpty() || sheetURL.isEmpty() || subURL.isEmpty()) 
		{
			System.out.println("Empty Values!!");
			String error = "<div class=\"alert alert-danger\" role=\"alert\">\r\n" + 
					"  <h6 class=\"alert-heading text-center\">Empty input fields! Try Again.</h6>\r\n" +
					"</div>";
			request.setAttribute("message", error);
			request.getRequestDispatcher("/index.jsp").forward(request, response);
			return;
		}
		else
		{
			for(int i =0; i<credentials.length; i++)
			{
//				System.out.println("checking for email: "+credentials[i][0]+" and password: "+credentials[i][1]+" with inputs: "+email+" and "+password);
				if(credentials[i][0].equalsIgnoreCase(email) && credentials[i][1].equalsIgnoreCase(password) && credentials[i][2].equalsIgnoreCase(department))
				{
					System.out.println("We are in!");
					gettingStarted(request,response,sdate,department,sheetURL,subURL);
					break;
				}
			}
//			System.out.println("Wrong Values!!");
//			String error = "<div class=\"alert alert-danger\" role=\"alert\">\r\n" + 
//					"  <h6 class=\"alert-heading text-center\">Wrong Credentials! Try Again.</h6>\r\n" +
//					"</div>";
//			request.setAttribute("message", error);
//			request.getRequestDispatcher("/index.jsp").forward(request, response);
//			return;		
		}
	}
	
	public static void gettingStarted(HttpServletRequest request, HttpServletResponse response, String sdate, String department, String sheetURL, String subURL) throws ServletException, IOException
	{
		
		Sheets sservice=null;
		Drive dservice=null;
		Gmail gservice=null;
		Script scservice=null;
		try {
			sservice = SheetsServiceUtil.setup(request);
			dservice = DriveServiceUtil.setup(request);
			gservice = GmailServiceUtil.setup(request);
			scservice = ScriptsServiceUtil.setup(request);
		} catch (GeneralSecurityException | IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Auth Error");
			String error = "<div class=\"alert alert-danger\" role=\"alert\">\r\n" + 
					"  <h6 class=\"alert-heading text-center\">Authorization Error!! Try Again.</h6>\r\n" +
					"</div>";
			request.setAttribute("message", error);
			request.getRequestDispatcher("/index.jsp").forward(request, response);
			e.printStackTrace();
			return;
		}
		
		//----------splitting the id of the URL-------------
			String remaining = sheetURL.split("d/")[1];
			String id = remaining.split("/edit")[0];
			
			String remainingSec = subURL.split("d/")[1];
			String subId = remainingSec.split("/edit")[0];
		//--------------------------------------------------
		System.out.println("Calling with id:" +id);
		String bCode = getBookCode(sservice,id,0);
		System.out.println("Calling with id:" +subId);
		String subBCode = getBookCode(sservice,subId,2);
		if(bCode.isEmpty() || subBCode.isEmpty())
		{
			System.out.println("Wrong Ids");
			String error = "<div class=\"alert alert-danger\" role=\"alert\">\r\n" + 
					"  <h6 class=\"alert-heading text-center\">The entered URLs are not correct.</h6>\r\n" +
					"</div>";
			request.setAttribute("message", error);
			request.getRequestDispatcher("/index.jsp").forward(request, response);
			return;
		}
		else if(bCode.equalsIgnoreCase(subBCode))  
		{
//||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
			//|||||||||||||||||||||||||||   Here is the Main Code   |||||||||||||||||||||||||||||||||||||||||||||||||
			
			List<List<Object>> subData  = new ArrayList<>();
			List<String> reqBookCodes = new ArrayList<>();
			
			subData = GetData.forSubmission(sservice, subId, bCode);
//			System.out.println("\nThis is what we get from submission sheet... \n\n");
			
			//have to increment the date
//			System.out.println("This is the date : "+sdate);
//			String day = sdate.substring(sdate.length()-2,sdate.length());
//			System.out.println("Substring : "+day+" and sdate: "+sdate);
//			int theday = Integer.parseInt(day);
//			theday++;
//			
////			int length = String.valueOf(theday).length();
////			theday = length==1 ? Integer.parseInt(Integer.toString(0)+Integer.toString(theday)) : theday;
//			sdate = sdate.substring(0,sdate.length()-2);
//			sdate = sdate+Integer.toString(theday);
			
			//-------------------------------------------
			System.out.println("The date : "+sdate);
			Date subDate = null;
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
	        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		
	        try{subDate = df.parse(sdate);} 
			catch (ParseException e1) {e1.printStackTrace();};
			
			try 
			{
				for(List list: subData)
				{
					if(sdf.parse(list.get(2).toString()).equals(subDate))
					{
						reqBookCodes.add(list.get(0).toString());
					}
				} 
			}
			catch (ParseException e) {
				e.printStackTrace();
			}
			
			System.out.println("What we got : "+reqBookCodes);
			try {
//				System.out.println("Calling computeDataForCT!!");
				ComputeData.computeDataForCT(dservice, gservice, sservice, scservice, reqBookCodes,id,subBCode,sdate,department,bCode);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (GeneralSecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			}
			//|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
//||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||

		}
		else
		{
			System.out.println("Wrong Submission URL");
			String error = "<div class=\"alert alert-danger\" role=\"alert\">\r\n" + 
					"  <h6 class=\"alert-heading text-center\">The entered URLs are not of same Bcode.</h6>\r\n" +
					"</div>";
			request.setAttribute("message", error);
			request.getRequestDispatcher("/index.jsp").forward(request, response);
			return;
		}
		String success = "<div class=\"alert alert-success\" role=\"alert\">\r\n" + 
				"  <h6 class=\"alert-heading text-center\">Submit Successfully</h6>\r\n" +
				"</div>";
		request.setAttribute("message", success);
		request.getRequestDispatcher("/index.jsp").forward(request, response);
		System.out.println("\n\n----------*******  End  *******--------");
	}

	public static String getBookCode(Sheets service,String id, int sheetNo) throws IOException
	{
		System.out.println("Called with id : "+id+" and SheetNo : "+(sheetNo));
		Spreadsheet response1= service.spreadsheets().get(id).setIncludeGridData (false)
				.execute ();
		String bCode = response1.getSheets().get(sheetNo).getProperties().getTitle().toString();
		bCode = bCode.split("_")[0];
		System.out.println("==================================BOOK CODE: "+bCode+"===========================================");
		return bCode;
	}
}