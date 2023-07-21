/**
 * base/org.reed.define/SomeErrorCode.java
 */
package org.reed.define;

/**
 * @author chenxiwen
 * @date 2017年8月16日上午11:44:26
 */
public class SomeErrorCode extends BaseErrorCode {
    @CodeDescTag(desc="测试解释说明")
    public static int TEST_ERROR_CODE = 0xff1100;
    @CodeDescTag(desc="测试带参数的解释说明，参数1=${arg1}, 参数2=《${参数2}》")
    public static int TEST_ARGS_DESCODE = 0xff1101;
    @CodeDescTag(desc="测试带参数的解释说明，参数1=${arg1}, 参数2=《${参数2}》", append="HOTLINE")
    public static int TEST_BRAND_DESCODE = 0xff1102;
//    @CodeDescTag(desc="测试带参数的解释说明，参数1=${arg1}, 参数2=《${参数2}》", append="HOTLINE")
//    public static int TEST_BRAND_DESCODE1 = 0xff1102;

    @CodeDescTag(desc = "未知错误！msg1=${arg1}, msg2=${arg2}, msg5=${arg5}")
    public static int UNKNOW_ERROR = -1;

    
    
    
    public static void main(String[] args){
//        CodeDescTranslator.init(SomeErrorCode.class);
//        System.out.println(CodeDescTranslator.explain(0xff1100, null));
//        System.out.println(CodeDescTranslator.explain(0xff1101, null, "arg1:参数1", "参数2:arg2"));
//        System.out.println(CodeDescTranslator.explain(0xff1102, null, "arg1:参数1", "参数2:arg2"));
//        System.out.println(CodeDescTranslator.explain(0, null, "arg1:参数1", "参数2:arg2"));
//        System.out.println(CodeDescTranslator.explain(0xff1103, null, "arg1:参数1", "参数2:arg2"));
        
        System.out.println(System.getProperty("user.home"));
        
    }
}
