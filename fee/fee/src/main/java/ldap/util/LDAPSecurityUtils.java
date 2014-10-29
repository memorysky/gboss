package ldap.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * LDAP安全主题相关
 * @author xiaoke
 * @version 2013-11-28
 */
public class LDAPSecurityUtils {
    /** Base64映射表 */
    private static final char emap[] = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', // A-H;0-7
            'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', // I-P; 8 -15
            'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', // Q-X; 16-23
            'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', // YZ, a-f; 24-31
            'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', // g-n; 32-39
            'o', 'p', 'q', 'r', 's', 't', 'u', 'v', // o-v; 40-47
            'w', 'x', 'y', 'z', '0', '1', '2', '3', // w-z, 0-3; 48-55
            '4', '5', '6', '7', '8', '9', '+', '/'}; // 4-9, + /; 56-63

    /**
     * 将字符串转换为SHA字节组
     * @param srcStr 源字符串
     * @return rtnByte SHA字节组
     */
    public static final byte[] encodeSHA(String srcStr) {
        MessageDigest md = null;
        byte[] rtnByte = null;
        try {
            md = MessageDigest.getInstance("SHA");
            rtnByte = md.digest(srcStr.getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return rtnByte;
    }
    /**
     * 生成适用于OpenLDAPServer存储的SHA密码字符串
     * @param srcPasswdStr 明文
     * @return rtnStr 处理完成的密码串
     */
    public static String getOpenLDAPSHA(String srcPasswdStr) {
        byte[] shaBytes = encodeSHA(srcPasswdStr);
        String rtnStr = encodeBase64(shaBytes);
        rtnStr = "{SHA}".concat(rtnStr);
        return rtnStr;
    }
    /**
     * 将给定源字符串转换为适用于OpenLDAPSHA格式存储的字符串
     * <p>
     *  <strong>此方法应用在LDIF文件内容，负责生成SHA加密的字符串用以存储在条目相关属性(如userPassword)中。</strong>
     * </p>
     * <p>
     * OpenLDAPSHA格式基本原理：
     * <ol>
     * <li>OpenLDAP以SHA加密方式存储的密码串是经过Base64编码之后的字符串，而不是单纯的SHA加密后的16进制格式的字符串。</li>
     * <li>转换方式主要有两步：
     * <p>
     * 第一步将源字符串进行SHA转换
     * </p>
     * <p>
     * 第二步Base64对进行SHA转换得到的字节组进行编码
     * </p>
     * <p>
     * 第三步通过JNDI(或其它LDAP Client)将处理完成的字符串存储到特定Entry的Attribute中
     * </p>
     * </li>
     * </ol>
     * </p>
     * @param srcStr 源字符串(密码串)
     * @return rtnStr 转换完成的字符串
     */
    public static String getOpenLDAPSHALDIF(String srcStr) {
        String rtnStr = getOpenLDAPSHA(srcStr);
        try {
            rtnStr = encodeBase64(rtnStr.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return rtnStr;
    }
    /**
     * 将字节数组转换为十六进制字符串形式
     * @param byteArr 字节数组
     * @return rtnStr 十六进制表示的字符串
     */
    public static String toHexStr(byte[] byteArr) {
        StringBuilder rtnStr = new StringBuilder();
        String tmpStr = null;
        for (int i = 0; i < byteArr.length; i++) {
            tmpStr = (Integer.toHexString(byteArr[i] & 0xFF));
            rtnStr.append(tmpStr.length() == 2 ? tmpStr : "0".concat(tmpStr));
        }
        return rtnStr.toString();
    }

    /**
     * 将字节数组转换为Base64编码
     * @param inputBytes 字节数组
     * @return rtnStr Base64处理完成后的字符串
     */
    public static final String encodeBase64(byte[] inputBytes) {
        int i, j, k;
        int t, t1, t2;
        int ntb;
        boolean onePadding = false, twoPaddings = false;
        char[] encodedChars;
        int len = inputBytes.length;
        if (len == 0) {
            return new String("");
        }
        ntb = len%3 == 0 ? (len / 3) : ((len / 3) + 1);
        if ((len % 3) == 1) {
            twoPaddings = true;
        } else if ((len % 3) == 2) {
            onePadding = true;
        }
        encodedChars = new char[ntb * 4];
        for (i = 0, j = 0, k = 1; i < len; i += 3, j += 4, k++) {
            t = 0x00ff & inputBytes[i];
            encodedChars[j] = emap[t >> 2];
            if ((k == ntb) && twoPaddings) {
                encodedChars[j + 1] = emap[(t & 0x03) << 4];
                encodedChars[j + 2] = '=';
                encodedChars[j + 3] = '=';
                break;
            } else {
                t1 = 0x00ff & inputBytes[i + 1];
                encodedChars[j + 1] = emap[((t & 0x03) << 4) + ((t1 & 0xf0) >> 4)];
            }
            if ((k == ntb) && onePadding) {
                encodedChars[j + 2] = emap[(t1 & 0x0f) << 2];
                encodedChars[j + 3] = '=';
                break;
            } else {
                t2 = 0x00ff & inputBytes[i + 2];
                encodedChars[j + 2] = (emap[(t1 & 0x0f) << 2 | (t2 & 0xc0) >> 6]);
            }
            encodedChars[j + 3] = (emap[(t2 & 0x3f)]);
        }
        return new String(encodedChars);
    }

    /**
     * 测试入口
     * @param args
     */
    public static void main(String[] args) {
        String passwd = "123456";
        byte[] shaBytes = LDAPSecurityUtils.encodeSHA(passwd);
        String base64Str = encodeBase64(shaBytes);
        String hexStr = "{SHA}".concat(base64Str);
        System.out.println(shaBytes.length + "\n" + LDAPSecurityUtils.toHexStr(shaBytes) + "\nBase64："
            + base64Str + "\nBase64HexStr：" + encodeBase64(hexStr.getBytes()));
        System.out.println(getOpenLDAPSHA(passwd));
    }
}