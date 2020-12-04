//Program to understand inheritance, method overloading and method overriding.
class NewMath extends MyMath
{
	public int addNumbers(int number1, int number2)
	{
		System.out.println("Method overriding.");
		return (number1 + number2);
	}

	public int addNumbers(int number1, int number2, int number3)
	{
		return (number1 + number2 + number3);
	}

	public int squareOfANumber(int number1)
	{
		return number1 * number1;
	}
}
