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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

// TODO: Auto-generated Javadoc
/**
 * The Class Encrypt.
 * @author <a href="mailto:eddie@tinyelements.com">Eddie Moojen</a>
 */
public class Encrypt {

    /** The dcipher. */
    private static Cipher dcipher;

    /** The ecipher. */
    private static Cipher ecipher;

    /**
     * Decrypt.
     * 
     * @param str
     *            the str
     * 
     * @return the string
     * 
     * @throws IllegalBlockSizeException
     *             the illegal block size exception
     * @throws BadPaddingException
     *             the bad padding exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private static String decrypt(String str) throws IllegalBlockSizeException,
            BadPaddingException, IOException {
        // Decode base64 to get bytes
        byte[] dec = null;
        try {
            dec = new Base64Decoder(str).processBytes();
        } catch (Base64FormatException e) {
            e.printStackTrace();
        }

        // Decrypt
        byte[] utf8 = dcipher.doFinal(dec);

        // Decode using utf-8
        return new String(utf8, "UTF8");
    }

    public static Date getLicenseIssueDate() throws IllegalBlockSizeException,
            BadPaddingException, IOException, ParseException {

        String code = new String(loadFile("license.txt"));

        String decrypted = decrypt(code);

        String values[] = decrypted.split(";");
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = formatter.parse(values[0]);

        return date;
    }

    public static String getLicenseType() throws IllegalBlockSizeException,
            BadPaddingException, IOException {

        String code = new String(loadFile("license.txt"));

        String decrypted = decrypt(code);

        String values[] = decrypted.split(";");
        return values[1];
    }

    public static String getLicenseValue() throws IllegalBlockSizeException,
            BadPaddingException, IOException {
        String code = new String(loadFile("license.txt"));

        String decrypted = decrypt(code);

        String values[] = decrypted.split(";");
        return values[2];
    }

    /**
     * Checks if is absolute path.
     * 
     * @param fileName
     *            the file name
     * 
     * @return true, if is absolute path
     */
    private static boolean isAbsolutePath(String fileName) {
        // windows absolute path
        if (fileName.indexOf("\\") != -1) {
            return true;
        }

        // Unix absolute path
        if (fileName.startsWith("/")) {
            return true;
        }

        // Unix absolute path
        if (fileName.startsWith("file:")) {
            return true;
        }

        return false;
    }

