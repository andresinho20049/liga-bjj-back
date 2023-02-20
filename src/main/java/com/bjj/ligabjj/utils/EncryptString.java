package com.bjj.ligabjj.utils;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EncryptString {

	public static final String KEY = "Ba7ZvHLNeKbqH5lN";
	public static final String ALGORITHM = "AES";
	public static final String CIPHER_INSTANCE = "AES/ECB/PKCS5Padding";

	public static String encryptAES(String value) {
		try {
			// Create key and cipher
			Key aesKey = new SecretKeySpec(KEY.getBytes(), ALGORITHM);
			Cipher cipher = Cipher.getInstance(CIPHER_INSTANCE);
			// encrypt the text
			cipher.init(Cipher.ENCRYPT_MODE, aesKey);
			byte[] encBytes = cipher.doFinal(value.getBytes("utf-8"));

			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < encBytes.length; i++) {
				String hex = Integer.toHexString(encBytes[i] & 0xFF);
				if (hex.length() == 1) {
					hex = '0' + hex;
				}
				sb.append(hex);
			}

			// the encrypted String
			String enc = sb.toString();
			return enc;
		} catch (Exception e) {
			log.error(e.getMessage());
			return value;
		}
	}

	public static String descriptAES(String encrypted) {
		try {
			Key aesKey = new SecretKeySpec(KEY.getBytes(), ALGORITHM);
			Cipher cipher = Cipher.getInstance(ALGORITHM);

			if (encrypted.length() < 1)
				return encrypted;
			
			byte[] result = new byte[encrypted.length() / 2];
			for (int i = 0; i < encrypted.length() / 2; i++) {
				int high = Integer.parseInt(encrypted.substring(i * 2, i * 2 + 1), 16);
				int low = Integer.parseInt(encrypted.substring(i * 2 + 1, i * 2 + 2), 16);
				result[i] = (byte) (high * 16 + low);
			}

			// decrypt the text
			cipher.init(Cipher.DECRYPT_MODE, aesKey);
			return new String(cipher.doFinal(result));
		} catch (Exception e) {
			log.error(e.getMessage());
			return encrypted;
		}
	}

}
