/**
 * base/org.reed.define/AnotherErrorCode.java
 */
package org.reed.define;

/**
 * @author chenxiwen
 * @date 2017年8月16日下午2:09:28
 */
public class AnotherErrorCode extends SomeErrorCode {

    @CodeDescTag(desc="test")
    public static final int TEST = 0xff1111;
    
    public static void main(String[] args){
        CodeDescTranslator.init(AnotherErrorCode.class);
        System.out.println(CodeDescTranslator.explain(0xff1100, null));
        System.out.println(CodeDescTranslator.explain(0xff1101, null, "arg1:参数1", "参数2:arg2"));
        System.out.println(CodeDescTranslator.explain(0xff1102, null, "arg1:参数1", "参数2:arg2"));
        System.out.println(CodeDescTranslator.explain(0, null, "arg1:参数1", "参数2:arg2"));
        System.out.println(CodeDescTranslator.explain(0xff1103, null, "arg1:参数1", "参数2:arg2"));
        System.out.println(CodeDescTranslator.explain(0xff1111, null));
    }
}
