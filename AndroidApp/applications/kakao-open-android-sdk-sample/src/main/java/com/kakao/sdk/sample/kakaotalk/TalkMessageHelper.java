package com.kakao.sdk.sample.kakaotalk;

import android.content.Context;
import android.content.DialogInterface;

import com.kakao.sdk.sample.BuildConfig;
import com.kakao.sdk.sample.R;
import com.kakao.sdk.sample.common.widget.DialogBuilder;
import com.kakao.sdk.sample.friends.FriendsMainActivity.MSG_TYPE;

/**
 * @author leoshin on 15. 9. 4.
 */
class TalkMessageHelper {
    static String getMemoTemplateId() {
        switch (BuildConfig.FLAVOR) {
            case "dev":
                return "124181";
            case "sandbox":
                return "637";
            case "cbt":
            case "production":
                return "17125";
            default:
                return null;
        }
    }

    static String getSampleTemplateId(MSG_TYPE msgType) {
        switch (BuildConfig.FLAVOR) {
            case "dev":
                return getAlphaTemplateId(msgType);
            case "sandbox":
                return getSandboxTemplateId(msgType);
            case "cbt":
            case "production":
            default:
                return getReleaseTemplateId(msgType);
        }
    }

    static String getAlphaTemplateId(MSG_TYPE msgType) {
        switch (msgType) {
            case FEED:
                return "124181";
            case LIST:
                return "124182";
            default:
                return "124183";
        }
    }

    static String getSandboxTemplateId(MSG_TYPE msgType) {
        switch (msgType) {
            case FEED:
                return "637";
            case LIST:
                return "638";
            default:
                return "639";
        }
    }

    static String getReleaseTemplateId(MSG_TYPE msgType) {
        switch (msgType) {
            case FEED:
                return "17125";
            case LIST:
                return "17126";
            default:
                return "17127";
        }
    }

    static void showSendMessageDialog(Context context, final DialogInterface.OnClickListener listener) {
        final String message = context.getString(R.string.send_message);
        new DialogBuilder(context)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    if (listener != null) {
                        listener.onClick(dialog, which);
                    }
                    dialog.dismiss();
                }).create().show();
    }
}
