package wordlistgenerator.utility;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
import java.io.FilenameFilter;
import java.io.IOException;


public class FileManager implements java.io.Serializable{

  private String separator = java.io.File.separator;
  public static final int PATTERN_NONE = 0;
  public static final int PATTERN_STARTS_WITH = 1;
  public static final int PATTERN_ENDS_WITH = 2;
  public static final int PATTERN_CONTAINS = 3;

  /**Creates a new folder
   * @param folderName The name of the folder to be created.
   * @return <code>true</code> if the folder is created successfully;
   * <code>false</code> otherwise
   * */
  public static boolean createFolder(String folderName) {
    java.io.File objFile = new java.io.File(folderName);
    if (!objFile.isDirectory()) {
      return objFile.mkdirs();
    }
    return true;
  }

  public static boolean fileExists(String fileName) {
    if (!new java.io.File(fileName).exists()) {
      return false;
    }
    return true;
  }

  public static boolean folderExists(String folderName) {
      try {
          if (!new java.io.File(folderName).exists()) {
              return false;
          }
      } catch (Exception e) {
          return false;
      }
    return true;
  }

  /**Return the size of the file.
   * @param fileName The name of the target file.
   * @return <code>size</code> the size of the file in bytes.
   * */
  public static long getFileSize(String fileName) {
    return new java.io.File(fileName).length();
  }

  public static long getFileTimeStamp(String fileName){
      return new java.io.File(fileName).lastModified();
  }

  /**Copies a file to the path specified.
   * @param fileName The name of the file to be copied.
   * @param pathName The path where the file is to be copied.
   * @return <code>true</code> if the file is copied successfully;
   * <code>false</code> otherwise.
   * @throws IOException if the pathName
   * does not exists.
   * */
  public static boolean copyFile(String fileName, String pathName) throws java.io.IOException {
    boolean isCopy = false;
    try {
      if (!new java.io.File(pathName).exists()) {
        if (!new java.io.File(pathName).mkdirs()) {
          return false;
        }
      }
      java.io.FileInputStream fin = new java.io.FileInputStream(fileName);
      String tempPath = fileName.substring(fileName.lastIndexOf(getPathSeparator()) + 1, fileName.length());
      System.out.println("Temp Path:"+tempPath);
//      pathName += getPathSeparator();
      pathName += tempPath;
      java.io.FileOutputStream fout = new java.io.FileOutputStream(pathName);
      java.nio.channels.FileChannel inc = fin.getChannel();
      java.nio.channels.FileChannel outc = fout.getChannel();
      //inc.transferTo(0, inc.size(), outc);
      java.nio.ByteBuffer buffer = java.nio.ByteBuffer.allocateDirect((int) inc.size());
      int ret = inc.read(buffer);
      if (ret == -1) {
        isCopy = false;
      }
      buffer.flip();
      outc.write(buffer);
      fout.close();
      fin.close();
      isCopy = true;
      buffer = null;
    } catch (java.io.IOException e) {
        e.printStackTrace();
      isCopy = false;
      System.out.println("Error Occured : Unable to copy file " + fileName + " to " + pathName);
      System.out.println("Reason : " + e.getMessage());
    }
    return isCopy;
  }

  /**Deletes a file.
   * @param fileName The name of the file to be deleted.
   * @return <code>true</code> if the file is deleted successfully;
   * <code>false</code> otherwise.
   * */
  public static boolean deleteFile(String fileName) {
    return new java.io.File(fileName).delete();
  }

  /**Moves a file to specified path.
   * @param fileName The name of the file to be moved.
   * @param pathName The pathname where the file is to be moved.
   * @return <code>true</code> if the file is moved successfully;
   * <code>false</code> otherwise.
   * */
  public static boolean moveFile(String fileName, String pathName) {
    try {
      return (copyFile(fileName, pathName) && deleteFile(fileName));
    } catch (java.io.IOException ex) {
      System.out.println("Error Occured: Unable to move file (" + fileName + ").");
      return false;
    }
  }

  /**Write data to the given file.
   * @param fileName The name of the file in which the data is to be written.
   * @param data The data to be written.
   * @param append Boolean value indicating that whether to append the data
   * in the file.
   * @return <code>true</code> if the data is written successfully in the file;
   * <code>false</code> otherwise.
   * */
  public static boolean writeToFile(String fileName, String data, boolean append) {
    boolean isWrite = false;
    int dirIndex = fileName.lastIndexOf(getPathSeparator());
    if (dirIndex != -1) {
      String dir = fileName.substring(0, dirIndex) + getPathSeparator();
      java.io.File fDir = new java.io.File(dir);
      if (!fDir.exists()) {
        if (!fDir.mkdirs()) {
          return false;
        }
      }
    }
    try {
      java.io.FileOutputStream fout = new java.io.FileOutputStream(fileName, append);
      java.nio.channels.FileChannel fChannelWriter = fout.getChannel();
      byte[] bytesToWrite = data.getBytes();
      java.nio.ByteBuffer bBuffW = java.nio.ByteBuffer.wrap(bytesToWrite);
      fChannelWriter.write(bBuffW);
      fChannelWriter.close();
      fout.close();
      isWrite = true;
    } catch (java.io.IOException ex) {
      System.out.println("Error Occured: Unable to write to file (" + fileName + ")");
      isWrite = false;
    }
    return isWrite;
  }

