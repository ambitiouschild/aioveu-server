package com.aioveu.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SportCommonUtils {

    public static boolean containString(String str, String val) {
        if (StringUtils.isEmpty(str) || StringUtils.isEmpty(val)) {
            return false;
        }
        List<String> strList = Arrays.stream(str.split(",")).collect(Collectors.toList());
        return strList.contains(val);
    }

    public static String addString(String str, String val) {
        return addString(str, val, ",");
    }

    public static String addString(String str, String val, String split) {
        if (StringUtils.isEmpty(val)) {
            return str;
        }
        List<String> strList = new ArrayList<>();
        if(!StringUtils.isEmpty(str)) {
            strList = Arrays.stream(str.split(split)).collect(Collectors.toList());
        }
        if (strList.contains(val)) {
            return str;
        }
        strList.add(val);
        return String.join(split, strList);
    }


    public static String removeString(String str, String val) {
        if (str == null) str = "";
        if (StringUtils.isEmpty(val)) {
            return str;
        }
        List<String> strList = new ArrayList<>();
        if(!StringUtils.isEmpty(str)) {
            strList = Arrays.stream(str.split(",")).collect(Collectors.toList());
        }
        int i = strList.indexOf(val);
        if (i < 0) {
            return str;
        }
        strList.remove(i);
        return String.join(",", strList);
    }
}
