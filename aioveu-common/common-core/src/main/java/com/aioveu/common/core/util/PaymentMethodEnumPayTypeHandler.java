package com.aioveu.common.core.util;


import com.aioveu.common.core.enums.pay.PaymentMethodEnum;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @ClassName: PaymentMethodEnumPayTypeHandler
 * @Description TODO  自定义 TypeHandler  PAY 专用（String ↔ 枚举，用 value字段）
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/6/15 19:06
 * @Version 1.0
 **/

/*
*
PAY 专用（String ↔ 枚举，用 value字段）
* */
@MappedTypes(PaymentMethodEnum.class)
public class PaymentMethodEnumPayTypeHandler extends BaseTypeHandler<PaymentMethodEnum> {

    @Override
    public void setNonNullParameter(PreparedStatement ps,
                                    int i,
                                    PaymentMethodEnum parameter,
                                    JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.getValue());
    }

    @Override
    public PaymentMethodEnum getNullableResult(ResultSet rs, String columnName)
            throws SQLException {
        return PaymentMethodEnum.fromValue(rs.getString(columnName));
    }

    @Override
    public PaymentMethodEnum getNullableResult(ResultSet rs, int columnIndex)
            throws SQLException {
        return PaymentMethodEnum.fromValue(rs.getString(columnIndex));
    }

    @Override
    public PaymentMethodEnum getNullableResult(CallableStatement cs, int columnIndex)
            throws SQLException {
        return PaymentMethodEnum.fromValue(cs.getString(columnIndex));
    }
}
