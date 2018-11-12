package com.helper.shiro.config;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by IntelliJ IDEA.
 *
 * @author XmasPiano
 * @date 2018/10/11 - 上午11:37
 * Created by IntelliJ IDEA.
 */
public class MyCredentialsMatcher extends SimpleCredentialsMatcher {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass().getName());

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        UsernamePasswordToken utoken=(UsernamePasswordToken) token;
        //获得用户输入的密码:(可以采用加盐(salt)的方式去检验)

        //确定计算方法
        String inPassword = String.copyValueOf(utoken.getPassword());
        //getStringMD5(new String(utoken.getPassword()));

        //获得数据库中的密码
        String dbPassword=(String) info.getCredentials();

        //进行密码的比对
        return this.equals(inPassword, dbPassword);
    }

    /**
     * 将字节数组转换为16进制字符串
     * @param byteArr
     * @return 16进制字符串
     */
    private static String byteArrToHex(byte[] byteArr) {
        // Initialize the character array, used to store each hexadecimal string
        char[] hexDigits = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        // Initialize a char Array, used to form the result string
        char[] resultCharArr = new char[byteArr.length*2];
        // Traverse the byte array, converted into characters in a character array
        int index = 0;
        for (byte b : byteArr) {
            resultCharArr[index++] = hexDigits[b>>> 4 & 0xf];
            resultCharArr[index++] = hexDigits[b & 0xf];
        }
        return new String(resultCharArr);
    }

    /**
     * 获取字符串的MD5
     * @param input
     * @return
     */
    public static String getStringMD5(String input){
        try {
            // get MD5 digest
            MessageDigest mDigest = MessageDigest.getInstance("MD5");
            // The input String to Byte Array
            byte[] inputArr = input.getBytes();
            // Updates the digest using the specified byte.
            mDigest.update(inputArr);
            // Completes the hash computation by performing final operations such as padding.
            // The digest is reset after this call is made.
            byte[] resultArr = mDigest.digest();
            //
            return byteArrToHex(resultArr);
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

}