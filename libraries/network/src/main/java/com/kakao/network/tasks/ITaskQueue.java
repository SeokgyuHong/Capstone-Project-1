package com.kakao.network.tasks;

import java.util.concurrent.Future;

/**
 * @author kevin.kang. Created on 2017. 11. 18..
 */

public interface ITaskQueue {
    <T> Future<T> addTask(KakaoResultTask<T> task);
}
