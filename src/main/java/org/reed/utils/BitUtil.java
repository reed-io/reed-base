package org.reed.utils;

public final class BitUtil {
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
    public static boolean isBit1(long x, int pos){ return ((x>>pos)&1) == 1; }

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
