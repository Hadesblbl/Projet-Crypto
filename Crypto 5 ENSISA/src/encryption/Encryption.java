package encryption;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

public class Encryption {
	/**
	 * @param password mdp
	 * @param mode (Cipher.ENCRYPT_MODE ou Cipher.DECRYPT_MODE selon l'utilisation)
	 * @param crypt (méthode de cryptage)
	 * @param salt sel
	 * @param ite (le nombre d'itérations lors de la création de la clé)
	 * @return
	 */
	public static Cipher getCipher(char[] password, int mode, String crypt, byte[] salt, int ite){ //Pareil que l'autre Cipher mais on a pas besoin d'instance de la classe pour l'utiliser
		try {
			Cipher cipher;
			PBEParameterSpec pSpecs = new PBEParameterSpec(salt, ite);
			SecretKeyFactory keyFact = SecretKeyFactory.getInstance("PBEWithMD5AndDES");//"PBEWithMD5AndDES"
			PBEKeySpec kSpecs = new PBEKeySpec(password);
			SecretKey key = keyFact.generateSecret(kSpecs); //On crée la clé secrète
			cipher = Cipher.getInstance(crypt);
			//TODO: InvalidAlgorithmParameterException pour pSpecs
			cipher.init(mode, key, pSpecs); //On crée et initialise le Cipher grâce à la clé
			System.out.println("7");
			return cipher;

		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | InvalidKeySpecException e) {
			e.printStackTrace();
		}
		System.out.println("wtf");
		return null;
	}
	
	/**
	 * Utilise RC4 avec un sel choisi arbitrairement pour chiffrer le bytearray
	 * @param password
	 * @param bytearray
	 * @param mode
	 * @return 
	 */
	private static byte[] crypt(char[] password,byte[] bytearray,int mode){
		String sel = "2HjkI9e0";
		byte[] salt = sel.getBytes();

		Cipher cipher = getCipher(password, mode,"RC4", salt,64);
		System.out.println("cipher: " + cipher);
		try {
			return cipher.doFinal(bytearray);
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
		return bytearray;
	}
	
	/**
	 * Utilise Cipher en ENCRYPT_MODE
	 * @param password
	 * @param bytearray
	 * @return
	 */
	private static byte[] encrypt(char[] password,byte[] bytearray){
		return crypt(password,bytearray,Cipher.ENCRYPT_MODE);
	}
	
	
	/**
	 * Utilise Cipher en DECRYPT_MODE
	 * @param password
	 * @param bytearray
	 * @return
	 */
	private static byte[] decrypt(char[] password,byte[] bytearray){
		return crypt(password,bytearray,Cipher.DECRYPT_MODE);
	}
	
	/**
	 * retourne un tableau de byte à partir des rectangles sélectionnés
	 * @param rectangles
	 * @param image
	 * @return
	 */
	private static byte[] byteArrayFromSelectedRectangle(Rectangle[] rectangles,BufferedImage image){
		StringBuilder result = new StringBuilder();

		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				for (Rectangle r : rectangles) {
					if (r.contains(new Point(i, j))) {
						result.append(String.valueOf(image.getRGB(i, j)));
						break;
					}
				}
			}
		}

		return result.toString().getBytes();
	}
	
	/**
	 * change les bytes en int et les utilise comme valeur rgb pour reremplir
	 * @param rectangles
	 * @param image
	 * @param cryptedArray
	 * @return
	 */
	private static BufferedImage insertRectanglesInImage(Rectangle[] rectangles, BufferedImage image, byte[] cryptedArray){
		ByteBuffer bb=ByteBuffer.allocate(cryptedArray.length);
		IntBuffer ib=bb.asIntBuffer();
		bb.put(cryptedArray); 
		int[] array=ib.array();
		
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				for (Rectangle r : rectangles) {
					if (r.contains(new Point(i, j))) {
						image.setRGB(i, j, array[i*image.getHeight()+j]);//verifier que ça retourne le bon résultat
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
	public static BufferedImage encryptImage(Rectangle[] r, BufferedImage image, char[] password){
		byte[] array = byteArrayFromSelectedRectangle(r, image);
		array = encrypt(password, array);
		image = insertRectanglesInImage(r, image, array); //Tester si ça remet bien les bonnes valeurs
		return image;
	}
	
	/**
	 * Décrypte l'image  à partir du mot de passe, des rectangles et de l'image cryptée
	 * @param r
	 * @param image
	 * @param password
	 * @return
	 */
	public static BufferedImage decryptImage(Rectangle[] r,BufferedImage image,char[] password){
		byte[] array= byteArrayFromSelectedRectangle(r,image);
		array=decrypt(password,array);
		image=insertRectanglesInImage(r,image,array);
		return image;
	}
	
}
