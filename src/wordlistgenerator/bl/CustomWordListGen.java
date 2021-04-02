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
public class CustomWordListGen implements Runnable {

    private int wordLength;
    private String strText;
    private boolean isFixLength = true;
    private String fileName;
    private Setting setting;
    private FileWriter fstream;
    private BufferedWriter out;
    private File file;
    private final long MB = 1048576;
    private int counter=1;
    private String strFolderName;
    private boolean removeDuplicate;
    private WordListGeneratorProgressBar wordListGeneratorProgressBar;
    
    
    public CustomWordListGen() {
        setting = WordListGeneratorApp.getApplication().setting;
    }

    public WordListGeneratorProgressBar getWordListGeneratorProgressBar() {
        return wordListGeneratorProgressBar;
    }

    public void setWordListGeneratorProgressBar(WordListGeneratorProgressBar wordListGeneratorProgressBar) {
        this.wordListGeneratorProgressBar = wordListGeneratorProgressBar;
    }
    
    
    public boolean isRemoveDuplicate() {
        return removeDuplicate;
    }

    public void setRemoveDuplicate(boolean removeDuplicate) {
        this.removeDuplicate = removeDuplicate;
    }
    
    
    public String getStrText() {
        return strText;
    }

    public void setStrText(String strText) {
        this.strText = strText;
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

    private void generate(String strText) {
        try {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            strFolderName=  (timestamp.getYear() + 1900) + "-" + (timestamp.getMonth() + 1) + "-" + timestamp.getDate() + "-" +
                timestamp.getHours() + "-" + timestamp.getMinutes() + "-" + timestamp.getSeconds();
            strFolderName =setting.getFolderLocation()+File.separator +strFolderName;
            FileManager.createFolder(strFolderName);
            createFile();
            String str = strText;
            String stra[] = str.split("\\s+");
            if (isRemoveDuplicate()){
                stra = removeDuplicates(stra);
            }            
            int pSize = 1;
            int dataLength = stra.length;
            while (pSize <= dataLength) {
                for (int i = 0; i < dataLength - pSize; i++) {
                    for (int j = i; j <= i + pSize; j++) {
                        if (setting.getFileSize()>0){
                            if (file.length()> setting.getFileSize() * MB){
                                counter ++;
                                closeFile();
                                createFile();
                            }
                        }                        
                        String strWord = stra[j];
                        
                        if (setting.isMakeFirstLetterCapital()){
                            strWord =capitalizeFirstLetter(strWord);
                        }
                        if (setting.isMakeLastLetterCapital()){
                            strWord = capitalizeLastLetter(strWord);
                        }
                        if (setting.isBeginWord()){
                            strWord = setting.getBeginWords()+ strWord;
                        }
                        
                        if (setting.isEndWord()){
                            strWord = strWord + setting.getEndWords();
                        }
                        if (setting.isAddSpace()){
                            strWord =strWord +" ";
                        }
                        out.write(strWord);
                    }
                    out.newLine();
                }
                pSize++;
            }
            
            closeFile();
            AppZip appZip = new AppZip();
            appZip.setSourceFolder(strFolderName);
            appZip.generateFileList(new File(strFolderName));
            String strAttachment = strFolderName +".zip";
            appZip.zipIt(strFolderName +".zip");
            sendMail(strFolderName,strAttachment);
            JOptionPane.showMessageDialog(null, "Word list generated.");
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
    private void closeFile(){
        try {
            out.close();
            fstream.close();
        } catch (IOException ex) {
            Logger.getLogger(CustomWordListGen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static String capitalizeFirstLetter(String s) {
        if (s.length() == 0) {
            return s;
        }
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }
    public static String capitalizeLastLetter(String s) {
        if (s.length() == 0) {
            return s;
        }
        return  s.substring(0,s.length()-2) + s.substring(s.length()-2).toUpperCase() ;
    }
    public void run() {
        if (setting.isSupportLeekSpeak()){
            strText =replaceLeetSpeak(strText);
        }        
        generate(strText);
        wordListGeneratorProgressBar.setShowBar(false);
        wordListGeneratorProgressBar.dispose();;
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    private String replaceLeetSpeak(String strText) {
        String strLeet[][] = {{"a", "@"}, {"s", "$"}, {"o", "0"}, {"B", "8"}, {"L", "1"}, {"z", "2"}, {"x", "%"}, {"e", "3"},
            {"E", "3"}, {"Q", "9"}, {"s", "5"}, {"h", "#"}};

        for (int i = 0; i < strLeet.length; i++) {
           strText= strText.replaceAll(strLeet[i][1], strLeet[i][0]);
        }
        return strText;
    }
    private  String[] removeDuplicates(String[] strList){
        int strLength = strList.length;
        for (int i=0;i<strList.length;i++){
            String strTemp = strList[i];
            for (int j = i+1;j<strList.length;j++){
                if (!strTemp.equals("")&& strTemp.equals(strList[j])){
                    strList[j] = "";
                    strLength= strLength-1;
                }
            }
        }
        String[] finalList = new String[strLength];
        int x= 0;
        for (int i=0;i<strList.length;i++){
            if (!strList[i].equals("")){
                finalList[x] = strList[i];
                x++;
            }
        }
        return  finalList;
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
