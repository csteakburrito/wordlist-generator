/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wordlistgenerator.bl;


import java.io.*;
import java.net.*;
import javax.swing.text.html.*;
import javax.swing.text.html.parser.*;

public class deTag extends HTMLEditorKit.ParserCallback {
  StringBuffer txt;
  Reader reader;

  // empty default constructor
  public deTag() {}

  // more convienient constructor
  public deTag(Reader r) {
    setReader(r);
  }

  public void setReader(Reader r) { reader = r; }

  public void parse() throws IOException {
    txt = new StringBuffer();
    ParserDelegator parserDelegator = new ParserDelegator();
    parserDelegator.parse(reader, this, true);
  }

  public void handleText(char[] text, int pos) {
    txt.append(text);
  }

  public String toString() {
    return txt.toString();
  }

 
}