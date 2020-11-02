package com.kakao.usermgmt;

/**
 * ApiErrorCode related to usermgmt module.
 *
 * @author kevin.kang. Created on 2017. 11. 30..
 */

public class ApiErrorCode extends com.kakao.auth.ApiErrorCode {
    /**
     * [사용자 관리 me, signup, updateProfile API]
     * 앱에 추가하지 않은 사용자 프로퍼티 키의 값을 불러오거나 저장하려고 한 경우 발생한다.
     * 개발자의 앱 관리 페이지에 등록된 user property key의 이름이 잘못되지 않았는지 확인 필요하다. code = -201
     */
    public static final int NOT_REGISTERED_PROPERTY_KEY_CODE = -201;
}
