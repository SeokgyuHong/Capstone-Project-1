/*
  Copyright 2017-2019 Kakao Corp.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package com.kakao.auth.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

/**
 * Wrapper around activity and fragment that provides startActivity() and startActivityForResult() methods
 *
 * @author kevin.kang
 * Created by kevin.kang on 2017. 2. 27..
 */

public class StartActivityWrapper {
    public Activity getActivity() {
        return activity;
    }

    public Fragment getFragment() {
        return fragment;
    }

    private Activity activity;
    private Fragment fragment;

    private static final String ERROR_MESSAGE = "StartActivityWrapper does not contain any activity or fragment.";

    public StartActivityWrapper(final Activity activity) {
        this.activity = activity;
    }

    public StartActivityWrapper(final Fragment fragment) {
        this.fragment = fragment;
    }

    public void startActivity(final Intent intent) {
        if (activity != null) {
            activity.startActivity(intent);
        } else if (fragment != null) {
            fragment.startActivity(intent);
        } else {
            throw new IllegalStateException(ERROR_MESSAGE);
        }
    }

    public void startActivityForResult(final Intent intent, final int requestCode) {
        if (activity != null) {
            activity.startActivityForResult(intent, requestCode);
        } else if (fragment != null) {
            fragment.startActivityForResult(intent, requestCode);
        } else {
            throw new IllegalStateException(ERROR_MESSAGE);
        }
    }

    public Context getContext() {
        if (activity != null) {
            return activity;
        } else if (fragment != null) {
            return fragment.getActivity();
        } else {
            throw new IllegalStateException(ERROR_MESSAGE);
        }
    }
}
