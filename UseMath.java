//UseMath class with main method.
class UseMath
{
	public static void main(String cmdArgs[])
	{
		NewMath objectNewMath =  new NewMath();
		System.out.println(objectNewMath.addNumbers(8, 2));
		System.out.println(objectNewMath.multiplyTwoNumbers(7, 7));
		System.out.println(objectNewMath.addNumbers(2, 12, 16));
		System.out.println(objectNewMath.squareOfANumber(5));
	}
} 