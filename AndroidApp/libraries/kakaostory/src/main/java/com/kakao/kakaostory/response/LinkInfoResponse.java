/**
 * Copyright 2014-2015 Kakao Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kakao.kakaostory.response;

import com.kakao.network.response.ApiResponseStatusError;
import com.kakao.network.response.JSONObjectResponse;
import com.kakao.kakaostory.StringSet;
import com.kakao.network.response.ResponseBody;
import com.kakao.network.response.ResponseData;
import com.kakao.network.response.ResponseStringConverter;

import java.util.Arrays;
import java.util.List;

/**
 * @author leoshin, created at 15. 8. 4..
 */
public class LinkInfoResponse extends JSONObjectResponse {

    private final String url;
    private final String requestedUrl;
    private final String host;
    private final String title;
    private final List<String> imageList;
    private final String description;
    private final String section;
    private final String type;

    @Deprecated
    public LinkInfoResponse(ResponseData responseData) throws ResponseBody.ResponseBodyException, ApiResponseStatusError {
        this(responseData.getStringData());
    }

    public LinkInfoResponse(String data) {
        super(data);
        this.url = getBody().optString(StringSet.url, null);
        this.requestedUrl = getBody().optString(StringSet.requested_url, null);
        this.host = getBody().optString(StringSet.host, null);
        this.title = getBody().optString(StringSet.title, null);
        this.imageList = ResponseStringConverter.IDENTITY_CONVERTER.convertList(getBody().optJSONArray(StringSet.image, null));
        this.description = getBody().optString(StringSet.description, null);
        this.section = getBody().optString(StringSet.section, null);
        this.type = getBody().optString(StringSet.type, null);
    }

    public static final ResponseStringConverter<LinkInfoResponse> CONVERTER = new ResponseStringConverter<LinkInfoResponse>() {
        @Override
        public LinkInfoResponse convert(String data) {
            return new LinkInfoResponse(data);
        }
    };

    /**
     * @return 요청시의 URL 원본. resolution을 하기 전의 URL
     */
    public String getRequestedUrl() {
        return requestedUrl;
    }

    /**
     * @return  Redirect 등을 거친 최종 도착지의 URL
     */
    public String getUrl() {
        return url;
    }

    /**
     * @return 요청한 주소의 Host 명
     */
    public String getHost() {
        return host;
    }

    /**
     * @return 웹페이지 타이틀
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return 해당 웹 페이지의 대표 이미지 주소. 최대 3개
     */
    public List<String> getImageList() {
        return imageList;
    }

    /**
     * @return 해당 웹 페이지의 설명
     */
    public String getDescription() {
        return description;
    }

    public String getSection() {
        return section;
    }

    public String getType() {
        return type;
    }

    /**
     * @return 스크랩 결과 해당 url로 링크 포스팅이 가능한지 여부
     */
    public boolean isValidResult(){
        return url != null && host != null;
    }

    @Override
    public String toString() {
        return "LinkInfoResponse{" + "url='" + url + '\'' +
                ", requestedUrl='" + requestedUrl + '\'' +
                ", host='" + host + '\'' +
                ", title='" + title + '\'' +
                ", imageList=" + Arrays.toString(imageList.toArray()) +
                ", description=" + description +
                ", section=" + section +
                ", type=" + type +
                '}';
    }
}
