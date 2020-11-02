/*
  Copyright 2014-2018 Kakao Corp.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package com.kakao.kakaotalk.response;

import com.kakao.kakaotalk.ChatListContext;
import com.kakao.kakaotalk.callback.TalkResponseCallback;
import com.kakao.network.response.JSONObjectResponse;
import com.kakao.friends.StringSet;
import com.kakao.kakaotalk.response.model.ChatInfo;
import com.kakao.network.response.ResponseBody;
import com.kakao.network.response.ResponseStringConverter;

import java.util.List;

/**
 * 챗방목록 {@link com.kakao.kakaotalk.v2.KakaoTalkService#requestChatRoomList(TalkResponseCallback, ChatListContext)} API의 응답 클래스.
 *
 * Response class for {@link com.kakao.kakaotalk.v2.KakaoTalkService#requestChatRoomList(TalkResponseCallback, ChatListContext)}
 *
 * @author leo.shin
 * Created by leoshin on 15. 8. 25..
 */
public class ChatListResponse extends JSONObjectResponse {
    final private List<ChatInfo> chatInfoList;
    final private int totalCount;
    private String beforeUrl;
    private String afterUrl;

    public List<ChatInfo> getChatInfoList() {
        return chatInfoList;
    }

    /**
     * Paging된 요청 중 전 요청의 url
     * @return Previous paged url
     */
    public String getBeforeUrl() {
        return beforeUrl;
    }

    /**
     * Paging된 요청 중 다음 요청의 url
     * @return Next paged url
     */
    public String getAfterUrl() {
        return afterUrl;
    }

    /**
     * 총 챗방 갯수
     *
     * @return Total number of chat rooms
     */
    public int getTotalCount() {
        return totalCount;
    }

    ChatListResponse(String stringData) throws ResponseBody.ResponseBodyException {
        super(stringData);
        this.chatInfoList = ChatInfo.CONVERTER.convertList(getBody().optJSONArray(StringSet.elements, null));
        this.totalCount = getBody().optInt(StringSet.total_count, 0);
        this.beforeUrl = getBody().optString(StringSet.before_url, null);
        this.afterUrl = getBody().optString(StringSet.after_url, null);
    }

    /**
     * paging된 chat list정보를 모은다.
     * @param response 기존에 paging되어 받아온 response에 merge시킬 ChatList 정보.
     */
    public void merge(ChatListResponse response) {
        this.chatInfoList.addAll(response.getChatInfoList());
        this.beforeUrl = response.getBeforeUrl();
        this.afterUrl = response.getAfterUrl();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (chatInfoList != null) {
            for(ChatInfo info : chatInfoList) {
                builder.append("\n[").append(info.toString()).append("]");
            }
        }

        builder.append("totalCount : ").append(totalCount)
                .append(", beforeUrl : ").append(beforeUrl)
                .append(", afterUrl : ").append(afterUrl);

        return builder.toString();
    }

    public static final ResponseStringConverter<ChatListResponse> CONVERTER = new ResponseStringConverter<ChatListResponse>() {
        @Override
        public ChatListResponse convert(String o) throws ResponseBody.ResponseBodyException {
            return new ChatListResponse(o);
        }
    };
}
