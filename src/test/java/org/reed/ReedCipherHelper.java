/**
 * E5Projects @ org.reed/ReedCipherHelper.java
 */
package org.reed;

import org.reed.utils.DESUtil;
import org.reed.utils.StringUtil;

/**
 * @author chenxiwen
 * @createTime 2020年03月06日 下午6:22
 * @description
 */
public class ReedCipherHelper {

    private static final String text="this.putArgs(\"ReedCipher{b56c0223134d4bb57b5def6bbcd405c05da3143c5e11ca5094a2cd3b552ad948feb959b7d4642fcb}\",\n" +
            "                String.valueOf(annotation.autoRefresh()));";

    public static void main(String[] args){
        String result = StringUtil.decryptCiphertext(text, DESUtil.DEFAULT_SECURITY_CODE);
        System.out.println(result);
    }
}
