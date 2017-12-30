package encryption;

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
}
