package com.rpc.register;

import com.rpc.common.entity.URL;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
*功能：
 作者：chenhao
*日期： 2025/6/2 下午7:25
*/
public class MapRemoteRegister {
    private static Map<String, List<URL>> map = new HashMap<>();

    //给注册中心注册服务
    public static void register(String interfaceName,URL url)
    {
        List<URL> list = map.get(interfaceName);
        if(list==null)
        {
            list = new ArrayList<>();
        }
        list.add(url);
        map.put(interfaceName,list);
        saveFile();
    }

    private static void saveFile() {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("/temp.txt");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(map);
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    public static List<URL> get(String interfaceName) throws IOException, ClassNotFoundException {
        map = getFile();
        return map.get(interfaceName);
    }
    private static Map<String,List<URL>> getFile() throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream("/temp.txt");
        ObjectInputStream  objectInputStream = new ObjectInputStream(fileInputStream);
        return (Map<String, List<URL>>)  objectInputStream.readObject();
    }
}
