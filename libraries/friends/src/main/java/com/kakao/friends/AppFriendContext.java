/*
  Copyright 2018-2019 Kakao Corp.

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

import com.kakao.auth.common.PageableContext;

/**
 * Context containing request parameters for Friends API. This class extends PageableContext, which
 * has {@link PageableContext#getAfterUrl()} method.
 * <p>
 * {@link com.kakao.friends.api.FriendsApi} will automatically fill in after url for this context so
 * that subsequent API requests can be made with this url field, overriding all other parameters
 * such as offset, limit and order.
 *
 * @author kevin.kang. Created on 2018. 5. 22..
 * @see com.kakao.friends.api.FriendsApi
 * @since 1.11.1
 */
public class AppFriendContext extends PageableContext {
    /**
     * 친구의 리스트를 얻어오는데 필요한 데이터 셋을 생성한다.
     * 친구의 리스트는 limit 별로 paging 되어서 내려올 수 있기 때문에 생성되는 context에 다음 요청 url 정보를 가지고 있게 되며,
     * next 요청 url 정보는 SDK 에서 응답에 성공할 경우 채워준다.
     *
     * @param secureResource 이미지 url 을 secure url 로 받을 것인지 여부(http/https)
     * @param offset         가져올 친구 리스트의 시작 offset. default 0
     * @param limit          한 페이지에 가져올 친구의 수, default 100,  max 2000
     * @param order          친구리스트 정렬방법. asc, desc 중 하나의 오더로 정렬
     */
    public AppFriendContext(boolean secureResource, int offset, int limit, String order) {
        super(offset, limit, order);
        this.secureResource = secureResource;
        this.appFriendOrder = null;
    }

    public AppFriendContext(AppFriendOrder friendOrder, int offset, int limit, String order) {
        super(offset, limit, order);
        this.secureResource = true;
        this.appFriendOrder = friendOrder;
    }

    /**
     * 친구 리스트 정렬 기준.
     */
    public AppFriendOrder getAppFriendOrder() {
        return appFriendOrder;
    }

    public boolean isSecureResource() {
        return secureResource;
    }

    /**
     * 가져올 친구 리스트의 시작 offset. default 0
     *
     * @return 가져올 친구 리스트의 시작 offset. default 0
     */
    @Override
    public int getOffset() {
        return super.getOffset();
    }

    /**
     * 한 페이지에 가져올 친구의 수, default 100,  max 2000
     *
     * @return 한 페이지에 가져올 친구의 수, default 100,  max 2000
     */
    @Override
    public int getLimit() {
        return super.getLimit();
    }

    /**
     * 친구리스트 정렬방법. asc, desc 중 하나의 오더로 정렬
     *
     * @return 친구리스트 정렬방법. asc, desc 중 하나의 오더로 정렬
     */
    @Override
    public String getOrder() {
        return super.getOrder();
    }

    private final boolean secureResource;
    private final AppFriendOrder appFriendOrder;


}
