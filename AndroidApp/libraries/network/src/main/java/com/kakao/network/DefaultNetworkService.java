package com.kakao.network;

import com.kakao.network.callback.ResponseCallback;
import com.kakao.network.response.ApiResponseStatusError;
import com.kakao.network.response.CustomErrorConverter;
import com.kakao.network.response.ResponseBody;
import com.kakao.network.response.ResponseData;
import com.kakao.network.response.ResponseStringConverter;
import com.kakao.network.tasks.ITaskQueue;
import com.kakao.network.tasks.KakaoResultTask;
import com.kakao.util.helper.log.Logger;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.concurrent.Future;

/**
 * @author kevin.kang. Created on 2017. 11. 18..
 */

class DefaultNetworkService implements NetworkService {
    private ITaskQueue taskQueue;

    DefaultNetworkService(final ITaskQueue taskQueue) {
        this.taskQueue = taskQueue;
    }

    @Override
    public <T> Future<T> request(final IRequest request, final ResponseStringConverter<T> converter, ResponseCallback<T> callback) {
        return taskQueue.addTask(new KakaoResultTask<T>(callback) {
            @Override
            public T call() throws Exception {
                return request(request, converter);
            }
        });
    }

    @Override
    public <T, E extends Exception> Future<T> request(final IRequest request, final ResponseStringConverter<T> converter, final CustomErrorConverter<E> errorConverter, ResponseCallback<T> callback) {
        return taskQueue.addTask(new KakaoResultTask<T>(callback) {
            @Override
            public T call() throws Exception {
                return request(request, converter, errorConverter);
            }
        });
    }


    @Override
    public <T> T request(final IRequest request, final ResponseStringConverter<T> converter) throws IOException, ResponseBody.ResponseBodyException, ApiResponseStatusError {
        NetworkTask task = new NetworkTask();
        ResponseData data = task.request(request);
        Logger.d("" + data.getStringData());
        if (data.getHttpStatusCode() != HttpURLConnection.HTTP_OK) {
            ResponseBody errorResponse = new ResponseBody(data.getStringData());
            throw new ApiResponseStatusError(errorResponse.getInt(StringSet.code), errorResponse.optString(StringSet.msg, ""), data.getHttpStatusCode(), errorResponse);
        }
        return converter.convert(data.getStringData());
    }

    @Override
    public <T, E extends Exception> T request(IRequest request, ResponseStringConverter<T> converter, CustomErrorConverter<E> errorConverter) throws IOException, E, ResponseBody.ResponseBodyException {
        NetworkTask task = new NetworkTask();
        ResponseData data = task.request(request);
        Logger.d("" + data.getStringData());
        if (data.getHttpStatusCode() != HttpURLConnection.HTTP_OK) {
            throw errorConverter.convert(data.getHttpStatusCode(), data.getStringData());
        }
        return converter.convert(data.getStringData());
    }

    @Override
    public <T> Future<List<T>> requestList(final IRequest request, final ResponseStringConverter<T> converter, ResponseCallback<List<T>> callback) {
        return taskQueue.addTask(new KakaoResultTask<List<T>>(callback) {
            @Override
            public List<T> call() throws Exception {
                return requestList(request, converter);
            }
        });
}

    @Override
    public <T> List<T> requestList(IRequest request, ResponseStringConverter<T> converter) throws IOException {
        NetworkTask networkTask = new NetworkTask();
        ResponseData data = networkTask.request(request);
        Logger.d("" + data.getStringData());
        if (data.getHttpStatusCode() != HttpURLConnection.HTTP_OK) {
            if (data.getHttpStatusCode() != HttpURLConnection.HTTP_OK) {
                ResponseBody errorResponse = new ResponseBody(data.getStringData());
                throw new ApiResponseStatusError(errorResponse.getInt(StringSet.code), errorResponse.optString(StringSet.msg, ""), data.getHttpStatusCode(), errorResponse);
            }
        }
        return converter.convertList(data.getStringData());
    }
}
