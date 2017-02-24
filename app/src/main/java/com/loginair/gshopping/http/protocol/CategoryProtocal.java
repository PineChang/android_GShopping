package com.loginair.gshopping.http.protocol;

import com.loginair.gshopping.domain.CategoryInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by PineChang on 2017/2/24.
 */

public class CategoryProtocal extends BaseProtocol<ArrayList<CategoryInfo>> {
    //去服务器请求那种对象
    @Override
    public String getKey() {
        return "category";
    }
    //请求的时候需要添加什么参数;
    @Override
    public String getParams() {
        return "";
    }
    //请求成功的时候,对数据怎么解析
    @Override
    public ArrayList<CategoryInfo> parseData(String result) {
        try{
            JSONArray ja = new JSONArray(result);
            ArrayList<CategoryInfo> list = new ArrayList<CategoryInfo>();

            for(int i=0;i<ja.length();i++){
                JSONObject jo = ja.getJSONObject(i);
                //返回的数据类似[{"title":"124"},{"infos":[{"name1":"ddd","name2":"ddd",.....},{....}]
                if(jo.has("title")){
                    CategoryInfo titleInfo = new CategoryInfo();
                    titleInfo.title=jo.getString("title");
                    titleInfo.isTitle = true;
                    list.add(titleInfo)
                }
                if(jo.has("infos")){
                    JSONArray ja1 = jo.getJSONArray("infos");
                    for (int j = 0;j<ja1.length();j++){
                        JSONObject jo1 = ja1.getJSONObject(j);
                        CategoryInfo info = new CategoryInfo();
                        info.name1 = jo1.getString("name1");
                        info.url1  = jo1.getString("url1");
                        info.name2 = jo1.getString("name2");
                        info.url2  = jo1.getString("url2");
                        info.name3 = jo1.getString("name3");
                        info.url3  = jo1.getString("url3");
                        info.isTitle = false;
                        list.add(info);
                    }
                }

            }
            return list;
        }catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }
}