  /**Reads the contents of the file and returns the contents in the form of
   * String.
   * @param fileName The name of the file from which the data is to be read.
   * @return <code>String</code> containing the contents of the file.
   * */
  public static String readFile(String fileName) {
    String strData = "";
    if (!new java.io.File(fileName).exists()) {
      return strData;
    }
    try {
      java.io.FileInputStream fin = new java.io.FileInputStream(fileName);
      java.nio.channels.FileChannel fChannelReader = fin.getChannel();
      java.nio.ByteBuffer readBuffer = java.nio.ByteBuffer.allocateDirect((int) fChannelReader.size());
      fChannelReader.read(readBuffer);
      byte[] bytesRead = new byte[(int) fChannelReader.size()];
      readBuffer.position(0);
      readBuffer.get(bytesRead, 0, readBuffer.limit());
      strData = new String(bytesRead);
      fChannelReader.close();
      readBuffer.clear();
      fin.close();
      readBuffer = null;
      fChannelReader = null;
      fin = null;
    } catch (java.io.IOException ex) {
      System.out.println("Error Occured: Unable to read from file (" + fileName + ")");
    }
    return strData;
  }

  /**Returns the path Separator.
   * @return <code>String</code> the path separator character.
   * */
  public static String getPathSeparator() {
    return java.io.File.separator;
  }

  /**Rename the file.
   * @param targetFileName The file to be renamed.
   * @param destFileName The new name of the file.
   * @return <code>true</code> if the file is renamed successfully;
   * <code>false</code> otherwise.
   * */
  public static boolean renameFile(String targetFileName, String destFileName) {
    java.io.File targetFile = new java.io.File(targetFileName);
    java.io.File destinationFileName = new java.io.File(destFileName);
    if (!targetFile.exists()) {
      return false;
    }
    return targetFile.renameTo(destinationFileName);
  }

  public static java.io.File[] getDirectoryContents(String directory) {
    java.io.File file = new java.io.File(directory);
    java.io.File[] directoryContents = file.listFiles();
    return directoryContents;
  }

  public static java.io.File[] getFilesList(String directory) {
    java.io.File file = new java.io.File(directory);
    java.io.File[] directoryContents = null;
    directoryContents = file.listFiles(new FilenameFilter() {
      public boolean accept(java.io.File dir, String name) {
        try {
          if (new java.io.File(dir.getCanonicalPath() + java.io.File.separator + name).isFile()) {
            return true;
          }
        } catch (IOException ex) {
          return false;
        }
        return false;
      }
    });
    return directoryContents;
  }

  public static String[] getFiles(String directory) {
    java.io.File file = new java.io.File(directory);
    String[] directoryContents = null;
    directoryContents = file.list(new FilenameFilter() {
      public boolean accept(java.io.File dir, String name) {
        try {
          if (new java.io.File(dir.getCanonicalPath() + java.io.File.separator + name).isFile()) {
            return true;
          }
        } catch (IOException ex) {
          return false;
        }
        return false;
      }
    });
    return directoryContents;
  }

  public static long getFileCount(String directory) {
    String[] listFiles = getFiles(directory);
    if (listFiles == null) {
      return 0;
    }
    return listFiles.length;
  }

  public static String[] getFiles(String directory, int patternType, String pattern) {
    final int pattType = patternType;
    final String patt = pattern;
    java.io.File file = new java.io.File(directory);
    String[] directoryContents = null;
    directoryContents = file.list(new FilenameFilter() {
      public boolean accept(java.io.File dir, String name) {
        try {
          if (new java.io.File(dir.getCanonicalPath() + java.io.File.separator + name).isFile()) {
            boolean isSuccessfull = false;
            switch (pattType) {
              case PATTERN_NONE:
                isSuccessfull = true;
                break;
              case PATTERN_STARTS_WITH:
                isSuccessfull = name.startsWith(patt);
                break;
              case PATTERN_ENDS_WITH:
                isSuccessfull = name.endsWith(patt);
                break;
              case PATTERN_CONTAINS:
                isSuccessfull = (name.indexOf(patt) != -1);
                break;
            }
            return isSuccessfull;
          }
        } catch (IOException ex) {
          return false;
        }
        return false;
      }
    });
    return directoryContents;
  }

  public static java.io.File[] getDirectoyList(String directory) {
    java.io.File file = new java.io.File(directory);
    java.io.File[] directoryList = file.listFiles(new FilenameFilter() {
      public boolean accept(java.io.File dir, String name) {
        try {
          if (new java.io.File(dir.getCanonicalPath() + java.io.File.separator + name).isDirectory()) {
            return true;
          }
        } catch (IOException ex) {
        }
        return false;
      }
    });
    return directoryList;
  }
  
}
