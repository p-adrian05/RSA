package com.company;

import java.math.BigInteger;
import java.util.List;


public class Main {

    public static void main(String[] args) {
        BigInteger m = BigInteger.valueOf(4756);
        BigInteger c;
	    RSA rsa = new RSA();

        if(rsa.generateKeys()){
            System.out.println(rsa);
            System.out.println("Int message: "+ m);
            c = rsa.encrypt(m,rsa.getPublicKey());
            System.out.println("Crypted int message: "+ c);
            m = rsa.decryptFast(c,rsa.getPublicKey(),rsa.getPrivateKey());
            System.out.println("Decrypted int message: "+m);

            System.out.println();
            System.out.println("EXTRAS");
            String stringMes = "password1234";
            System.out.println("String message: "+ stringMes);
            List<BigInteger> encryptedMessageList = rsa.encrypt(stringMes,rsa.getPublicKey());
            System.out.println("Crypted string message list: "  +encryptedMessageList);
            String encryptedMessage = rsa.encryptString(stringMes,rsa.getPublicKey());
            System.out.println("Crypted string message : "  +encryptedMessage);
            String mes = rsa.decrypt(encryptedMessageList,rsa.getPublicKey(),rsa.getPrivateKey());
            String mes2 = rsa.decryptString(encryptedMessage,rsa.getPublicKey(),rsa.getPrivateKey());
            System.out.println("Decrypted string message from list: "+mes);
            System.out.println("Decrypted string message from string: "+mes2);
        }
    }

}
