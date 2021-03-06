package com.github.jinahya.jsonrpc.bind.v2;

/*-
 * #%L
 * jsonrpc-bind
 * %%
 * Copyright (C) 2019 Jinahya, Inc.
 * %%
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
 * #L%
 */

import com.github.jinahya.jsonrpc.bind.v2.ResponseObject.ErrorObject;

public class CalculatorResponseError extends ErrorObject<CalculatorResponseErrorData> {

    public static CalculatorResponseError of(final long code, final String message,
                                             final CalculatorResponseErrorData data) {
        final CalculatorResponseError instance = new CalculatorResponseError();
        instance.setCode(code);
        instance.setMessage(message);
        instance.setData(data);
        return instance;
    }
}
