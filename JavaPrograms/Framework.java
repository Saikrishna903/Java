//CRUD operations.
import java.sql.*;
import java.util.Scanner;

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
			fieldNames = objiCRUD.getfieldNames();
			menu = objiCRUD.getData("Menu");
			updatableFields = objiCRUD.getData("UpdatableFields");
			messages = objiCRUD.getData("Messages");
		}
		catch(Exception error)
		{
			System.out.println("Class not found.");
		}
	}

	public void showMenu() throws SQLException
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

	public void insertRecord() throws SQLException 
	{
		String fieldValue = "";
		String fieldValues = "";
		for(String fieldName : fieldNames)
		{
			System.out.print("Enter " + fieldName + ": ");
			fieldValue = scanner.next();
			fieldValues += "'" + fieldValue + "', ";
		}
		fieldValues += "'A'";
		String query = "INSERT INTO My_Table Values(" + fieldValues + ")";
		int numOfRecordsAffected = objiCRUD.insertRecord(query);
		if(numOfRecordsAffected != 0)
		{
			System.out.println(messages[0]);
		}
	}

	public void printRecords() throws SQLException
	{
		String query = "SELECT * FROM My_Table WHERE Status = 'A'";
		resultSet = objiCRUD.printRecords(query);
		int numOfRecords = 0;
		while(resultSet.next())
		{
			printRecord(resultSet);
			numOfRecords++;
		}
		System.out.println(messages[1] + " " + numOfRecords + "\n");
	}

	private void printRecord(ResultSet resultSet) throws SQLException
	{
		for(String fieldName : fieldNames)
		{
			System.out.println(fieldName + ": " + resultSet.getString(fieldName));
		}
		System.out.println("-------------------------------");
	}
 
	public void searchRecord() throws SQLException
	{
		String idToSearchRecord = getID();
		if(checkRecordPresentOrNot(idToSearchRecord) == true)
		{
			String query = "SELECT * FROM My_Table WHERE Status = 'A' and " + fieldNames[0] + " = " + idToSearchRecord;
			resultSet = objiCRUD.searchRecord(query);
			if(resultSet.next())
			{
				printRecord(resultSet);
			}
		}
		else
		{
			System.out.println(messages[4]);
		}
	}

	public void updateRecord() throws SQLException
	{
		String idToUpdateRecord = getID();
		if(checkRecordPresentOrNot(idToUpdateRecord) == true)
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
			String updateQuery = "UPDATE My_Table SET "+ updateFieldName + " = " + newFieldValue + " WHERE " + fieldNames[0] + " = " + idToUpdateRecord;
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

	private String getID()
	{
		System.out.print("Enter " + fieldNames[0] + ": ");
		String ID = scanner.next();
		return ID;
	}
 
	private boolean checkRecordPresentOrNot(String fieldValue) throws SQLException
	{
		String query = "SELECT * from My_Table WHERE Status = 'A' and " + fieldNames[0] + " = " + fieldValue;
		resultSet = objiCRUD.searchRecord(query);
		if(resultSet.next())
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public void deleteRecord() throws SQLException
	{
		String idToDeleteRecord = getID();
		if(checkRecordPresentOrNot(idToDeleteRecord) == true)
		{
			String query = "UPDATE My_Table SET Status = 'D' where " + fieldNames[0] + " = " + idToDeleteRecord;
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
