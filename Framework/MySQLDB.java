//CRUD operations on MySQL data base.
import java.sql.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

class MySQL implements iCRUD
{
	Connection connection;
	Statement statement;
	ResultSet resultSet;
	String[] fieldNames;

	public MySQL() 
	{
		try
		{
			String url = "jdbc:mysql://165.22.14.77/dbSaikrishna?user=Saikrishna&password=Saikrishna";
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(url);
			statement = connection.createStatement();
			fieldNames = getFieldNames();
		}
		catch(Exception error)
		{
			System.out.println(error.getMessage());
		}
	}

	public int insertRecord(String query) throws Exception 
	{
		return statement.executeUpdate(query);
	}

	public JSONObject printRecords(String query) throws Exception
	{
		return convertDataintoJSONFormat(query);
	}

	public JSONObject searchRecord(String query) throws Exception
	{
		return convertDataintoJSONFormat(query);
	}

	public int updateRecord(String query) throws Exception
	{
		return statement.executeUpdate(query);
	}

	public int deleteRecord(String query) throws Exception
	{
		return statement.executeUpdate(query);
	}
	
	public JSONObject convertDataintoJSONFormat(String query) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		JSONArray array = new JSONArray();
		resultSet = statement.executeQuery(query);
		while(resultSet.next())
		{
			JSONObject objRecord = new JSONObject();
			for(int index = 0; index < fieldNames.length; index++)
			{
				objRecord.put(fieldNames[index], resultSet.getString(fieldNames[index]));
			}
			array.add(objRecord);
		}
		jsonObject.put("My_Table", array);
		return jsonObject;
	}

	public String[] getData(String fileName) throws Exception
	{
		statement = connection.createStatement();
		String query = "SELECT Content FROM Config WHERE FileName = '" + fileName + "'";
		resultSet = statement.executeQuery(query);
		resultSet.next();
		String temp = resultSet.getString("Content");
		String[] data = temp.split(", ");
		return data;	
	}

	public String[] getFieldNames() throws Exception
	{
		String query = "SELECT * FROM My_Table";
		statement = connection.createStatement();
		resultSet = statement.executeQuery(query);
		ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
		String[] fieldNames = new String[resultSetMetaData.getColumnCount() - 1];
		for(int index = 0; index < fieldNames.length; index++)
		{
			String columnName = resultSetMetaData.getColumnName(index + 1);
			if (columnName.equals("Status") != true)
			{
				fieldNames[index] = columnName;
			}
		}
		return fieldNames;
	}

	public boolean checkRecordPresentOrNot(String fieldValue) throws Exception
	{
		boolean isRecordPresent = false;
		String query = "SELECT * from My_Table WHERE Status = 'A' and " + fieldNames[0] + " = '" + fieldValue + "'";
		resultSet = statement.executeQuery(query);
		if(resultSet.next())
		{
			isRecordPresent = true;
		}
		return isRecordPresent;
	}

}