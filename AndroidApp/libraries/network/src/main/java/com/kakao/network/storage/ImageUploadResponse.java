package com.kakao.network.storage;

import com.kakao.network.response.JSONObjectResponse;
import com.kakao.network.response.ResponseBody;
import com.kakao.network.response.ResponseStringConverter;

/**
 * @author kevin.kang. Created on 2017. 3. 20..
 */

public class ImageUploadResponse extends JSONObjectResponse {
    private ImageInfo original;

    private static final String INFOS = "infos";
    private static final String URL = "url";
    private static final String LENGTH = "length";
    private static final String CONTENT_TYPE = "content_type";
    private static final String WIDTH = "width";
    private static final String HEIGHT = "height";

    public ImageUploadResponse(String stringData) {
        super(stringData);
        ResponseBody infos = getBody().getBody(INFOS);
        this.original = getImageInfo(infos.getBody(ImageInfo.ImageSize.ORIGINAL.getValue()));
    }

    public ImageInfo getOriginal() {
        return original;
    }

    ImageInfo getImageInfo(final ResponseBody jsonObject) throws ResponseBody.ResponseBodyException {
        String url = jsonObject.getString(URL);
        Integer length = jsonObject.getInt(LENGTH);
        String contentType =jsonObject.getString(CONTENT_TYPE);
        Integer width = jsonObject.getInt(WIDTH);
        Integer height = jsonObject.getInt(HEIGHT);
        return new ImageInfo(url, length, contentType, width, height);
    }

    public static final ResponseStringConverter<ImageUploadResponse> CONVERTER = new ResponseStringConverter<ImageUploadResponse>() {
        @Override
        public ImageUploadResponse convert(String o) throws ResponseBody.ResponseBodyException {
            return new ImageUploadResponse(o);
        }
    };
}
