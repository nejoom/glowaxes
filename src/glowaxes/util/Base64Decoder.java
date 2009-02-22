/**
 *
 * Copyright 2008-2009 Elements. All Rights Reserved.
 *
 * License version: CPAL 1.0
 *
 * The Original Code is glowaxes.org code. Please visit glowaxes.org to see how
 * you can contribute and improve this software.
 *
 * The contents of this file are licensed under the Common Public Attribution
 * License Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 *    http://glowaxes.org/license.
 *
 * The License is based on the Mozilla Public License Version 1.1.
 *
 * Sections 14 and 15 have been added to cover use of software over a computer
 * network and provide for attribution determined by Elements.
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific language governing permissions and limitations under the
 * License.
 *
 * Elements is the Initial Developer and the Original Developer of the Original
 * Code.
 *
 * The contents of this file may be used under the terms of the Elements 
 * End-User License Agreement (the Elements License), in which case the 
 * provisions of the Elements License are applicable instead of those above.
 *
 * You may wish to allow use of your version of this file under the terms of
 * the Elements License please visit http://glowaxes.org/license for details.
 *
 */
package glowaxes.util;

/*
 * Copyright ? World Wide Web Consortium, (Massachusetts Institute of Technology, 
 * Institut National de Recherche en Informatique et en Automatique, Keio University).
 * All Rights Reserved. http://www.w3.org/Consortium/Legal/
 */

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

// TODO: Auto-generated Javadoc
/**
 * Decode a BASE64 encoded input stream to some output stream. This class
 * implements BASE64 decoding, as specified in the <a
 * href="http://ds.internic.net/rfc/rfc1521.txt">MIME specification</a>.
 * 
 * @see org.w3c.tools.codec.Base64Encoder
 */
public class Base64Decoder {
    
    /** The Constant BUFFER_SIZE. */
    private static final int BUFFER_SIZE = 1024;

    /**
     * Test the decoder. Run it with one argument: the string to be decoded, it
     * will print out the decoded value.
     * 
     * @param args the args
     */
    public static void main(String args[]) {
        if (args.length == 1) {
            try {
                Base64Decoder b = new Base64Decoder(args[0]);
                System.out.println("[" + b.processString() + "]");
            } catch (Base64FormatException e) {
                System.out.println("Invalid Base64 format !");
                System.exit(1);
            }
        } else if ((args.length == 2) && (args[0].equals("-f"))) {
            try {
                FileInputStream in = new FileInputStream(args[1]);
                Base64Decoder b = new Base64Decoder(in, System.out);
                b.process();
            } catch (Exception ex) {
                System.out.println("error: " + ex.getMessage());
                System.exit(1);
            }
        } else {
            System.out.println("Base64Decoder [strong] [-f file]");
        }
        System.exit(0);
    }

    /** The in. */
    InputStream in = null;

    /** The out. */
    OutputStream out = null;

    /** The stringp. */
    boolean stringp = false;

    /**
     * Create a decoder to decode a String.
     * 
     * @param bytes the bytes
     */
    public Base64Decoder(byte bytes[]) {
        //        
        // try {
        // bytes = input.getBytes("ISO-8859-1");
        // } catch (UnsupportedEncodingException ex) {
        // throw new RuntimeException(this.getClass().getName()
        // + "[Constructor] Unable to convert"
        // + "properly char to bytes");
        // }
        this.stringp = true;
        this.in = new ByteArrayInputStream(bytes);
        this.out = new ByteArrayOutputStream();
    }

    /**
     * Create a decoder to decode a stream.
     * 
     * @param in The input stream (to be decoded).
     * @param out The output stream, to write decoded data to.
     */
    public Base64Decoder(InputStream in, OutputStream out) {
        this.in = in;
        this.out = out;
        this.stringp = false;
    }

    /**
     * Create a decoder to decode a String.
     * 
     * @param input The string to be decoded.
     */
    public Base64Decoder(String input) {
        byte bytes[];
        try {
            bytes = input.getBytes("ISO-8859-1");
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(this.getClass().getName()
                    + "[Constructor] Unable to convert"
                    + "properly char to bytes");
        }
        this.stringp = true;
        this.in = new ByteArrayInputStream(bytes);
        this.out = new ByteArrayOutputStream();
    }

    /**
     * Check.
     * 
     * @param ch the ch
     * 
     * @return the int
     */
    private final int check(int ch) {
        if ((ch >= 'A') && (ch <= 'Z')) {
            return ch - 'A';
        } else if ((ch >= 'a') && (ch <= 'z')) {
            return ch - 'a' + 26;
        } else if ((ch >= '0') && (ch <= '9')) {
            return ch - '0' + 52;
        } else {
            switch (ch) {
            case '=':
                return 65;
            case '+':
                return 62;
            case '/':
                return 63;
            default:
                return -1;
            }
        }
    }

