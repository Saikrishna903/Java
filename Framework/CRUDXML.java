//Perform CRUD operations using XML data.

import java.io.File;
import java.io.FileWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;  
import org.w3c.dom.Node;
import java.util.Scanner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

class XML implements iCRUD
{
	File file;
	DocumentBuilderFactory dbf;
	DocumentBuilder db;
	Document document;
	Element root;
	String[] fieldNames;
	JSONObject jsonObject;
	JSONArray array;

	public XML()
	{
		try
		{
			file = new File("Data.xml");
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			document = db.parse(file);
			document.getDocumentElement().normalize();
			root = document.getDocumentElement();
			fieldNames = getFieldNames();
		}
		catch(Exception error)
	    {
	    	System.out.println(error.getMessage());
	    }
	}

    public int insertRecord(String query) throws Exception
    {
    	int insertRecordStatus = 0;
    	SQLParser objSQLParser = new SQLParser();
    	String[] fieldValues = objSQLParser.getFieldValues(query);
    	System.out.println(query);
    	for(String fieldValue : fieldValues)
    	{
    		System.out.println(fieldValue);
    	}
    	Element eElement = document.createElement("Record");
    	root.appendChild(eElement);
    	Attr attribute = document.createAttribute("Status");
    	attribute.setValue("A");
    	eElement.setAttributeNode(attribute);
    	for(int index = 0; index < fieldNames.length; index++)
    	{
    		Element child = document.createElement(fieldNames[index]);
    		child.appendChild(document.createTextNode(fieldValues[index]));
    		eElement.appendChild(child);
    	}
    	insertRecordStatus = saveRecords();
    	return insertRecordStatus;
    }

    public JSONObject printRecords(String query) throws Exception
    {
    	NodeList nList = document.getElementsByTagName("Record");
    	jsonObject = new JSONObject();
    	array = new JSONArray();
    	for (int index = 0; index < nList.getLength(); index++)
    	{
    		Node nNode = nList.item(index);
    		Element eElement = (Element) nNode;
    		String attribute = eElement.getAttribute("Status");
    		if(attribute.equals("A"))
    		{
    			JSONObject record = new JSONObject();
    			NodeList childNodes = nNode.getChildNodes();
    			for(int indexNumber = 0; indexNumber < childNodes.getLength(); indexNumber++)
    			{
    				Node node = childNodes.item(indexNumber);
    				record.put(node.getNodeName(), node.getTextContent());
    			}
    			array.add(record);
    		}
    	}
    	jsonObject.put("My_Table", array);
    	return jsonObject;
    }

    public JSONObject searchRecord(String query) throws Exception
    {
    	jsonObject = new JSONObject();
    	array = new JSONArray();
    	SQLParser objSQLParser = new SQLParser();
    	String[] fieldValues = objSQLParser.getFieldValues(query);
    	NodeList nList = document.getElementsByTagName("Record");
    	for (int index = 0; index < nList.getLength(); index++)
    	{
    		Node nNode = nList.item(index);
    		Element eElement = (Element) nNode;
    		String attribute = eElement.getAttribute("Status");
    		String id = eElement.getElementsByTagName(fieldNames[0]).item(0).getTextContent();
    		if(attribute.equals("A") && id.equals(fieldValues[1]))
    		{
    			jsonObject = new JSONObject();
    			array = new JSONArray();
    			JSONObject record = new JSONObject();
    			for(int indexNumber = 0; indexNumber < fieldNames.length; indexNumber++)
    			{
    				record.put(fieldNames[indexNumber], eElement.getElementsByTagName(fieldNames[indexNumber]).item(0).getTextContent());
    			}
    			array.add(record);
    			break;
    		}
    	}
    	jsonObject.put("My_Table", array);
    	return jsonObject;
    }

    public int updateRecord(String query) throws Exception
    {
    	int updateRecordSatus = 0;
    	SQLParser objSQLParser = new SQLParser();
    	String[] fieldValues = objSQLParser.getFieldValues(query);
    	String[] fieldNames = objSQLParser.getFieldNames(query);
    	NodeList nodeList = document.getElementsByTagName("Record");
    	for (int index = 0; index < nodeList.getLength(); index++)
    	{
    		Node nNode = nodeList.item(index);
    		Element eElement = (Element) nNode;
    		String attribute = eElement.getAttribute("Status");
    		String id = eElement.getElementsByTagName(fieldNames[1]).item(0).getTextContent();
    		if(attribute.equals("A") && id.equals(fieldValues[1]))
    		{
    			NodeList nodes = nNode.getChildNodes();
    			for(int indexNumber = 0; indexNumber < nodes.getLength(); indexNumber++)
    			{
    				Node node = nodes.item(indexNumber);
    				if(fieldNames[0].equals(node.getNodeName()))
    				{
    					node.setTextContent(fieldValues[0]);
    					updateRecordSatus = saveRecords();
    					break;
    				}
    			}
    		}
    	}
    	return updateRecordSatus;
    }

    public int deleteRecord(String query) throws Exception
    {
    	int deleteRecordStatus = 0;
    	SQLParser objSQLParser = new SQLParser();
    	String[] fieldValues = objSQLParser.getFieldValues(query);
    	String[] fieldNames = objSQLParser.getFieldNames(query);
    	NodeList nodeList = document.getElementsByTagName("Record");
    	for (int index = 0; index < nodeList.getLength(); index++)
    	{
    		Node node = nodeList.item(index);
    		Element eElement = (Element) node;
    		String attribute = eElement.getAttribute("Status");
    		String id = eElement.getElementsByTagName(fieldNames[1]).item(0).getTextContent();
    		if(attribute.equals("A") && id.equals(fieldValues[1]))
    		{
    			eElement.setAttribute("Status", "D");
    			deleteRecordStatus = saveRecords();
    			break;
    		}
    	}
    	return deleteRecordStatus;
    }

    public int saveRecords()
    {
    	try
    	{
    		TransformerFactory transformerFactory = TransformerFactory.newInstance();
    		Transformer transformer = transformerFactory.newTransformer();
    		DOMSource domSource = new DOMSource(document);
    		StreamResult streamResult = new StreamResult(new FileWriter(file));
    		transformer.transform(domSource, streamResult);
    		return 1;
    	}
    	catch (Exception error)
    	{
    		return 0;
    	}
    }

    public String[] getFieldNames() throws Exception
    {
    	String tempFieldNames = "";
    	File myObj = new File("FieldNames.cfg");
    	Scanner scanner = new Scanner(myObj);
    	while (scanner.hasNextLine())
    	{
    		tempFieldNames = scanner.nextLine();
    	}
    	scanner.close();
    	return tempFieldNames.split(", ");
    }

    public String[] getData(String fileName) throws Exception
    {
    	String data = "";
    	fileName += ".cfg";
    	File myObj = new File(fileName);
    	Scanner scanner = new Scanner(myObj);
    	while (scanner.hasNextLine())
    	{
    		data = scanner.nextLine();
    	}
    	scanner.close();
    	return data.split(", ");
    }
	public boolean checkRecordPresentOrNot(String fieldValue) throws Exception
	{
		boolean isRecordPresent = false;
		NodeList nodeList = document.getElementsByTagName("Record");  
		for(int index = 0; index < nodeList.getLength(); index++)
		{
			Node node = nodeList.item(index);
			Element eElement = (Element) node;
			String attribute = eElement.getAttribute("Status");
			if(attribute.equals("A") && eElement.getElementsByTagName(fieldNames[0]).item(0).getTextContent().equals(fieldValue))
			{
				isRecordPresent = true;
			}
		}
		return isRecordPresent;
	}
}