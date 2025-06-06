/*
 * Copyright 2002-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.aioveu.auth.oauth2.extension.smscode;

/**
 * @Description: TODO 短信验证码模式参数名称常量
 * @Author: 雒世松
 * @Date: 2025/6/5 17:49
 * @param
 * @return:
 **/

public final class SmsCodeParameterNames {

    /**
     * 手机号
     */
    public static final String MOBILE = "mobile";

    /**
     * 验证码
     */
    public static final String CODE = "code";


    private SmsCodeParameterNames() {
    }

}
