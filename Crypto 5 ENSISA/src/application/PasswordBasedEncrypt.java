package application;

import java.math.BigInteger;

public class PasswordBasedEncrypt {
	//Quand on veut decrypter, avec le même mdp on doit avoir la meme cle
	int c = 64; //iterations
	byte[] salt;
	int lK; //length Key -> fixee du coup 
	
	/**
	 * @param a
	 * @param b
	 * @return the ceil of the division a/b
	 */
	int ceil(int a,int b){
		if (a%b==0){
			return a/b;
		} else {
			return a/b+1;
		}
	}
	
	/**
	 * @param a
	 * @param b
	 * @return the result of the xor operation between a and b
	 */
	byte[] xor(byte[] a,byte[] b){
		byte[] result= new byte[a.length];
		for(int i=0;i<a.length;i++){
			result[i]= (byte) (a[i]^b[i]);
		}
		return result;
	}
	
	byte[] encrypt(byte[] a,byte[] key){ //mettre le cryptage ici ou remplacer par une autre fonction
		return a;
	}
	
	/**
	 * @param salt
	 * @param indice
	 * @return salt+(byte[])indice
	 */
	byte[] append(byte[] salt,int indice){
		byte[] fin= BigInteger.valueOf(indice).toByteArray();
		byte[] result= new byte[salt.length+4];
		for(int i=0;i<salt.length;i++){
			result[i]=salt[i];
		}
		for(int i=0;i<4;i++){
			result[salt.length+i]=fin[i];
		}
		return result;
	}
	
	/**
	 * @param pwd
	 * @return an encrypted key made from the password with the PBKDF2 method
	 */
	public String encryptPBKDF2(String pwd){
		byte[] password = pwd.getBytes();
		int hL=10; //a changer //longueur de la chaine générée par notre fonction d'encryption
		
		int l = ceil(lK,hL);
		int r = lK-hL*(l-1);
		String K="";
		
		byte[] U;
		byte[] A;
		for(int i=1;i<=l;i++){
			U = encrypt(password,append(salt,i)); // on appliquera la fonction de cryptage ici
			A = U;
			for(int j=1;j<=c-1;j++){
				U = encrypt(password,U);//Rajouter la fonction appliquee sur U avec le password
				A= xor(A,U);
			}
			K=K+A.toString();
		}
		
		return K;
	}

}
