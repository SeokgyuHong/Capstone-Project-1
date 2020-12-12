package com.kakao.kakaostory.request;

import android.net.Uri;

import androidx.test.core.app.ApplicationProvider;

import com.kakao.auth.network.AuthorizedRequest;
import com.kakao.network.ServerProtocol;
import com.kakao.network.multipart.Part;
import com.kakao.test.common.KakaoTestCase;

import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author kevin.kang. Created on 2018. 1. 11..
 */

public class MultiUploadRequestTest extends KakaoTestCase {
    @Override
    public void setup() {
        super.setup();
    }

    @Test
    public void create() {
        List<File> files = new ArrayList<>();
        String root = ApplicationProvider.getApplicationContext().getFilesDir().getAbsolutePath();
        File file1 = new File(root + "/files1");
        File file2 = new File(root + "/files2");
        files.add(file1);
        files.add(file2);
        AuthorizedRequest request = new MultiUploadRequest(files);

        assertEquals("POST", request.getMethod());
        Uri uri = Uri.parse(request.getUrl());
        assertEquals(ServerProtocol.STORY_MULTI_UPLOAD_PATH, uri.getPath().substring(1));
        List<Part> parts = request.getMultiPartList();
        assertEquals(2, parts.size());
    }
}
