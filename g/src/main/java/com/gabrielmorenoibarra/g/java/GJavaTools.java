package com.gabrielmorenoibarra.g.java;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Java Standard Tools.
 * Created by Gabriel Moreno on 2016-11-24.
 */
public class GJavaTools {

    public static void main(String[] strings) {

    }

    /**
     * @param is <code>InputStream</code> to parse.
     * @return a String from an <code>InputStream</code>.
     */
    public static String inputStreamToString(InputStream is) {
        String rLine;
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        try {
            while ((rLine = br.readLine()) != null) {
                sb.append(rLine).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    /**
     * Check if a String is a long number.
     * @param s String to check.
     * @return true if it is correct and false otherwise.
     */
    public static boolean isALongNumber(String s) {
        try {
            Long.parseLong(s);
        } catch (NumberFormatException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
        return true;
    }

    /**
     * Check if a String is a natural number (or zero).
     * @param s <code>String</code> to check.
     * @return true whether it is an unsigned integer number or not.
     */
    public static boolean isAUnsignedInteger(String s) {
        return s.matches("[0-9]*");
    }

    /**
     * Parse a boolean to a String '1' or '0' for avoiding incompatibilities on server database.
     * @param b Boolean to parse.
     * @return a String "integer" with the boolean parsed.
     */
    public static String boolean2String(boolean b) {
        return b ? "1" : "0";
    }

    /**
     * Parse a String '1' or '0' to a boolean.
     * @param s String to parse.
     * @return the boolean parsed.
     */
    public static boolean string2boolean(String s) {
        return s.equalsIgnoreCase("1");
    }

    /**
     * Parse an integer '1' or '0' to a boolean.
     * @param i Integer to parse.
     * @return the boolean parsed.
     */
    public static boolean int2Boolean(int i) {
        // WARNING: We suppose that if it isn't a '0', it will be a '1'. But we don't contemplate another case. It would be whatever String
        return i == 1;
    }

    /**
     * Splits a String separated with a specific character in an <code>ArrayList</code>.
     * @param s String to parse.
     * @param separator Character to split.
     * @return an <code>ArrayList</code>.
     */
    public static ArrayList<String> stringToArrayList(String s, String separator) {
        return new ArrayList<>(Arrays.asList(s.split(separator)));
    }

    /**
     * Replace all waste characters in a phone number.
     * @param countryCode The country code. E.g.: +34
     * @param phoneNumber The phone number. E.g.: +34 668 646 423
     * @return the phone number with the correct format. E.g.: 668646423
     */
    public static String cleanPhoneNumber(String countryCode, String phoneNumber) {
        String prefix = phoneNumber.startsWith("+") ? "+" : ""; // If the number start with a + we keep it
        phoneNumber = prefix + phoneNumber.replaceAll("\\D", "");

        // And then, we check if the phone number begins with the country code and remove it if applies:
        if (phoneNumber.startsWith(countryCode)) phoneNumber = phoneNumber.substring(countryCode.length());
        return phoneNumber;
    }

    /**
     * Write in a file.
     * @param file <code>File</code> where is written the information.
     * @param s A string with the text to write.
     */
    public void write2File(File file, String s) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
            bw.write(s);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Copy a <code>File</code>.
     * @param from Source file name.
     * @param to Destiny file name.
     * @return the file size in number of bytes. If it returns -1, it is due to the action failed.
     */
    public static int copyFile(String from, String to) {
        try {
            int byteSum = 0;
            int byteRead;
            if (new File(from).exists()) {
                InputStream is = new FileInputStream(from);
                OutputStream os = new FileOutputStream(to);
                byte[] buffer = new byte[1444];
                while ((byteRead = is.read(buffer)) != -1) {
                    byteSum += byteRead;
                    os.write(buffer, 0, byteRead);
                }
                is.close();
                os.close();
            }
            return byteSum;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        return file.exists() && file.delete();
    }

    public static boolean deleteFiles(String... fileNames) {
        boolean b = true;
        for (String fileName : fileNames) {
            if (!deleteFile(fileName)) {
                b = false;
                System.err.println("File '" + fileName + "' was not deleted.");
            }
        }
        if (b) System.out.println("Files were successfully deleted.");
        return b;
    }

    /**
     * Creates a folder if it doesn't exists yet.
     * It doesn't make missing parent directories.
     * @param dirName Folder name.
     * @return if it has been created.
     */
    public static boolean makeDir(String dirName) {
        File dir = new File(dirName);
        if (!dir.exists()) {
            if (dir.mkdir()) {
                System.out.println("Directory '" + dirName + "' has been created.");
                return true;
            }
        }
        return false;
    }

    /**
     * Replace an extension by another.
     * @param s File name with 3 characters extension.
     * @param newExtension New extension.
     * @return the formatted <code>String</code> with the new extension.
     */
    public static String replaceExtension(String s, String newExtension) {
        return s.substring(0, s.length() - 3) + newExtension;
    }

    /**
     * Log size of a file in MB or KB.
     * @param fileName Absolute path of the file.
     * @param magnitude Magnitude parameter. Use only 'MB' or 'KB'.
     */
    public static void logSizeFile(String fileName, String magnitude) {
        float divisor = 1;
        switch (magnitude) {
            case "MB":
                divisor = divisor * 1024 * 1024;
                break;
            case "KB":
                divisor = divisor * 1024;
                break;
            default:
                System.err.println("Magnitude parameter inserted doesn't exists. Please, use 'MB' or 'KB'.");
        }
        System.out.println(fileName + " size: " + (new File(fileName).length() / divisor) + " " + magnitude);
    }

    /**
     * @param number Number to round.
     * @param decimals Quantity of decimals desired.
     * @return a specific rounded number un with its corresponding decimals.
     */
    public static float round(float number, int decimals) {
        return (float) (Math.round(number * Math.pow(10, decimals)) / Math.pow(10, decimals));
    }
}