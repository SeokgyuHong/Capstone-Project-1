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
package com.kakao.kakaotalk.v2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kakao.auth.common.MessageSendable;
import com.kakao.friends.AppFriendContext;
import com.kakao.friends.FriendContext;
import com.kakao.friends.response.AppFriendsResponse;
import com.kakao.friends.response.FriendsResponse;
import com.kakao.kakaotalk.ChatListContext;
import com.kakao.kakaotalk.api.KakaoTalkApi;
import com.kakao.kakaotalk.callback.TalkResponseCallback;
import com.kakao.kakaotalk.response.ChatListResponse;
import com.kakao.kakaotalk.response.ChatMembersResponse;
import com.kakao.kakaotalk.response.KakaoTalkProfile;
import com.kakao.kakaotalk.response.PlusFriendsResponse;
import com.kakao.kakaotalk.response.MessageSendResponse;
import com.kakao.message.template.TemplateParams;
import com.kakao.network.tasks.ITaskQueue;
import com.kakao.network.tasks.KakaoResultTask;
import com.kakao.network.tasks.KakaoTaskQueue;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * 카카오톡 API 요청을 담당한다.
 *
 * @author leo.shin
 */
public class KakaoTalkService {
    /**
     * 카카오톡 프로필 요청
     *
     * @param callback 요청 결과에 대한 callback
     */
    @SuppressWarnings("UnusedReturnValue")
    public Future<KakaoTalkProfile> requestProfile(final TalkResponseCallback<KakaoTalkProfile> callback) {
        return taskQueue.addTask(new KakaoResultTask<KakaoTalkProfile>(callback) {
            @Override
            public KakaoTalkProfile call() throws Exception {
                return api.requestProfile();
            }
        });
    }

    /**
     * 카카오톡 프로필 요청
     *
     * @param callback       요청 결과에 대한 callback
     * @param secureResource 이미지 url을 https로 반환할지 여부.
     */
    @SuppressWarnings("unused")
    public Future<KakaoTalkProfile> requestProfile(final TalkResponseCallback<KakaoTalkProfile> callback, final boolean secureResource) {
        return taskQueue.addTask(new KakaoResultTask<KakaoTalkProfile>(callback) {
            @Override
            public KakaoTalkProfile call() throws Exception {
                return api.requestProfile(secureResource);
            }
        });
    }

    /**
     * Request for a list of KakaoTalk friends who also:
     * - Registered to this app (Connect with Kakao)
     * - Agreed to provide friends info to this app
     * <p>
     * This API will return an error if this Kakao account user does not use KakaoTalk. Use
     * {@link TalkResponseCallback#onNotKakaoTalkUser()} to handle the error.
     *
     * @param context  Context
     * @param callback Success/Failure callback
     * @since 1.11.1
     */
    @SuppressWarnings("unused")
    public Future<AppFriendsResponse> requestAppFriends(final AppFriendContext context, final TalkResponseCallback<AppFriendsResponse> callback) {
        return taskQueue.addTask(new KakaoResultTask<AppFriendsResponse>(callback) {
            @Override
            public AppFriendsResponse call() throws Exception {
                return api.requestAppFriends(context);
            }
        });
    }

    /**
     * 앱에 가입한 카카오톡 친구들에게 커스텀 템플릿을 사용하여 카카오톡 메시지 전송
     *
     * @param uuids        친구 API 를 통하여 획득한 친구 uuid 목록. 현재 최대 길이 5.
     * @param templateId   커스텀 템플릿 id
     * @param templateArgs 커스텀 템플릿에 사용하는 파라미터들의 값
     * @param callback     {@link MessageSendResponse} 응답을 전달받을 콜백
     */
    @SuppressWarnings("unused")
    public Future<MessageSendResponse> sendMessageToFriends(
            @NonNull final List<String> uuids,
            @NonNull final String templateId,
            @Nullable final Map<String, String> templateArgs,
            @Nullable final TalkResponseCallback<MessageSendResponse> callback
    ) {
        return taskQueue.addTask(new KakaoResultTask<MessageSendResponse>(callback) {
            @Override
            public MessageSendResponse call() throws Exception {
                return api.sendMessageToFriends(uuids, templateId, templateArgs);
            }
        });
    }

