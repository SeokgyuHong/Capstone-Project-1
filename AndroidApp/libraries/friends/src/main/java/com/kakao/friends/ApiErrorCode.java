package com.kakao.friends;

/**
 * @author kevin.kang. Created on 2018. 5. 23..
 */
public class ApiErrorCode extends com.kakao.auth.ApiErrorCode {
    /**
     * [친구 API]
     * 카카오톡 미가입 사용자가 요청한 경우 발생한다. code = -501
     */
    public static final int NOT_EXIST_KAKAOTALK_USER_CODE = -501;
}
