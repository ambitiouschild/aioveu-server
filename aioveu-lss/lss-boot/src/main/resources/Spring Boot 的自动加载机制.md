## Spring Boot 的自动加载机制

当您设置 `spring.profiles.active=dev`时，Spring Boot 会自动：

1. 加载 `application.yml`
2. 加载 `application-dev.yml`
3. 然后执行 `spring.config.import`

**您不需要手动导入 `application-dev.yml`**，Spring Boot 会自动处理。