/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wordlistgenerator.bl;

import java.io.Serializable;

/**
 *
 * @author Abdullah
 */
public class Setting implements Serializable {
    private boolean supportLeekSpeak;
    private boolean beginWord;
    private boolean endWord;
    private boolean addSpace;
    private boolean makeFirstLetterCapital;
    private boolean makeLastLetterCapital;
    private String beginWords;
    private String endWords;
    private GmailSetting gmailAccount;
    private String folderLocation;
    private int fileSize;
    
    public Setting() {
        supportLeekSpeak = false;
        beginWord = false;
        endWord = false;
        addSpace = false;
        makeFirstLetterCapital = false;
        gmailAccount = new GmailSetting();
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }
    
    public String getFolderLocation() {
        return folderLocation;
    }

    public void setFolderLocation(String folderLocation) {
        this.folderLocation = folderLocation;
    }

    
    public boolean isAddSpace() {
        return addSpace;
    }

    public void setAddSpace(boolean addSpace) {
        this.addSpace = addSpace;
    }

    public boolean isBeginWord() {
        return beginWord;
    }

    public void setBeginWord(boolean beginWord) {
        this.beginWord = beginWord;
    }

    public String getBeginWords() {
        return beginWords;
    }

    public void setBeginWords(String beginWords) {
        this.beginWords = beginWords;
    }

    public boolean isEndWord() {
        return endWord;
    }

    public void setEndWord(boolean endWord) {
        this.endWord = endWord;
    }

    public String getEndWords() {
        return endWords;
    }

    public void setEndWords(String endWords) {
        this.endWords = endWords;
    }

    public GmailSetting getGmailAccount() {
        return gmailAccount;
    }

    public void setGmailAccount(GmailSetting gmailAccount) {
        this.gmailAccount = gmailAccount;
    }

    public boolean isMakeFirstLetterCapital() {
        return makeFirstLetterCapital;
    }

    public void setMakeFirstLetterCapital(boolean makeFirstLetterCapital) {
        this.makeFirstLetterCapital = makeFirstLetterCapital;
    }

    public boolean isMakeLastLetterCapital() {
        return makeLastLetterCapital;
    }

    public void setMakeLastLetterCapital(boolean makeLastLetterCapital) {
        this.makeLastLetterCapital = makeLastLetterCapital;
    }

    public boolean isSupportLeekSpeak() {
        return supportLeekSpeak;
    }

    public void setSupportLeekSpeak(boolean supportLeekSpeak) {
        this.supportLeekSpeak = supportLeekSpeak;
    }
    
    
}
