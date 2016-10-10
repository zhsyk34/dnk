package com.cat.dnk.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonKit {

	public static boolean isEmpty(JsonObject jsonObject) {
		return jsonObject.entrySet().size() == 0;
	}

	public static Object get(JsonObject jsonObject, String key) {
		JsonElement element = jsonObject.get(key);
		return element;
	}

}
