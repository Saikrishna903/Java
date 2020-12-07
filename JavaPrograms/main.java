//Program to pass .class file at run time. 
import java.sql.*;

class TestiCRUD
{
	public static void main(String cmdArgs[])
	{
		try
		{
			String className = cmdArgs[0];
			CRUDOperations objCRUDOperations = new CRUDOperations(className);
			objCRUDOperations.showMenu();
		}
		catch(Exception e)
		{
			System.out.println("Class not found.");
		}
	}
}

