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

import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.List;

public interface CalculatorService {

    // -----------------------------------------------------------------------------------------------------------------
    String METHOD_ADD_NAMED = "add_named";

    String METHOD_ADD_POSITIONED = "add_positioned";

    // -----------------------------------------------------------------------------------------------------------------
    String METHOD_SUBTRACT_NAMED = "subtract_named";

    String METHOD_SUBTRACT_POSITIONED = "subtract_positioned";

    // -----------------------------------------------------------------------------------------------------------------
    String METHOD_MULTIPLY_NAMED = "multiply_named";

    String METHOD_MULTIPLY_POSITIONED = "multiply_positioned";

    // -----------------------------------------------------------------------------------------------------------------
    String METHOD_DIVIDE_NAMED = "divide_named";

    String METHOD_DIVIDE_POSITIONED = "divide_positioned";

    // -----------------------------------------------------------------------------------------------------------------
    static Method findCalculatorMethod(@NonNull final Class<?> leafClass, @NonNull final String requestMethod) {
        final Logger log = LoggerFactory.getLogger(CalculatorService.class);
        for (final Method declaredMethod : leafClass.getDeclaredMethods()) {
            final CalculatorProcedure calculatorProcedure = declaredMethod.getAnnotation(CalculatorProcedure.class);
            if (calculatorProcedure == null) {
                log.debug("not annotated with {}: {}", CalculatorProcedure.class, declaredMethod);
                continue;
            }
            final String procedureMethod = calculatorProcedure.method();
            if (!procedureMethod.equals(requestMethod)) {
                log.debug("procedureMethod({}) <> requestMethod({})", procedureMethod, requestMethod);
                continue;
            }
            return declaredMethod;
        }
        for (final Class<?> interfaceClass : leafClass.getInterfaces()) {
            final Method method = findCalculatorMethod(interfaceClass, requestMethod);
            if (method != null) {
                return method;
            }
        }
        final Class<?> superClass = leafClass.getSuperclass();
        if (superClass != null) {
            final Method method = findCalculatorMethod(superClass, requestMethod);
            if (method != null) {
                return method;
            }
        }
        return null;
    }

    // ------------------------------------------------------------------------------------------------------------- add
    @CalculatorProcedure(method = METHOD_ADD_NAMED)
    @NotNull BigDecimal add(@Valid @NotNull CalculatorRequestParams.AdditionParams named);

    @CalculatorProcedure(method = METHOD_ADD_POSITIONED)
    default @NotNull BigDecimal add(
            @Size(min = 2, max = 2) @NotNull List</*@NotNull*/ ? extends BigDecimal> positioned) {
        return add(CalculatorRequestParams.of(CalculatorRequestParams.AdditionParams.class, positioned));
    }

    // -------------------------------------------------------------------------------------------------------- subtract
    @CalculatorProcedure(method = METHOD_SUBTRACT_NAMED)
    @NotNull BigDecimal subtract(@Valid @NotNull CalculatorRequestParams.SubtractionParams named);

    @CalculatorProcedure(method = METHOD_SUBTRACT_POSITIONED)
    default @NotNull BigDecimal subtract(
            @Size(min = 2, max = 2) @NotNull List</*@NotNull*/ ? extends BigDecimal> positioned) {
        return subtract(CalculatorRequestParams.of(CalculatorRequestParams.SubtractionParams.class, positioned));
    }

    // -------------------------------------------------------------------------------------------------------- multiply
    @CalculatorProcedure(method = METHOD_MULTIPLY_NAMED)
    @NotNull BigDecimal multiply(@Valid @NotNull CalculatorRequestParams.MultiplicationParam named);

    @CalculatorProcedure(method = METHOD_MULTIPLY_POSITIONED)
    default @NotNull BigDecimal multiply(
            @Size(min = 2, max = 2) @NotNull List</*@NotNull*/ ? extends BigDecimal> positioned) {
        return multiply(CalculatorRequestParams.of(CalculatorRequestParams.MultiplicationParam.class, positioned));
    }

    // ---------------------------------------------------------------------------------------------------------- divide
    @CalculatorProcedure(method = METHOD_DIVIDE_NAMED)
    @NotNull BigDecimal divide(@Valid @NotNull CalculatorRequestParams.DivisionParam named);

    @Deprecated
    @CalculatorProcedure(method = METHOD_DIVIDE_POSITIONED)
    default @NotNull BigDecimal divide(
            @Size(min = 2, max = 2) @NotNull List</*@NotNull*/ ? extends BigDecimal> positioned) {
        return divide(CalculatorRequestParams.of(CalculatorRequestParams.DivisionParam.class, positioned));
    }
}
