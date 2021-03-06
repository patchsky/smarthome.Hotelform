package com.kincony.utils;

/**
 *  LICENSE AND TRADEMARK NOTICES
 *  
 *  Except where noted, sample source code written by Motorola Mobility Inc. and
 *  provided to you is licensed as described below.
 *  
 *  Copyright (c) 2012, Motorola, Inc.
 *  All  rights reserved except as otherwise explicitly indicated.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  - Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *
 *  - Redistributions in binary form must reproduce the above copyright notice,
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 *
 *  - Neither the name of Motorola, Inc. nor the names of its contributors may
 *  be used to endorse or promote products derived from this software without
 *  specific prior written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 *  AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 *  IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 *  ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 *  LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 *  SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 *  INTERRUPTION) HOWEVER  CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 *  CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 *  ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 *  POSSIBILITY OF SUCH DAMAGE.
 *  
 *  Other source code displayed may be licensed under Apache License, Version
 *  2.
 *  
 *  Copyright ¬© 2012, Android Open Source Project. All rights reserved unless
 *  otherwise explicitly indicated.
 *  
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not
 *  use this file except in compliance with the License. You may obtain a copy
 *  of the License at
 *  
 *  http://www.apache.org/licenses/LICENSE-2.0.
 *  
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  License for the specific language governing permissions and limitations
 *  under the License.
 *  
 */

// Please refer to the accompanying article at 
// http://developer.motorola.com/docs/using_the_advanced_encryption_standard_in_android/
// A tutorial guide to using AES encryption in Android
// First we generate a 256 bit secret key; then we use that secret key to AES encrypt a plaintext message.
// Finally we decrypt the ciphertext to get our original message back.
// We don't keep a copy of the secret key - we generate the secret key whenever it is needed, 
// so we must remember all the parameters needed to generate it -
// the salt, the IV, the human-friendly passphrase, all the algorithms and parameters to those algorithms.
// Peter van der Linden, April 15 2012

