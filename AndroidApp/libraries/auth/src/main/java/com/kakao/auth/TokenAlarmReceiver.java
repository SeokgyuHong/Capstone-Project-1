/**
 * Copyright 2014-2016 Kakao Corp.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kakao.auth;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;

import com.kakao.auth.Session;
import com.kakao.util.helper.log.Logger;


/**
 * 주기적으로 AccessToken 만료 되었는지를 체크하는 Alarm Receiver
 * @author MJ
 */
@SuppressWarnings("DeprecatedIsStillUsed")
@Deprecated
public class TokenAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (isApplicationActive(context)) {
            try {
                Session.getCurrentSession().checkAccessTokenInfo();
            } catch (IllegalStateException e) {
                Logger.e(e.toString());
            }
        }
    }

    @SuppressWarnings("deprecation")
    private boolean isApplicationActive(Context context) {
        try {
            final PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                return powerManager.isScreenOn();
            } else {
                return powerManager.isInteractive();
            }
        } catch (Exception e) {
            // nothing to do;
        }

        return true;
    }
}
