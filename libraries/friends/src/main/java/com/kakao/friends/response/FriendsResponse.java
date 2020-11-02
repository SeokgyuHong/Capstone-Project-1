/*
  Copyright 2014-2017 Kakao Corp.

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
import com.kakao.friends.FriendContext;
import com.kakao.network.response.JSONObjectResponse;
import com.kakao.friends.StringSet;
import com.kakao.friends.response.model.FriendInfo;
import com.kakao.network.response.ResponseBody;
import com.kakao.network.response.ResponseStringConverter;

import java.util.List;

/**
 * API response for {@link com.kakao.friends.FriendsService#requestFriends(ApiResponseCallback, FriendContext)}.
 *
 * @author leo.shin
 */
public class FriendsResponse extends JSONObjectResponse {
    final private List<FriendInfo> friendInfoList;
    final private int totalCount;
    final private int favoriteCount;
    final private String id;
    private String beforeUrl;
    private String afterUrl;

    FriendsResponse(String stringData) throws ResponseBody.ResponseBodyException {
        super(stringData);
        this.friendInfoList = FriendInfo.CONVERTER.convertList(getBody().optJSONArray(StringSet.elements, null));
        this.totalCount = getBody().getInt(StringSet.total_count);
        this.favoriteCount = getBody().optInt(StringSet.favorite_count, 0);
        this.id = getBody().optString(StringSet.result_id, null);
        this.beforeUrl = getBody().optString(StringSet.before_url, null);
        this.afterUrl = getBody().optString(StringSet.after_url, null);
    }

    public List<FriendInfo> getFriendInfoList() {
        return friendInfoList;
    }

    public int getTotalCount() {
        return totalCount;
    }

    /**
     * 목록 내 전체 즐겨찾기 친구의 수.
     */
    public int getFavoriteCount() {
        return favoriteCount;
    }
    
    public String getBeforeUrl() {
        return beforeUrl;
    }

    public String getAfterUrl() {
        return afterUrl;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (friendInfoList != null) {
            for (FriendInfo info : friendInfoList) {
                builder.append("\n[").append(info.toString()).append("]");
            }
        }

        builder.append("totalCount : ").append(totalCount)
                .append(", beforeUrl : ").append(beforeUrl)
                .append(", afterUrl : ").append(afterUrl)
                .append(", id : ").append(id);

        return builder.toString();
    }

    public static final ResponseStringConverter<FriendsResponse> CONVERTER = new ResponseStringConverter<FriendsResponse>() {
        @Override
        public FriendsResponse convert(String o) throws ResponseBody.ResponseBodyException {
            return new FriendsResponse(o);
        }
    };
}
