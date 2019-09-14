package com.marcelo.animalguide.encryption;

import android.util.Base64;

public class Base64Custom
{

    public static String encryption(String texto)
    {
        return Base64.encodeToString(texto.getBytes(), Base64.DEFAULT).replaceAll("(\\n|\\r)", "");
    }

    public static String decryption(String textoCodificado)
    {
        return new String(Base64.decode(textoCodificado, Base64.DEFAULT));
    }

}
