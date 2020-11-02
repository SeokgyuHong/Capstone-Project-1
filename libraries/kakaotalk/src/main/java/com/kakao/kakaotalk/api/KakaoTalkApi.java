/*
  Copyright 2014-2019 Kakao Corp.

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
package com.kakao.kakaotalk.api;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.Map;

import com.kakao.auth.common.MessageSendable;
import com.kakao.auth.network.AuthNetworkService;
import com.kakao.auth.network.AuthorizedRequest;
import com.kakao.friends.AppFriendContext;
import com.kakao.friends.FriendContext;
import com.kakao.friends.api.FriendsApi;
import com.kakao.friends.response.AppFriendsResponse;
import com.kakao.friends.response.FriendsResponse;
import com.kakao.kakaotalk.request.ChatMembersRequest;
import com.kakao.kakaotalk.request.CustomMessageBroadcastRequest;
import com.kakao.kakaotalk.request.DefaultMemoRequest;
import com.kakao.kakaotalk.request.DefaultMessageBroadcastRequest;
import com.kakao.kakaotalk.request.DefaultMessageRequest;
import com.kakao.kakaotalk.request.PlusFriendsRequest;
import com.kakao.kakaotalk.request.ScrapMessageBroadcastRequest;
import com.kakao.kakaotalk.request.ScrapTemplateRequest;
import com.kakao.kakaotalk.request.SendMemoRequest;
import com.kakao.kakaotalk.request.SendMessageRequest;
import com.kakao.kakaotalk.response.ChatMembersResponse;
import com.kakao.kakaotalk.response.MessageSendResponse;
import com.kakao.kakaotalk.response.PlusFriendsResponse;
import com.kakao.network.response.BlankApiResponse;
import com.kakao.kakaotalk.ChatListContext;
import com.kakao.kakaotalk.request.ChatRoomListRequest;
import com.kakao.kakaotalk.request.TalkProfileRequest;
import com.kakao.kakaotalk.response.ChatListResponse;
import com.kakao.kakaotalk.response.KakaoTalkProfile;
import com.kakao.kakaotalk.response.TalkProfileResponse;
import com.kakao.message.template.TemplateParams;

/**
 * 카카오톡 API 요청을 담당한다.
 *
 * @author leo.shin
 */
public class KakaoTalkApi {
    private static KakaoTalkApi instance = new KakaoTalkApi(AuthNetworkService.Factory.getInstance(),
            FriendsApi.getInstance());

    private AuthNetworkService networkService;
    private FriendsApi friendsApi;

    @SuppressWarnings("WeakerAccess")
    KakaoTalkApi(final AuthNetworkService networkService, FriendsApi friendsApi) {
        this.networkService = networkService;
        this.friendsApi = friendsApi;
    }

    public static KakaoTalkApi getInstance() {
        return instance;
    }

    /**
     * 카카오톡 프로필 요청
     *
     * @throws Exception if request or parsing fails
     */
    public KakaoTalkProfile requestProfile() throws Exception {
        return requestProfile(false);
    }

    /**
     * 카카오톡 프로필 요청
     *
     * @param secureResource 이미지 url을 https로 반환할지 여부.
     * @throws Exception Exception if request or parsing fails
     */
    public KakaoTalkProfile requestProfile(boolean secureResource) throws Exception {
        return networkService.request(new TalkProfileRequest(secureResource), TalkProfileResponse.CONVERTER);
    }

    public FriendsResponse requestFriends(final FriendContext context) throws Exception {
        return friendsApi.requestFriends(context);
    }

    public AppFriendsResponse requestAppFriends(final AppFriendContext context) throws Exception {
        return friendsApi.requestAppFriends(context);
    }

    /**
     * 카카오톡 메시지 전송하며, 리치메시지 4.0 spec으로 구성된 template으로 카카오톡 메시지 전송.
     * (제휴를 통해 권한이 부여된 특정 앱에서만 호출이 가능합니다.)
     *
     * @param receiverInfo 메세지 전송할 대상에 대한 정보를 가지고 있는 object
     * @param templateId   개발자 사이트를 통해 생성한 메시지 템플릿 id
     * @param args         메시지 템플릿에 정의한 arg key:value. 템플릿에 정의된 모든 arg가 포함되어야 함.
     * @return <code>true</code> if request succeeds
     * @throws Exception if request fails
     */
    public boolean requestSendV2Message(final MessageSendable receiverInfo, final String templateId, final Map<String, String> args) throws Exception {
        AuthorizedRequest request = getSendMessageRequest(receiverInfo, templateId, args);
        return networkService.request(request, BlankApiResponse.CONVERTER);
    }

    public boolean requestSendV2Message(final MessageSendable receiverInfo, final TemplateParams params) throws Exception {
        AuthorizedRequest request = getDefaultMessageRequest(receiverInfo, params);
        return networkService.request(request, BlankApiResponse.CONVERTER);
    }

