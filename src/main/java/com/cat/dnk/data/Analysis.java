package com.cat.dnk.data;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Analysis {

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	private static class Login {
		private String action;
		private int clientType;
		private String devSn;
		private int UDPPort;
	}

	public static void main(String[] args) {
		String json = new Gson().toJson(new Login("loginReq", 1, "abc_xyz", 1234));
		System.out.println(json);
		//ByteBuf msg = Unpooled.copiedBuffer(json + "abc", CharsetUtil.UTF_8);

		JsonParser parser = new JsonParser();
		JsonObject jsonObject = parser.parse(json).getAsJsonObject();
		System.out.println(jsonObject.get("action"));
		System.out.println(jsonObject.get("clientType"));
		Set<Map.Entry<String, JsonElement>> entries = jsonObject.entrySet();

		entries.forEach(entry -> {
			System.out.println(entry.getKey());
			System.out.println(entry.getValue());
		});

		jsonObject.addProperty("new",new Gson().toJsonTree(110));

		new JsonObject(json);
		HashMap map = new Gson().fromJson(json, HashMap.class);
		map.forEach((k, v) -> System.out.println(k + "-" + v));
	}

}
