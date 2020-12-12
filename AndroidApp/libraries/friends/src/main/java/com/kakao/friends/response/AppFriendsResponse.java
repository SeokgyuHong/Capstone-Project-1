/*
  Copyright 2018 Kakao Corp.

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
package com.kakao.friends.response;

import com.kakao.auth.ApiResponseCallback;
import com.kakao.friends.AppFriendContext;
import com.kakao.friends.StringSet;
import com.kakao.friends.response.model.AppFriendInfo;
import com.kakao.network.response.JSONObjectResponse;
import com.kakao.network.response.ResponseBody;
import com.kakao.network.response.ResponseStringConverter;

import java.util.List;

/**
 * API response for {@link com.kakao.friends.FriendsService#requestAppFriends(AppFriendContext, ApiResponseCallback)}.
 *
 * @author kevin.kang. Created on 2018. 5. 18..
 * @since 1.11.1
 */
public class AppFriendsResponse extends JSONObjectResponse {
    final private List<AppFriendInfo> friends;
    final private int totalCount;
    final private int favoriteCount;
    private String beforeUrl;
    private String afterUrl;
    private String resultId;

    AppFriendsResponse(String data) throws ResponseBody.ResponseBodyException {
        super(data);
        this.friends = AppFriendInfo.CONVERTER.convertList(getBody().optJSONArray(StringSet.elements, null));
        this.totalCount = getBody().getInt(StringSet.total_count);
        this.favoriteCount = getBody().optInt(StringSet.favorite_count, 0);
        this.beforeUrl = getBody().optString(StringSet.before_url, null);
        this.afterUrl = getBody().optString(StringSet.after_url, null);
        this.resultId = getBody().optString(StringSet.result_id, null);
    }

    /**
     * total count of friends
     *
     * @return total count of friends
     */
    public int getTotalCount() {
        return totalCount;
    }

    /**
     * 목록 내 전체 즐겨찾기 친구의 수.
     */
    public int getFavoriteCount() {
        return favoriteCount;
    }

    /**
     * Return friends list
     *
     * @return list of {@link AppFriendInfo}
     */
    public List<AppFriendInfo> getFriends() {
        return friends;
    }

    /**
     * API request url for previous page
     *
     * @return Previous paged url. Null if initial page
     */
    public String getBeforeUrl() {
        return beforeUrl;
    }

    /**
     * API request url for next page
     *
     * @return Next paged url. Null if last page
     */
    public String getAfterUrl() {
        return afterUrl;
    }

    public String getResultId() {
        return resultId;
    }

    public static final ResponseStringConverter<AppFriendsResponse> CONVERTER = new ResponseStringConverter<AppFriendsResponse>() {
        @Override
        public AppFriendsResponse convert(String o) throws ResponseBody.ResponseBodyException {
            return new AppFriendsResponse(o);
        }
    };


}
