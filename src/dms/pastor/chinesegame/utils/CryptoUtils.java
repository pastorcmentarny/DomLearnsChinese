package dms.pastor.chinesegame.utils;

import android.annotation.SuppressLint;
import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import static android.util.Base64.DEFAULT;
import static android.util.Base64.decode;
import static javax.crypto.Cipher.ENCRYPT_MODE;
import static javax.crypto.Cipher.getInstance;

/**
 * Author: Dominik Symonowicz "Pastor cmentarny"
 * WWW:	http://pastor.ovh.org
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: uk.linkedin.com/pub/dominik-symonowicz/5a/706/981/
 * Email: email can be found on my website
 */
public final class CryptoUtils {
    private static final String ALGORITHM = "AES";

    private CryptoUtils() {
        //Utility classes
    }

    public static String encrypt(byte[] raw, byte[] clear) throws Exception {
        SecretKeySpec secretKeySpecification = new SecretKeySpec(raw, ALGORITHM);
        @SuppressLint("GetInstance") Cipher cipher = getInstance(ALGORITHM);
        cipher.init(ENCRYPT_MODE, secretKeySpecification);
        byte[] encrypted = cipher.doFinal(clear);
        return Base64.encodeToString(encrypted, DEFAULT);
    }

    public static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
        SecretKeySpec secretKeySpecification = new SecretKeySpec(raw, ALGORITHM);
        @SuppressLint("GetInstance") Cipher cipher = getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpecification);
        return cipher.doFinal(decode(encrypted, DEFAULT));
    }

}
