package fkg.patch.api;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Base64;

import javax.json.Json;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;

import fkg.patch.CommunicateHandler.FKGApiHandler;
import tool.ZLibUtils;

public class GetMaster extends FKGApiHandler {
	public final static String dir = "resources\\getMaster";

	public GetMaster() {
		super(getUri());
	}

	public static String getUri() {
		return "/api/v1/master/getMaster";
	}

	@Override
	public void onSuccess(HttpServletRequest httpRequest, HttpServletResponse httpResponse, ByteArrayOutputStream requestBody, ByteArrayOutputStream responseBody) {
		Json.createReader(new ByteArrayInputStream(ZLibUtils.decompress(responseBody.toByteArray()))).readObject().forEach((key, value) -> {
			byte[] data;

			switch (value.getValueType()) {
				case STRING:
					if ("resultCode".equals(key) || "buildVersion".equals(key) || "serverTime".equals(key) || "version".equals(key)) {
						data = value.toString().getBytes();
					} else {
						try {
							data = Base64.getDecoder().decode(value.toString().getBytes());
						} catch (Exception e) {
							data = e.toString().getBytes();
						}
					}
					break;
				case NULL:
					data = "null".getBytes();
					break;
				//未观察到有以下类型
				case OBJECT:
				case NUMBER:
				case ARRAY:
				case FALSE:
				case TRUE:
				default:
					data = ("新出现的类型:" + value.getValueType().toString() + "\n" + value.toString()).getBytes();
					break;
			}

			try {
				FileUtils.writeByteArrayToFile(new File(dir + "\\" + key + ".csv"), data);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	@Override
	public boolean storeResponseBody() {
		return true;
	}
}
