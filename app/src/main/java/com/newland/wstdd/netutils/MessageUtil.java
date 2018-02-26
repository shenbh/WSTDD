package com.newland.wstdd.netutils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.newland.wstdd.common.tools.UiHelper;


public class MessageUtil {
	/**
	 * 对象转换成JSON字符串
	 * @param {BaseMessage} src 要转换的对象
	 * @return {String} JSON字符串
	 */
	public static String objectToJSONStr(Object src) {
		String gsonStr = null;
		try {
			
		
		if(src == null || "".equals(src)){
			throw new IllegalArgumentException("请求对象为空");
		}
//		Gson gson = new Gson();
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		gsonStr = gson.toJson(src);
		Log.i("-Req_body-", "----decode---"+gsonStr);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return gsonStr;
	}
	
	/**
	 * JSON字符串转换成对象
	 * @param {String} str 要转换的字符串
	 * @param {Map<Stirng, Class>} aliasMap 对象别名Map
	 * @return {Object} 对象
	 */	
	@SuppressWarnings("unchecked")
	public static Object JSONStrToObject(String json, Class classOfT) {
		try {
			
		
		if(json == null || "".equals(json)){
			throw new IllegalArgumentException("请求参数为空");
		}
//		Gson gson = new Gson();
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		return gson.fromJson(json, classOfT);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			
		}
		return null;
	}	
	

	//这一步话费了太多的时间，导致图片上传很慢
	public static String WBRequestToJson(WBRequest request) {
		String result = null;
		try {
			
		
		Object object = request.getReqBody();
		String dataJson = new String(Des3.encode(new String(objectToJSONStr(object).getBytes("UTF-8")),
				request.getTimestamp()));
//				"");//储存data转化字符串（并转base64）
//		Log.i("-Client-", "----encode---"+dataJson);
		//这里进行空格变成加号的操作
		String sendBuf = dataJson.toString().replace("+", "%2B");
		request.setReqBody(null);//清除data
		request.setParams(sendBuf);
		 result = objectToJSONStr(request);
//		Log.i("-Client-", "----encode finish---"+dataJson);

		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}
	
	
	//json字符串变成对象
	public static WBResponse JsonStrToWBResponse(String jsonStr ,Class cls) {
		WBResponse response =null;
		if(!"".equals(jsonStr)&&jsonStr!=null){
			try {
				response = (WBResponse) JSONStrToObject(jsonStr, WBResponse.class);
				String string=null;
				String dataJson=null;
				if(response.getResult()!=null){
					string=new String(response.getResult().toCharArray());
				}	
			
				if(string!=null){
					dataJson = new String(Des3.decode((string),response.getTimeStamp()));
				}
			
				Log.i("-Res_body-", "----decode---"+dataJson);
				if(dataJson!=null){
					response.setRespBody(JSONStrToObject(dataJson, cls));
				}else{
					response.setRespBody(null);
				}
				response.setResult(null);//清除data
				
			} catch (Exception e) {
				e.printStackTrace();
				
				
				
			}
		}
		return response;			
	}
	
	
	
	

}
