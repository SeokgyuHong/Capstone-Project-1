package com.kakao.friends.request;

import android.net.Uri;

import com.kakao.auth.network.AuthorizedApiRequest;
import com.kakao.friends.AppFriendContext;
import com.kakao.friends.AppFriendOrder;
import com.kakao.friends.FriendContext;
import com.kakao.friends.StringSet;
import com.kakao.network.ServerProtocol;
import com.kakao.test.common.KakaoTestCase;
import com.kakao.util.helper.log.Logger;

import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Objects;

/**
 * @author kevin.kang. Created on 2017. 12. 5..
 */

public class FriendsRequestTest extends KakaoTestCase {
    @Before
    public void setup() {
        super.setup();
    }

    @Test
    public void createFriendsRequest() {
        FriendContext friendContext = FriendContext.createContext(FriendsRequest.FriendType.KAKAO_TALK,
                FriendsRequest.FriendFilter.NONE, FriendsRequest.FriendOrder.NICKNAME,
                false, 0, 100, "asc");
        AuthorizedApiRequest request = new FriendsRequest(friendContext);

        Uri uri = request.getUriBuilder().build();
        assertEquals(ServerProtocol.apiAuthority(), uri.getAuthority());
        assertEquals(1, uri.getQueryParameters(StringSet.secure_resource).size());
        assertEquals(String.valueOf(true), uri.getQueryParameter(StringSet.secure_resource));
        assertEquals(1, uri.getQueryParameters(StringSet.friend_order).size());
        assertEquals(FriendsRequest.FriendOrder.NICKNAME.getValue(), uri.getQueryParameter(StringSet.friend_order));
    }

    @Test
    public void appFriendsRequest() {
        AppFriendContext context = new AppFriendContext(AppFriendOrder.FAVORITE, 0, 100, "asc");
        AuthorizedApiRequest request = new AppFriendsRequest(context);
        Uri uri = request.getUriBuilder().build();
        assertEquals(ServerProtocol.apiAuthority(), uri.getAuthority());
        assertEquals(5, uri.getQueryParameterNames().size());
        assertEquals(ServerProtocol.OPEN_FRIENDS_V1_PATH, Objects.requireNonNull(uri.getPath()).substring(1));

        assertEquals(1, uri.getQueryParameters(StringSet.secure_resource).size());
        assertEquals(String.valueOf(true), uri.getQueryParameter(StringSet.secure_resource));
        assertEquals(1, uri.getQueryParameters(StringSet.friend_order).size());
        assertEquals(AppFriendOrder.FAVORITE.getValue(), uri.getQueryParameter(StringSet.friend_order));

        assertEquals(String.valueOf(0), uri.getQueryParameter(StringSet.offset));
        assertEquals(String.valueOf(100), uri.getQueryParameter(StringSet.limit));
        assertEquals("asc", uri.getQueryParameter(StringSet.order));
    }
}
