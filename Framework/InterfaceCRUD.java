//Interface 
import java.sql.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

interface iCRUD
{
	public int insertRecord(String query) throws Exception;
	public JSONObject printRecords(String query) throws Exception;
	public JSONObject searchRecord(String query) throws Exception;
	public int updateRecord(String query) throws Exception;
	public int deleteRecord(String query) throws Exception;
	public boolean checkRecordPresentOrNot(String fieldValue) throws Exception;
	public String[] getFieldNames() throws Exception;
	public String[] getData(String fileName) throws Exception;
}