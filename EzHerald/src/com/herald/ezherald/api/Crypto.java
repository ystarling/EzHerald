package com.herald.ezherald.api;

import android.util.Base64;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Crypto{
    private static byte[] encrypt(byte[] ivBytes, byte[] keyBytes, byte[] textBytes)
            throws java.io.UnsupportedEncodingException,
            NoSuchAlgorithmException,
            NoSuchPaddingException,
            InvalidKeyException,
            InvalidAlgorithmParameterException,
            IllegalBlockSizeException,
            BadPaddingException {
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        SecretKeySpec newKey = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);
        return cipher.doFinal(textBytes);
    }
    private static byte[] decrypt(byte[] ivBytes, byte[] keyBytes, byte[] textBytes)
            throws java.io.UnsupportedEncodingException,
            NoSuchAlgorithmException,
            NoSuchPaddingException,
            InvalidKeyException,
            InvalidAlgorithmParameterException,
            IllegalBlockSizeException,
            BadPaddingException {
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        SecretKeySpec newKey = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
        return cipher.doFinal(textBytes);
    }
    private static String key = "9f9094e65ef7245239d9f5d03fba74b4";
    private static byte[] ivBytes = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
    public static String encrypt(String text){
        String result = "";
        try{
            byte[] data = encrypt(ivBytes,key.getBytes(),text.getBytes());
            result = Base64.encodeToString(data,Base64.DEFAULT );
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
    public static String decrypt(String text){
        String result = "";
        try{
            byte[] data = decrypt(ivBytes,key.getBytes(),Base64.decode(text,Base64.DEFAULT));
            result = new String (data, "UTF-8");
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

}