package com.company;

import java.math.BigInteger;
import java.util.*;
import java.util.function.Supplier;

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

    public BigInteger encrypt(int m, BigInteger[] publicKey){
      return encrypt(BigInteger.valueOf(m),publicKey);
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
        Random random = new Random();
        Supplier<BigInteger> randomSupplier;
        while(p.equals(q)){
            p=RsaMath.generatePrime(MIN,MAX);
        }

        try{
            n = p.multiply(q);
            f = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
            System.out.println("p: "+p);
            System.out.println("q: "+q);
            randomSupplier = ()-> BigInteger.valueOf(random.nextInt(f.intValue()));

            while (!RsaMath.isRelativePrime(e,f)){
                e = randomSupplier.get();
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
