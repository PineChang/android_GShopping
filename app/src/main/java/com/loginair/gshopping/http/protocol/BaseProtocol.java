package com.loginair.gshopping.http.protocol;

import com.loginair.gshopping.http.HttpHelper;
import com.loginair.gshopping.utils.IOUtils;
import com.loginair.gshopping.utils.StringUtils;
import com.loginair.gshopping.utils.UIUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by PineChang on 2017/2/22.
 *
 * 功能: 获取数据的流程
 * 第0 先从缓存取一下数据,如果没有或者过期那么都会返回null,那么就从服务端获取
 * 第一通过HttpHelper拿到服务器返回的字符串型数据,并将其写入缓存,\
 * 如果是过期的情况,在写入的过程中用FileWriter,直接将其覆盖,也就是FileWriter在写之前将原来的数据全部删除后从新从
 * 第一行第一个字符开始写的;
 * 第三将这个数据解析为想要的List型数据;
 */
//
public abstract  class BaseProtocol<T> {
    //由于得到的数据是一个字符串型ArrayList,所以第一步拿到这个字符串,
    //那么得到的数据一般是给一个目前列表中最大的索引号,那么就从这个索引号开始获取连续20行的数据
    public  abstract  String getKey();//key就是要确定获取的是那个模型的集合,这个由子类实现;
    public  abstract  String getParams();//获取查询的条件,注意要拼接成字符串返回;
    public  abstract  T parseData(String result);
    public  T getData(int index){
        //在获取数据的时候先从缓存中获取
        String result = getCache(index);
        if (StringUtils.isEmptyString(result)){
            result= getDataFromServer(index);
        }
        if(result!=null){
            T data = parseData(result);
            return data;
        }

        return null;
    }

    //采用restful架构那么这个url是按照模型写的,所以可以规范化
    public  String getDataFromServer(int index){
        HttpHelper.HttpResult  httpResult = HttpHelper.get(HttpHelper.URL+getKey()+"?index"+getParams());
        if(httpResult!=null){
            //服务器返回的是一个字符串
            String result = httpResult.getString();
          //拿到数据后就将数据写入缓存
            if (!StringUtils.isEmptyString(result)){
                setCache(index,result)
            }
           return result;
        }

        return null;

    }

    //以url为key值,以返回的字符串为value值将其写入内存
    public  void setCache(int index,String json){
       /* FileWriter(File file, boolean append)
        根据给定的 File 对象构造一个 FileWriter 对象。
        看到了后面那个boolean append了吧，这个参数是用于确定是否在原基础上追加的，
        所以你以后用这个构造方法就行了，把后面的boolean append设置为true，就可以实现不覆盖了，*/
        //拿到缓存文件夹
        File cacheDir = UIUtils.getContext().getCacheDir();
        //生成一个缓存文件
        File cacheFile = new File(cacheDir,getKey()+"?index="+index+getParams());
        //将其写入这个文件
        FileWriter writer = null;
        try{
            writer = new FileWriter(cacheFile);
            //在写入之前要获取一下过期时间
            long deadline = System.currentTimeMillis()+30*60*1000;
            //第一行写入过期时间,第二行写入数据
            writer.write(deadline+"\n");
            writer.write(json);
            writer.flush();
        }catch(IOException e){
            e.printStackTrace();
        }finally {
            IOUtils.close();
        }
    }
    public  String getCache(int index){
        File cacheDir = UIUtils.getContext().getCacheDir();
        File cacheFile = new File(cacheDir,getKey()+"?index="+index);
        if(cacheFile.exists()){
            BufferedReader reader  = null;
            try {
                reader = new BufferedReader(new FileReader(cacheFile));
                String deadline = reader.readLine();
                long deadtime = Long.parseLong(deadline);
                if(System.currentTimeMillis()<deadtime){
                    //缓存有效,那么就将其取出
                    StringBuffer sb  = new StringBuffer();
                    String line;
                    while((line = reader.readLine())!=null){
                        sb.append(line);
                    }
                    return sb.toString();
                }

            } catch (Exception e){
                e.printStackTrace();
            } finally {
                IOUtils.close(reader);
            }

        }
        //如果不存在那么直接返回null;
    }
}