    /**
     * Load file.
     * 
     * @param file
     *            the file
     * 
     * @return the string
     */
    private static String loadFile(String file) {

        InputStream in = null;
        // Get next line
        String cbuf = "";

        try {

            boolean absolutePath = isAbsolutePath(file);

            if (!absolutePath) {

                URL fileURL = null;
                try {
                    fileURL =
                            (new Encrypt()).getClass().getClassLoader()
                                    .getResource(file);
                } catch (InvalidKeyException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (InvalidKeySpecException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (InvalidAlgorithmParameterException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                if (fileURL != null)
                    in = new FileInputStream(fileURL.getFile());
                else {
                    System.out.println("cant load: " + file);
                    // logger.error("Cant load (in=null): " + tempFile);
                    return null;
                }

                /*
                 * dirty Unix seems to cache its resource streams so this wont
                 * work on unix... one must load as file
                 */
            } else {

                in = new FileInputStream(file);

            }

            BufferedReader br =
                    new BufferedReader(new InputStreamReader(in, "8859_1"));

            String line = "";
            while ((line = br.readLine()) != null) {
                cbuf += line + System.getProperty("line.separator");
            }

            in.close();

        } catch (IOException e) {
            System.out.println(e);

            // logger.error(e + ": IOException");

        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
                System.out.println(e);
                // logger.error(e + ": can't close stream");
            }
        }
        return cbuf;
    }

    /**
     * The main method.
     * 
     * @param args
     *            the arguments
     * @throws IOException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws ParseException
     */
    public static void main(String[] args) throws IllegalBlockSizeException,
            BadPaddingException, IOException, ParseException {

        long t0 = System.currentTimeMillis();
        // Encrypt.test();
        for (int i = 0; i < 100; i++) {
            System.out.println(Encrypt.getLicenseType());
            System.out.println(Encrypt.getLicenseValue());
            System.out.println(Encrypt.getLicenseIssueDate());
        }
        System.out.println(System.currentTimeMillis() - t0);
        // Here is an example that uses the class
        try {

            String code = new String(Encrypt.loadFile("license.txt"));
            System.out.println(code);

            String decrypted = Encrypt.decrypt(code);

            System.out.println(decrypted);

        } catch (Exception e) {
            if (e instanceof BadPaddingException) {
                throw new RuntimeException("Incorrect passphrase!");
            } else if (e instanceof Base64FormatException) {
                throw new RuntimeException("Incorrect file!");
            } else if (e instanceof IllegalArgumentException) {
                throw new RuntimeException("Incorrect file argument!");
            } else {
                throw new RuntimeException(e);
            }
            // e.printStackTrace();
        }
    }

    public static void test() {
        // Here is an example that uses the class
        try {
            // Create encrypter/decrypter class
            Encrypt encrypter = new Encrypt();

            // Encrypt
            byte[] encrypted =
                    encrypter.encrypt("26-09-2008;TLD;opentracker.net");

            System.out.println(new String(encrypted));

            // Decrypt
            String back = new String(Encrypt.decrypt(new String(encrypted)));
            System.out.println(back);

        } catch (Exception e) {
            if (e instanceof BadPaddingException) {
                throw new RuntimeException("Incorrect passphrase!");
            } else if (e instanceof Base64FormatException) {
                throw new RuntimeException("Incorrect file!");
            } else if (e instanceof IllegalArgumentException) {
                throw new RuntimeException("Incorrect file argument!");
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    // Iteration count
    /** The iteration count. */
    private int iterationCount = 20;

    private final String PASSPHRASE = "Elements";

    // 8-byte Salt
    /** The salt. */
    private byte[] salt =
            { (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32, (byte) 0x56,
                    (byte) 0x35, (byte) 0xE3, (byte) 0x03 };

    /**
     * Instantiates a new encrypt.
     * 
     * @param passPhrase
     *            the pass phrase
     * 
     * @throws NoSuchAlgorithmException
     *             the no such algorithm exception
     * @throws NoSuchPaddingException
     *             the no such padding exception
     * @throws InvalidKeySpecException
     *             the invalid key spec exception
     * @throws InvalidKeyException
     *             the invalid key exception
     * @throws InvalidAlgorithmParameterException
     *             the invalid algorithm parameter exception
     */
    public Encrypt() throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeySpecException, InvalidKeyException,
            InvalidAlgorithmParameterException {
        // Create the key
        KeySpec keySpec =
                new PBEKeySpec(PASSPHRASE.toCharArray(), salt, iterationCount);
        SecretKey key =
                SecretKeyFactory.getInstance("PBEWithMD5AndDES")
                        .generateSecret(keySpec);
        ecipher = Cipher.getInstance(key.getAlgorithm());
        dcipher = Cipher.getInstance(key.getAlgorithm());

        // Prepare the parameter to the ciphers
        AlgorithmParameterSpec paramSpec =
                new PBEParameterSpec(salt, iterationCount);

        // Create the ciphers
        ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
        dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
    }

    /**
     * Encrypt.
     * 
     * @param str
     *            the str
     * 
     * @return the byte[]
     * 
     * @throws IllegalBlockSizeException
     *             the illegal block size exception
     * @throws BadPaddingException
     *             the bad padding exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private byte[] encrypt(String str) throws IllegalBlockSizeException,
            BadPaddingException, IOException {
        // Encode the string into bytes using utf-8
        byte[] utf8 = str.getBytes("UTF8");

        // Encrypt
        byte[] enc = ecipher.doFinal(utf8);

        // Encode bytes to base64 to get a string
        return new Base64Encoder(enc).processBytes();
    }
}