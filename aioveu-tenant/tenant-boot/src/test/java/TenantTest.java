import com.aioveu.common.security.model.UserAuthInfoWithTenantId;
import com.aioveu.common.tenant.TenantContextHolder;
import com.aioveu.tenant.aioveu02User.service.UserService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * @ClassName: TenantTest
 * @Description TODO 测试数据隔离
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/13 20:44
 * @Version 1.0
 **/
@SpringBootTest
@RequiredArgsConstructor
public class TenantTest {


    private final UserService userService;

    @Test
    public void testTenantIsolation() {

        String admin = "admin";
        // 设置租户1
        TenantContextHolder.setTenantId(1L);
        Long tenantId1 =TenantContextHolder.getTenantId();
        UserAuthInfoWithTenantId userAuthInfo1 = userService.getAuthInfoByUsernameAndTenantId(admin,tenantId1);

        // 设置租户2
        TenantContextHolder.setTenantId(2L);
        Long tenantId2 =TenantContextHolder.getTenantId();
        UserAuthInfoWithTenantId userAuthInfo2 = userService.getAuthInfoByUsernameAndTenantId(admin,tenantId2);

        // 验证两个租户看到的数据不同
        assertNotEquals(userAuthInfo1, userAuthInfo2);
    }
}
