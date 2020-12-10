//CRUD operations on DB
import java.sql.*;
import java.util.Scanner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

class CRUDOperations 
{
	Scanner scanner = new Scanner(System.in);
	ResultSet resultSet;
	String[] fieldNames;
	String[] menu;
	String[] updatableFields;
	String[] messages;
	iCRUD objiCRUD;

	public CRUDOperations(String className)
	{
		try
		{
			objiCRUD = (iCRUD)Class.forName(className).newInstance();
			fieldNames = objiCRUD.getFieldNames();
			menu = objiCRUD.getData("Menu");
			updatableFields = objiCRUD.getData("UpdatableFields");
			messages = objiCRUD.getData("Messages");
		}
		catch(Exception error)
		{
			System.out.println(error.getMessage());
		}
	}

	public void showMenu() throws Exception
	{
		String userChoice;
		while(true)
		{
			for(String menuLine : menu)
			{
				System.out.println(menuLine);
			}
			System.out.print("Enter your choice: ");
			userChoice = scanner.next();
			switch(userChoice)
			{
				case "1": 
					insertRecord();
					break;
				case "2": 
					printRecords();
					break;
				case "3": 
					searchRecord();
					break;
				case "4": 
					updateRecord();
					break;
				case "5": 
					deleteRecord();
					break;
				case "6": 
					System.out.print("Entered exit as your choice.\nDo you really want to exit y or n?: ");
					String exitChoice = scanner.next();
					if(exitChoice.toUpperCase().equals("Y"))
					{
						System.exit(0);
					}
					else if(exitChoice.toUpperCase().equals("N"))
					{
						continue;
					}
				default: 
					System.out.println("Invalid choice!");
			}
		}
	}

	public void insertRecord() throws Exception 
	{
		String fieldValue = "";
		String fieldValues = "";
		String tempFieldNames = "";
		for(String fieldName : fieldNames)
		{
			System.out.print("Enter " + fieldName + ": ");
			fieldValue = scanner.next();
			fieldValues += "'" + fieldValue + "', ";
		}
		fieldValues += "'A'";
		String query = "INSERT INTO My_Table VALUES (" + fieldValues + ")";
		int numOfRecordsAffected = objiCRUD.insertRecord(query);
		if(numOfRecordsAffected != 0)
		{
			System.out.println(messages[0]);
		}
	}

	public void printRecords() throws Exception
	{
		String query = "SELECT * FROM My_Table WHERE Status = 'A'";
		JSONObject objJSON = objiCRUD.printRecords(query);
		int numOfRecords = 0;
		JSONArray jsonArray = (JSONArray) objJSON.get("My_Table");
		for(int index = 0; index < jsonArray.size(); index++)
		{
			JSONObject fieldValues = (JSONObject)jsonArray.get(index);
			for(String fieldName : fieldNames)
			{
				System.out.println(fieldName + ": " + fieldValues.get(fieldName));
			}
			System.out.println("-------------------------------");
			numOfRecords++;
		}
		System.out.println(messages[1] + " " + numOfRecords + "\n");
	}
 
	public void searchRecord() throws Exception
	{
		String idToSearchRecord = getID();
		if(objiCRUD.checkRecordPresentOrNot(idToSearchRecord) == true)
		{
			String query = "SELECT * FROM My_Table WHERE Status = 'A' and " + fieldNames[0] + " = '" + idToSearchRecord + "'";
			JSONObject objJSON = objiCRUD.searchRecord(query);
			JSONArray jsonArray = (JSONArray) objJSON.get("My_Table");
			for(int index = 0; index < jsonArray.size(); index++)
			{
				JSONObject fieldValues = (JSONObject)jsonArray.get(index);
				for(String fieldName : fieldNames)
				{
					System.out.println(fieldName + ": " + fieldValues.get(fieldName));
				}
				System.out.println("-------------------------------");
			}
		}
		else
		{
			System.out.println(messages[4]);
		}
	}

	private String getID()
	{
		System.out.print("Enter " + fieldNames[0] + ": ");
		String ID = scanner.next();
		return ID;
	}

	public void updateRecord() throws Exception
	{
		String idToUpdateRecord = getID();
		if(objiCRUD.checkRecordPresentOrNot(idToUpdateRecord) == true)
		{
			int counter = 1;
			for(String fieldIndex : updatableFields)
			{
				System.out.println(counter + ". Update " + fieldNames[Integer.parseInt(fieldIndex) - 1]);
				counter++;
			}
			System.out.print("Enter your choice: ");
			int updateOption = scanner.nextInt();
			String updateFieldName = fieldNames[Integer.parseInt(updatableFields[updateOption - 1]) - 1];
			System.out.print("Enter new " + updateFieldName + ": ");
			String newFieldValue = scanner.next();
			String updateQuery = "UPDATE My_Table SET "+ updateFieldName + " = '" + newFieldValue + "' WHERE " + fieldNames[0] + " = '" + idToUpdateRecord + "'";
			int numOfRecordsAffected = objiCRUD.updateRecord(updateQuery);
			if(numOfRecordsAffected != 0)
			{
				System.out.println(messages[2]);
			}
		}
		else
		{	
			System.out.println(messages[4]);
		}
	}

	public void deleteRecord() throws Exception
	{
		String idToDeleteRecord = getID();
		if(objiCRUD.checkRecordPresentOrNot(idToDeleteRecord) == true)
		{
			String query = "UPDATE My_Table SET Status = 'D' where " + fieldNames[0] + " = '" + idToDeleteRecord + "'";
			int numOfRecordsAffected = objiCRUD.deleteRecord(query);
			if(numOfRecordsAffected != 0)
			{
				System.out.println(messages[3]);
			}
		}
		else
		{
			System.out.println(messages[4]);
		}
	}
}