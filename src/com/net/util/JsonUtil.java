package com.net.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;

import com.huilet.util.DateUtil;
import com.huilet.util.PubMethod;
import com.jfinal.plugin.activerecord.Record;



public class JsonUtil {
	
	/**
     * 从一个JSON 对象字符格式中得到一个java对象
     * @param jsonString
     * @param pojoCalss
     * @return
     */
    public static Object getObject4JsonString(String jsonString,Class pojoCalss){
        Object pojo;
        JSONObject jsonObject = JSONObject.fromObject( jsonString );  
        pojo = JSONObject.toBean(jsonObject,pojoCalss);
        return pojo;
    }
    /**
     * 从json HASH表达式中获取一个map，改map支持嵌套功能
     * @param jsonString
     * @return
     */
    public static Map getMap4Json(String jsonString){
        JSONObject jsonObject = JSONObject.fromObject( jsonString );  
        Iterator  keyIter = jsonObject.keys();
        String key;
        Object value;
        Map valueMap = new HashMap();

        while( keyIter.hasNext())
        {
            key = (String)keyIter.next();
            value = jsonObject.get(key);
            valueMap.put(key, value);
        }
        
        return valueMap;
    }
    
    
    /**
     * 从json数组中得到相应java数组
     * @param jsonString
     * @return
     */
    public static Object[] getObjectArray4Json(String jsonString){
        JSONArray jsonArray = JSONArray.fromObject(jsonString);
        return jsonArray.toArray();
        
    }
    
    
    /**
     * 从json对象集合表达式中得到一个java对象列表
     * @param jsonString
     * @param pojoClass
     * @return
     */
    public static List getList4Json(String jsonString, Class pojoClass){
        
        JSONArray jsonArray = JSONArray.fromObject(jsonString);
        JSONObject jsonObject;
        Object pojoValue;
        
        List list = new ArrayList();
        for ( int i = 0 ; i<jsonArray.size(); i++){
            
            jsonObject = jsonArray.getJSONObject(i);
            pojoValue = JSONObject.toBean(jsonObject,pojoClass);
            list.add(pojoValue);
            
        }
        return list;

    }
    
    /**
     * 从json数组中解析出java字符串数组
     * @param jsonString
     * @return
     */
    public static String[] getStringArray4Json(String jsonString){
        
        JSONArray jsonArray = JSONArray.fromObject(jsonString);
        String[] stringArray = new String[jsonArray.size()];
        for( int i = 0 ; i<jsonArray.size() ; i++ ){
            stringArray[i] = jsonArray.getString(i);
            
        }
        
        return stringArray;
    }
    
    /**
     * 从json数组中解析出javaLong型对象数组
     * @param jsonString
     * @return
     */
    public static Long[] getLongArray4Json(String jsonString){
        
        JSONArray jsonArray = JSONArray.fromObject(jsonString);
        Long[] longArray = new Long[jsonArray.size()];
        for( int i = 0 ; i<jsonArray.size() ; i++ ){
            longArray[i] = jsonArray.getLong(i);
            
        }
        return longArray;
    }
    
    /**
     * 从json数组中解析出java Integer型对象数组
     * @param jsonString
     * @return
     */
    public static Integer[] getIntegerArray4Json(String jsonString){
        
        JSONArray jsonArray = JSONArray.fromObject(jsonString);
        Integer[] integerArray = new Integer[jsonArray.size()];
        for( int i = 0 ; i<jsonArray.size() ; i++ ){
            integerArray[i] = jsonArray.getInt(i);
            
        }
        return integerArray;
    }
    
    /**
     * 从json数组中解析出java Date 型对象数组，使用本方法必须保证
     * @param jsonString
     * @return
     */
    public static Date[] getDateArray4Json(String jsonString,String DataFormat){
        
        JSONArray jsonArray = JSONArray.fromObject(jsonString);
        Date[] dateArray = new Date[jsonArray.size()];
        String dateString;
        Date date;
        
        for( int i = 0 ; i<jsonArray.size() ; i++ ){
            dateString = jsonArray.getString(i);
            date = DateUtil.strForDate(dateString, DataFormat);
            dateArray[i] = date;
            
        }
        return dateArray;
    }
    
