package com.github.jinahya.jsonrpc.bind.v2.jackson;

import com.github.jinahya.jsonrpc.bind.v2.CalculatorService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Validated
@Service
public interface ExtendedCalculatorService extends CalculatorService {

}