    /**
     * 앱에 가입한 카카오톡 친구들에게 디폴트 템플릿을 사용하여 카카오톡 메시지 전송
     *
     * @param receiverUuids  친구 API 를 통하여 획득한 친구 uuid 목록. 현재 최대 길이 5.
     * @param templateParams 디폴트 템플릿 파라미터
     * @param callback       {@link MessageSendResponse} 응답을 전달받을 콜백
     */
    @SuppressWarnings("unused")
    public Future<MessageSendResponse> sendMessageToFriends(
            @NonNull final List<String> receiverUuids,
            @NonNull final TemplateParams templateParams,
            @Nullable final TalkResponseCallback<MessageSendResponse> callback
    ) {
        return taskQueue.addTask(new KakaoResultTask<MessageSendResponse>(callback) {
            @Override
            public MessageSendResponse call() throws Exception {
                return api.sendMessageToFriends(receiverUuids, templateParams);
            }
        });
    }

    /**
     * 앱에 가입한 카카오톡 친구들에게 스크랩 템플릿을 사용하여 카카오톡 메시지 전송
     *
     * @param receiverUuids 친구 API 를 통하여 획득한 친구 uuid 목록. 현재 최대 길이 5.
     * @param url           스크랩할 url
     * @param callback      {@link MessageSendResponse} 응답을 전달받을 콜백
     */
    public Future<MessageSendResponse> sendMessageToFriends(
            @NonNull final List<String> receiverUuids,
            @NonNull final String url,
            @Nullable final TalkResponseCallback<MessageSendResponse> callback
    ) {
        return taskQueue.addTask(new KakaoResultTask<MessageSendResponse>(callback) {
            @Override
            public MessageSendResponse call() throws Exception {
                return api.sendMessageToAppFriends(receiverUuids, url, null, null);
            }
        });
    }

    /**
     * 앱에 가입한 카카오톡 친구들에게 스크랩 템플릿을 사용하여 카카오톡 메시지 전송
     *
     * @param receiverUuids 친구 API 를 통하여 획득한 친구 uuid 목록. 현재 최대 길이 5.
     * @param url           스크랩할 url
     * @param templateId    커스텀 템플릿 id
     * @param templateArgs  커스텀 템플릿에 사용하는 파라미터들의 값
     * @param callback      {@link MessageSendResponse} 응답을 전달받을 콜백
     */
    @SuppressWarnings("unused")
    public Future<MessageSendResponse> sendMessageToFriends(
            @NonNull final List<String> receiverUuids,
            @NonNull final String url,
            @Nullable final String templateId,
            @Nullable final Map<String, String> templateArgs,
            @Nullable final TalkResponseCallback<MessageSendResponse> callback
    ) {
        return taskQueue.addTask(new KakaoResultTask<MessageSendResponse>(callback) {
            @Override
            public MessageSendResponse call() throws Exception {
                return api.sendMessageToAppFriends(receiverUuids, url, templateId, templateArgs);
            }
        });
    }


    /**
     * Send KakaoTalk message to self with message template v2.
     * <p>
     * 퍼미션 불필요. 수신자별/발신자별 쿼터 제한 없음.
     * 초대 메시지는 나에게 전송 불가.
     * 카카오톡에 가입이 되어있어야함.
     *
     * @param callback     요청 결과에 대한 callback
     * @param templateId   개발자 사이트를 통해 생성한 메시지 템플릿 id
     * @param templateArgs 메시지 템플릿에 정의한 arg key:value. 템플릿에 정의된 모든 arg 가 포함되어야 함.
     */
    @SuppressWarnings("UnusedReturnValue")
    public Future<Boolean> requestSendMemo(final TalkResponseCallback<Boolean> callback,
                                           final String templateId,
                                           final Map<String, String> templateArgs) {
        return taskQueue.addTask(new KakaoResultTask<Boolean>(callback) {
            @Override
            public Boolean call() throws Exception {
                return api.requestSendV2Memo(templateId, templateArgs);
            }
        });
    }

    /**
     * Send KakaoTalk message to self with message template v2.
     * <p>
     * 퍼미션 불필요. 수신자별/발신자별 쿼터 제한 없음.
     * 초대 메시지는 나에게 전송 불가.
     * 카카오톡에 가입이 되어있어야함.
     *
     * @param callback       요청 결과에 대한 callback
     * @param templateParams 템플릿 파라미터
     */
    @SuppressWarnings("UnusedReturnValue")
    public Future<Boolean> requestSendMemo(final TalkResponseCallback<Boolean> callback,
                                           final TemplateParams templateParams) {
        return taskQueue.addTask(new KakaoResultTask<Boolean>(callback) {
            @Override
            public Boolean call() throws Exception {
                return api.requestSendV2Memo(templateParams);
            }
        });
    }

