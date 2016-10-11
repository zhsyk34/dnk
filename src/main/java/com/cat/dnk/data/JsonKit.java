package com.cat.dnk.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonKit {

	public static boolean isEmpty(JsonObject jsonObject) {
		return jsonObject.entrySet().size() == 0;
	}

	public static JsonElement get(JsonObject jsonObject, String key) {
		return jsonObject.get(key);
	}

	public static int getInt(JsonObject jsonObject, String key) {
		return get(jsonObject, key).getAsInt();
	}

}
