package com.aioveu.common.web.exception;

import com.aioveu.common.result.IResultCode;
import lombok.Getter;

/**
 * @Description: TODO 自定义业务异常
 * @Author: 雒世松
 * @Date: 2025/6/5 16:26
 * @param
 * @return:
 **/


@Getter
public class BizException extends RuntimeException {

    public IResultCode resultCode;

    public BizException(IResultCode errorCode) {
        super(errorCode.getMsg());
        this.resultCode = errorCode;
    }

    public BizException(String message){
        super(message);
    }

    public BizException(String message, Throwable cause){
        super(message, cause);
    }

    public BizException(Throwable cause){
        super(cause);
    }


}
