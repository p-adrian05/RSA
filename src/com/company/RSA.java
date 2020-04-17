package com.company;

import java.math.BigInteger;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class RSA {

    private static final int MIN = 200;
    private static final int MAX = 1000;

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

    private void setPublicKey(BigInteger[] publicKey) {
        this.publicKey = publicKey;
    }

    private void setPrivateKey(BigInteger privateKey) {
        this.privateKey = privateKey;
    }

    private void setP(BigInteger p) {
        this.p = p;
    }

    private void setQ(BigInteger q) {
        this.q = q;
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

    public List<BigInteger> encrypt(String m, BigInteger[] publicKey){
        List<Integer> charCodes;
        List<BigInteger> encryptedCharCodes = new ArrayList<>();
         charCodes = m.chars()
                        .boxed()
                        .collect(Collectors.toList());
         charCodes.forEach(num -> encryptedCharCodes.add(encrypt(BigInteger.valueOf(num),publicKey)));
         return encryptedCharCodes;
    }
//    public String encrypt(String m, BigInteger[] publicKey){
//        List<Integer> charCodes;
//        StringBuilder encryptedCharCodes = new StringBuilder();
//        charCodes = m.chars()
//                .boxed()
//                .collect(Collectors.toList());
//        charCodes.forEach(code-> encryptedCharCodes
//                .append(encrypt(BigInteger.valueOf(code), publicKey))
//                .append(RsaMath.generateRandomCharacter()));
//
//        return encryptedCharCodes.toString();
//    }

    public BigInteger encrypt(int m, BigInteger[] publicKey){
      return encrypt(BigInteger.valueOf(m),publicKey);
    }

    public String decrypt(List<BigInteger> c, BigInteger[] publicKey, BigInteger privateKey){
        StringBuilder decryptedMessage = new StringBuilder();
        c.forEach(num->decryptedMessage.append(Character.toChars(decryptFast(num,publicKey,privateKey).intValue())));
        return decryptedMessage.toString();
    }
//    public String decrypt(String c, BigInteger[] publicKey, BigInteger privateKey){
//        StringBuilder decryptedMessage = new StringBuilder();
//        StringBuilder messageToDecrypt = new StringBuilder();
//        for(int i = 0; i<c.length();i++){
//            if( ((int)c.charAt(i)) <48 || ((int)c.charAt(i))>57){
//                messageToDecrypt.append(" ");
//            }else{
//                messageToDecrypt.append(c.charAt(i));
//            }
//     }
//
//     List<String> listToDecrypt = List.of(messageToDecrypt.toString().split(" "));
//     listToDecrypt.forEach(num->decryptedMessage.append(Character.toChars(decryptFast(BigInteger.valueOf(Integer.parseInt(num)),publicKey,privateKey).intValue())));
//    return decryptedMessage.toString();
//
//    }


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
        BigInteger d = privateKey;
        BigInteger dp;
        BigInteger dq;
        BigInteger mp;
        BigInteger mq;

        if(publicKey.length!=2){
            throw new IllegalArgumentException("Invalid argument: publicKey length must be 2,");
        }else{
            dp = d.mod(p.subtract(BigInteger.ONE));
            dq = d.mod(q.subtract(BigInteger.ONE));
            mp = c.modPow(dp,p);
            mq = c.modPow(dq,q);
            return RsaMath.chineseRemainder(new BigInteger[]{mp,mq},new BigInteger[]{p,q});
        }
    }



    public boolean generateKeys(){
        BigInteger p = RsaMath.generatePrime(MIN,MAX);
        BigInteger q = RsaMath.generatePrime(MIN,MAX);
        BigInteger n;
        BigInteger f;
        BigInteger e = BigInteger.TWO;
        BigInteger d;
        while(p.equals(q)){
            p=RsaMath.generatePrime(MIN,MAX);
        }

        try{
            n = p.multiply(q);
            f = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
            System.out.println("p: "+p);
            System.out.println("q: "+q);

            while (!RsaMath.isRelativePrime(e,f)){
                e = RsaMath.generateRandomBigInteger(2,f.intValue()-1);
            }

            System.out.println("e: "+e);
            d = RsaMath.euclidExtended(e,f)[1].mod(f);

            setPublicKey(new BigInteger[]{n,e});
            setPrivateKey(d);
            setP(p);
            setQ(q);
            return true;
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return false;
    }






}
