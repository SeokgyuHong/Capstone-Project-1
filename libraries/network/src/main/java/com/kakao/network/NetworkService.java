package com.kakao.network;

import com.kakao.network.callback.ResponseCallback;
import com.kakao.network.response.ApiResponseStatusError;
import com.kakao.network.response.CustomErrorConverter;
import com.kakao.network.response.ResponseBody;
import com.kakao.network.response.ResponseStringConverter;
import com.kakao.network.tasks.ITaskQueue;
import com.kakao.network.tasks.KakaoTaskQueue;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Future;

/**
 * Network interface for all other modules throughout the SDK.
 *
 * @author kevin.kang. Created on 2017. 11. 18..
 */

public interface NetworkService {
    /**
     *
     * Sends a network request synchronously. This should be called only on non-UI thread.
     *
     * @param request {@link IRequest} object that represents API requests
     * @param converter String converter for Response Type T
     * @param <T> Response Type
     * @return Response object of type T
     * @throws IOException when network fails
     * @throws ResponseBody.ResponseBodyException when there is an error in the response body
     * @throws ApiResponseStatusError when an error is returned from the API server
     */
    <T> T request(final IRequest request, final ResponseStringConverter<T> converter) throws IOException, ResponseBody.ResponseBodyException, ApiResponseStatusError;

    /**
     *
     * Sends a network request asynchronously. All exceptions are caught and passed to callback.
     *
     * @param request {@link IRequest} object that represents API requests
     * @param converter Converter object that will convert ResponseBody into a give Generic type.
     * @param callback Callback that will handle either success/failure of API requests
     * @param <T> Response Type
     * @return Future containing the desired API response
     */
    <T> Future<T> request(final IRequest request, final ResponseStringConverter<T> converter, ResponseCallback<T> callback);

    /**
     *
     * Sends a network request with custom error converter. Servers might have different error formats.
     * For example, Kakao API server and OAuth server have different error formats. Each module
     * should know the custom error format and provide {@link CustomErrorConverter} isntance if
     * necessary.
     *
     * @param request {@link IRequest} object that represents API requests
     * @param converter String converter for Response Type T
     * @param errorConverter {@link ResponseBody} Converter for Custom Error Type E
     * @param <T> Response Type
     * @param <E> Custom Error type
     * @return Response of type T
     */
    <T, E extends Exception> T request(final IRequest request, final ResponseStringConverter<T> converter, CustomErrorConverter<E> errorConverter) throws IOException, ResponseBody.ResponseBodyException, E;

    /**
     *
     * Sends a network request asynchronously with custom error converter. See {@link NetworkService#request(IRequest, ResponseStringConverter, CustomErrorConverter)}
     * for further detail.
     *
     * @param request {@link IRequest} object that represents API requests
     * @param converter String converter for Response Type T
     * @param errorConverter {@link ResponseBody} Converter for Custom Error Type E
     * @param callback {@link ResponseCallback} object that will handle either success/failure of API requests
     * @param <T> Response Type
     * @param <E> Custom Error type
     * @return Future containing T
     */
    <T, E extends Exception> Future<T> request(final IRequest request, final ResponseStringConverter<T> converter, CustomErrorConverter<E> errorConverter, ResponseCallback<T> callback);

    /**
     *
     * Sends a network request that expects an array response.
     *
     * @param request {@link IRequest} object that represents API requests
     * @param converter String converter for Response Type T
     * @param <T> Response Type
     * @return Desired API response as {@link List}
     * @throws IOException when network fails
     */
    <T> List<T> requestList(final IRequest request, final ResponseStringConverter<T> converter) throws IOException;

    /**
     * Asynchronous version of {@link NetworkService#requestList(IRequest, ResponseStringConverter)}.
     *
     * @param request {@link IRequest} object that represents API requests
     * @param converter String converter for Response Type T
     * @param callback {@link ResponseCallback} object that will handle either success/failure of API requests
     * @param <T> Response Type
     * @return Future containing the desired API response
     */
    <T> Future<List<T>> requestList(final IRequest request, final ResponseStringConverter<T> converter, ResponseCallback<List<T>> callback);

    /**
     * Factory class for getting instances of {@link NetworkService} implementations.
     */
    class Factory {
        public static NetworkService getInstance() {
            ITaskQueue taskQueue = KakaoTaskQueue.getInstance();
            return new DefaultNetworkService(taskQueue);
        }
    }
}
