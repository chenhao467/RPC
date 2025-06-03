package com.rpc.common;

import lombok.Data;

import java.io.Serializable;

/*
*功能：
 作者：chenhao
*日期： 2025/6/2 下午7:54
*/
@Data
public class URL implements Serializable {
    private String hostname;
    private Integer port;
    public URL(String hostname,Integer port)
    {
        this.hostname=hostname;
        this.port=port;
    }
}
