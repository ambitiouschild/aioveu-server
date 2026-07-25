package com.aioveu.common.util;


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
 * @ClassName: PaymentChannelEnumPayTypeHandler
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
@MappedTypes(PaymentChannelEnum.class)
public class PaymentChannelEnumPayTypeHandler extends BaseTypeHandler<PaymentChannelEnum> {

    @Override
    public void setNonNullParameter(PreparedStatement ps,
                                    int i,
                                    PaymentChannelEnum parameter,
                                    JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.getValue());
    }

    @Override
    public PaymentChannelEnum getNullableResult(ResultSet rs, String columnName)
            throws SQLException {
        return PaymentChannelEnum.fromValue(rs.getString(columnName));
    }

    @Override
    public PaymentChannelEnum getNullableResult(ResultSet rs, int columnIndex)
            throws SQLException {
        return PaymentChannelEnum.fromValue(rs.getString(columnIndex));
    }

    @Override
    public PaymentChannelEnum getNullableResult(CallableStatement cs, int columnIndex)
            throws SQLException {
        return PaymentChannelEnum.fromValue(cs.getString(columnIndex));
    }
}
