//Interface 
import java.sql.*;

interface iCRUD
{
	public int insertRecord(String query) throws SQLException;
	public ResultSet printRecords(String query) throws SQLException;
	public ResultSet searchRecord(String query) throws SQLException;
	public int updateRecord(String query) throws SQLException;
	public int deleteRecord(String query) throws SQLException;
	public String[] getfieldNames() throws SQLException;
	public String[] getData(String fileName) throws SQLException;
}