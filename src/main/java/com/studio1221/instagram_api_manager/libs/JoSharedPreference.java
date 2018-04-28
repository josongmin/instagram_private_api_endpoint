package com.studio1221.instagram_api_manager.libs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


public class JoSharedPreference {


	private final static String TOKEN = "¿";
	static Gson gson = new Gson();
	public static Context context;
	static JoSharedPreference joSharedPreference;

	SharedPreferences sharedPreferences;
	SharedPreferences.Editor editor;

	public static void setContext(Context context) {
		JoSharedPreference.context = context;
	}

	public JoSharedPreference(Context context) {
		if(context == null){
			context = JoSharedPreference.context;
		}
		this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		this.editor = sharedPreferences.edit();
	}

	public <E> void pushToList(String key, E param){
		List<E> listItems = null;
		if(get(key) == null){
			listItems = new ArrayList<>();
		}else{
			listItems = get(key);
		}
		listItems.add(param);
		push(key, listItems);
	}

	public <E> List<E> getList(String key){
		List<E> list = get(key);
		return list;
	}

	public <E> void push(String key, E param){

		if(param == null){
			editor.remove(key);
			editor.commit();
			return;
		}

		String json = gson.toJson(param);
		String value = "";
		if(param instanceof List){
			if(((List)param).size() <= 0){
				return;
			}

			String className = ((List)param).get(0).getClass().getName();
			value=  "LIST" + TOKEN + className + TOKEN + json;
		}else{
			String className = param.getClass().getName();
			value=  "SINGLE" + TOKEN + className + TOKEN + json;
		}

		editor.putString(key, value);
		editor.commit();
	}

	public <T> T get(String key){

		String valueSet = sharedPreferences.getString(key, "");

		String values[] = parseValues(valueSet);
		String dataType = values[0];
		String className = values[1];
		String json = values[2];

		try{
			if(dataType.equals("SINGLE")){
				Class<?> cls = Class.forName(className);
				return (T)gson.fromJson(json, cls);
			}else{
				return (T)gson.fromJson(json, new CustomParameterizedType(Class.forName(className)));
			}
		}catch(Exception e){
			return null;
		}

	}


	private final static String[] parseValues(String valueSet){

		String[] values = new String[3];
		StringTokenizer st = new StringTokenizer(valueSet, TOKEN);
		int i = 0;
		while(st.hasMoreElements()){
			String token = (String)st.nextToken();
			values[i++] = token;
		}

		return values;
	}


	public static class CustomParameterizedType<T> implements ParameterizedType {
		private Class<?> innerClass;

		public CustomParameterizedType(Class<T> innerClass){
			this.innerClass = innerClass;
		}

		@Override
		public Type[] getActualTypeArguments(){
			return new Type[] { innerClass };
		}

		@Override
		public Type getRawType(){
			return List.class;
		}

		@Override
		public Type getOwnerType(){
			return null;
		}
	}

	///

	public static final JoSharedPreference with(Context context){
		return new JoSharedPreference(context);
	}

	public static final JoSharedPreference with(){
		if(joSharedPreference == null){
			joSharedPreference = new JoSharedPreference(context);
		}
		return joSharedPreference;
	}
}
