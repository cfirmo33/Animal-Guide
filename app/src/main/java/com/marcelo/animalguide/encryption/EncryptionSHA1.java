package com.marcelo.animalguide.encryption;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptionSHA1
{
    private static MessageDigest messageDigest = null;

    static
    {
        try
        {
            messageDigest = MessageDigest.getInstance("SHA1");
        }
        catch (NoSuchAlgorithmException ex)
        {
            ex.printStackTrace();
        }
    }

    private static char[] hexCodes(byte[] text)
    {
        char[] hexOutput = new char[text.length * 2];
        String hexString;
        for (int i = 0; i < text.length; i++)
        {
            hexString = "00" + Integer.toHexString(text[i]);
            hexString.toUpperCase().getChars(hexString.length() - 2, hexString.length(), hexOutput, i * 2);
        }
        return hexOutput;
    }

    public static String encryptionString(String string)
    {
        if (messageDigest != null)
        {
            return new String(hexCodes(messageDigest.digest(string.getBytes())));
        }
        return null;
    }
}

