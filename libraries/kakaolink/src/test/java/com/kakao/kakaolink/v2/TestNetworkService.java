package com.kakao.kakaolink.v2;

import android.os.Handler;
import android.os.SystemClock;

import com.kakao.network.IRequest;
import com.kakao.network.NetworkService;
import com.kakao.network.callback.ResponseCallback;
import com.kakao.network.response.CustomErrorConverter;
import com.kakao.network.response.ResponseBody;
import com.kakao.network.response.ResponseStringConverter;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Mock {@link NetworkService} that can simulate networking with background tasks.
 *
 * @author kevin.kang. Created on 2017. 11. 20..
 */

public class TestNetworkService implements NetworkService {
    private Handler handler;
    private ExecutorService executorService;

    /**
     *
     * @param handler {@link Handler} for main thread
     * @param executorService {@link ExecutorService} for simulating background task
     */
    TestNetworkService(final Handler handler, final ExecutorService executorService) {
        this.handler = handler;
        this.executorService = executorService;
    }
    @Override
    @SuppressWarnings("unchecked")
    public <T> Future<T> request(IRequest request, ResponseStringConverter<T> converter, final ResponseCallback<T> callback) {
        final T response = (T) new JSONObject();

        executorService.submit(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(1000);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSuccess(response);
                    }
                });
            }
        });

        return CompletableFuture.completedFuture(response);
    }

    @Override
    public <T, E extends Exception> T request(IRequest request, ResponseStringConverter<T> converter, CustomErrorConverter<E> errorConverter) throws IOException, ResponseBody.ResponseBodyException, E {
        return null;
    }

    @Override
    public <T, E extends Exception> Future<T> request(IRequest request, ResponseStringConverter<T> converter, CustomErrorConverter<E> errorConverter, ResponseCallback<T> callback) {
        return null;
    }

    @Override
    public <T> T request(IRequest request, ResponseStringConverter<T> converter) throws IOException, ResponseBody.ResponseBodyException {
        return null;
    }

    @Override
    public <T> Future<List<T>> requestList(IRequest request, ResponseStringConverter<T> converter, ResponseCallback<List<T>> callback) {
        return null;
    }

    @Override
    public <T> List<T> requestList(IRequest request, ResponseStringConverter<T> converter) throws IOException {
        return null;
    }
}