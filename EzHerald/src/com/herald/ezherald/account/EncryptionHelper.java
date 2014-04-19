package com.herald.ezherald.account;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 加密解密字符串用的
 * 
 * @author BorisHe
 * 
 */

public class EncryptionHelper {
	private static byte[] iv = { 1, 2, 3, 4, 5, 6, 7, 8 };
	private static String ENC_METHOD = "DES";
	private static String CIPHER_TRANSFORMATION = "DES/CBC/PKCS5Padding";
	public static String KEY = "ENC1234E";
	/**
	 * 加密
	 * 
	 * @param encryptStr
	 *            明文
	 * @param encryptKey
	 *            密钥
	 * @return 无法加密则返回null
	 */
	public static String encryptDES(String encryptStr, String encryptKey) {
		String retString = null;
		try {
			IvParameterSpec zeroIv = new IvParameterSpec(iv);
			SecretKeySpec key = new SecretKeySpec(encryptKey.getBytes(),
					ENC_METHOD);
			Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
			cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
			byte[] encryptedData = cipher.doFinal(encryptStr.getBytes());
			retString = byte2HexString(encryptedData);

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retString;
	}

	/**
	 * 解密
	 * 
	 * @param decryptStr
	 *            密文
	 * @param decryptKey
	 *            密钥
	 * @return 无法解密则返回null
	 */
	public static String decryptDES(String decryptStr, String decryptKey) {
		String retStr = null;

		byte[] byteMi = String2Byte(decryptStr);
		IvParameterSpec zeroIv = new IvParameterSpec(iv);
		SecretKeySpec key = new SecretKeySpec(decryptKey.getBytes(), ENC_METHOD);
		try {
			Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
			cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
			byte decryptedData[] = cipher.doFinal(byteMi);
			retStr = new String(decryptedData);

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return retStr;
	}

	/**
	 * byte[]转换成字符串
	 * 
	 * @param b
	 * @return
	 */
	public static String byte2HexString(byte[] b) {
		StringBuffer sb = new StringBuffer();
		int length = b.length;
		for (int i = 0; i < length; i++) {
			String stmp = Integer.toHexString(b[i] & 0xff);
			if (stmp.length() == 1)
				sb.append("0" + stmp);
			else
				sb.append(stmp);
		}
		return sb.toString();
	}

	/**
	 * 16进制转换成byte[]
	 * 
	 * @param hexString
	 * @return
	 */
	public static byte[] String2Byte(String hexString) {
		if (hexString.length() % 2 == 1)
			return null;
		byte[] ret = new byte[hexString.length() / 2];
		for (int i = 0; i < hexString.length(); i += 2) {
			ret[i / 2] = Integer.decode("0x" + hexString.substring(i, i + 2))
					.byteValue();
		}
		return ret;
	}
}
