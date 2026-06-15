package com.aioveu.oms.aioveu01Order.utils;


import com.aioveu.common.enums.pay.PaymentChannelEnum;
import com.aioveu.common.enums.pay.PaymentMethodEnum;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @ClassName: PaymentChannelEnumCodeTypeHandler
 * @Description TODO  自定义 TypeHandler（OMS 专用）
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/6/15 19:06
 * @Version 1.0
 **/

/*
*
* ✅ 只 OMS 用
✅ 不影响 PAY
✅ 不污染其他 Enum
* */
@MappedTypes(PaymentChannelEnum.class)
public class PaymentMethodEnumCodeTypeHandler extends BaseTypeHandler<PaymentMethodEnum> {

    @Override
    public void setNonNullParameter(PreparedStatement ps,
                                    int i,
                                    PaymentMethodEnum parameter,
                                    JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.getCode());
    }

    @Override
    public PaymentMethodEnum getNullableResult(ResultSet rs, String columnName)
            throws SQLException {
        return PaymentMethodEnum.fromCode(rs.getInt(columnName));
    }

    @Override
    public PaymentMethodEnum getNullableResult(ResultSet rs, int columnIndex)
            throws SQLException {
        return PaymentMethodEnum.fromCode(rs.getInt(columnIndex));
    }

    @Override
    public PaymentMethodEnum getNullableResult(CallableStatement cs, int columnIndex)
            throws SQLException {
        return PaymentMethodEnum.fromCode(cs.getInt(columnIndex));
    }
}
