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
package com.kakao.friends;

import com.kakao.auth.ApiResponseCallback;
import com.kakao.friends.api.FriendsApi;
import com.kakao.friends.response.AppFriendsResponse;
import com.kakao.network.callback.ResponseCallback;
import com.kakao.friends.response.FriendsResponse;
import com.kakao.network.tasks.ITaskQueue;
import com.kakao.network.tasks.KakaoResultTask;
import com.kakao.network.tasks.KakaoTaskQueue;

/**
 * 유저의 친구 리스트와 각 친구의 정보를 얻어오는 API
 *
 * @author leo.shin
 */
public class FriendsService {
    /**
     * 친구의 리스트를 얻어온다.
     * (제휴를 통해 권한이 부여된 특정 앱에서만 호출이 가능합니다.)
     * @param context {@link FriendContext} 친구리스트 요청정보를 담고있는 context
     */
    public void requestFriends(final ApiResponseCallback<FriendsResponse> callback, final FriendContext context) {
        taskQueue.addTask(new KakaoResultTask<FriendsResponse>(callback) {
            @Override
            public FriendsResponse call() throws Exception {
                return api.requestFriends(context);
            }
        });
    }

    /**
     * 카카오톡 친구 중 아래의 조건을 만족하는 친구들의 리스트를 얻어온다.
     * <ol>
     *     <li>이 앱에 카카오 로그인을 통해 로그인을 하였음</li>
     *     <li>친구 목록 정보 제공동의를 하였음</li>
     * </ol>
     *
     * Request for a list of KakaoTalk friends who also:
     *
     * <ol>
     *     <li>Registered to this app (Connect with Kakao)</li>
     *     <li>Agreed to provide friends info to this app</li>
     * </ol>
     *
     * Please check the difference between {@link com.kakao.friends.response.model.AppFriendInfo}
     * and {@link com.kakao.friends.response.model.FriendInfo}, where the latter is only provided
     * to the Kakao partners with privilege.
     *
     * This API will return {@link ApiErrorCode#NOT_EXIST_KAKAOTALK_USER_CODE} if this Kakao account user does not use KakaoTalk.
     *
     * @param context Context
     * @param callback Success/Failure callback
     * @since 1.11.1
     */
    public void requestAppFriends(final AppFriendContext context, final ApiResponseCallback<AppFriendsResponse> callback) {
        taskQueue.addTask(new KakaoResultTask<AppFriendsResponse>(callback) {
            @Override
            public AppFriendsResponse call() throws Exception {
                return api.requestAppFriends(context);
            }
        });
    }

    /**
     * 친구 정보요청을 통해 얻은 데이터를 토대로 Operation을 수행할 수 있다.
     * (제휴를 통해 권한이 부여된 특정 앱에서만 호출이 가능합니다.)
     * @param callback 응답결과를 받을 callback.
     * @param context Operation에 필요한 데이터를 담은 Context.
     *
     * @deprecated 이 API는 더 이상 SDK에서 제공하지 않습니다.
     */
    @Deprecated
    public void requestFriendsOperation(final ResponseCallback<FriendsResponse> callback, final FriendOperationContext context) {
        taskQueue.addTask(new KakaoResultTask<FriendsResponse>(callback) {
            @Override
            public FriendsResponse call() throws Exception {
                return api.requestFriendsOperation(context);
            }
        });
    }

    private static FriendsService instance = new FriendsService(FriendsApi.getInstance(),
            KakaoTaskQueue.getInstance());

    /**
     * Get a singleton instance of FriendsService
     *
     * @return a singleton FriendsService instace
     */
    public static FriendsService getInstance() {
        return instance;
    }

    private FriendsApi api;
    private ITaskQueue taskQueue;

    @SuppressWarnings("WeakerAccess")
    FriendsService(final FriendsApi api, final ITaskQueue taskQueue) {
        this.api = api;
        this.taskQueue = taskQueue;
    }
}