    /**
     * 스크랩 템플릿을 사용하여 나에게 메시지 전송
     *
     * @param url      스크랩할 url
     * @param callback {@link MessageSendResponse} 응답을 전달받을 콜백
     */
    public Future<Boolean> requestSendMemo(@NonNull final String url, @NonNull final TalkResponseCallback<Boolean> callback) {
        return taskQueue.addTask(new KakaoResultTask<Boolean>(callback) {
            @Override
            public Boolean call() throws Exception {
                return api.requestSendV2Memo(url, null, null);
            }
        });
    }

    /**
     * 스크랩 정보를 커스텀 템플릿에 사용하여 나에게 메시지 전송
     *
     * @param url          스크랩할 url
     * @param templateId   커스텀 템플릿 id
     * @param templateArgs 커스텀 템플릿에 사용하는 파라미터들의 값
     * @param callback     {@link MessageSendResponse} 응답을 전달받을 콜백
     */
    @SuppressWarnings("unused")
    public Future<Boolean> requestSendMemo(
            @NonNull final String url,
            @Nullable final String templateId,
            @Nullable final Map<String, String> templateArgs,
            @NonNull final TalkResponseCallback<Boolean> callback
    ) {
        return taskQueue.addTask(new KakaoResultTask<Boolean>(callback) {
            @Override
            public Boolean call() throws Exception {
                return api.requestSendV2Memo(url, templateId, templateArgs);
            }
        });
    }

    /**
     * 유저가 특정 카카오톡 채널과 친구를 맺었는지 확인.
     *
     * @param callback success/failure callback for this API
     * @return Future containing {@link PlusFriendsResponse}
     * @since 1.17.0
     */
    @SuppressWarnings("unused")
    public Future<PlusFriendsResponse> plusFriends(final TalkResponseCallback<PlusFriendsResponse> callback) {
        return taskQueue.addTask(new KakaoResultTask<PlusFriendsResponse>(callback) {
            @Override
            public PlusFriendsResponse call() throws Exception {
                return api.plusFriends(null);
            }
        });
    }

    /**
     * 유저가 특정 카카오톡 채널과 친구를 맺었는지 확인.
     *
     * @param publicIds 정보 조회하려는 plus friend의 public id 리스트.
     * @param callback  success/failure callback for this API
     * @return Future containing {@link PlusFriendsResponse}
     * @since 1.17.0
     */
    @SuppressWarnings("unused")
    public Future<PlusFriendsResponse> plusFriends(final List<String> publicIds, final TalkResponseCallback<PlusFriendsResponse> callback) {
        return taskQueue.addTask(new KakaoResultTask<PlusFriendsResponse>(callback) {
            @Override
            public PlusFriendsResponse call() throws Exception {
                return api.plusFriends(publicIds);
            }
        });
    }

    /**
     * 카카오톡 친구 리스트를 요청한다. Friends에 대한 접근권한이 있는 경우에만 얻어올 수 있다.
     * (제휴를 통해 권한이 부여된 특정 앱에서만 호출이 가능합니다.)
     *
     * @param callback 요청 결과에 대한 callback
     * @param context  친구리스트 요청정보를 담고있는 context
     */
    @SuppressWarnings("unused")
    public Future<FriendsResponse> requestFriends(final TalkResponseCallback<FriendsResponse> callback, final FriendContext context) {
        return taskQueue.addTask(new KakaoResultTask<FriendsResponse>(callback) {
            @Override
            public FriendsResponse call() throws Exception {
                return api.requestFriends(context);
            }
        });
    }

