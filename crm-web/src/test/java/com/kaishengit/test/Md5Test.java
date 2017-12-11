package com.kaishengit.test;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

/**
 * @author NativeBoy
 */
public class Md5Test {

    @Test
    public void md5(){
        System.out.println(DigestUtils.md5Hex("admin"));
    }
}
