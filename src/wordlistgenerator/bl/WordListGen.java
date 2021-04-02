/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wordlistgenerator.bl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.swing.JOptionPane;
import wordlistgenerator.WordListGeneratorApp;
import wordlistgenerator.WordListGeneratorProgressBar;
import wordlistgenerator.utility.AppZip;
import wordlistgenerator.utility.FileManager;
import wordlistgenerator.utility.SendMailUsingAuthentication;

/**
 *
 * @author Abdullah
 */
public class WordListGen implements Runnable {
 
    private int wordLength;
    private char[] alphabet;
    private boolean isFixLength= true;
    private String fileName;
    private FileWriter fstream;
    private BufferedWriter out;
    private File file;
    private int counter = 1;
    private String strFolderName;
    private Setting setting;
    private final long MB = 1048576;
    
    private int start;
    private int end;
    private WordListGeneratorProgressBar wordListGeneratorProgressBar;
    
    public WordListGen() {
        setting = WordListGeneratorApp.getApplication().setting;
    }
    public WordListGeneratorProgressBar getWordListGeneratorProgressBar() {
        return wordListGeneratorProgressBar;
    }

    public void setWordListGeneratorProgressBar(WordListGeneratorProgressBar wordListGeneratorProgressBar) {
        this.wordListGeneratorProgressBar = wordListGeneratorProgressBar;
    }
    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }
    
    
    public char[] getAlphabet() {
        return alphabet;
    }

    public void setAlphabet(char[] alphabet) {
        this.alphabet = alphabet;
    }

    public int getWordLength() {
        return wordLength;
    }

    public void setWordLength(int wordLength) {
        this.wordLength = wordLength;
    }

    public boolean isFixLength() {
        return isFixLength;
    }

    public void setIsFixLength(boolean isFixLength) {
        this.isFixLength = isFixLength;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }      
    
    private  void generate(int wl, char[] alpha) {
        
        try {            
            
            
            int wordlength = wl; //10;
            char[] alphabet = alpha; //{ '0', '1','2' };
            final long MAX_WORDS = (long) Math.pow(alphabet.length, wordlength);
            final int RADIX = alphabet.length;

            for (long i = 0; i < MAX_WORDS; i++) {
                int[] indices = convertToRadix(RADIX, i, wordlength);
                char[] word = new char[wordlength];
                for (int k = 0; k < wordlength; k++) {
                    word[k] = alphabet[indices[k]];
                }
                if (setting.getFileSize()>0){
                    if (file.length()> setting.getFileSize() * MB){
                        counter ++;
                        closeFile();
                        createFile();
                    }
                }  
               // int val = Integer.parseInt( word.toString());
//                int val = Integer.parseInt(new String(word));
//                if (val>=start && val <=end){
                    out.write(word);
                    out.newLine();
                //}
                
                //out.flush();
               //System.out.println(file.length());                                
            }            
            
            //out.close();
        } catch (IOException ex) {
            Logger.getLogger(WordListGen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void closeFile(){
        try {
            out.close();
            fstream.close();
        } catch (IOException ex) {
            Logger.getLogger(CustomWordListGen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void createFile() {
        try {
            file = new File(strFolderName +File.separator+ fileName +counter+".txt");
            fstream = new FileWriter(file);
            out = new BufferedWriter(fstream);
        } catch (IOException ex) {
            Logger.getLogger(CustomWordListGen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private  int[] convertToRadix(int radix, long number, int wordlength) {
        int[] indices = new int[wordlength];
        for (int i = wordlength - 1; i >= 0; i--) {
            if (number > 0) {
                int rest = (int) (number % radix);
                number /= radix;
                indices[i] = rest;
            } else {
                indices[i] = 0;
            }
   
        }
        return indices;
    }

    public void run() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        strFolderName=  (timestamp.getYear() + 1900) + "-" + (timestamp.getMonth() + 1) + "-" + timestamp.getDate() + "-" +
            timestamp.getHours() + "-" + timestamp.getMinutes() + "-" + timestamp.getSeconds();
        strFolderName =setting.getFolderLocation()+File.separator +strFolderName;
        FileManager.createFolder(strFolderName);
        createFile();
        if (isFixLength){
            generate(wordLength,alphabet);
        }else{
            for (int i=1;i<= wordLength;i++){
                generate(i,alphabet);
            }
        }
        closeFile();
        AppZip appZip = new AppZip();
        appZip.setSourceFolder(strFolderName);
        appZip.generateFileList(new File(strFolderName));
        String strAttachment = strFolderName +".zip";
        appZip.zipIt(strAttachment);
        sendMail(strFolderName,strAttachment);
        wordListGeneratorProgressBar.setShowBar(false);
        wordListGeneratorProgressBar.dispose();;
        JOptionPane.showMessageDialog(null, "Word list generated.");
        //throw new UnsupportedOperationException("Not supported yet.");
    }
    private void sendMail(String strTimeStamp, String strAttachment){
        SendMailUsingAuthentication mailUsingAuthentication =
                new SendMailUsingAuthentication();
        
        try {
            String[] receipientsList = new String[1];
            receipientsList[0] = setting.getGmailAccount().getRecipientEmail();
            String subject = " Word List generated on = " +strTimeStamp;
            String message ="";
            String fromAddress = setting.getGmailAccount().getUserLogin();
            String authenticationPassword = setting.getGmailAccount().getPassword();           
            String[] attachments = new String[1];
            attachments[0] = strAttachment;
            mailUsingAuthentication.postMail(receipientsList,
                    subject, message, fromAddress, authenticationPassword, attachments);
        } catch (MessagingException messagingException) {
            JOptionPane.showMessageDialog(null, "Invalid Mail credential.");
            messagingException.printStackTrace();
        }
    }
}
