package com.rpc.common.exception;

public enum ResultCodeEnum {
    SUCCESS(200, "操作成功"),
    BAD_REQUEST(400, "参数错误"),
    IO_ERROR(500, "IO异常"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源未找到"),
    INTERNAL_ERROR(500, "服务器异常"),
    BUSINESS_ERROR(600, "业务异常"),
    CLASS_NOT_FOUND(601, "类未找到"),
    SERVICE_NOT_FOUND(601, "服务未找到");

    private final Integer code;
    private final String message;

    ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