    /**
     * Gets the 1.
     * 
     * @param buf the buf
     * @param off the off
     * 
     * @return the 1
     */
    private final int get1(byte buf[], int off) {
        return ((buf[off] & 0x3f) << 2) | ((buf[off + 1] & 0x30) >>> 4);
    }

    /**
     * Gets the 2.
     * 
     * @param buf the buf
     * @param off the off
     * 
     * @return the 2
     */
    private final int get2(byte buf[], int off) {
        return ((buf[off + 1] & 0x0f) << 4) | ((buf[off + 2] & 0x3c) >>> 2);
    }

    /**
     * Gets the 3.
     * 
     * @param buf the buf
     * @param off the off
     * 
     * @return the 3
     */
    private final int get3(byte buf[], int off) {
        return ((buf[off + 2] & 0x03) << 6) | (buf[off + 3] & 0x3f);
    }

    /**
     * Prints the hex.
     * 
     * @param buf the buf
     * @param off the off
     * @param len the len
     */
    private void printHex(byte buf[], int off, int len) {
        while (off < len) {
            printHex(buf[off++]);
            System.out.print(" ");
        }
        System.out.println("");
    }

    /**
     * Prints the hex.
     * 
     * @param x the x
     */
    private void printHex(int x) {
        int h = (x & 0xf0) >> 4;
        int l = (x & 0x0f);
        System.out.print((new Character((char) ((h > 9) ? 'A' + h - 10
                : '0' + h))).toString()
                + (new Character((char) ((l > 9) ? 'A' + l - 10 : '0' + l)))
                        .toString());
    }

    /**
     * Prints the hex.
     * 
     * @param s the s
     */
    @SuppressWarnings("unused")
    private void printHex(String s) {
        byte bytes[];
        try {
            bytes = s.getBytes("ISO-8859-1");
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(this.getClass().getName()
                    + "[printHex] Unable to convert" + "properly char to bytes");
        }
        printHex(bytes, 0, bytes.length);
    }

    /**
     * Do the actual decoding. Process the input stream by decoding it and
     * emiting the resulting bytes into the output stream.
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws Base64FormatException the base64 format exception
     * 
     * @exception IOException
     * If the input or output stream accesses failed.
     * @exception Base64FormatException
     * If the input stream is not compliant with the BASE64
     * specification.
     */
    public void process() throws IOException, Base64FormatException {
        byte buffer[] = new byte[BUFFER_SIZE];
        byte chunk[] = new byte[4];
        int got = -1;
        int ready = 0;

        fill: while ((got = in.read(buffer)) > 0) {
            int skiped = 0;
            while (skiped < got) {
                // Check for un-understood characters:
                while (ready < 4) {
                    if (skiped >= got)
                        continue fill;
                    int ch = check(buffer[skiped++]);
                    if (ch >= 0)
                        chunk[ready++] = (byte) ch;
                }
                if (chunk[2] == 65) {
                    out.write(get1(chunk, 0));
                    return;
                } else if (chunk[3] == 65) {
                    out.write(get1(chunk, 0));
                    out.write(get2(chunk, 0));
                    return;
                } else {
                    out.write(get1(chunk, 0));
                    out.write(get2(chunk, 0));
                    out.write(get3(chunk, 0));
                }
                ready = 0;
            }
        }
        if (ready != 0)
            throw new Base64FormatException("Invalid length.");
        out.flush();
    }

    /**
     * Do the decoding, and return a String. This methods should be called when
     * the decoder is used in <em>String</em> mode. It decodes the input
     * string to an output string that is returned.
     * 
     * @return the byte[]
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws Base64FormatException the base64 format exception
     * 
     * @exception RuntimeException
     * If the object wasn't constructed to decode a String.
     * @exception Base64FormatException
     * If the input string is not compliant with the BASE64
     * specification.
     */
    public byte[] processBytes() throws Base64FormatException, IOException {

        process();
        byte[] s;

        s = ((ByteArrayOutputStream) out).toByteArray();
        return s;
    }

    /**
     * Do the decoding, and return a String. This methods should be called when
     * the decoder is used in <em>String</em> mode. It decodes the input
     * string to an output string that is returned.
     * 
     * @return the string
     * 
     * @throws Base64FormatException the base64 format exception
     * 
     * @exception RuntimeException
     * If the object wasn't constructed to decode a String.
     * @exception Base64FormatException
     * If the input string is not compliant with the BASE64
     * specification.
     */
    public String processString() throws Base64FormatException {
        if (!stringp)
            throw new RuntimeException(this.getClass().getName()
                    + "[processString]" + "invalid call (not a String)");
        try {
            process();
        } catch (IOException e) {
        }
        String s;
        try {
            s = ((ByteArrayOutputStream) out).toString("ISO-8859-1");
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(this.getClass().getName()
                    + "[processString] Unable to convert"
                    + "properly char to bytes");
        }
        return s;
    }
}
