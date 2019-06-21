package com.github.jinahya.jsonrpc.bind.v2.jackson;

import javax.validation.constraints.NotNull;

public abstract class CalculatorClientRequest<ParamsType> extends JacksonClientRequest<ParamsType, Long> {

    @NotNull
    @Override
    public ParamsType getParams() {
        return super.getParams();
    }
}
