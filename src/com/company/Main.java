package com.company;

import java.math.BigInteger;


public class Main {

    public static void main(String[] args) {
        BigInteger m = BigInteger.valueOf(4756);

        System.out.println("Message: "+ m);
        BigInteger c;
	    RSA rsa = new RSA();
        if(rsa.generateKeys()){
            System.out.println(rsa);
            c = rsa.encrypt(m,rsa.getPublicKey());
            System.out.println("Crypted message: "+ c);
            m = rsa.decryptFast(c,rsa.getPublicKey(),rsa.getPrivateKey());
            System.out.println("Decrypted message: "+m);
        }
    }
}
