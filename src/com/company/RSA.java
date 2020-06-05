package com.company;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

public class RSA {

    private static final int MIN = 200;
    private static final int MAX = 46000;

    private BigInteger[] publicKey;
    private BigInteger privateKey;
    private BigInteger p;
    private BigInteger q;

    public RSA(){}

    public BigInteger getPrivateKey() {
        return privateKey;
    }

    public BigInteger[] getPublicKey() {
        return publicKey;
    }

    @Override
    public String toString() {
        return "RSA{" +
                "publicKey=" + Arrays.toString(publicKey) +
                ", privateKey=" + privateKey +
                '}';
    }

    public BigInteger encrypt(BigInteger m, BigInteger[] publicKey){
        BigInteger n = publicKey[0];
        BigInteger e = publicKey[1];
        if(publicKey.length!=2){
            throw new IllegalArgumentException("Invalid argument: publicKey length must be 2,");
        }else if(m.compareTo(n)!=-1){
            throw new IllegalArgumentException("Invalid argument: m must be >=0 and < publicKey[0]");
        }
        else{
            return RsaMath.fastMod(m,e,n);
        }
    }
    public BigInteger decrypt(BigInteger c, BigInteger[] publicKey, BigInteger privateKey){
        BigInteger d = privateKey;
        BigInteger n = publicKey[0];
        if(publicKey.length!=2){
            throw new IllegalArgumentException("Invalid argument: publicKey length must be 2,");
        }else{
            return RsaMath.fastMod(c,d,n);
        }
    }
    public BigInteger decryptFast(BigInteger c, BigInteger[] publicKey, BigInteger privateKey){
        BigInteger dp;
        BigInteger dq;
        BigInteger mp;
        BigInteger mq;

        if(publicKey.length!=2){
            throw new IllegalArgumentException("Invalid argument: publicKey length must be 2,");
        }else{
            dp = privateKey.mod(p.subtract(BigInteger.ONE));
            dq = privateKey.mod(q.subtract(BigInteger.ONE));
            mp = RsaMath.fastMod(c,dp,p);
            mq = RsaMath.fastMod(c,dq,q);
            return RsaMath.chineseRemainder(new BigInteger[]{mp,mq},new BigInteger[]{p,q});
        }
    }
    public void generateKeys(){
        BigInteger p = RsaMath.generatePrime(MIN,MAX);
        BigInteger q = RsaMath.generatePrime(MIN,MAX);
        BigInteger n;
        BigInteger f;
        BigInteger e = BigInteger.TWO;
        BigInteger d;
        BigInteger x;
        while(p.equals(q)){
            p=RsaMath.generatePrime(MIN,MAX);
        }
        n = p.multiply(q);
        f = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

        while (!RsaMath.isRelativePrime(e,f)){
            e = RsaMath.generateRandomBigInteger(2,f.intValue()-1);
        }
        x = RsaMath.euclidExtended(e,f)[1];
        if(x.intValue()<0){
            d = x.add(f);
        }else{
            d = x;
        }
        this.publicKey = new BigInteger[]{n,e};
        this.privateKey = d;
        this.p = p;
        this.q = q;
    }

    /**
     * Extras
     */
    public List<BigInteger> encrypt(String m, BigInteger[] publicKey){
        List<Integer> charCodes;
        List<BigInteger> encryptedCharCodes = new ArrayList<>();
         charCodes = m.chars()
                        .boxed()
                        .collect(Collectors.toList());
         charCodes.forEach(num -> encryptedCharCodes.add(encrypt(BigInteger.valueOf(num),publicKey)));
         return encryptedCharCodes;
    }
    public String encryptString(String m, BigInteger[] publicKey){
        List<Integer> charCodes;
        StringBuilder encryptedCharCodes = new StringBuilder();
        charCodes = m.chars()
                .boxed()
                .collect(Collectors.toList());
        charCodes.forEach(code-> encryptedCharCodes
                .append(encrypt(BigInteger.valueOf(code), publicKey))
                .append(RsaMath.generateRandomCharacter()));

        return encryptedCharCodes.toString();
    }

    public BigInteger encrypt(int m, BigInteger[] publicKey){
      return encrypt(BigInteger.valueOf(m),publicKey);
    }

    public String decrypt(List<BigInteger> c, BigInteger[] publicKey, BigInteger privateKey){
        StringBuilder decryptedMessage = new StringBuilder();
        c.forEach(num->decryptedMessage.append(Character.toChars(decryptFast(num,publicKey,privateKey).intValue())));
        return decryptedMessage.toString();
    }
    public String decryptString(String c, BigInteger[] publicKey, BigInteger privateKey){
        StringBuilder decryptedMessage = new StringBuilder();
        StringBuilder messageToDecrypt = new StringBuilder();
        for(int i = 0; i<c.length();i++){
            if( ((int)c.charAt(i)) <48 || ((int)c.charAt(i))>57){
                messageToDecrypt.append(" ");
            }else{
                messageToDecrypt.append(c.charAt(i));
            }
     }
     List<String> listToDecrypt = List.of(messageToDecrypt.toString().split(" "));
     listToDecrypt.forEach(num->decryptedMessage.append(Character.toChars(decryptFast(BigInteger.valueOf(Integer.parseInt(num)),publicKey,privateKey).intValue())));

     return decryptedMessage.toString();
    }
}
