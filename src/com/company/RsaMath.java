package com.company;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class RsaMath {

    public RsaMath(){}

    public static boolean isRelativePrime(BigInteger a, BigInteger b){
        if(euclid(a,b).equals(BigInteger.ONE)){
            return true;
        }
        return false;
    }


    public static BigInteger generatePrime(int min, int max){
        Random random = new Random();
        Integer prime = 4;
        Supplier<Integer> randomSupplier = ()-> random.nextInt((max - min) + 1)+ min;
        while (!isPrime(BigInteger.valueOf(prime),BigInteger.valueOf(3))){
            prime = randomSupplier.get();
        }

        return BigInteger.valueOf(prime);

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
        BigInteger k = BigInteger.ZERO;
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
            k = k.add(BigInteger.ONE);
        }
        x = BigInteger.valueOf(-1).pow(k.intValue()).multiply(x0);
        y = BigInteger.valueOf(-1).pow(k.intValue() + 1).multiply(y0);
        return new BigInteger[]{a, x, y};
    }


    /**
     *
     *
     * @param p > 3
     * @param a >=2 and <p
     * A p - 1 erteket addig osztjuk 22-vel ameddig maradék nélkül lehetseges, kozben noveljuk s-t.
     * es d tarolja a maradekot.
     * Az elso teszt: a^d mod p ha == 1 akkor prim
     * Tovabbi tobbkoros teszt kovetkezik:( a^(2^i*d) (mod p)) == p-1 akkor prim
     * @return
     */
    public static boolean isPrime(BigInteger p,BigInteger a){
        if(p.intValue()>3 && (a.intValue()>=2 && a.compareTo(p)==-1)){
            BigInteger d = p.subtract(BigInteger.ONE);
            BigInteger s = BigInteger.ZERO;
            while (d.mod(BigInteger.TWO)==BigInteger.ZERO){
                d = d.divide(BigInteger.TWO);
                s = s.add(BigInteger.ONE);
            }
            BigInteger t = a.modPow(d,p);
            if(t.equals(BigInteger.ONE)){
                return true;
            }
            for(int i = 1; i<s.intValue();i++){
                t=a.modPow(BigInteger.valueOf(2).pow(i).multiply(d),p);

                if(t.equals(p.subtract(BigInteger.ONE))){
                    return true;
                }

            }
            return false;
        }
        throw new IllegalArgumentException("Wrong arguments: p must be > 3 and a must be >=2 and <p");

    }

    /**
     *
     *
     * @param a egesz szam alap
     * @param b >1 kitevo
     * @param m pozitiv egesz szam
     *          a^b (mod m) keplet
     * Eloszor felbontjuk a kitevot 2 hatvanyaira, osztjuk kettovel amig 0-t nem kapunk
     * es elmentjuk a 2 hatvanyait egy listaba, ami pont i lesz akkor ha 1 maradekot kapunk 2-vel osztva.
     * Masodik lepesben pedig az alapot a kitevo 2 hatvanyaira emeljuk egyesevel es vesszuk az m-mel osztott maradekat (a^2^i mod 100)
     * amiket osszeszorzunk es annak vesszuk az m-el osztott maradekat, ezt adjuk vissza sum(a^2^i mod 100) mod 100
     * @return
     */
    public static BigInteger fastMod(BigInteger a, BigInteger b, BigInteger m){
        if(b.compareTo(BigInteger.ONE)==1 && m.compareTo(BigInteger.ZERO)==1) {
            int i = 0;
            BigInteger q = b;
            BigInteger sum = BigInteger.ONE;
            BigInteger dAndR[];
            List<Integer> listPows = new LinkedList<>();

            while (!q.equals(BigInteger.ZERO)) {
                dAndR = q.divideAndRemainder(BigInteger.TWO);
                q = dAndR[0];
                if (dAndR[1].equals(BigInteger.ONE)) {
                    listPows.add(i);
                }
                i++;
            }

            for(int k = 0; k<listPows.size(); k++){
                sum =  sum.multiply(a.modPow(BigInteger.TWO.pow(listPows.get(k)),m));
            }
            return sum.mod(m);
        }
        throw new IllegalArgumentException("Wrong arguments: b must be > 1 and m must be > 0");

    }

    public static BigInteger chineseRemainder(BigInteger[] a, BigInteger[] n){
        BigInteger M = Arrays.stream(n).reduce((i, j)->i.multiply(j)).get();
        BigInteger Mi;
        BigInteger sum = BigInteger.ZERO;
        for(int i = 0; i<a.length;i++){
            Mi = M.divide(n[i]);
            sum = sum.add(a[i].multiply(euclidExtended(Mi,n[i])[1]).multiply(Mi));
        }
        return sum.mod(M);
    }


}
