package com.kakao.network.storage;

import android.net.Uri;

import com.kakao.common.PhaseInfo;
import com.kakao.network.ApiRequest;
import com.kakao.network.ServerProtocol;
import com.kakao.network.StringSet;
import com.kakao.network.multipart.FilePart;
import com.kakao.network.multipart.Part;
import com.kakao.common.IConfiguration;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @author kevin.kang. Created on 2017. 3. 20..
 */

public class ImageUploadRequest extends ApiRequest {
    private Boolean secureResource;
    private File imageFile;

    protected ImageUploadRequest(final PhaseInfo phaseInfo, final IConfiguration configuration, final Boolean secureResource, final File imageFile) {
        super(phaseInfo, configuration);
        this.secureResource = secureResource;
        this.imageFile = imageFile;
    }

    @Override
    public String getMethod() {
        return POST;
    }

    @Override
    public Uri.Builder getUriBuilder() {
        Uri.Builder builder = super.getUriBuilder().authority(ServerProtocol.apiAuthority());
        if (secureResource) {
            builder.appendQueryParameter(StringSet.SECURE_RESOURCE, String.valueOf(secureResource));
        }
        return builder;
    }

    @Override
    public Map<String, String> getParams() {
        return super.getParams();
    }

    @Override
    public List<Part> getMultiPartList() {
        List<Part> filePart = super.getMultiPartList();
        if (imageFile != null && imageFile.exists()) {
            filePart.add(new FilePart(StringSet.FILE, imageFile));
        }
        return filePart;
    }
}