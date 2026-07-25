package com.aioveu.common.util;


import com.aioveu.common.enums.pay.PaymentMethodEnum;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @ClassName: PaymentMethodEnumOrdinalTypeHandlerForOms
 * @Description TODO  自定义 TypeHandler（OMS 专用）
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/7/25 19:06
 * @Version 1.0
 **/

/*
*
* ✅ 只 OMS 用
✅ 不影响 PAY
✅ 不污染其他 Enum
* OMS 专用（int ↔ 枚举，用 code字段）
* */

@MappedTypes(PaymentMethodEnum.class)
public class PaymentMethodEnumOmsTypeHandler
        extends BaseTypeHandler<PaymentMethodEnum> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i,
                                    PaymentMethodEnum parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.getCode());
    }

    @Override
    public PaymentMethodEnum getNullableResult(ResultSet rs, String columnName) throws SQLException {
        int val = rs.getInt(columnName);
        return rs.wasNull() ? null : PaymentMethodEnum.fromCode(val);
    }

    @Override
    public PaymentMethodEnum getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        int val = rs.getInt(columnIndex);
        return rs.wasNull() ? null : PaymentMethodEnum.fromCode(val);
    }

    @Override
    public PaymentMethodEnum getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        int val = cs.getInt(columnIndex);
        return cs.wasNull() ? null : PaymentMethodEnum.fromCode(val);
    }
}
