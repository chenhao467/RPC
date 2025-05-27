package com.rpc.register;

import java.util.HashMap;
import java.util.Map;

/*
*功能：
 作者：chenhao
*日期： 2025/5/26 下午9:15
*/
public class LocalRegister {
    private static Map<String,Class> map = new HashMap<>();

    public static void register(String interfaceName,String version,Class implClass)
    {
    	map.put(interfaceName+version,implClass);
    }
    public static Class get(String interfaceName,String version)
    {
    	return map.get(interfaceName+version);
    }
}
