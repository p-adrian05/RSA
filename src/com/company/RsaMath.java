package com.company;

import java.math.BigInteger;
import java.util.*;
import java.util.function.Supplier;

public class RsaMath<type, t> {

    public RsaMath(){}

    public static boolean isRelativePrime(BigInteger a, BigInteger b){
        return euclid(a, b).equals(BigInteger.ONE);
    }

    public static BigInteger generatePrime(int min, int max){
        BigInteger prime = generateRandomBigInteger(min, max);
        while (!isPrime(prime, BigInteger.valueOf(2))){
                prime = generateRandomBigInteger(min, max);
        }
        return prime;

    }
    public static String generateRandomCharacter() {
            Random r = new Random();
            return String.valueOf((char)(r.nextInt(26)+'a'));
    }

    public static BigInteger generateRandomBigInteger(int min, int max){
        Random random = new Random();
        return BigInteger.valueOf(random.nextInt((max - min) + 1)+ min);
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
        //r maradek
        //q osztas eredmenye
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

    private static boolean isPrime(BigInteger p,BigInteger a){
        if(p.intValue()>3 && (a.intValue()>=2 && a.compareTo(p)==-1)){
            //ha paros akkor false
            if(p.mod(BigInteger.TWO).equals(BigInteger.ZERO)){
                return false;
            }
            //a bazis szamok generalasa
            int aMax = p.subtract(BigInteger.ONE).intValue();
            int aMin = a.intValue()+1;
            List<BigInteger> aValues = new ArrayList<>();
            aValues.add(a);
            for(int i = 0; i<2;i++){
                aValues.add(generateRandomBigInteger(aMin,aMax));
            }
            //addig osztjuk amig maradek nelkul lehet
            BigInteger d = p.subtract(BigInteger.ONE);
            int s =0;
            while (d.mod(BigInteger.TWO).equals(BigInteger.ZERO)){
                d = d.divide(BigInteger.TWO);
                s++;
            }

            //tobb a bazis szamra vizsgaljuk
            BigInteger t;

            int checkIfPrimeCount = 0;
            int checkForAllA = 0;
            for (BigInteger aValue : aValues) {
                t = fastMod(aValue,d, p);
                if(t.equals(BigInteger.ONE)){
                    checkIfPrimeCount++;
                }
                for (int i = 0; i < s; i++) {
                    t = fastMod(aValue,BigInteger.valueOf(2).pow(i).multiply(d), p);
                    if (t.equals(BigInteger.valueOf(-1))) {
                        checkIfPrimeCount++;
                    }
                }
                if(checkIfPrimeCount==s){
                    checkForAllA++;
                }
            }
            return checkForAllA == aValues.size();
        }
        throw new IllegalArgumentException("Wrong arguments: p must be > 3 and a must be >=2 and <p");

    }

    /**
     *
     *
     * @param a egesz szam alap
     * @param b  kitevo
     * @param m  egesz szam
     *          a^b (mod m) keplet
     * Eloszor felbontjuk a kitevot 2 hatvanyaira, osztjuk kettovel amig 0-t nem kapunk
     * es elmentjuk a 2 hatvanyait egy listaba, ami pont i lesz akkor ha 1 maradekot kapunk 2-vel osztva.
     * Masodik lepesben pedig az alapot a kitevo 2 hatvanyaira emeljuk egyesevel es vesszuk az m-mel osztott maradekat (a^2^i mod 100)
     * amiket osszeszorzunk es annak vesszuk az m-el osztott maradekat, ezt adjuk vissza sum(a^2^i mod 100) mod 100
     * @return a biginteger
     */
    public static BigInteger fastMod(BigInteger a, BigInteger b, BigInteger m){
            int i = 0;
            BigInteger q = b;
            BigInteger sum = BigInteger.ONE;
            BigInteger[] dAndR;
            List<Integer> listPows = new LinkedList<>();

            //a kitevot 2-vel addig osztjuk amig 0 nem lesz
            while (!q.equals(BigInteger.ZERO)) {
                //eltaroljuk a osztas eredmenyet es a maradekot egy tombbe
                dAndR = q.divideAndRemainder(BigInteger.TWO);
                //tomb elso eleme az osztas eredmenye
                q = dAndR[0];
                //tomb masodik eleme a maradek, ami ha 1, akkor eltaroljuk az aktualis i erteket, minden iteracional novelve
                //ez lesz a ketto havtanyai
                if (dAndR[1].equals(BigInteger.ONE)) {
                    listPows.add(i);
                }
                i++;
            }
            //vegigmegyunk ketto hatvanyain, az a alapot a kitevo ketto hatvanyaira emeljuk es vesszuk a megadott az m-el osztott
            //maradekot, minden iteracional kapott eredmenyt osszeszorozzuk az elozoekkel
            for (Integer listPow : listPows) {
                sum = sum.multiply(a.modPow(BigInteger.TWO.pow(listPow), m));
            }
            //vegul a szorzas eredmenyen vesszuk az m mel osztott maradekot
            return sum.mod(m);
    }

    //x ≡ SUM (ai ∗ yi ∗ Mi) (mod M)
    public static BigInteger chineseRemainder(BigInteger[] a, BigInteger[] m){
        //M a kapott modulok szorzata
        BigInteger M = Arrays.stream(m).reduce(BigInteger::multiply).get();
        BigInteger Mi;
        BigInteger sum = BigInteger.ZERO;
        //minden a ra Mi az aktualis i-edik modulo M-mel valo osztasanak eredmenye
        //az aktualais a erteket szorozzuk az Mi és i-edik moduloval kapott kiterjesztett euc.
        //kapott tomb masodik ertekevel ami az x erteke, majd szorozzuk még a Mi ertekevel.
        for(int i = 0; i<a.length;i++){
            Mi = M.divide(m[i]);
            sum = sum.add(a[i].multiply(euclidExtended(Mi,m[i])[1]).multiply(Mi));
        }
        //vegul vesszuk az M-vel valo osztasi maradekot
        return sum.mod(M);
    }


}
