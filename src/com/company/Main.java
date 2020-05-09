package com.company;

import java.math.BigInteger;
import java.util.List;
import java.util.function.Consumer;


public class Main {

    public static void main(String[] args) {
        BigInteger m = BigInteger.valueOf(4756);
        BigInteger c;
        Consumer<String> log = System.out::println;
	    RSA rsa = new RSA();
        rsa.generateKeys();

        log.accept(rsa.toString());
        log.accept("Int message: "+ m);
        c = rsa.encrypt(m,rsa.getPublicKey());
        log.accept("Crypted int message: "+ c);
        m = rsa.decryptFast(c,rsa.getPublicKey(),rsa.getPrivateKey());
        log.accept("Decrypted int message: "+m);

        log.accept("");
        log.accept("EXTRAS");
        String stringMes = "password1234";
        log.accept("String message: "+ stringMes);
        List<BigInteger> encryptedMessageList = rsa.encrypt(stringMes,rsa.getPublicKey());
        log.accept("Crypted string message list: "  +encryptedMessageList);
        String encryptedMessage = rsa.encryptString(stringMes,rsa.getPublicKey());
        log.accept("Crypted string message : "  +encryptedMessage);
        String mes = rsa.decrypt(encryptedMessageList,rsa.getPublicKey(),rsa.getPrivateKey());
        String mes2 = rsa.decryptString(encryptedMessage,rsa.getPublicKey(),rsa.getPrivateKey());
        log.accept("Decrypted string message from list: "+mes);
        log.accept("Decrypted string message from string: "+mes2);

    }

}
