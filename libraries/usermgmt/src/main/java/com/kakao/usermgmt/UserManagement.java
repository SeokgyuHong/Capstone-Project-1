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
package com.kakao.usermgmt;

import android.app.Activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import com.kakao.auth.AccessTokenCallback;
import com.kakao.auth.ApiResponseCallback;
import com.kakao.auth.AuthService.AgeLimit;
import com.kakao.auth.ISession;
import com.kakao.auth.Session;
import com.kakao.network.callback.ResponseCallback;
import com.kakao.network.tasks.ITaskQueue;
import com.kakao.network.tasks.KakaoResultTask;
import com.kakao.network.tasks.KakaoTaskQueue;
import com.kakao.usermgmt.api.UserApi;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;
import com.kakao.usermgmt.response.AgeAuthResponse;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.usermgmt.response.ServiceTermsResponse;
import com.kakao.usermgmt.response.ShippingAddressResponse;

/**
 * UserManagement API 요청을 담당한다.
 *
 * @author MJ
 */
public class UserManagement {

    /**
     * 추가 동의가 필요로 하는 인증정보를 response에 포함하고 싶은 경우 해당 키 리스트.
     * 현재는 "account_ci"만 제공("account_ci" 추가 동의 필요하므로 해당 동의를 받지 않은 사용자에게는 추가 동의창이 뜨게 됨)
     */
    public enum AgeAuthProperty {
        ACCOUNT_CI("account_ci");

        final private String value;

