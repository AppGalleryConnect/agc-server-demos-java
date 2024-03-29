/*
 * Copyright 2023. Huawei Technologies Co., Ltd. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.huawei.faas;

import com.huawei.agconnect.server.commons.AGCClient;
import com.huawei.agconnect.server.commons.AGCParameter;
import com.huawei.agconnect.server.commons.credential.CredentialParser;
import com.huawei.agconnect.server.commons.credential.CredentialService;
import com.huawei.agconnect.server.commons.exception.AGCException;
import com.huawei.agconnect.server.commons.rest.HttpMethod;
import com.huawei.agconnect.server.commons.rest.RestResponse;
import com.huawei.agconnect.server.function.AGCFunction;
import com.huawei.agconnect.server.function.FunctionCallable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class InvokeFunctionTest {
    private static FunctionCallable callable;

    public static void main(String[] args) throws AGCException {
        init();
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        // projectId clientId clientSecret
        // TODO: fill in the projectId, clientId and clientSecret
        callable.setClientCredential("99536292******", "101063***********",
            "983CD7067239CACE3C3A1A8E63**************************");
        JSONObject jsonObject = JSON.parseObject("{}");
        // TODO: fill in the HttpMethod, url, headers and requestBody
        RestResponse restResponse = callable.callHttpOnClient(HttpMethod.POST, "/invoke", headers, jsonObject);
        System.out.println("getHttpStatus:" + restResponse.getHttpStatus());
        System.out.println("getContent:" + restResponse.getContent());
        System.out.println("getHeader:" + restResponse.getHeader());
        System.out.println("getBody:" + restResponse.getBody());
    }

    public static void init() throws AGCException {
        // TODO: update agcApiClientKeyPath
        String agcApiClientKeyPath = "xxxxx\\agc-apiclient-key.json";
        CredentialService credential = CredentialParser.toCredential(agcApiClientKeyPath);
        AGCParameter agcParameter = AGCParameter.builder().setCredential(credential).build();
        AGCClient.initialize(agcParameter);
        AGCFunction function = AGCFunction.getInstance();
        // TODO: fill in the functionName and version
        callable = function.wrap("called", "$latest");
    }
}