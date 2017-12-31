package encryption;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

public class Encryption {
	/**
	 * @param password
	 * @param mode (Cipher.ENCRYPT_MODE ou Cipher.DECRYPT_MODE selon l'utilisation)
	 * @param crypt (méthode de cryptage)
	 * @param salt
	 * @param ite (le nombre d'itération lors de la création de la clé)
	 * @return
	 */
	public static Cipher getCipher(char[] password,int mode,String crypt,byte[] salt,int ite){ //Pareil que l'autre Cipher mais on a pas besoin d'instance de la classe pour l'utiliser
		Cipher cipher=null;
		try {
			PBEParameterSpec pSpecs = new PBEParameterSpec(salt, ite);
			SecretKeyFactory keyFact = SecretKeyFactory.getInstance(crypt);//"PBEWithMD5AndDES"
			PBEKeySpec kSpecs = new PBEKeySpec(password);
			SecretKey key = keyFact.generateSecret(kSpecs); //On crée la clé secrète
			
			cipher = Cipher.getInstance(crypt);
			cipher.init(mode, key, pSpecs); //On crée et initialise le Cipher grâce à la clé
			
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return cipher;
	}
	
	/**
	 * Utilise RC4 avec un sel choisi arbitrairement pour chiffrer le bytearray
	 * @param password
	 * @param bytearray
	 * @param mode
	 * @return 
	 */
	private static byte[] crypt(char[] password,byte[] bytearray,int mode){
		String sel= "2HjkI9e0";
		byte[] salt= sel.getBytes();
		Cipher cipher= getCipher(password,mode,"RC4",salt,64);
		try {
			return cipher.doFinal(bytearray);
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
		return bytearray;
	}
	
	public static byte[] encrypt(char[] password,byte[] bytearray){
		return crypt(password,bytearray,Cipher.ENCRYPT_MODE);
	}
	
	public static byte[] decrypt(char[] password,byte[] bytearray){
		return crypt(password,bytearray,Cipher.DECRYPT_MODE);
	}
	
	private static byte[] byteArrayFromSelectedRectangle(Rectangle[] rectangles,BufferedImage image){
		String result = "";
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				for (Rectangle r : rectangles) {
					if (r.contains(new Point(i, j))) {
						result += String.valueOf(image.getRGB(i, j));
						break;
					}
				}
			}
		}
		return result.getBytes();
	}
	
	private static BufferedImage insertRectanglesInImage(Rectangle[] rectangles,BufferedImage image,byte[] cryptedArray){
		ByteBuffer bb=ByteBuffer.allocate(cryptedArray.length);
		IntBuffer ib=bb.asIntBuffer();
		bb.put(cryptedArray);
		int[] array=ib.array();
		BufferedImage result=image;
		
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				for (Rectangle r : rectangles) {
					if (r.contains(new Point(i, j))) {
						result.setRGB(i, j, array[i*image.getHeight()+j]);
						break;
					}
				}
			}
		}
		return result;
		
	}
	
	public static BufferedImage encryptImage(Rectangle[] r,BufferedImage i,char[] password){
		BufferedImage result=i;
		byte[] array= byteArrayFromSelectedRectangle(r,i);
		array=encrypt(password,array);
		result=insertRectanglesInImage(r,i,array); //Tester si ça remet bien les bonnes valeurs
		return result;
	}
	
	public static BufferedImage decryptImage(Rectangle[] r,BufferedImage i,char[] password){
		BufferedImage result=i;
		byte[] array= byteArrayFromSelectedRectangle(r,i);
		array=decrypt(password,array);
		result=insertRectanglesInImage(r,i,array); 
		return result;
	}
	
}
