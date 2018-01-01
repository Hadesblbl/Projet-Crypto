package encryption;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class Encryption {

	final static byte[] salt = { (byte) 0x32, (byte) 0x11, (byte) 0xA2, (byte) 0x3C, (byte) 0x43, (byte) 0xA2, (byte) 0xE1, (byte) 0x23 };

	/**
	 * @param password mdp
	 * @param mode (Cipher.ENCRYPT_MODE ou Cipher.DECRYPT_MODE selon l'utilisation)
	 * @param salt sel
	 * @return
	 */
	private static Cipher getCipher(char[] password, int mode, byte[] salt) { //Pareil que l'autre Cipher mais on a pas besoin d'instance de la classe pour l'utiliser
		try {
			Cipher cipher;
			
			IvParameterSpec iv = new IvParameterSpec("testTestTestTest".getBytes("UTF-8"));
			SecretKeyFactory keyFact = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");//"PBEWithMD5AndDES"
			PBEKeySpec kSpecs = new PBEKeySpec(password,salt, 65536, 128);
			SecretKey key = keyFact.generateSecret(kSpecs); //On crée la clé secrète
			SecretKey secret = new SecretKeySpec(key.getEncoded(), "AES");
			
			cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(mode, secret, iv); //On crée et initialise le Cipher grâce à la clé
			return cipher;

		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | InvalidKeySpecException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		System.out.println("Didn't manage to create the Cipher");
		return null;
	}
	
	/**
	 * encrypte bytearray
	 * @param password
	 * @param bytearray
	 * @param mode
	 * @return 
	 */
	private static byte[] encrypt(char[] password,byte[] bytearray){	
		//32 11 a2 3c 43 a2 e1 23
		byte[] salt = { (byte) 0x32, (byte) 0x11, (byte) 0xA2, (byte) 0x3C, (byte) 0x43, (byte) 0xA2, (byte) 0xE1, (byte) 0x23 };

		Cipher cipher = getCipher(password, Cipher.ENCRYPT_MODE, salt);
		System.out.println("cipher: " + cipher);
		try {
			return cipher.doFinal(bytearray);
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
		return bytearray;
	}
	
	
	/**
	 * Utilise Cipher en DECRYPT_MODE
	 * @param password
	 * @param bytearray
	 * @return
	 */
	private static byte[] decrypt(char[] password,byte[] bytearray){
			Cipher cipher = getCipher(password, Cipher.DECRYPT_MODE, salt);
			System.out.println("cipher: " + cipher);
			try {
				return cipher.doFinal(bytearray);
			} catch (IllegalBlockSizeException | BadPaddingException e) {
				e.printStackTrace();
			}
			return bytearray;
		}
	
	/**
	 * retourne un tableau de byte à partir des rectangles sélectionnés
	 * @param rectangles
	 * @param image
	 * @return
	 */
	private static byte[] byteArrayFromSelectedRectangle(ArrayList<Rectangle> rectangles,BufferedImage image){
		StringBuilder result = new StringBuilder();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				for (Rectangle r : rectangles) {
					if (r.contains(new Point(i, j))) {
						baos.write(image.getRGB(i, j));
						//result.append(String.valueOf(image.getRGB(i, j)));
						break;
					}
				}
			}
		}
		return baos.toByteArray();
	}
	
	/**
	 * 
	 * @param rectangles
	 * @param image
	 * @return le nombre de pixels cryptés dans l'image
	 */
	private static int nbPixels(ArrayList<Rectangle> rectangles,BufferedImage image){
		int nb=0;
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				for (Rectangle r : rectangles) {
					if (r.contains(new Point(i, j))) {
						nb += 1;
						break;
					}
				}
			}
		}
		return nb;
	}
	
	/**
	 * change les bytes en int et les utilise comme valeur rgb pour reremplir
	 * @param rectangles
	 * @param image
	 * @param cryptedArray
	 * @return
	 */
	private static BufferedImage insertRectanglesInImage(ArrayList<Rectangle> rectangles, BufferedImage image, byte[] cryptedArray){
		int index=0;
		System.out.println("------------------------------");
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				for (Rectangle r : rectangles) {
					if (r.contains(new Point(i, j))) {
						if (index+5<cryptedArray.length){
							//int rgb = cryptedArray[index + 5] << 40 | cryptedArray[index + 4] << 32 | cryptedArray[index + 3] << 24 | cryptedArray[index + 2] << 16 | cryptedArray[index + 1] << 8 | cryptedArray[index];
							image.setRGB(i, j, cryptedArray[index]);
							//System.out.println(String.valueOf(rgb));
						}
						index += 1;
						break;
					}
				}
			}
		}

		return image;
		
	}
	
	/**
	 * Encrypte l'image à partir du mot de passe, des rectangles et de l'image
	 * @param r
	 * @param image
	 * @param password
	 * @return
	 */
	public static BufferedImage encryptImage(ArrayList<Rectangle> r, BufferedImage image, char[] password){
		byte[] array= byteArrayFromSelectedRectangle(r,image);
		array=encrypt(password,array);
		image=insertRectanglesInImage(r,image,array);
		return image;
	}
	
	/**
	 * Décrypte l'image  à partir du mot de passe, des rectangles et de l'image cryptée
	 * @param r
	 * @param image
	 * @param password
	 * @return
	 */
	public static BufferedImage decryptImage(ArrayList<Rectangle> r,BufferedImage image,char[] password){
		byte[] array= byteArrayFromSelectedRectangle(r,image);
		array=decrypt(password,array);
		image=insertRectanglesInImage(r,image,array);
		return image;
	}
	
	/**
	 * @param r
	 * @param image
	 * @param password
	 * @return image normalement si le test se passe bien
	 */
	public static BufferedImage test(ArrayList<Rectangle> r,BufferedImage image,char[] password){
		return decryptImage(r,encryptImage(r,image,password),password);
	}
	
	public static boolean test2(ArrayList<Rectangle> r,BufferedImage image,char[] password){
		byte[] array= byteArrayFromSelectedRectangle(r,image);
		return array.equals(encrypt(password,decrypt(password,array)));
	}
}
