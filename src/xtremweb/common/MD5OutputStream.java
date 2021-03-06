/*
 * Copyrights     : CNRS
 * Author         : Oleg Lodygensky
 * Acknowledgment : XtremWeb-HEP is based on XtremWeb 1.8.0 by inria : http://www.xtremweb.net/
 * Web            : http://www.xtremweb-hep.org
 *
 *      This file is part of XtremWeb-HEP.
 *
 *    XtremWeb-HEP is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    XtremWeb-HEP is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with XtremWeb-HEP.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package xtremweb.common;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * MD5OutputStream is a subclass of FilterOutputStream adding MD5 hashing of the
 * output.
 * <p>
 * Originally written by Santeri Paavolainen, Helsinki Finland 1996 <br>
 * (c) Santeri Paavolainen, Helsinki Finland 1996 <br>
 * Some changes Copyright (c) 2002 Timothy W Macinta <br>
 * <p>
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Library General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library General Public License for more
 * details.
 * <p>
 * You should have received a copy of the GNU Library General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 675 Mass Ave, Cambridge, MA 02139, USA.
 * <p>
 * See http://www.twmacinta.com/myjava/fast_md5.php for more information on this
 * file.
 * <p>
 * Please note: I (Timothy Macinta) have put this code in the com.twmacinta.util
 * package only because it came without a package. I was not the the original
 * author of the code, although I did optimize it (substantially) and fix some
 * bugs.
 *
 * @author Santeri Paavolainen <santtu@cs.hut.fi>
 * @author Timothy W Macinta (twm@alum.mit.edu) (added main() method)
 **/

public class MD5OutputStream extends FilterOutputStream {
	/**
	 * MD5 context
	 */
	private final MD5 md5;

	/**
	 * Creates MD5OutputStream
	 *
	 * @param out
	 *            The output stream
	 */

	public MD5OutputStream(final OutputStream out) {
		super(out);

		md5 = new MD5();
	}

	/**
	 * Writes a byte.
	 *
	 * @see java.io.FilterOutputStream
	 */

	@Override
	public void write(final int b) throws IOException {
		out.write(b);
		md5.update((byte) b);
	}

	/**
	 * Writes a sub array of bytes.
	 *
	 * @see java.io.FilterOutputStream
	 */

	@Override
	public void write(final byte b[], final int off, final int len) throws IOException {
		out.write(b, off, len);
		md5.update(b, off, len);
	}

	/**
	 * Returns array of bytes representing hash of the stream as finalized for
	 * the current state.
	 *
	 * @see MD5#Final
	 */

	public byte[] hash() {
		return md5.Final();
	}

	public MD5 getMD5() {
		return md5;
	}

	/**
	 * This method is here for testing purposes only - do not rely on it being
	 * here.
	 **/
	public static void main(final String[] arg) {
		try (final MD5OutputStream out = new MD5OutputStream(new NullOutputStream());
				final InputStream in = new BufferedInputStream(new FileInputStream(arg[0]))) {
			final byte[] buf = new byte[65536];
			int numRead;
			while ((numRead = in.read(buf)) != -1) {
				out.write(buf, 0, numRead);
			}
			System.out.println(MD5.asHex(out.hash()) + "  " + arg[0]);
			in.close();
			out.close();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

}
