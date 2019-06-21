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

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public interface CalculatorRequestParams {

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.ANNOTATION_TYPE})
    @interface Argument {

    }

    @Argument
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD})
    @interface Argument1 {

    }

    @Argument
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD})
    @interface Argument2 {

    }

    static <T extends CalculatorRequestParams> Field of(@NotNull final Class<? extends T> paramsClass,
                                                        @NotNull final Class<? extends Annotation> annotationClass)
            throws ReflectiveOperationException {
        if (annotationClass.getAnnotation(Argument.class) == null) {
            throw new IllegalArgumentException("annotationClass(" + annotationClass + ") is not annotated with "
                                               + Argument.class);
        }
        for (final Field declaredField : paramsClass.getDeclaredFields()) {
            if (declaredField.getAnnotation(annotationClass) != null) {
                if (!declaredField.isAccessible()) {
                    declaredField.setAccessible(true);
                }
                return declaredField;
            }
        }
        throw new NoSuchMethodException("no declared method in " + paramsClass + " annotated with " + annotationClass);
    }

    static <T extends CalculatorRequestParams> T of(
            @NotNull final Class<? extends T> paramsClass,
            @Size(min = 2, max = 2) @NotNull final List<@NotNull ? extends BigDecimal> positionedParams) {
        try {
            final Constructor<? extends T> constructor = paramsClass.getDeclaredConstructor();
            if (!constructor.isAccessible()) {
                constructor.setAccessible(true);
            }
            final T instance = constructor.newInstance();
            of(paramsClass, Argument1.class).set(instance, positionedParams.get(0));
            of(paramsClass, Argument2.class).set(instance, positionedParams.get(1));
            return instance;
        } catch (final ReflectiveOperationException roe) {
            throw new RuntimeException(roe);
        }
    }

    class AdditionParams implements CalculatorRequestParams {

        @Override
        public String toString() {
            return super.toString() + "{"
                   + "augend=" + augend
                   + ",addend=" + addend
                   + "}";
        }

        @Argument1
        @NotNull
        @Setter
        @Getter
        private BigDecimal augend;

        @Argument2
        @NotNull
        @Setter
        @Getter
        private BigDecimal addend;
    }

    class SubtractionParams implements CalculatorRequestParams {

        @Override
        public String toString() {
            return super.toString() + "{"
                   + "minuend=" + minuend
                   + ",subtrahend=" + subtrahend
                   + "}";
        }

        @Argument1
        @NotNull
        @Setter
        @Getter
        private BigDecimal minuend;

        @Argument2
        @NotNull
        @Setter
        @Getter
        private BigDecimal subtrahend;
    }

    class MultiplicationParam implements CalculatorRequestParams {

        @Override
        public String toString() {
            return super.toString() + "{"
                   + "multiplicand=" + multiplicand
                   + ",multiplier=" + multiplier
                   + "}";
        }

        @Argument1
        @NotNull
        @Setter
        @Getter
        private BigDecimal multiplicand;

        @Argument2
        @NotNull
        @Setter
        @Getter
        private BigDecimal multiplier;
    }

    class DivisionParam implements CalculatorRequestParams {

        public static final RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.HALF_EVEN;

        @Override
        public String toString() {
            return super.toString() + "{"
                   + "dividend=" + dividend
                   + ",divisor=" + divisor
                   + ",roundingMode=" + roundingMode
                   + "}";
        }

        @Argument1
        @NotNull
        @Setter
        @Getter
        private BigDecimal dividend;

        @Argument2
        @NotNull
        @Setter
        @Getter
        private BigDecimal divisor;

        @NotNull
        @Setter
        @Getter
        private RoundingMode roundingMode = DEFAULT_ROUNDING_MODE;
    }
}
