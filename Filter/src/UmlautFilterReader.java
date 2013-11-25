import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;

public class UmlautFilterReader extends FilterReader {

	protected UmlautFilterReader(Reader in) {
		super(in);
	}

	public int read() throws IOException {
		int c = super.read();
		if (c == -1)
			return -1;
		else
			return (char) c;
	}

	public int read(char cbuf[], int off, int len) throws IOException {
		int n = super.read(cbuf, off, len);

		if (n != -1) {
			for (int i = 0; i < n; i++) {
				switch (cbuf[i]) {
				case 'ä':
					cbuf = freeSpaceForUmlaut(cbuf, i);
					cbuf[off + i] = 'a';
					cbuf[off + i + 1] = 'e';
					i++;
					break;
				case 'ö':
					cbuf = freeSpaceForUmlaut(cbuf, i);
					cbuf[off + i] = 'o';
					cbuf[off + i + 1] = 'e';
					i++;
					break;
				case 'ü':
					cbuf = freeSpaceForUmlaut(cbuf, i);
					cbuf[off + i] = 'u';
					cbuf[off + i + 1] = 'e';
					i++;
					break;
				case 'ß':
					cbuf = freeSpaceForUmlaut(cbuf, i);
					cbuf[off + i] = 's';
					cbuf[off + i + 1] = 's';
					i++;
					break;
				default:
					cbuf[off + i] = (char) (cbuf[off + i]);
					break;
				}
			}
		}
		return n;
	}

	private char[] freeSpaceForUmlaut(char[] buf, int pos) {
		for (int i = buf.length - 1; i > pos; i--) {
			buf[i] = buf[i - 1];
		}
		return buf;
	}
}