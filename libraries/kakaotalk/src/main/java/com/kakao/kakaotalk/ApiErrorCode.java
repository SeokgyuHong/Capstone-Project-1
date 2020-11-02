package com.kakao.kakaotalk;

/**
 * ApiErorCode related to kakaotalk module.
 *
 * @author kevin.kang. Created on 2017. 11. 30..
 */

public class ApiErrorCode extends com.kakao.friends.ApiErrorCode {
    /**
     * [카카오톡 API]
     * 친구관계에서만 호출하는 API에서 친구관계가 아닌데 해당 API를 호출한 경우. code = -502
     */
    public static final int NOT_FRIEND_CODE = -502;
    /**
     * [카카오톡 API]
     * 지원하지 않는 기기로 메시지를 보내는 경우 발생한다.
     */
    public static final int NOT_SUPPORTED_OS = -504;
    /**
     * [카카오톡 API]
     * 받은이가 메시지 수신 거부를 설정한 경우 발생한다. code = -530
     */
    public static final int MSG_DISABLED = -530;
    /**
     * [카카오톡 API]
     * 한명이 특정앱에 대해 특정인에게 보낼 수 있는 한달 쿼터 초과시 발생. code = -531
     */
    public static final int MSG_SENDER_RECEIVER_MONTHLY = -531;
    /**
     * [카카오톡 API]
     * 한명이 특정앱에 대해 보낼 수 있는 하루 쿼터(받는 사람 관계없이) 초과시 발생. code = -532
     */
    public static final int MSG_SENDER_DAILY = -532;
}
