package com.marcelo.animalguide.models.message_toast;

import android.app.Activity;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AlignmentSpan;

import com.pranavpandey.android.dynamic.toasts.DynamicToast;

public class MessagesToast
{
    private static Spannable centralizedToast;

    public static void createMessageSucess(String text, Activity activity)
    {
        DynamicToast.Config.getInstance()
                .setTextSize(14)
                .apply();

        centralizedToast = new SpannableString(text);
        centralizedToast.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0,
                text.length() - 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        DynamicToast.makeSuccess(activity, centralizedToast).show();
    }

    public static void createMessageWarning(String text, Activity activity)
    {
        DynamicToast.Config.getInstance()
                .setTextSize(14)
                .apply();

        centralizedToast = new SpannableString(text);
        centralizedToast.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0,
                text.length() - 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        DynamicToast.makeWarning(activity, centralizedToast).show();
    }

    public static void createMessageError(String text, Activity activity)
    {
        DynamicToast.Config.getInstance()
                .setTextSize(16)
                .apply();

        centralizedToast = new SpannableString(text);
        centralizedToast.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0,
                text.length() - 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        DynamicToast.makeError(activity, centralizedToast).show();
    }
}
