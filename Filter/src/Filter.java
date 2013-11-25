import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Filter {

	public static void main(String[] args) throws IOException {
		String input;
		BufferedReader upperReader = new BufferedReader(
				new UpperCaseFilterReader(new InputStreamReader(System.in)));
		BufferedReader umlautReader = new BufferedReader(
				new UmlautFilterReader(new InputStreamReader(System.in)));
		BufferedReader upperUmlautReader = new BufferedReader(
				new UpperCaseFilterReader(new UmlautFilterReader(
						new InputStreamReader(System.in))));

		do {
			input = upperReader.readLine();
			System.out.println(input);
		} while (!input.endsWith("@"));

		do {
			input = umlautReader.readLine();
			System.out.println(input);
		} while (!input.endsWith("@"));
		
		do {
			input = upperUmlautReader.readLine();
			System.out.println(input);
		} while (!input.endsWith("@"));
		System.in.read();
		umlautReader.close();
		upperReader.close();
		upperUmlautReader.close();
	}
}
