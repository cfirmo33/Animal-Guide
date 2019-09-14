package com.marcelo.animalguide.models.classes;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;

public class DatesCustomized
{

    public static String getData()
    {
        long data = System.currentTimeMillis();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        return simpleDateFormat.format(data);
    }

    public static String dateCustom (String data)
    {
        String[] returnData = data.split("/");
        String dia = returnData[0];//dia 13
        String mes = returnData[1];//mes 09
        String ano = returnData[2];//ano 2019

        String date = dia+mes+ano;
        return date;
    }
}
