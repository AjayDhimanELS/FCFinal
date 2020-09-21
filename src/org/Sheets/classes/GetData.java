package org.Sheets.classes;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;

public class GetData {

	public static List<List<Object>> forSubmission(Sheets service, String id, String bCode) throws IOException
	{
		final String range = bCode+"_Solutions!A2:C";
        ValueRange response = service.spreadsheets().values()
                .get(id, range)
                .execute();
        List<List<Object>> values = response.getValues();
    	return values;
	}
	
    public static List<List<Object>> forHeaders(Sheets service, String id, String bCode) throws IOException, GeneralSecurityException {

    	final String range = bCode+"_Assignments!A2:BH2";
        ValueRange response = service.spreadsheets().values()
                .get(id, range)
                .execute();
        List<List<Object>> values = response.getValues();
    	return values;
    }
    
    public static List<List<Object>> forData(Sheets service, String id, String bCode) throws IOException, GeneralSecurityException {

    	final String range = bCode+"_Assignments!A3:BH";
        ValueRange response = service.spreadsheets().values()
                .get(id, range)
                .execute();
        List<List<Object>> values2 = response.getValues();
    	return values2;
    }
}
