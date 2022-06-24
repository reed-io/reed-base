package org.reed.system;

public class MacOSTest {

    private int a;
    private String s;

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public static void main(String[] args){
        System.out.println(System.getProperty("os.name"));
        System.out.println(System.getProperty("sun.arch.data.model"));

        SystemInfo.loadLib();
    }


}
