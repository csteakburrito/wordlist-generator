/*
 * WordListGeneratorApp.java
 */

package wordlistgenerator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;
import wordlistgenerator.bl.Setting;

/**
 * The main class of the application.
 */
public class WordListGeneratorApp extends SingleFrameApplication {

    public Setting setting;
    /**
     * At startup create and show the main frame of the application.
     */
    @Override protected void startup() {
        loadSettings();
        show(new WordListGeneratorView(this));
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of WordListGeneratorApp
     */
    public static WordListGeneratorApp getApplication() {
        return Application.getInstance(WordListGeneratorApp.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        launch(WordListGeneratorApp.class, args);
    }
    private void loadSettings(){

        try {
                FileInputStream fileIn = new FileInputStream("Setting.ser");
                ObjectInputStream in = new ObjectInputStream(fileIn);
                setting= (Setting) in.readObject();
                in.close();
                fileIn.close();
                }catch (FileNotFoundException ex){
                    setting = new Setting();
                    System.out.println("Setting File not found.");
                } 
                    catch (IOException i) {
                    i.printStackTrace();
                   
                } catch (ClassNotFoundException c) {
                    System.out.println("Student class not found");
                    c.printStackTrace();
                
                }
    }

}
