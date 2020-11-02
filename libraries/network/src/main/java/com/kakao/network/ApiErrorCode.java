package com.kakao.network;

/**
 * ApiErrorCode related to network module.
 *
 * @author kevin.kang. Created on 2017. 11. 30..
 */

public class ApiErrorCode {
    /**
     * 클라이언트 단에서 http 요청 전,후로 에러 발생한 경우. 대게 인터넷 연결이 끊어진 경우 발생한다. code = -778
     */
    public static final int CLIENT_ERROR_CODE = -777;
    /**
     * SDK가 인지 못하고 있는 에러코드
     */
    public static final int UNDEFINED_ERROR_CODE = -888;
    /**
     * 서버 내부에서 에러가 발생한 경우. code = -1
     */
    public static final int INTERNAL_ERROR_CODE = -1;
    /**
     * 올바르지 않은 파라미터가 전송된 경우. code = -2
     */
    public static final int INVALID_PARAM_CODE = -2;
    /**
     * [현재스펙에서는 아직 발생하지 않는다]
     * 카카오계정으로 로그인하지 않고 다른 타입의 계정으로 로그인한 사용자가 카카오서비스(카카오스토리,카카오톡) API를 호출한 경우 또는
     * 해당 API를 개발자 싸이트에서 disable 해놓은 경우. code = -3
     */
    public static final int NOT_SUPPORTED_API_CODE = -3;
    /**
     * 계정 제재 또는 contents 제재로 인해 해당 API 호출이 되지 않는 경우. code = -4
     */
    public static final int BLOCKED_ACTION_CODE = -4;
    /**
     * API 요청시 권한이 없는 경우. code = -5
     */
    public static final int ACCESS_DENIED_CODE = -5;
    /**
     * 허용된 요청 회수가 초과한 경우로 자세한 내용은 쿼터 정책을 참고. code = -10
     */
    public static final int EXCEED_LIMIT_CODE = -10;
    /**
     * 등록되지 않은 앱키 또는 앱키로 구성된 access token으로 요청한 경우 발생한다. code = -301
     */
    public static final int NOT_EXIST_APP_CODE = -301;
    /**
     * 유효하지 않은 앱키 또는 access token으로 요청한 경우 발생한다. code = -401
     */
    public static final int INVALID_TOKEN_CODE = -401;
    /**
     * 등록되지 않은 orgin에서 요청이 들어온 경우. code = -403
     */
    public static final int NOT_REGISTERED_ORIGIN_CODE = -403;
    /**
     * 타임아웃 발생. code = -603
     */
    public static final int EXECUTION_TIMED_OUT = -603;
    /**
     * 현재 존재하지 않는 개발자가 생성한 앱으로부터 요청이 들어온 경우 code = -903
     */
    public static final int DEVELOPER_NOT_EXISTENT_CODE = -903;
    /**
     * 카카오서비스가 점검중인 경우로 올바른 요청을 할 수 없는 경우 발생한다. code = -9798
     */
    public static final int KAKAO_MAINTENANCE_CODE = -9798;
}
