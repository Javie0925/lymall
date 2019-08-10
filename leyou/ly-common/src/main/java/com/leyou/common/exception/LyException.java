package com.leyou.common.exception;

import com.leyou.common.enmus.ExceptionEnmu;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LyException extends RuntimeException {

    private ExceptionEnmu exceptionEnmu;

}
