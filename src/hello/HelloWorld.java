package hello;

public class HelloWorld {
	public static void main(String[] args) {
		System.out.println("Hello World");
		System.out.println("Hello World");
		System.out.println("Hello World");
		System.out.println("Hello World");
		System.out.println("Hello World");
		System.out.println("Hello World");
		// hello?
		System.out.println("hi vernon");
		System.out.println("I verified my email");
		System.out.println("SEE MY WORDS BEN");
		System.out.println("test");
		System.out.println(howToAddComments(1));
	}

	/**
	 * A method that returns an explanation on how to write java-Docs for
	 * methods
	 * 
	 * @param uselessParameter
	 *            a useless parameter that exists only to illustrate
	 * @return A Didatic String that explains how good comments are created.
	 */
	public static String howToAddComments(int uselessParameter) {
		return "To add the ninfty thing above this method, simply type - \"/**\" above the method you wish to explaine and press enter\n"
				+ "To create in line comments, put \"\\\\\" next to the thing you are describing.";
	}
}
