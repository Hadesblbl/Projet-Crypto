package application;

public class PasswordBasedEncrypt {
	//Quand on veut decrypter, avec le même mdp on doit avoir la meme cle
	int c = 100; //iterations
	byte[] salt;
	int lK; //length Key -> fixee du coup
	
	int ceil(int a,int b){
		if (a%b==0){
			return a/b;
		} else {
			return a/b+1;
		}
	}
	
	public String encryptPBKDF2(String pwd){
		byte[] password = pwd.getBytes();
		int hL=10; //a changer //longueur de la chaine générée par notre fonction d'encryption
		
		int l = ceil(lK,hL);
		int r = lK-hL*(l-1);
		String K="";
		
		byte[] U;
		byte[] A;
		for(int i=1;i<=l;i++){
			U = password; // on appliquera la fonction de cryptage ici
			A = U;
			for(int j=1;j<=c-1;j++){
				//Rajouter la fonction appliquer sur U avec le password
				//Puis A= A xor U
			}
			K=K+A.toString();
		}
		
		return K;
	}

}