import java.net.URLEncoder;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.util.Iterator;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class AES {
	private final static int HASH_ITERATIONS = 110;
	private final static int KEY_LENGTH = 128;

	// private final String KEY_GENERATION_ALG = "PBEWITHSHAANDTWOFISH-CBC";

	private final static String KEY_GENERATION_ALG = "PBKDF2WithHmacSHA1";

	private static char[] humanPassphrase = { 'a', 'n', ' ', 'u', 'n', 'b',
			'r', 'e', 'a', 'k', 'a', 'b', 'l', 'e', ' ', 'c', 'r', 'y', 'p',
			't', 'o', 'g', 'r' , 'a', 'p', 'h', 'i', 'c', ' ', 'k', 'e', 'y'};

	// char[] humanPassphrase = { 'v', 't', 'i', 'o', 'n','s','f','o','t', '.',
	// 'c', 'o', 'm',
	// 'p'};
	private static byte[] salt = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0xA, 0xB, 0xC,
			0xD, 0xE, 0xF }; // must save this for next time we want the key

	private static PBEKeySpec myKeyspec = new PBEKeySpec(humanPassphrase, salt,
			HASH_ITERATIONS, KEY_LENGTH);
	private static final String CIPHERMODEPADDING = "AES/CBC/PKCS7Padding";

	private static SecretKeyFactory keyfactory = null;
	private static SecretKey sk = null;
	private static SecretKeySpec skforAES = null;
	private static byte[] iv = { 0xA, 1, 0xB, 5, 4, 0xF, 7, 9, 0x17, 3, 1, 6,
			8, 0xC, 0xD, 91 };

	private static IvParameterSpec IV;

	static {
		try {
			// 这个地方调用BouncyCastleProvider 让java支持PKCS7Padding
			Security.addProvider(new BouncyCastleProvider());

			keyfactory = SecretKeyFactory.getInstance(KEY_GENERATION_ALG);
			sk = keyfactory.generateSecret(myKeyspec);
		} catch (NoSuchAlgorithmException nsae) {
			System.out.println("no key factory support for PBEWITHSHAANDTWOFISH-CBC");
		} catch (InvalidKeySpecException ikse) {
			System.out.println("invalid key spec for PBEWITHSHAANDTWOFISH-CBC");
		}

		// This is our secret key. We could just save this to a file instead of
		// regenerating it
		// each time it is needed. But that file cannot be on the device (too
		// insecure). It could
		// be secure if we kept it on a server accessible through https.
		byte[] skAsByteArray = sk.getEncoded();
		// Log.d("",
		// "skAsByteArray=" + skAsByteArray.length + ","
		// + Base64Encoder.encode(skAsByteArray));
		skforAES = new SecretKeySpec(skAsByteArray, "AES");

		IV = new IvParameterSpec(iv);
	}

	/**
	 * 加密
	 */
	public static String encrypt(String plaintext) {
		try {
			if (StringUtils.isEmpty(plaintext))
				return "";

			byte[] ciphertext = encrypt(CIPHERMODEPADDING, skforAES, IV,
					plaintext.getBytes("UTF-8"));
			String base64_ciphertext = Base64Encoder.encode(ciphertext);
			return base64_ciphertext;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 解密
	 */
	public static String decrypt(String ciphertext_base64) {
		try {
			if (StringUtils.isEmpty(ciphertext_base64))
				return "";
			byte[] s = Base64Decoder.decodeToBytes(ciphertext_base64);
			String decrypted = new String(decrypt(CIPHERMODEPADDING, skforAES,
					IV, s));

			return decrypted;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	// Use this method if you want to add the padding manually
	// AES deals with messages in blocks of 16 bytes.
	// This method looks at the length of the message, and adds bytes at the end
	// so that the entire message is a multiple of 16 bytes.
	// the padding is a series of bytes, each set to the total bytes added (a
	// number in range 1..16).
	private byte[] addPadding(byte[] plain) {
		byte plainpad[] = null;
		int shortage = 16 - (plain.length % 16);
		// if already an exact multiple of 16, need to add another block of 16
		// bytes
		if (shortage == 0)
			shortage = 16;

		// reallocate array bigger to be exact multiple, adding shortage bits.
		plainpad = new byte[plain.length + shortage];
		for (int i = 0; i < plain.length; i++) {
			plainpad[i] = plain[i];
		}
		for (int i = plain.length; i < plain.length + shortage; i++) {
			plainpad[i] = (byte) shortage;
		}
		return plainpad;
	}

	// Use this method if you want to remove the padding manually
	// This method removes the padding bytes
	private byte[] dropPadding(byte[] plainpad) {
		byte plain[] = null;
		int drop = plainpad[plainpad.length - 1]; // last byte gives number of
													// bytes to drop

		// reallocate array smaller, dropping the pad bytes.
		plain = new byte[plainpad.length - drop];
		for (int i = 0; i < plain.length; i++) {
			plain[i] = plainpad[i];
			plainpad[i] = 0; // don't keep a copy of the decrypt
		}
		return plain;
	}

	private static byte[] encrypt(String cmp, SecretKey sk, IvParameterSpec IV,
			byte[] msg) {
		try {
			Cipher c = Cipher.getInstance(cmp);
			c.init(Cipher.ENCRYPT_MODE, sk, IV);
			return c.doFinal(msg);
		} catch (NoSuchAlgorithmException nsae) {
			nsae.printStackTrace();
			System.out.println("no cipher getinstance support for " + cmp);
		} catch (NoSuchPaddingException nspe) {
			System.out.println("no cipher getinstance support for padding "
					+ cmp);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			System.out.println("invalid key exception");
		} catch (InvalidAlgorithmParameterException e) {
			System.out.println("invalid algorithm parameter exception");
		} catch (IllegalBlockSizeException e) {
			System.out.println("illegal block size exception");
		} catch (BadPaddingException e) {
			System.out.println("bad padding exception");
		}
		return null;
	}

	private static byte[] decrypt(String cmp, SecretKey sk, IvParameterSpec IV,
			byte[] ciphertext) {
		try {
			Cipher c = Cipher.getInstance(cmp);
			c.init(Cipher.DECRYPT_MODE, sk, IV);
			return c.doFinal(ciphertext);
		} catch (NoSuchAlgorithmException nsae) {
			System.out.println("no cipher getinstance support for " + cmp);
		} catch (NoSuchPaddingException nspe) {
			System.out.println("no cipher getinstance support for padding "
					+ cmp);
		} catch (InvalidKeyException e) {
			System.out.println("invalid key exception");
		} catch (InvalidAlgorithmParameterException e) {
			System.out.println("invalid algorithm parameter exception");
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
			System.out.println("illegal block size exception");
		} catch (BadPaddingException e) {
			System.out.println("bad padding exception");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 递归加密JSON所有数据
	 */
	public static String AESJson(JSONObject json) {
		Iterator iterator = json.keys();

		if (json.get("code") != null) {
			if (json.getInt("code") == -1)
				return json.toString();
		}

		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			if (key.equals("code") || key.equals("msg") || key.equals("page")
					|| key.equals("pagecount") || key.equals("pagesize")
					|| key.equals("total"))
				continue;
			if (json.get(key) instanceof JSONArray) {
				for (int i = 0; i < json.getJSONArray(key).size(); i++) {
					JSONObject obj = json.getJSONArray(key).getJSONObject(i);
					if (!obj.isNullObject())
						AESJson(obj);
				}
			} else if (json.get(key) instanceof JSONObject) {
				JSONObject obj = json.getJSONObject(key);
				if (!obj.isNullObject())
					AESJson(obj);
			} else if (!json.getString(key).equals("null")) {
				json.put(key, encrypt(json.getString(key)));
			}
		}
		return json.toString();
	}

	public static String getStr(String... p) {
		String raw = "";
		String urlRaw = "";
		for (int i = 0; i < p.length; i++) {
			String[] str = p[i].split("@");
			if (str[0].equals("page") || str[0].equals("pagesize")) {
				raw += str[0] + "=" + str[1] + "&";
				urlRaw += str[0] + "=" + str[1] + "&";
			} else {
				raw += str[0] + "=" + AES.encrypt(str[1]) + "&";
				urlRaw += str[0] + "=" + URLEncoder.encode(AES.encrypt(str[1]))
						+ "&";
			}

		}
		raw = raw.substring(0, raw.length() - 1);
		urlRaw = urlRaw.substring(0, urlRaw.length() - 1);
		/*System.out.println("SIGN:" + Md5.MD5(raw + "12345"));*/
		System.out.println("RAW:" + urlRaw);
		return "";
	}

	public static void main(String[] args) {
	/*	String encrypt = encrypt("马法师打发实打实大师");
		System.err.println(encrypt);
		System.err.println(decrypt(encrypt));*/
		System.err.println(encrypt("13280715302"));
		System.err.println(decrypt("uDsEWu+6N3O93/EM5aVzqg=="));
		/*System.err.println(humanPassphrase);*/
		// String str =
		// "Phone=lfm/RSugiJUNgIUCctQP8A==&pwd=Vo4CmchovSjR592ySrgOSg==";
		// System.out.println(Md5.MD5(str));

	}

}