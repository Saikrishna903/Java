//CRUD operations on MySQL data base.
import java.sql.*;

class MySQLDB implements iCRUD
{
	Connection connection;
	Statement statement;
	ResultSet resultSet;

	public MySQLDB() 
	{
		try
		{
			String url = "jdbc:mysql://165.22.14.77/dbSaikrishna?user=Saikrishna&password=Saikrishna";
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(url);
			statement = connection.createStatement();
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
	public int insertRecord(String query) throws SQLException 
	{
		return statement.executeUpdate(query);
	}

	public ResultSet printRecords(String query) throws SQLException
	{
		return statement.executeQuery(query);
	}

	public ResultSet searchRecord(String query) throws SQLException
	{
		return statement.executeQuery(query);
	}

	public int updateRecord(String query) throws SQLException
	{
		return statement.executeUpdate(query);
	}

	public int deleteRecord(String query) throws SQLException
	{
		return statement.executeUpdate(query);
	}

	public String[] getData(String fileName) throws SQLException
	{
		statement = connection.createStatement();
		String query = "SELECT Content FROM Config WHERE FileName = '" + fileName + "'";
		resultSet = statement.executeQuery(query);
		resultSet.next();
		String temp = resultSet.getString("Content");
		String[] data = temp.split(", ");
		return data;	
	}

	public String[] getfieldNames() throws SQLException
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
}