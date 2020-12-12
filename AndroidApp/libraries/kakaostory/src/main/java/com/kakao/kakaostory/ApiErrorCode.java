package com.kakao.kakaostory;

/**
 * ApiErrorCode related to kakaostory module.
 *
 * @author kevin.kang. Created on 2017. 11. 30..
 */

public class ApiErrorCode extends com.kakao.auth.ApiErrorCode {
    /**
     * [카카오스토리 API]
     * 카카오스토리 미가입 사용자가 요청한 경우 발생한다. code = -601
     */
    public static final int NOT_EXIST_KAKAOSTORY_USER_CODE = -601;
    /**
     * [카카오스토리 upload, postPhoto API]
     * 카카오스토리 이미지 업로드시 max 제한 크기(5M. 단, gif 경우 3M)를 넘을 경우 발생한다. code = -602
     */
    public static final int EXCEED_MAX_UPLOAD_SIZE = -602;
    /**
     * [카카오스토리 upload, linkinfo API]
     * 카카오스토리 이미지 업로드/스크랩 정보 요청시 timeout 넘을 경우 발생한다. code = -603
     */
    public static final int EXECUTION_TIMED_OUT = -603;
    /**
     * [카카오스토리 postLink API]
     * 스크랩하려는 URL이 scrap 불가능한 경우 발생한다. (404 not found 등) code = -604
     */
    public static final int INVALID_STORY_SCRAP_URL = -604;
    /**
     * [카카오스토리 getMyStory API]
     * 카카오스토리 이미지 업로드시 5M 제한 크기를 넘을 경우한다. code = -605
     */
    public static final int INVALID_STORY_ACTIVITY_ID = -605;
    /**
     * [카카오스토리 upload, postPhoto API]
     * 업로드 image 갯수가 max값(5개. 단, gif 경우 1개)을 넘을 경우 발생한다.code = -606
     */
    public static final int EXCEED_MAX_UPLOAD_NUMBER = -606;
}
