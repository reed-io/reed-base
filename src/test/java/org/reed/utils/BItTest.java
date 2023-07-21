package org.reed.utils;

public class BItTest {
    public static void main(String[] args){
//        long x = 777777;
//        System.out.println(Long.toBinaryString(x));
        int x = 5;
        System.out.println(Integer.toBinaryString(x));
        System.out.println(getBit(x, 0));
        System.out.println(getBit(x, 1));
        System.out.println(getBit(x, 2));
        System.out.println(getBit(x, 3));
        System.out.println(getBit(x, 4));
        System.out.println(getBit(x, 5));
        System.out.println(getBit(x, 6));
        x = setBit(x,5);
        System.out.println(Integer.toBinaryString(x));
        x = resetBit(x, 0);
        System.out.println(Integer.toBinaryString(x));

        int[] a = {1,2,3,4,5,6,7,8,9};
        int[] b = {1,2,3,4,5};
        int[] c = func(a,b);
        for(int i:c){
            System.out.println(i);
        }

    }


    public static int[] func(int[] a, int[] b){
        int[] result = a.length>b.length?a:b;
        for(int i=0 ; i<result.length ; i++){
            result[i]+=i>=b.length?0:b[i];
        }
        return result;
    }


    /**
     * 指定位置的bit是否等于1
     * @param x  要判断的数字
     * @param pos 判断bit的位置从0开始，从右向左
     * @return
     */
    public static boolean isBit1(int x, int pos){
        return ((x>>pos)&1) == 1;
    }

    /**
     * 指定位置的bit是否等于1
     * @param x  要判断的数字
     * @param pos 判断bit的位置从0开始，从右向左
     * @return
     */
    public static boolean isBit1(short x, int pos){
        return ((x>>pos)&1) == 1;
    }

    /**
     * 指定位置的bit是否等于1
     * @param x  要判断的数字
     * @param pos 判断bit的位置从0开始，从右向左
     * @return
     */
    public static boolean isBit1(long x, int pos){
        return ((x>>pos)&1) == 1;
    }

    /**
     * 指定位置的bit (0,1)
     * @param x  要判断的数字
     * @param pos 判断bit的位置从0开始，从右向左
     * @return
     */
    public static int getBit(int x, int pos){
        return (x>>pos)&1;
    }

    /**
     * 指定位置的bit (0,1)
     * @param x  要判断的数字
     * @param pos 判断bit的位置从0开始，从右向左
     * @return
     */
    public static int getBit(short x, short pos){
        return (x>>pos)&1;
    }

    /**
     * 指定位置的bit (0,1)
     * @param x  要判断的数字
     * @param pos 判断bit的位置从0开始，从右向左
     * @return
     */
    public static int getBit(long x, int pos){
        return (int)(x>>pos)&1;
    }

    /**
     * 把指定pos的bit设置为1
     * @param x
     * @param pos
     * @return
     */
    public static int setBit(short x, short pos){return x|(1<<pos);}

    /**
     * 把指定pos的bit设置为1
     * @param x
     * @param pos
     * @return
     */
    public static int setBit(int x, int pos){return x|(1<<pos);}

    /**
     * 把指定pos的bit设置为1
     * @param x
     * @param pos
     * @return
     */
    public static int setBit(long x, int pos){return (int)x|(1<<pos);}


    /**
     * 把指定pos的bit重置为0
     * @param x
     * @param pos
     * @return
     */
    public static int resetBit(short x, short pos){return x&~(1<<pos);}

    /**
     * 把指定pos的bit重置为0
     * @param x
     * @param pos
     * @return
     */
    public static int resetBit(int x, int pos){return x&~(1<<pos);}

    /**
     * 把指定pos的bit重置为0
     * @param x
     * @param pos
     * @return
     */
    public static int resetBit(long x, int pos){return (int)x&~(1<<pos);}
}
