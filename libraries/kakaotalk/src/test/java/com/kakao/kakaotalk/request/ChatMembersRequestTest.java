package com.kakao.kakaotalk.request;

import android.net.Uri;
import androidx.annotation.Nullable;

import com.kakao.kakaotalk.StringSet;
import com.kakao.network.ServerProtocol;
import com.kakao.test.common.KakaoTestCase;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author kevin.kang. Created on 2018. 8. 7..
 */
public class ChatMembersRequestTest extends KakaoTestCase {
    private final Long chatId = 217706717492573L;
    @Override
    public void setup() {
        super.setup();
    }

    @Test
    public void normal() {
        ChatMembersRequest request = new ChatMembersRequest(chatId, true);
        checkParameters(request, chatId, true);
    }

    @Test
    public void nullParameters() {
        ChatMembersRequest request = new ChatMembersRequest(chatId, null);
        checkParameters(request, chatId, null);
    }

    private void checkParameters(final ChatMembersRequest request,
                                 final Long chatId,
                                 final @Nullable Boolean friendsOnly) {
        assertEquals("GET", request.getMethod());

        Uri uri = Uri.parse(request.getUrl());
        assertEquals(ServerProtocol.SCHEME, uri.getScheme());
        assertEquals(ServerProtocol.apiAuthority(), uri.getAuthority());

        assertEquals(ServerProtocol.TALK_CHAT_MEMBERS_PATH, uri.getPath().substring(1));
        assertEquals(chatId.toString(), uri.getQueryParameter(StringSet.chat_id));
        if (friendsOnly != null) {
            assertEquals(friendsOnly.toString(), uri.getQueryParameter(StringSet.friends_only));
        } else {
            assertNull(uri.getQueryParameter(StringSet.friends_only));
        }
    }
}