    /**
     * 카카오톡 메시지 전송하며, message template v2로 구성된 template으로 카카오톡 메시지 전송.
     * <p>
     * (제휴를 통해 권한이 부여된 특정 앱에서만 호출이 가능합니다.)
     * <p>
     * 오픈 API 용으로는 {@link #sendMessageToFriends(List, String, Map, TalkResponseCallback)} 참고.
     *
     * @param callback     요청 결과에 대한 callback
     * @param receiverInfo 메세지 전송할 대상에 대한 정보를 가지고 있는 object
     * @param templateId   개발자 사이트를 통해 생성한 메시지 템플릿 id
     * @param templateArgs 메시지 템플릿에 정의한 arg key:value. 템플릿에 정의된 모든 arg가 포함되어야 함.
     */
    @SuppressWarnings("UnusedReturnValue")
    public Future<Boolean> requestSendMessage(final TalkResponseCallback<Boolean> callback,
                                              final MessageSendable receiverInfo,
                                              final String templateId,
                                              final Map<String, String> templateArgs) {
        return taskQueue.addTask(new KakaoResultTask<Boolean>(callback) {
            @Override
            public Boolean call() throws Exception {
                return api.requestSendV2Message(receiverInfo, templateId, templateArgs);
            }
        });
    }

    /**
     * 카카오톡 메시지 전송하며, message template v2로 구성된 template 으로 카카오톡 메시지 전송.
     * <p>
     * (제휴를 통해 권한이 부여된 특정 앱에서만 호출이 가능합니다.)
     * 오픈 API 용으로는 {@link #sendMessageToFriends(List, TemplateParams, TalkResponseCallback)} 참고.
     *
     * @param callback       요청 결과에 대한 callback
     * @param receiverInfo   메세지 전송할 대상에 대한 정보를 가지고 있는 object
     * @param templateParams 템플릿 파라미터
     */
    @SuppressWarnings("unused")
    public Future<Boolean> requestSendMessage(final TalkResponseCallback<Boolean> callback,
                                              final MessageSendable receiverInfo,
                                              final TemplateParams templateParams) {
        return taskQueue.addTask(new KakaoResultTask<Boolean>(callback) {
            @Override
            public Boolean call() throws Exception {
                return api.requestSendV2Message(receiverInfo, templateParams);
            }
        });
    }

    /**
     * 톡의 채팅방 리스트 정보
     * 권한이 있는 방에 대한 정보만 내려받는다. 권한이 없는 {@link com.kakao.kakaotalk.ChatFilterBuilder.ChatFilter} 타입에 대해서는 카카오톡에 채팅방이 존재해도 값이 내려가지 않는다.
     * 기본 정렬은 asc로 최근 대화 순으로 정렬한다. (desc는 반대로 가장 오래된 대화 순으로 정렬한다.)
     * 권한이 필요한 채팅방 정보(regular_direct, regular_multi)
     * regular에 대한 권한은 제휴된 앱에만 부여합니다.
     *
     * @param callback 요청 결과에 대한 callback
     * @param context  {@link ChatListContext} 챗방리스트 요청정보를 담고있는 context
     */
    public Future<ChatListResponse> requestChatRoomList(final TalkResponseCallback<ChatListResponse> callback, final ChatListContext context) {
        return taskQueue.addTask(new KakaoResultTask<ChatListResponse>(callback) {
            @Override
            public ChatListResponse call() throws Exception {
                return api.requestChatRoomList(context);
            }
        });
    }

    /**
     * 톡 채팅방 멤버 리스트 정보
     * (제휴를 통해 권한이 부여된 특정 앱에서만 호출이 가능합니다.)
     *
     * @param chatId      chat id retrieved from {@link #requestChatRoomList(TalkResponseCallback, ChatListContext)}
     * @param friendsOnly true if only friends are wanted, false otherwise
     * @param callback    success/failure callback for this API
     * @return Future containing {@link ChatMembersResponse}
     * @since 1.13.0
     */
    @SuppressWarnings("unused")
    public Future<ChatMembersResponse> requestChatMembers(final @NonNull Long chatId,
                                                          final Boolean friendsOnly,
                                                          final TalkResponseCallback<ChatMembersResponse> callback) {
        return taskQueue.addTask(new KakaoResultTask<ChatMembersResponse>(callback) {
            @Override
            public ChatMembersResponse call() throws Exception {
                return api.requestChatMembers(chatId, friendsOnly);
            }
        });
    }

    private static KakaoTalkService instance = new KakaoTalkService(KakaoTalkApi.getInstance(),
            KakaoTaskQueue.getInstance());

    private KakaoTalkApi api;
    private ITaskQueue taskQueue;

    @SuppressWarnings("unused")
    private KakaoTalkService() {
    }

    public static KakaoTalkService getInstance() {
        return instance;
    }

    @SuppressWarnings("WeakerAccess")
    KakaoTalkService(final KakaoTalkApi api, final ITaskQueue taskQueue) {
        this.api = api;
        this.taskQueue = taskQueue;
    }
}
