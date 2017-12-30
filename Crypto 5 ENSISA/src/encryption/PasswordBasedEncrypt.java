package encryption;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

public class PasswordBasedEncrypt {
	//Quand on veut decrypter, avec le même mdp on doit avoir la meme cle
	private int c; //iterations
	private byte[] salt;
	private PBEParameterSpec pSpecs;
	
	public PasswordBasedEncrypt(int c,byte[] salt){
		this.salt= salt;
		this.c=c;
		pSpecs = new PBEParameterSpec(salt, c);
	}
	
	/**
	 * @param password
	 * @return une SecretKey générée à partir du mot de passe
	 */
	private SecretKey fromPassword(char[] password){
		SecretKey key= null;
		try {
			SecretKeyFactory keyFact = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
			PBEKeySpec kSpecs = new PBEKeySpec(password);
			key = keyFact.generateSecret(kSpecs); //On crée la clé secrète
		} catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return key;
	}
	
	/**
	 * @param password
	 * @param mode (Cipher.ENCRYPT_MODE ou Cipher.DECRYPT_MODE selon l'utilisation)
	 * @return une instance de Cipher qui nous permettra de crypter notre image
	 */
	public Cipher getCipher(char[] password,int mode){
		Cipher cipher=null;
		try {
			cipher = Cipher.getInstance("PBEWithMD5AndDES");
			cipher.init(mode, fromPassword(password), pSpecs);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		}
		return cipher;
	}
	
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
}