    /**
     * 从json数组中解析出java Integer型对象数组
     * @param jsonString
     * @return
     */
    public static Double[] getDoubleArray4Json(String jsonString){
        
        JSONArray jsonArray = JSONArray.fromObject(jsonString);
        Double[] doubleArray = new Double[jsonArray.size()];
        for( int i = 0 ; i<jsonArray.size() ; i++ ){
            doubleArray[i] = jsonArray.getDouble(i);
            
        }
        return doubleArray;
    }
    
    
    /**
     * 将java对象转换成json字符串
     * @param javaObj
     * @return
     */
    public static String getJsonString4JavaPOJO(Object javaObj){
        
        JSONObject json;
        json = JSONObject.fromObject(javaObj);
        return json.toString();
        
    }
    
    
    
    
    /**
     * 将java对象转换成json字符串,并设定日期格式
     * @param javaObj
     * @param dataFormat
     * @return
     */
    public static String getJsonString4JavaPOJO(Object javaObj , String dataFormat){
        
        JSONObject json;
        JsonConfig jsonConfig = configJson(dataFormat);
        json = JSONObject.fromObject(javaObj,jsonConfig);
        return json.toString();
        
        
    }
    
    
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO 自动生成方法存根

    }
    
    /**
     * JSON 时间解析器具
     * @param datePattern
     * @return
     */
    public static JsonConfig configJson(String datePattern) {   
            JsonConfig jsonConfig = new JsonConfig();   
            jsonConfig.setExcludes(new String[]{""});   
            jsonConfig.setIgnoreDefaultExcludes(false);   
            jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);   
            jsonConfig.registerJsonValueProcessor(Date.class,   
                new DateJsonValueProcessor(datePattern));   
          
            return jsonConfig;   
        }  
    
    /**
     * 
     * @param excludes
     * @param datePattern
     * @return
     */
    public static JsonConfig configJson(String[] excludes,   
            String datePattern) {   
            JsonConfig jsonConfig = new JsonConfig();   
            jsonConfig.setExcludes(excludes);   
            jsonConfig.setIgnoreDefaultExcludes(false);   
            jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);   
            jsonConfig.registerJsonValueProcessor(Date.class,   
                new DateJsonValueProcessor(datePattern));   
          
            return jsonConfig;
        }
    
   /**
    * 将JSONObject 转换为   Map
    * @param json
    * @return
    * @author yuanhuaihao
    * company huilet
    * 2013-5-6
    */
	public static HashMap<String, Object> getMapByJsonObject(JSONObject json){
		HashMap<String, Object> map = new HashMap<String, Object>();
		Set<?> keys = json.keySet();
		for(Object key : keys){
			Object o = json.get(key);
			if(o instanceof JSONArray)
				map.put((String) key, getListByJsonArray((JSONArray) o));
			else if(o instanceof JSONObject)
				map.put((String) key, getMapByJsonObject((JSONObject) o));
			else
				map.put((String) key, o);
		}
		return map;
	}
	
	
	/**
	 * 将JSONArray 转换为 List
	 * @param json
	 * @return
	 * @author yuanhuaihao
	 * company huilet
	 * 2013-5-6
	 */
	public static List getListByJsonArray(JSONArray json){
		List<Object> list = new ArrayList<Object>();
		for(Object o : json){
			if(o instanceof JSONArray)
				list.add(getListByJsonArray((JSONArray) o));
			else if(o instanceof JSONObject)
				list.add(getMapByJsonObject((JSONObject) o));
			else
				list.add(o);
		}
		return list;
	}
	
	/**
	 * List 转化为JSONArray
	 * @param list
	 * @return
	 * @author yuanhuaihao
	 * company huilet
	 * 2013-5-6
	 */
	public static JSONArray getJsonArrayByList(List list){
		JSONArray obj = new JSONArray();
		//System.out.println(JSONArray.fromObject(list));
		return JSONArray.fromObject(list);
	}
	
	/**
	 * Map 转化为JSONObject
	 * @param map
	 * @return
	 * @author yuanhuaihao
	 * company huilet
	 * 2013-5-6
	 */
	public static JSONObject getJsonObjByMap(Map map){
		return JSONObject.fromObject(map);
	}
	
	public static HashMap getMapByJfinalRecord(Record rc){
		HashMap map = new HashMap();
		String[] names = rc.getColumnNames();
		for (String name :names) {
			String  v = "";
			if(rc.get(name) != null){
				try {
					//针对时间进行处理
					if(rc.get(name) instanceof Date){
						v = DateUtil.dateForString(rc.getDate(name), "yyyy-MM-dd HH:mm:ss");
					}else{
						v = rc.get(name).toString();
					}
				} catch (Exception e) {
					e.printStackTrace();
					v = rc.get(name)+"";
				}
			}
			if(PubMethod.isEmpty(v)|| "<null>".equals(v)){
				v = "";
			}
			map.put(name.toLowerCase(), v);
		}
		
		return map;
	}
	
	public static JSONArray getJsonObjByjfinalList(List<Record> rec) {
		// TODO Auto-generated method stub
		List<HashMap> list =  new ArrayList<HashMap>();
		for(Record r: rec){
			list.add(getMapByJfinalRecord(r));
		}
		return getJsonArrayByList(list);
	}
	
	
	public static List<Map> getListForResult(JSONObject result) {
		List<Map> hosList = new ArrayList<Map>();
		JSONObject retObj = JSONObject.fromObject(result);
		JSONObject json=(JSONObject)retObj.get("message");
		if(json.getString("li")!=null){
			hosList=(List<Map>)json.get("li");
		}
		
		
		return hosList;
	}
}
