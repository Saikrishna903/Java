// Program to write SQLParser

class SQLParser
{
	String tableName;
	String[] fieldNames;
	String[] fieldValues;

	public String removeExtraSpaces(String query)
	{
		return query.trim().replaceAll("[ ]{2,}", " ");
	}

	public String getTableName(String query)
	{
		String modifiedQuery = removeExtraSpaces(query);
		String[] splitedQuery = modifiedQuery.split(" ");
		if(splitedQuery[0].toUpperCase().equals("INSERT"))
		{
			tableName = splitedQuery[2];
		}
		else if(splitedQuery[0].toUpperCase().equals("SELECT"))
		{
			for(int index = 0; index < splitedQuery.length; index++)
			{
				if(splitedQuery[index].toUpperCase().equals("FROM"))
				{
					tableName = splitedQuery[index + 1];
				}
			}
		}
		else if(splitedQuery[0].toUpperCase().equals("UPDATE"))
		{
			tableName = splitedQuery[1];
		}
		return tableName;
	}

	public String[] getFieldNames(String query)
	{
		String fieldNames = "";
		String modifiedQuery = removeExtraSpaces(query);
		String replacedQuery = modifiedQuery.replaceAll("[(,)]", "");
		String[] splitedQuery = replacedQuery.split(" ");
		if(splitedQuery[0].toUpperCase().equals("INSERT"))
		{
			for (int index = 3; index < splitedQuery.length; index++)
			{
				if (splitedQuery[index].toUpperCase().equals("VALUES") == false)
				{
					fieldNames += splitedQuery[index] + " ";
				}
				else
				{
					break;
				}
			}
		}
		else if(splitedQuery[0].toUpperCase().equals("SELECT"))
		{
			for (int index = 1; index < splitedQuery.length; index++)
			{
				if (splitedQuery[index].toUpperCase().equals("WHERE"))
				{
					index += 1;
					fieldNames += splitedQuery[index] + " ";
					while ((index + 2) != (splitedQuery.length - 1))
					{
						index += 4;
						fieldNames += splitedQuery[index] + " ";
					}
					break;
				}
				
			}			
		}
		else if(splitedQuery[0].toUpperCase().equals("UPDATE"))
		{
			fieldNames += splitedQuery[3] + " " + splitedQuery[7];
		}
		return fieldNames.split(" ");
	}

	public String[] getFieldValues(String query)
	{
		String fieldValues = "";
		String modifiedQuery = removeExtraSpaces(query);
		String replacedQuery = query.replaceAll("[(,\')]", "");
		String[] splitedQuery = replacedQuery.split(" ");
		if(splitedQuery[0].toUpperCase().equals("INSERT"))
		{
			int counter = 0;
			for (int index = 3; index < splitedQuery.length; index++)
			{
				if (splitedQuery[index].toUpperCase().equals("VALUES"))
				{
					counter = index;
				}
				else if(index > counter && counter != 0)
				{
					fieldValues += splitedQuery[index] + " ";
				}
			}
		}
		else if(splitedQuery[0].toUpperCase().equals("SELECT"))
		{
			for (int index = 1; index < splitedQuery.length; index++)
			{
				if (splitedQuery[index].toUpperCase().equals("WHERE"))
				{
					index += 3;
					fieldValues += splitedQuery[index] + " ";
					while (index != splitedQuery.length - 1)
					{
						index += 4;
						fieldValues += splitedQuery[index] + " ";
					}
					break;
				}
			}			
		}
		else if(splitedQuery[0].toUpperCase().equals("UPDATE"))
		{
			fieldValues += splitedQuery[5] + " " + splitedQuery[9];
		}
		return fieldValues.split(" ");
	}
}