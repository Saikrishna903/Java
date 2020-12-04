//Framework program using MySQL database.
import java.sql.*;
import java.util.Scanner;

class FrameWork
{
	static Connection connection;
	static Statement statement;
	static ResultSet resultSet;
	static Scanner scanner = new Scanner(System.in);
	static String[] fieldNames;
	static String[] messages;
	static String menu;
	public static void main(String args[]) 
	{
		try
		{
			String url = "jdbc:mysql://165.22.14.77/dbSaikrishna?user=Saikrishna&password=Saikrishna";
			connection = DriverManager.getConnection(url);
			getfieldNames();
			getPromptMessages();
			getMenu();
			int userChoice;
			while(true)
			{
				System.out.println(menu);
				System.out.print("Enter your choice: ");
				userChoice = scanner.nextInt();
				switch(userChoice)
				{
					case 1: insertRecord();
							break;
					case 2: printRecords();
							break;
					case 3:	searchRecord();
							break;
					case 4:	updateRecord();
							break;
					case 5:	deleteRecord();
							break;
					case 6:	confirmToExit();
					default: System.out.println("Invalid choice!");
				}
			}

		}
		catch(SQLException error)
		{
			System.out.print(error.getMessage());
		}
	}

	public static void getMenu() throws SQLException
	{
		statement = connection.createStatement();
		String menuQuery = "SELECT Content FROM Config WHERE FileName = 'Menu'";
		resultSet = statement.executeQuery(menuQuery);
		resultSet.next();
		menu = resultSet.getString("Content");
	}

	public static void confirmToExit() throws SQLException
	{
		System.out.print("Entered exit as your choice.\nDo you really want to exit y or n?: ");
		String exitChoice = scanner.next();
		if(exitChoice.toUpperCase().equals("Y"))
		{
			statement.close();
			resultSet.close();
			connection.close();
			System.exit(0);
		}
	}

	public static void getfieldNames() throws SQLException
	{
		String query = "SELECT * FROM My_Table";
		statement = connection.createStatement();
		resultSet = statement.executeQuery(query);
		ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
		fieldNames = new String[resultSetMetaData.getColumnCount() -1];
		for(int index = 0; index < fieldNames.length; index++)
		{
			String columnName = resultSetMetaData.getColumnName(index + 1);
			if (columnName.equals("Status") != true)
			{
				fieldNames[index] = columnName;
			}
		}
	}

	public static void getPromptMessages() throws SQLException
	{
		String query = "SELECT * FROM Config WHERE FileName = 'Messages'";
		resultSet = statement.executeQuery(query);
		String temporary = "";
		resultSet.next();
		temporary = resultSet.getString("Content");  
		messages = temporary.split(", ");
	}

	public static void insertRecord() throws SQLException 
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
		statement.executeUpdate(query);
		System.out.println(messages[0]);
	}

	public static void printRecords() throws SQLException
	{
		String query = "SELECT * FROM My_Table WHERE Status = 'A'";
		resultSet = statement.executeQuery(query);
		int numOfRecords = 0;
		while(resultSet.next())
		{
			printRecord(resultSet);
			numOfRecords++;
		}
		System.out.println(messages[1] + " " + numOfRecords + "\n");
	}

	public static void printRecord(ResultSet resultSet) throws SQLException
	{
		for(String fieldName : fieldNames)
		{
			System.out.println(fieldName + ": " + resultSet.getString(fieldName));
		}
		System.out.println("-------------------------------");
	}

	public static void searchRecord() throws SQLException
	{
		String idToSearchRecord = getID();
		if(checkRecordPresentOrNot(idToSearchRecord) == true)
		{
			String query = "SELECT * FROM My_Table WHERE Status = 'A' and " + fieldNames[0] + " = " + idToSearchRecord;
			resultSet = statement.executeQuery(query);
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

	public static void updateRecord() throws SQLException
	{
		String idToUpdateRecord = getID();
		if(checkRecordPresentOrNot(idToUpdateRecord) == true)
		{
			String query = "SELECT * FROM Config WHERE FileName = 'UpdatableFields'";
			resultSet = statement.executeQuery(query);
			String tempUpdatableFields = "";
			resultSet.next();
			tempUpdatableFields = resultSet.getString("Content");  
			String[] updatableFields = tempUpdatableFields.split(", ");
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
			int numOfRecordsAffected = statement.executeUpdate(updateQuery);
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

	public static String getID()
	{
		System.out.print("Enter " + fieldNames[0] + ": ");
		String ID = scanner.next();
		return ID;
	}

	public static boolean checkRecordPresentOrNot(String fieldValue) throws SQLException
	{
		String query = "SELECT * from My_Table WHERE Status = 'A' and " + fieldNames[0] + " = " + fieldValue;
		resultSet = statement.executeQuery(query);
		if(resultSet.next())
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public static void deleteRecord() throws SQLException
	{
		String idToDeleteRecord = getID();
		if(checkRecordPresentOrNot(idToDeleteRecord) == true)
		{
			String query = "UPDATE My_Table SET Status = 'D' where " + fieldNames[0] + " = " + idToDeleteRecord;
			int noRecordsAffected = statement.executeUpdate(query);
			if(noRecordsAffected != 0)
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