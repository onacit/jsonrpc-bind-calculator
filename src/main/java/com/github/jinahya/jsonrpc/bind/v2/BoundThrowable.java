package com.github.jinahya.jsonrpc.bind.v2;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

public class BoundThrowable {

    public static class BoundStackTraceElement {

        public static BoundStackTraceElement of(final StackTraceElement element) {
            if (element == null) {
                throw new NullPointerException("element is null");
            }
            final BoundStackTraceElement instance = new BoundStackTraceElement();
            instance.setFileName(element.getFileName());
            instance.setClassName(element.getClassName());
            instance.setMethodName(element.getMethodName());
            instance.setLineNumber(element.getLineNumber());
            return instance;
        }

        @Setter
        @Getter
        private String fileName;

        @Setter
        @Getter
        private String className;

        @Setter
        @Getter
        private String methodName;

        @Setter
        @Getter
        private int lineNumber;
    }

    public static BoundThrowable of(final Throwable throwable) {
        if (throwable == null) {
            throw new NullPointerException("throwable is null");
        }
        final BoundThrowable instance = new BoundThrowable();
        instance.message = throwable.getMessage();
        instance.boundStackTraceElements
                = Arrays.stream(throwable.getStackTrace()).map(BoundStackTraceElement::of).collect(toList());
        instance.suppressed = Arrays.stream(throwable.getSuppressed()).map(BoundThrowable::of).collect(toList());
        instance.cause = ofNullable(throwable.getCause()).map(BoundThrowable::of).orElse(null);
        return instance;
    }

    @Setter
    @Getter
    private String message;

    @Setter
    @Getter
    private List<BoundStackTraceElement> boundStackTraceElements;

    @Setter
    @Getter
    private List<BoundThrowable> suppressed;

    @Setter
    @Getter
    private BoundThrowable cause;
}
