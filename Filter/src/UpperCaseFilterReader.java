import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;

public class UpperCaseFilterReader extends FilterReader {

	protected UpperCaseFilterReader(Reader in) {
		super(in);
	}

	public int read() throws IOException {
		int c = super.read();
		if (c == -1)
			return -1;
		else
			return Character.toUpperCase((char) c);
	}

	public int read(char cbuf[], int off, int len) throws IOException {
		int n = super.read(cbuf, off, len);

		if (n != -1) {
			for (int i = 0; i < n; i++) {
				cbuf[off + i] = Character.toUpperCase(cbuf[off + i]);
			}
		}
		return n;
	}
}
