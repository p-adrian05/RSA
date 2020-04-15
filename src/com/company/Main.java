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

//        c = rsa.encrypt(BigInteger.valueOf('m'),rsa.getPublicKey());
//        System.out.println("Crypted message: "+ c);
//        m = rsa.decryptFast(c,rsa.getPublicKey(),rsa.getPrivateKey());
//        System.out.println("Decrypted message: "+Character.toChars(m.intValue())[0]);
//
//        System.out.println(message);
//        message.chars().boxed().forEach(mes -> System.out.print(Character.toChars(mes)));

    }

}
