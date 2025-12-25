package com.aioveu.common.security.validator;

/**
 * @ClassName: ServiceTokenValidator
 * @Description TODO 服务令牌验证器接口
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/21 23:06
 * @Version 1.0
 **/

/**
 * 服务令牌验证器接口
 */
public interface ServiceTokenValidator {

    /**
     * 验证服务令牌
     * @param token 服务令牌
     * @return 验证结果
     */
    boolean validate(String token);

    /**
     * 验证服务令牌并返回详细结果
     * @param token 服务令牌
     * @return 验证结果详情
     */
    default ValidationResult validateWithDetails(String token) {
        boolean valid = validate(token);
        return new ValidationResult(valid, valid ? "验证成功" : "验证失败", "unknown");
    }

    /**
     * 验证结果类
     */
    class ValidationResult {
        private final boolean valid;
        private final String message;
        private final String serviceName;

        public ValidationResult(boolean valid, String message, String serviceName) {
            this.valid = valid;
            this.message = message;
            this.serviceName = serviceName;
        }

        public boolean isValid() {
            return valid;
        }

        public String getMessage() {
            return message;
        }

        public String getServiceName() {
            return serviceName;
        }

        @Override
        public String toString() {
            return String.format("ValidationResult{valid=%s, message='%s', serviceName='%s'}",
                    valid, message, serviceName);
        }
    }
}
