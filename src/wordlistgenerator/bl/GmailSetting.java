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
public class GmailSetting implements Serializable {
    private String userLogin;
    private String password;
    private String recipientEmail;

    public GmailSetting() {
        this.userLogin = "";
        this.password ="";
        this.recipientEmail = "";
    }
 
    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }
    
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }
    
}
