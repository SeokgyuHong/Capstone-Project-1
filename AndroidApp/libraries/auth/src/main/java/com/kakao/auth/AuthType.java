/*
  Copyright 2014-2019 Kakao Corp.
  <p>
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  <p>
  http://www.apache.org/licenses/LICENSE-2.0
  <p>
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package com.kakao.auth;

/**
 * Kakao SDK 로그인을 하는 방식에 대한 Enum class
 *
 * @author MJ
 */
public enum AuthType {
    /**
     * Kakaotalk으로 login을 하고 싶을 경우 지정. Webviews are used if not installed.
     */
    KAKAO_TALK(0),

    /**
     * Kakaostory으로 login을 하고 싶을 경우 지정. Webviews are used if not installed.
     */
    KAKAO_STORY(1),

    /**
     * 웹뷰를 통해 카카오 계정연결을 제공하고 싶을 경우 지정.
     */
    KAKAO_ACCOUNT(2),

    /**
     * 모든 로그인방식을 사용하고 싶을때 지정.
     */
    KAKAO_LOGIN_ALL(4),

    /**
     * Kakaotalkㅇㅡ로 login을 하고 싶을 경우 지정. Webviews are not used even if talk is not installed.
     */
    KAKAO_TALK_ONLY(5);

    private final int number;

    AuthType(int i) {
        this.number = i;
    }

    public int getNumber() {
        return number;
    }

    public static AuthType valueOf(int number) {
        if (number == KAKAO_TALK.getNumber()) {
            return KAKAO_TALK;
        } else if (number == KAKAO_STORY.getNumber()) {
            return KAKAO_STORY;
        } else if (number == KAKAO_ACCOUNT.getNumber()) {
            return KAKAO_ACCOUNT;
        } else if (number == KAKAO_LOGIN_ALL.getNumber()) {
            return KAKAO_LOGIN_ALL;
        } else {
            return null;
        }
    }
}
