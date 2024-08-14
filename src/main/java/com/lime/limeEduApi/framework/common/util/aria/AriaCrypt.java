package com.lime.limeEduApi.framework.common.util.aria;

import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;


public class AriaCrypt {

	/**
	 * ARIA encryption algorithm block size
	 */
	private static final int ARIA_BLOCK_SIZE = 16;

	private static final String charset = null;

	private static int keySize = 0;

	/**
	 * ARIA algorithm to encrypt the data.
	 *
	 * @param data
	 *            Target Data
	 * @return Encrypted data
	 * @throws UnsupportedEncodingException
	 *             If character is not supported
	 * @throws InvalidKeyException
	 *             If the Masterkey is not valid
	 */
	public static String encrypt(String data) throws UnsupportedEncodingException, InvalidKeyException {
		try {

			if (StringUtils.isEmpty(data) || "null".equals(data.toLowerCase())) {
				return "";
			}

			byte[] key1 = new byte[16];
			// for( int i = 0; i < key1.length; i++ ) {
			String str = "1234567890123456";
			key1 = str.getBytes("UTF-8");
			// System.out.println("------key====>"+key1[0] );
			// }
			keySize = key1.length * 8;
			// System.out.println("------keySize====>"+keySize );
			byte[] encrypt = null;
			if (charset == null) {
				encrypt = BlockPadding.getInstance().addPadding(data.getBytes("UTF-8"), ARIA_BLOCK_SIZE);
			} else {
				encrypt = BlockPadding.getInstance().addPadding(data.getBytes(charset), ARIA_BLOCK_SIZE);
			}

			ARIAEngine engine = new ARIAEngine(keySize);
			engine.setKey(key1);
			engine.setupEncRoundKeys();

			int blockCount = encrypt.length / ARIA_BLOCK_SIZE;
			for (int i = 0; i < blockCount; i++) {

				byte buffer[] = new byte[ARIA_BLOCK_SIZE];
				System.arraycopy(encrypt, (i * ARIA_BLOCK_SIZE), buffer, 0, ARIA_BLOCK_SIZE);
				// System.out.println("buffer======0000=========>"+buffer[i]);
				buffer = engine.encrypt(buffer, 0);
				// System.out.println("buffer=====1111==========>"+buffer);
				System.arraycopy(buffer, 0, encrypt, (i * ARIA_BLOCK_SIZE), buffer.length);
			}

			/*
			 * length에 따라 복호화시 ? 생기는 버그 있어서  ?개 생기는 경우 length+' '으로  ? 생기는 문제 해결하기 위해 소스 추가
			 */
			if(data.equals( decrypt(Base64.toString(encrypt)))){
				return Base64.toString(encrypt);
			}else{
				data = data + " ";

				encrypt = null;
				if (charset == null) {
					encrypt = BlockPadding.getInstance().addPadding(data.getBytes("UTF-8"), ARIA_BLOCK_SIZE);
				} else {
					encrypt = BlockPadding.getInstance().addPadding(data.getBytes(charset), ARIA_BLOCK_SIZE);
				}
				engine = new ARIAEngine(keySize);
				engine.setKey(key1);
				engine.setupEncRoundKeys();

				blockCount = encrypt.length / ARIA_BLOCK_SIZE;
				for (int i = 0; i < blockCount; i++) {

					byte buffer[] = new byte[ARIA_BLOCK_SIZE];
					System.arraycopy(encrypt, (i * ARIA_BLOCK_SIZE), buffer, 0, ARIA_BLOCK_SIZE);
					// System.out.println("buffer======0000=========>"+buffer[i]);
					buffer = engine.encrypt(buffer, 0);
					// System.out.println("buffer=====1111==========>"+buffer);
					System.arraycopy(buffer, 0, encrypt, (i * ARIA_BLOCK_SIZE), buffer.length);
				}

				return Base64.toString(encrypt);
//				return encrypt(data);

			}

		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * ARIA algorithm to DecryptKey the data.
	 *
	 * @param data
	 *            Target Data
	 * @return Decrypted data
	 * @throws UnsupportedEncodingException
	 *             If character is not supported
	 * @throws InvalidKeyException
	 *             If the Masterkey is not valid
	 */
	public static String decrypt(String data) throws UnsupportedEncodingException, InvalidKeyException {
		try {
			byte[] key1 = new byte[16];
			String str = "1234567890123456";
			key1 = str.getBytes("UTF-8");
			keySize = key1.length * 8;
			byte[] decrypt = Base64.toByte(data);

			if (null == data || "".equals(data)) {
				return data;
			}

			ARIAEngine engine = new ARIAEngine(keySize);
			engine.setKey(key1);
			engine.setupDecRoundKeys();

			int blockCount = decrypt.length / ARIA_BLOCK_SIZE;
			for (int i = 0; i < blockCount; i++) {

				byte buffer[] = new byte[ARIA_BLOCK_SIZE];
				System.arraycopy(decrypt, (i * ARIA_BLOCK_SIZE), buffer, 0, ARIA_BLOCK_SIZE);

				buffer = engine.decrypt(buffer, 0);
				System.arraycopy(buffer, 0, decrypt, (i * ARIA_BLOCK_SIZE), buffer.length);
			}

			String result  = "";
			if (charset == null) {
				result = new String(BlockPadding.getInstance().removePadding(decrypt, ARIA_BLOCK_SIZE), "UTF-8");
				if(result.endsWith(" ")){
					result = result.replaceAll(" ", "");
				}
				return result;

			} else {
				result = new String(BlockPadding.getInstance().removePadding(decrypt, ARIA_BLOCK_SIZE), charset);
				if(result.endsWith(" ")){
					result = result.replaceAll(" ", "");
				}
				return result;
			}
		} catch (Exception e) {
			return "";
		}
	}


}

