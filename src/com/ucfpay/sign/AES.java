/**
 * Copyright: Copyright (c)2014
 * Company: UCFGROUP
 */
package com.ucfpay.sign;

import java.security.GeneralSecurityException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.ucfpay.config.UcfPayConfig;

/** 
* 功能：先锋支付AES加密处理核心文件，不需要修改
* 版本：1.0
* 修改日期：2014-11-09
* 说明：
* 以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
* 该代码仅供学习和研究先锋支付接口使用，只是提供一个示例
* */
public class AES {
	
	
	private static final String AES = "AES";
	
	
	private static final int DEFAULT_AES_KEYSIZE = 128;
	
	/**
	 * 使用AES加密原始字符串.
	 * 
	 * @param input 原始输入字符数组
	 * @param key 符合AES要求的密钥
	 */
	public static String aesEncrypt(String input) {
		 
		byte [] aes_key = Base64.decode(UcfPayConfig.key,Base64.NO_PADDING);
		return Base64.encodeToString(aes(input.getBytes(), aes_key, Cipher.ENCRYPT_MODE), Base64.NO_PADDING);
	}
	
	
	/**
	 * 使用AES解密字符串, 返回原始字符串.
	 * 
	 * @param input Hex编码的加密字符串
	 * @param key 符合AES要求的密钥
	 */
	public static String aesDecrypt(String input) {
		
		byte [] aes_key = Base64.decode(UcfPayConfig.key,Base64.NO_PADDING);
		byte[] decryptResult = aes(Base64.decode(input,Base64.NO_PADDING), aes_key, Cipher.DECRYPT_MODE);
		return new String(decryptResult);
	}

	
	/**
	 * 使用AES加密或解密无编码的原始字节数组, 返回无编码的字节数组结果.
	 * 
	 * @param input 原始字节数组
	 * @param key 符合AES要求的密钥
	 * @param mode Cipher.ENCRYPT_MODE 或 Cipher.DECRYPT_MODE
	 */
	private static byte[] aes(byte[] input, byte[] key, int mode) {
		try {
			SecretKey secretKey = new SecretKeySpec(key, AES);
			Cipher cipher = Cipher.getInstance(AES);
			cipher.init(mode, secretKey);
			return cipher.doFinal(input);
		} catch (GeneralSecurityException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 生成AES密钥,返回字节数组, 默认长度为128位(16字节).
	 */
	public static byte[] generateAesKey() {
		return generateAesKey(DEFAULT_AES_KEYSIZE);
	}

	/**
	 * 生成AES密钥,可选长度为128,192,256位.
	 */
	public static byte[] generateAesKey(int keysize) {
		try {
			KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
			keyGenerator.init(keysize);
			SecretKey secretKey = keyGenerator.generateKey();
			return secretKey.getEncoded();
		} catch (GeneralSecurityException e) {
			throw new RuntimeException(e);
		}
	}

}
