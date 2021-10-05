package com.benz.user.ops.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

import com.benz.user.ops.exception.UserOpsException;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DataEncryptionDecryption {

	private static SecretKeySpec secretKey;
	private static final String ALGORITHM = "AES";

	private static void prepareSecreteKey(String myKey) {
		byte[] key;
		MessageDigest sha = null;
		try {
			key = myKey.getBytes(StandardCharsets.UTF_8);
			sha = MessageDigest.getInstance("SHA-1");
			key = sha.digest(key);
			key = Arrays.copyOf(key, 16);
			secretKey = new SecretKeySpec(key, ALGORITHM);
		} catch (NoSuchAlgorithmException exception) {
			log.error("Failed to generate the secretKey");
			throw new UserOpsException("Failed to generate the secretKey due to " + exception.getMessage());
		}
	}

	public String encrypt(String strToEncrypt, String secret) {
		try {
			prepareSecreteKey(secret);
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
		} catch (Exception exception) {
			log.error("Error while encrypting: " + exception.toString());
			throw new UserOpsException("Error while encrypting due to " + exception.getMessage());
		}
	}

	public String decrypt(String strToDecrypt, String secret) {
		try {
			prepareSecreteKey(secret);
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
		} catch (Exception exception) {
			log.error("Error while decrypting: " + exception.toString());
			throw new UserOpsException("Error while decrypting due to " + exception.getMessage());
		}
	}
}
