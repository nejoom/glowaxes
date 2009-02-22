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

/**
 * HttpRequestGet.java
 * Copyright (c) 2002 by Dr. Herong Yang. All rights reserved.
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
// TODO: Auto-generated Javadoc

/**
 * The Class HttpRequestGet.
 * @author <a href="mailto:eddie@tinyelements.com">Eddie Moojen</a>
 */
public class HttpRequestGet {
   
   /**
    * Gets the.
    * 
    * @param path the path
    */
   public static void get(String path) {

      int port = 80;
      String host = "localhost";
      try {
         Socket c = new Socket(host,port);
         BufferedWriter w = new BufferedWriter(new OutputStreamWriter(
            c.getOutputStream()));
         BufferedReader r = new BufferedReader(new InputStreamReader(
            c.getInputStream()));
         String m = "GET "+ path + " HTTP/1.0";
         w.write(m,0,m.length());
         w.newLine();
         w.newLine();
         w.flush();
         while ((m=r.readLine())!= null) {
            System.out.println(m);
         }
         w.close();
         r.close();
         c.close();
      } catch (IOException e) {
         System.err.println(e.toString());
      }
   }
}