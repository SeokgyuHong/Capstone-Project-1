package com.kakao.kakaotalk.response;

import com.kakao.kakaotalk.StringSet;
import com.kakao.kakaotalk.response.model.PlusFriendInfo;
import com.kakao.network.response.JSONObjectResponse;
import com.kakao.network.response.ResponseBody;
import com.kakao.network.response.ResponseStringConverter;

import java.util.List;

/**
 * /v1/api/talk/plusfriends 의 응답 모델
 *
 * @since 1.17.0
 *
 * @author kevin.kang. Created on 2019-03-19..
 */
public class PlusFriendsResponse extends JSONObjectResponse {
    private final Long userId;
    private final List<PlusFriendInfo> plusFriends;

    public PlusFriendsResponse(String stringData) {
        super(stringData);
        userId = getBody().has(StringSet.user_id) ? getBody().getLong(StringSet.user_id) : null;
        plusFriends = getBody().has(StringSet.plus_friends) ?
                PlusFriendInfo.CONVERTER.convertList(getBody().getJSONArray(StringSet.plus_friends)) :
                null;
    }

    /**
     * 요청자의 사용자 ID
     *
     * @return user id
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 각 카카오톡 채널과의 요청사용자 상태 정보
     *
     * @return this user's relation info with each plus friend
     */
    public List<PlusFriendInfo> getPlusFriends() {
        return plusFriends;
    }

    public static final ResponseStringConverter<PlusFriendsResponse> CONVERTER = new ResponseStringConverter<PlusFriendsResponse>() {
        @Override
        public PlusFriendsResponse convert(String o) throws ResponseBody.ResponseBodyException {
            return new PlusFriendsResponse(o);
        }
    };
}