        AgeAuthProperty(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    private UserApi api;
    private ITaskQueue taskQueue;
    private ISession session;

    UserManagement(UserApi api, ITaskQueue taskQueue, ISession session) {
        this.api = api;
        this.taskQueue = taskQueue;
        this.session = session;
    }

    private static UserManagement instance = new UserManagement(UserApi.getInstance(),
            KakaoTaskQueue.getInstance(), Session.getCurrentSession());

    public static UserManagement getInstance() {
        return instance;
    }

    /**
     * 가입 요청
     *
     * @param callback   signup 요청 결과에 대한 callback
     * @param properties 가입시 받은 사용자 정보
     */
    public void requestSignup(final ApiResponseCallback<Long> callback, final Map<String, String> properties) {
        taskQueue.addTask(new KakaoResultTask<Long>(callback) {
            @Override
            public Long call() throws Exception {
                return api.requestSignup(properties);
            }
        });
    }

    /**
     * 로그아웃 요청
     *
     * @param callback logout 요청 결과에 대한 callback
     */
    public void requestLogout(final LogoutResponseCallback callback) {
        taskQueue.addTask(new KakaoResultTask<Long>(callback) {
            @Override
            public Long call() throws Exception {
                return api.requestLogout();
            }

            @Override
            public void onDidEnd() {
                super.onDidEnd();
                session.close();
            }
        });
    }

    /**
     * Unlink 요청
     *
     * @param callback unlink 요청 결과에 대한 handler
     */
    public void requestUnlink(final UnLinkResponseCallback callback) {
        taskQueue.addTask(new KakaoResultTask<Long>(callback) {
            @Override
            public Long call() throws Exception {
                return api.requestUnlink();
            }

            @Override
            public void onDidEnd() {
                super.onDidEnd();
                session.close();
            }
        });
    }

    /**
     * @param callback           updateProfile 요청 결과에 대한 callback
     * @param nickName           사용자 이름
     * @param thumbnailImagePath 사용자 profile image thumbnail image path
     * @param profileImage       사용자의 profile image path
     * @param properties         저장할 사용자 extra 정보
     */
    public void requestUpdateProfile(final ApiResponseCallback<Long> callback, final String nickName, final String thumbnailImagePath, final String profileImage, final Map<String, String> properties) {
        Map<String, String> userProperties = properties;
        if (userProperties == null) {
            userProperties = new HashMap<>();
        }
        properties.put(StringSet.nickname, nickName);
        properties.put(StringSet.thumbnail_image, thumbnailImagePath);
        properties.put(StringSet.profile_image, profileImage);

        requestUpdateProfile(callback, userProperties);
    }

    /**
     * 사용자정보 저장 요청
     *
     * @param callback   updateProfile 요청 결과에 대한 callback
     * @param properties 저장할 사용자 정보
     */
    public void requestUpdateProfile(final ApiResponseCallback<Long> callback, final Map<String, String> properties) {
        taskQueue.addTask(new KakaoResultTask<Long>(callback) {
            @Override
            public Long call() throws Exception {
                return api.requestUpdateProfile(properties);
            }
        });
    }

    /**
     * Request user info with /v2/user/me.
     *
     * @param callback callback for {@link MeV2Response}
     * @return a Future representing a pending result of /v2/user/me API
     * @see #me(List, MeV2ResponseCallback)
     * @since 1.11.0
     */
    public Future<MeV2Response> me(final MeV2ResponseCallback callback) {
        return taskQueue.addTask(new KakaoResultTask<MeV2Response>(callback) {
            @Override
            public MeV2Response call() throws Exception {
                return api.me(null, true);
            }
        });
    }

    /**
     * Request user info with /v2/user/me.
     * <p>
     * There are a few things you have to note in /v2/user/me.
     * <p>
     * - Now this API does not invoke error callback even if user has not signed up yet.
     * <p>
     * - This version does not pass {@link com.kakao.auth.network.response.InsufficientScopeException}
     * to error callback even if user does not have required scopes. To get the data for missing
     * scopes (such as email or phone number), you should request for user's scope update by invoking
     * {@link Session#updateScopes(Activity, List, AccessTokenCallback)}
     * <p>
     * - Be careful since data only corresponding to given list of property keys will be retrieved.
     * Hierarchy is respected in property keys. If you want to request nickname and email,
     * you should put "properties.nickname" and "kakao_account.email". Refer to developers guide
     * for more info.
     *
     * @param propertyKeys List of user property keys to retrieve
     * @param callback     callback for {@link MeV2Response}
     * @return a Future representing a pending result of /v2/user/me API
     * @see com.kakao.usermgmt.response.model.UserAccount
     * @see Session#updateScopes(Activity, List, AccessTokenCallback)
     * @since 1.11.0
     */
    public Future<MeV2Response> me(final List<String> propertyKeys, final MeV2ResponseCallback callback) {
        return taskQueue.addTask(new KakaoResultTask<MeV2Response>(callback) {
            @Override
            public MeV2Response call() throws Exception {
                return api.me(propertyKeys, true);
            }
        });
    }

    /**
     * 토큰으로 인증날짜와 CI값을 얻는다.
     * (제휴를 통해 권한이 부여된 특정 앱에서만 호출이 가능합니다.)
     *
     * @param callback        요청 결과에 대한 callback
     * @param ageLimit        {@link AgeLimit} enum representing minimum age to be allowed
     * @param propertyKeyList List of {@link AgeAuthProperty} enum
     */
    public void requestAgeAuthInfo(final ResponseCallback<AgeAuthResponse> callback, final AgeLimit ageLimit, final List<AgeAuthProperty> propertyKeyList) {
        List<String> propertyKeys = null;
        if (propertyKeyList != null && propertyKeyList.size() > 0) {
            propertyKeys = new ArrayList<>();
            for (AgeAuthProperty property : propertyKeyList) {
                propertyKeys.add(property.getValue());
            }
        }
        final List<String> finalPropertyKeys = propertyKeys;
        taskQueue.addTask(new KakaoResultTask<AgeAuthResponse>(callback) {
            @Override
            public AgeAuthResponse call() throws Exception {
                return api.requestAgeAuthInfo(ageLimit, finalPropertyKeys);
            }
        });
    }

    /**
     * User 가 3rd의 동의항목에 동의한 내역을 반환한다.
     *
     * @param callback 요청 결과에 대한 callback
     * @return Future containing {@link ServiceTermsResponse}
     * @since 1.17.0
     */
    public Future<ServiceTermsResponse> serviceTerms(final ApiResponseCallback<ServiceTermsResponse> callback) {
        return taskQueue.addTask(new KakaoResultTask<ServiceTermsResponse>(callback) {
            @Override
            public ServiceTermsResponse call() throws Exception {
                return api.serviceTerms();
            }
        });
    }

    /**
     * 앱에 가입한 사용자의 배송지 정보를 얻어간다.
     * 배송지의 정렬 순서는 기본배송지가 무조건 젤 먼저, 그후에는 배송지 수정된 시각을 기준으로 최신순으로 정렬되어 나간다.
     *
     * @param callback 요청 결과에 대한 callback
     * @return Future contaning {@link ShippingAddressResponse}
     * @since 1.17.0
     */
    public Future<ShippingAddressResponse> shippingAddresses(final ApiResponseCallback<ShippingAddressResponse> callback) {
        return taskQueue.addTask(new KakaoResultTask<ShippingAddressResponse>(callback) {
            @Override
            public ShippingAddressResponse call() throws Exception {
                return api.shippingAddresses(null, null, null);
            }
        });
    }

    /**
     * 앱에 가입한 사용자의 배송지 정보 중 특정 배송지 id 만을 지정하여 조회.
     *
     * @param addressId 특정 배송지 정보만 얻고 싶을 때 배송지 ID 지정
     * @param callback  요청 결과에 대한 callback
     * @return Future containing {@link ShippingAddressResponse}
     * @since 1.17.0
     */
    public Future<ShippingAddressResponse> shippingAddresses(
            final Long addressId,
            final ApiResponseCallback<ShippingAddressResponse> callback
    ) {
        return taskQueue.addTask(new KakaoResultTask<ShippingAddressResponse>(callback) {
            @Override
            public ShippingAddressResponse call() throws Exception {
                return api.shippingAddresses(addressId, null, null);
            }
        });
    }

    /**
     * 앱에 가입한 사용자의 배송지 정보를 페이지 사이즈를 주어서 여러 페이지로 나누어 조회.
     * 배송지의 정렬 순서는 기본배송지가 무조건 젤 먼저, 그후에는 배송지 수정된 시각을 기준으로 최신순으로 정렬되어 나간다.
     *
     * @param fromUpdatedAt 기준이 되는 배송지 updated_at 시각. 해당 시각(미포함) 이전에 수정된 배송지부터 조회함. 이전 페이지의 마지막 배송지의 updated_at을 다음 페이지 input 으로 준다.
     * @param pageSize      2이상. 한 페이지에 포함할 배송지 개수.
     * @param callback      요청 결과에 대한 callback
     * @return Future containing {@link ShippingAddressResponse}
     * @since 1.17.0
     */
    public Future<ShippingAddressResponse> shippingAddresses(
            final Integer fromUpdatedAt,
            final Integer pageSize,
            final ApiResponseCallback<ShippingAddressResponse> callback
    ) {
        return taskQueue.addTask(new KakaoResultTask<ShippingAddressResponse>(callback) {
            @Override
            public ShippingAddressResponse call() throws Exception {
                return api.shippingAddresses(null, fromUpdatedAt, pageSize);
            }
        });
    }
}
