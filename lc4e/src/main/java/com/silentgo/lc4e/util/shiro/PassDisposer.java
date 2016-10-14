package com.silentgo.lc4e.util.shiro;


import com.silentgo.lc4e.dao.User;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

public class PassDisposer {

    private static RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();

    private final static String algorithmName = "md5";
    private final static int hashIterations = 2;

    public static void encryptPassword(User user) {

        user.setPasssalt(randomNumberGenerator.nextBytes().toHex());

        String newPassword = new SimpleHash(algorithmName, user.getPassword(), ByteSource.Util.bytes(user.getName() + user.getPasssalt()), hashIterations).toHex();

        user.setPassword(newPassword);
    }
}
