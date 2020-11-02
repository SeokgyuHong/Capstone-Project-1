package com.kakao.plusfriend;

import android.content.Context;
import android.net.Uri;

import androidx.browser.customtabs.CustomTabsIntent;

import com.kakao.common.KakaoContextService;
import com.kakao.common.PhaseInfo;
import com.kakao.common.ServerProtocol;
import com.kakao.common.IConfiguration;

import com.kakao.util.exception.KakaoException;

/**
 * Class for adding plus friend or chatting with plus friend. This class provides methods for
 * 1) opening a plus friend bridge page in browser, which will call KakaoTalk custom scheme for adding or chatting.
 * 2) creating urls for plus friend bridge page.
 *
 * @author kevin.kang. Created on 2018. 7. 4..
 */
public class PlusFriendService {

    private static PlusFriendService instance = new PlusFriendService();

    /**
     * Returns a singleton PlusFriendService instance
     *
     * @return PlusFriendService instance
     */
    public static PlusFriendService getInstance() {
        return instance;
    }

    /**
     * Opens a bridge page in browser for adding a speicifed plus friend.
     * This bridge page will call KakaoTalk custom scheme.
     *
     * @param context      Context to start custom tab or browser intent and get app configurations
     *                     such as app key and key hash.
     * @param plusFriendId plus friend id (https://pf.kakao.com/${plusFriendId})
     * @throws KakaoException with {@link com.kakao.util.exception.KakaoException.ErrorType#ILLEGAL_ARGUMENT}
     *                        when context or plusFriendId is null
     * @throws KakaoException with {@link com.kakao.util.exception.KakaoException.ErrorType#MISS_CONFIGURATION}
     *                        when required app configuration such as kakao app keys is not correctly set
     */
    public void addFriend(final Context context, final String plusFriendId) throws KakaoException {
        openPlusFriend(context, plusFriendId, ServerProtocol.PF_ADD_PATH);
    }

    /**
     * Opens a bridge page in browser for 1:1 chat with a specified plus friend.
     * This bridge page will call KakaoTalk custom scheme.
     *
     * @param context      Context to start custom tab or browser intent and get app configurations
     *                     such as app key and key hash.
     * @param plusFriendId plus friend id (https://pf.kakao.com/${plusFriendId})
     * @throws KakaoException with {@link com.kakao.util.exception.KakaoException.ErrorType#ILLEGAL_ARGUMENT}
     *                        when context or plusFriendId is null
     * @throws KakaoException with {@link com.kakao.util.exception.KakaoException.ErrorType#MISS_CONFIGURATION}
     *                        when required app configuration such as kakao app keys is not correctly set
     */
    public void chat(final Context context, final String plusFriendId) throws KakaoException {
        openPlusFriend(context, plusFriendId, ServerProtocol.PF_CHAT_PATH);
    }

    /**
     * Returns a url of plus friend bridge page for {@link #addFriend(Context, String)}
     *
     * @param context      Context to start custom tab or browser intent and get app configurations
     *                     such as app key and key hash.
     * @param plusFriendId plus friend id (https://pf.kakao.com/${plusFriendId})
     * @return uri
     * @throws KakaoException with {@link com.kakao.util.exception.KakaoException.ErrorType#ILLEGAL_ARGUMENT}
     *                        when context or plusFriendId is null
     * @throws KakaoException with {@link com.kakao.util.exception.KakaoException.ErrorType#MISS_CONFIGURATION}
     *                        when required app configuration such as kakao app keys is not correctly set
     */
    public Uri addFriendUrl(final Context context, final String plusFriendId) throws KakaoException {
        return createPlusFriendUrl(context, plusFriendId, ServerProtocol.PF_ADD_PATH);
    }

    /**
     * Returns a url of plus friend bridge page for {@link #chat(Context, String)}
     *
     * @param context      Context to start custom tab or browser intent and get app configurations
     *                     such as app key and key hash.
     * @param plusFriendId plus friend id (https://pf.kakao.com/${plusFriendId})
     * @return uri
     * @throws KakaoException with {@link com.kakao.util.exception.KakaoException.ErrorType#ILLEGAL_ARGUMENT}
     *                        when context or plusFriendId is null
     * @throws KakaoException with {@link com.kakao.util.exception.KakaoException.ErrorType#MISS_CONFIGURATION}
     *                        when required app configuration such as kakao app keys is not correctly set
     */
    public Uri chatUrl(final Context context, final String plusFriendId) throws KakaoException {
        return createPlusFriendUrl(context, plusFriendId, ServerProtocol.PF_CHAT_PATH);
    }


    Uri createPlusFriendUrl(final Context context, final String plusFriendId, final String path) throws KakaoException {
        if (context == null) {
            throw new KakaoException(KakaoException.ErrorType.ILLEGAL_ARGUMENT, "context cannot be null.");
        }
        if (plusFriendId == null) {
            throw new KakaoException(KakaoException.ErrorType.ILLEGAL_ARGUMENT, "plusFriendId cannot be null.");
        }
        try {
            IConfiguration configuration = getConfiguration(context);
            PhaseInfo phaseInfo = getPhaseInfo();
            return createPlusFriendUrl(plusFriendId, path, phaseInfo.appKey(), configuration.getKAHeader());
        } catch (Exception e) {
            if (e instanceof KakaoException) throw e;
            throw new KakaoException(KakaoException.ErrorType.UNSPECIFIED_ERROR, e.getMessage());
        }
    }


    void openPlusFriend(final Context context, final String plusFriendId, final String path) {
        try {
            Uri uri = createPlusFriendUrl(context, plusFriendId, path);
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            CustomTabsIntent intent = builder.build();
            intent.launchUrl(context, uri);
//            Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);
//            context.startActivity(browserIntent);

        } catch (Exception e) {
            if (e instanceof KakaoException) throw e;
            throw new KakaoException(KakaoException.ErrorType.UNSPECIFIED_ERROR, e.getMessage());
        }

    }


    Uri createPlusFriendUrl(final String plusFriendId, final String path, final String appKey, final String kaHeader) {
        return new Uri.Builder()
                .scheme(ServerProtocol.SCHEME)
                .authority(ServerProtocol.plusFriendAuthority())
                .path(String.format("%s/%s", plusFriendId, path))
                .appendQueryParameter(StringSet.app_key, appKey)
                .appendQueryParameter(StringSet.api_ver, StringSet.pf_api_ver)
                .appendQueryParameter(StringSet.kakao_agent, kaHeader)
                .build();
    }

    IConfiguration getConfiguration(final Context context) {
        KakaoContextService.getInstance().initialize(context);
        return KakaoContextService.getInstance().getAppConfiguration();
    }

    PhaseInfo getPhaseInfo() {
        return KakaoContextService.getInstance().phaseInfo();
    }
}
