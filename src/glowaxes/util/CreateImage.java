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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;


import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.batik.transcoder.image.TIFFTranscoder;
import org.apache.fop.svg.PDFTranscoder;
import org.apache.log4j.Logger;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;

// TODO: Auto-generated Javadoc
/**
 * The Class CreateImage.
 * @author <a href="mailto:eddie@tinyelements.com">Eddie Moojen</a>
 */
public class CreateImage implements Runnable {

    /** The jpg transcoder. */
    private static JPEGTranscoder jpgTranscoder = new JPEGTranscoder();

    // Define a static logger variable so that it references the
    // Logger instance named after class.
    /** The logger. */
    private static Logger logger =
            Logger.getLogger(CreateImage.class.getName());

    /** The pdf transcoder. */
    private static PDFTranscoder pdfTranscoder = new PDFTranscoder();

    /** The png transcoder. */
    private static PNGTranscoder pngTranscoder = new PNGTranscoder();

    /** The t. */
    private static Transcoder t;

    /** The tiff transcoder. */
    private static TIFFTranscoder tiffTranscoder = new TIFFTranscoder();

    /** The doc. */
    private org.w3c.dom.Document doc;

    /** The expiretime. */
    private long expiretime;

    /** The file. */
    private String file;

    /** The fs file. */
    private String fsFile;

    /** The fs path. */
    private String fsPath;

    /** The jpg quality. */
    private float jpgQuality = 0.8f;

    /** The png bits. */
    private int pngBits = 16;

    /** The thread. */
    private Thread thread;

    /** The type. */
    private String type;

    /**
     * Instantiates a new creates the image.
     * 
     * @param _doc the _doc
     * @param _file the _file
     * @param _expiretime the _expiretime
     * @param _fsPath the _fs path
     * @param _fsFile the _fs file
     */
    public CreateImage(org.w3c.dom.Document _doc, String _file,
            long _expiretime, String _fsPath, String _fsFile) {

        this.doc = _doc;
        this.file = _file;
        this.type =
                file.substring(file.lastIndexOf('.') + 1, file.length())
                        .toLowerCase();

        this.expiretime = _expiretime;
        this.fsFile = _fsFile;
        this.fsPath = _fsPath;

        if (!type.equals("jpg") && !type.equals("png") && !type.equals("tiff") && !type.equals("pdf")) {
            throw new RuntimeException(
                    "Unkown image format. Please specify jpg, png, pdf or tiff");
        }

        thread = new Thread(this);
        thread.start();
    }
    
    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public void run() {
        long t0 = System.currentTimeMillis();

        // get the type based transcoder
        if (type.equals("jpg")) {

            t = jpgTranscoder;

            // Set the transcoding hints.
            t.addTranscodingHint(JPEGTranscoder.KEY_QUALITY, new Float(
                    jpgQuality));

        } else if (type.equals("png")) {

            t = pngTranscoder;

            // Set the transcoding hints.
            t.addTranscodingHint(PNGTranscoder.KEY_INDEXED,
                    new Integer(pngBits));

        } else if (type.equals("tiff")) {
            
            t = tiffTranscoder;
            
        } else if (type.equals("pdf")) {
            
            t = pdfTranscoder;
            
        }

        // Create the transcoder input.
        TranscoderInput input = new TranscoderInput(doc);

        // Create the transcoder output.
        ByteArrayOutputStream ostream = new ByteArrayOutputStream();

        TranscoderOutput output = new TranscoderOutput(ostream);

        // // Save the image.
        ChartRegistry.SINGLETON.addChart(file, "".getBytes(), expiretime);

        try {
            t.transcode(input, output);
        } catch (TranscoderException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // // Flush and close the stream.
        try {
            ostream.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            ostream.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ChartRegistry.SINGLETON.addChart(file, ostream.toByteArray(),
                expiretime);

        if (fsPath != null && fsFile != null)
            ChartRegistry.SINGLETON.saveChart(file, "/Users/eddie/Desktop/",
                    "test.png");

        logger.info(file + " produced in "
                + ((System.currentTimeMillis() - t0)) + " ms");

    }

    /**
     * Serialize.
     * 
     * @param doc the doc
     * @param out the out
     * 
     * @throws Exception the exception
     */
    public void serialize(org.w3c.dom.Document doc, OutputStream out)
            throws Exception {
        OutputFormat format = new OutputFormat();
        format.setIndenting(true);
        format.setLineWidth(65);
        format.setIndent(2);
        XMLSerializer serializer = new XMLSerializer(out, format);
        serializer.serialize(doc);
    }

    /**
     * Sets the doc.
     * 
     * @param _doc the new doc
     */
    public void setDoc(org.w3c.dom.Document  _doc) {
        this.doc = _doc;
    }

    /**
     * Sets the jpg quality.
     * 
     * @param _jpgQuality the new jpg quality
     */
    public void setJpgQuality(float _jpgQuality) {
        this.jpgQuality = _jpgQuality;
    }

    /**
     * Sets the png bits.
     * 
     * @param _pngBits the new png bits
     */
    public void setPngBits(int _pngBits) {
        this.pngBits = _pngBits;
    }
}
