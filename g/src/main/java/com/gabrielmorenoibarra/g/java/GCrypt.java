/*
 * 
 */
package com.gabrielmorenoibarra.g.java;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Utility for encrypting/decrypting messages.
 */
public class GCrypt {

    private static final char[] HEX_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private IvParameterSpec vectorSpec;
    private SecretKeySpec secretKeySpec;
    private Cipher cipher;

    /**
     * Constructor.
     * @param vector Initialization vector.
     * @param secretKey Secret key.
     */
    public GCrypt(String vector, String secretKey) {
        vectorSpec = new IvParameterSpec(vector.getBytes());
        secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "AES");
        try {
            cipher = Cipher.getInstance("AES/CBC/NoPadding");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param text Message to encrypt.
     * @return an encrypted message.
     * @throws Exception
     */
    public String encrypt(String text) throws Exception {
        if (text == null || text.length() == 0) throw new Exception("Empty string");
        byte[] encrypted;
        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, vectorSpec);
            encrypted = cipher.doFinal(padString(text).getBytes());
        } catch (Exception e) {
            throw new Exception("[encrypt] " + e.getMessage());
        }
        return bytesToHex(encrypted);
    }

    /**
     * @param code Message to decrypt.
     * @return a decrypted message.
     * @throws Exception
     */
    public String decrypt(String code) throws Exception {
        if (code == null || code.length() == 0) throw new Exception("Empty string");
        byte[] decrypted;
        try {
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, vectorSpec);
            decrypted = cipher.doFinal(hexToBytes(code));
            if (decrypted.length > 0) { // Remove trailing zeros
                int trim = 0;
                for (int i = decrypted.length - 1; i >= 0; i--) if (decrypted[i] == 0) trim++;

                if (trim > 0) {
                    byte[] newArray = new byte[decrypted.length - trim];
                    System.arraycopy(decrypted, 0, newArray, 0, decrypted.length - trim);
                    decrypted = newArray;
                }
            }
        } catch (Exception e) {
            throw new Exception("[decrypt] " + e.getMessage());
        }
        return new String(decrypted);
    }

    /**
     * Convert from bytes to hexadecimal.
     * @param buf Byte array buffer.
     * @return a String composed by hexadecimal chars.
     */
    private String bytesToHex(byte[] buf) {
        char[] chars = new char[2 * buf.length];
        for (int i = 0; i < buf.length; ++i) {
            chars[2 * i] = HEX_CHARS[(buf[i] & 0xF0) >>> 4];
            chars[2 * i + 1] = HEX_CHARS[buf[i] & 0x0F];
        }
        return new String(chars);
    }

    /**
     * Convert from hexadecimal to bytes.
     * @param s String to convert.
     * @return a byte array.
     */
    private byte[] hexToBytes(String s) {
        if (s == null) {
            return null;
        } else if (s.length() < 2) {
            return null;
        } else {
            int len = s.length() / 2;
            byte[] buffer = new byte[len];
            for (int i = 0; i < len; i++) {
                buffer[i] = (byte) Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16);
            }
            return buffer;
        }
    }

    /**
     * Pads a String.
     * @param source String to pad.
     * @return a String with trailing zeros (to the right).
     */
    private String padString(String source) {
        char paddingChar = 0;
        int size = 16;
        int x = source.length() % size;
        int padLength = size - x;

        for (int i = 0; i < padLength; i++) {
            source += paddingChar;
        }
        return source;
    }
}