    /**
     * 카카오톡에 나에게 메세지를 전송한다.
     * 퍼미션 불필요. 수신자별/발신자별 쿼터 제한 없음.
     * 초대 메시지는 나에게 전송 불가.
     * 카카오톡에 가입이 되어있어야함.
     *
     * @param templateId 개발자 사이트를 통해 생성한 메시지 템플릿 id
     * @param args       메시지 템플릿에 정의한 arg key:value. 템플릿에 정의된 모든 arg가 포함되어야 함.
     * @return <code>true</code> if request succeeds
     * @throws Exception if request fails
     */
    public boolean requestSendV2Memo(@NonNull final String templateId, @Nullable final Map<String, String> args) throws Exception {
        AuthorizedRequest request = getSendMemoRequest(templateId, args);
        return networkService.request(request, BlankApiResponse.CONVERTER);
    }

    public boolean requestSendV2Memo(@NonNull final TemplateParams params) throws Exception {
        AuthorizedRequest request = getDefaultMemoRequest(params);
        return networkService.request(request, BlankApiResponse.CONVERTER);
    }

    public boolean requestSendV2Memo(
            @NonNull final String url,
            @Nullable final String templateId,
            @Nullable final Map<String, String> templateArgs
    ) throws Exception {
        AuthorizedRequest request = new ScrapTemplateRequest(url, templateId, templateArgs);
        return networkService.request(request, BlankApiResponse.CONVERTER);
    }

    public MessageSendResponse sendMessageToFriends(
            @NonNull final List<String> receiverUuids,
            @NonNull final String templateId,
            @Nullable final Map<String, String> args
    ) throws Exception {
        AuthorizedRequest request = new CustomMessageBroadcastRequest(receiverUuids, templateId, args);
        return networkService.request(request, MessageSendResponse.CONVERTER);
    }

    public MessageSendResponse sendMessageToFriends(@NonNull final List<String> receiverUuids,
                                                    @NonNull final TemplateParams templateParams) throws Exception {
        AuthorizedRequest request = new DefaultMessageBroadcastRequest(receiverUuids, templateParams);
        return networkService.request(request, MessageSendResponse.CONVERTER);
    }

    public MessageSendResponse sendMessageToAppFriends(
            @NonNull final List<String> receiverUuids,
            @NonNull final String url,
            @Nullable final String templateId,
            @Nullable final Map<String, String> templateArgs
    ) throws Exception {
        ScrapMessageBroadcastRequest request = new ScrapMessageBroadcastRequest(receiverUuids, url, templateId, templateArgs);
        return networkService.request(request, MessageSendResponse.CONVERTER);
    }

    /**
     * 톡의 그룹챗방 리스트 정보
     * 해당 유저의 group 챗방을 가져온다.
     * 기본 정렬은 asc로 최근 대화 순으로 정렬한다. (desc는 반대로 가장 오래된 대화 순으로 정렬한다.)
     * (제휴를 통해 권한이 부여된 특정 앱에서만 호출이 가능합니다.)
     * <p>
     * {@link }
     *
     * @param context {@link ChatListContext} 챗방리스트 요청정보를 담고있는 context
     * @return ChatListResponse containing list of KakaoTalk's group chats
     * @throws Exception if request fails
     */
    public ChatListResponse requestChatRoomList(ChatListContext context) throws Exception {
        AuthorizedRequest request = new ChatRoomListRequest(context);
        ChatListResponse response = networkService.request(request, ChatListResponse.CONVERTER);
        context.setAfterUrl(response.getAfterUrl());
        context.setBeforeUrl(response.getBeforeUrl());
        return response;
    }

    /**
     * 톡 챗방의 멤버 리스트 정보를 가져온다.
     *
     * @param chatId      chat id retrieved from {@link #requestChatRoomList(ChatListContext)}
     * @param friendsOnly true if only friends are wanted, false otherwise
     * @return Chat members response
     * @throws Exception if request or parsing fails
     */
    public ChatMembersResponse requestChatMembers(final @NonNull Long chatId, final Boolean friendsOnly) throws Exception {
        AuthorizedRequest request = new ChatMembersRequest(chatId, friendsOnly);
        return networkService.request(request, ChatMembersResponse.CONVERTER);
    }

    /**
     * 유저가 특정 카카오톡 채널을 추가했는지 확인.
     *
     * @param publicIds 정보 조회하려는 plus friend 의 public id 리스트.
     * @throws Exception
     * @since 1.17.0
     */
    public PlusFriendsResponse plusFriends(List<String> publicIds) throws Exception {
        AuthorizedRequest request = new PlusFriendsRequest(publicIds);
        return networkService.request(request, PlusFriendsResponse.CONVERTER);
    }

    @SuppressWarnings("WeakerAccess")
    SendMessageRequest getSendMessageRequest(final MessageSendable receiverInfo, final String templateId, final Map<String, String> args) {
        return new SendMessageRequest(receiverInfo, templateId, args);
    }

    @SuppressWarnings("WeakerAccess")
    SendMemoRequest getSendMemoRequest(final String templateId, final Map<String, String> args) {
        return new SendMemoRequest(templateId, args);
    }

    @SuppressWarnings("WeakerAccess")
    DefaultMessageRequest getDefaultMessageRequest(final MessageSendable receiverInfo, final TemplateParams templateParams) {
        return new DefaultMessageRequest(receiverInfo, templateParams);
    }

    @SuppressWarnings("WeakerAccess")
    DefaultMemoRequest getDefaultMemoRequest(final TemplateParams templateParams) {
        return new DefaultMemoRequest(templateParams);
    }
}
