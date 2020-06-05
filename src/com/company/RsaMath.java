package com.company;

import java.math.BigInteger;
import java.util.*;
public class RsaMath {

    public RsaMath(){}

    public static boolean isRelativePrime(BigInteger a, BigInteger b){
        return euclid(a, b).equals(BigInteger.ONE);
    }

    public static BigInteger generatePrime(int min, int max){
        BigInteger randomNum = generateRandomBigInteger(min, max);
        if(!isPrime(randomNum)){
            randomNum = generatePrime(min,max);
        }
        return randomNum;
    }
    public static String generateRandomCharacter() {
        return String.valueOf((char)(new Random().nextInt(26)+'a'));
    }
    public static BigInteger generateRandomBigInteger(int min, int max){
        return BigInteger.valueOf(new Random().nextInt((max - min) + 1)+ min);
    }
    public static List<BigInteger> generateRandomBigInteger(int amount,int min, int max){
        Set<BigInteger> bigArray = new HashSet();
        for(int i = 0; i<amount; i++){
            bigArray.add(generateRandomBigInteger(min,max));
        }
        if(bigArray.size()!=amount){
            bigArray = new HashSet<>(generateRandomBigInteger(amount, min, max));
        }
        return new LinkedList<>(bigArray);
    }
    public static BigInteger euclid(BigInteger a, BigInteger b){
        BigInteger r;
        while(!b.equals(BigInteger.ZERO)){
            r = a.mod(b);
            a = b;
            b = r;
        }
        return a;
    }
    public static BigInteger[] euclidExtended(BigInteger a, BigInteger b) {
        BigInteger x0 = BigInteger.ONE;
        BigInteger x1 = BigInteger.ZERO;
        BigInteger y0 = BigInteger.ZERO;
        BigInteger y1 = BigInteger.ONE;
        int k = 0;
        BigInteger x;
        BigInteger y;
        BigInteger r;
        BigInteger q;

        while (!b.equals(BigInteger.ZERO)) {
            r = a.mod(b);
            q = a.divide(b);
            a = b;
            b = r;
            x = x1;
            y = y1;
            x1 = x1.multiply(q).add(x0);
            y1 = y1.multiply(q).add(y0);
            x0 = x;
            y0 = y;
            k++;
        }
        x = BigInteger.valueOf(-1).pow(k).multiply(x0);
        y = BigInteger.valueOf(-1).pow(k + 1).multiply(y0);
        return new BigInteger[]{a, x, y};
    }

    public static boolean isPrime(BigInteger p){
        if(p.mod(BigInteger.TWO).equals(BigInteger.ZERO)){
            return false;
        }
        List<BigInteger> aValues = generateRandomBigInteger(3,3,p.subtract(BigInteger.ONE).intValue());
        LinkedList<BigInteger> dValues = new LinkedList<>();
        BigInteger d = p.subtract(BigInteger.ONE);
        int s = 0;
        while (d.mod(BigInteger.TWO).equals(BigInteger.ZERO)){
            d = d.divide(BigInteger.TWO);
            dValues.addFirst(d);
            s++;
        }
        BigInteger temp;
        int checkForAllA = 0;
        for (BigInteger aValue : aValues) {
            temp = fastMod(aValue,d, p);
            if(temp.equals(BigInteger.ONE)){
                checkForAllA++;
            }else{
                for (int i = 1; i < s; i++) {
                    temp = fastMod(aValue,dValues.get(i), p);
                    if (temp.equals(p.subtract(BigInteger.ONE))) {
                        checkForAllA++;
                    }
                }
            }
        }
        return checkForAllA == aValues.size();
    }

    public static BigInteger fastMod(BigInteger a, BigInteger b, BigInteger m){
        int i = 0;
        BigInteger q = b;
        BigInteger sum = BigInteger.ONE;
        BigInteger[] dAndR;
        List<BigInteger> bTwoPows = new LinkedList<>();

        while (!q.equals(BigInteger.ZERO)) {
            dAndR = q.divideAndRemainder(BigInteger.TWO);
            q = dAndR[0];
            if (dAndR[1].equals(BigInteger.ONE)) {
                bTwoPows.add(BigInteger.TWO.pow(i));
            }
            i++;
        }
        BigInteger twoPow = BigInteger.TWO;
        BigInteger modValue = a.mod(m);
        if(bTwoPows.contains(BigInteger.ONE)){
            sum = sum.multiply(modValue);
        }
        while (twoPow.intValue()<=bTwoPows.get(bTwoPows.size() - 1).intValue()){
            modValue = modValue.pow(2);
            modValue = modValue.mod(m);
            if(bTwoPows.contains(twoPow)){
                sum = sum.multiply(modValue);
            }
            twoPow = twoPow.multiply(BigInteger.TWO);
        }
        return sum.mod(m);
    }

    //x ≡ SUM (ai ∗ yi ∗ Mi) (mod M)
    public static BigInteger chineseRemainder(BigInteger[] a, BigInteger[] m){
        BigInteger M = Arrays.stream(m).reduce(BigInteger::multiply).get();
        BigInteger Mi;
        BigInteger sum = BigInteger.ZERO;
        for(int i = 0; i<a.length;i++){
            Mi = M.divide(m[i]);
            sum = sum.add(a[i].multiply(euclidExtended(Mi,m[i])[1]).multiply(Mi));
        }
        return sum.mod(M);
    }


}
