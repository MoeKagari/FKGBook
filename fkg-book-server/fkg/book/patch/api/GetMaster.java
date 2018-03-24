package fkg.book.patch.api;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Files;
import java.util.Map;

import javax.json.Json;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fkg.book.main.FKGBOOKApplicationMain;
import fkg.book.patch.server.FKGBOOKApiHandler;
import tool.compress.Base64;
import tool.compress.ZLib;

public class GetMaster extends FKGBOOKApiHandler {
	public final static String dir = "resources" + File.separator + "getMaster";
	public final static String URI = "/api/v1/master/getMaster";

	public GetMaster() {
		super(URI);
	}

	@Override
	public void onSuccess(HttpServletRequest httpRequest, HttpServletResponse httpResponse, Map<String, String> headers, ByteArrayOutputStream requestBody, ByteArrayOutputStream responseBody) {
		try {
			byte[] masterData = responseBody.toByteArray();

			//备份主数据
			if (FKGBOOKApplicationMain.getMainConfig().isMakeBackupForMasterData()) {
				File masterDataFile = new File(dir + File.separator + "backup" + File.separator + System.currentTimeMillis());
				Files.createDirectories(masterDataFile.getParentFile().toPath());
				Files.write(masterDataFile.toPath(), masterData);
			}

			//分离各部分数据
			Json.createReader(new ByteArrayInputStream(ZLib.decompressOptional(masterData).get())).readObject().forEach((key, value) -> {
				try {
					byte[] data;

					switch (value.getValueType()) {
						case STRING:
							if ("resultCode".equals(key) || "buildVersion".equals(key) || "serverTime".equals(key) || "version".equals(key)) {
								data = value.toString().getBytes();
							} else {
								data = Base64.decompress(value.toString().getBytes()).orElseGet(() -> "Base64.decompress 错误".getBytes());
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

					Files.write(new File(dir + File.separator + key + ".csv").toPath(), data);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean storeResponseBody() {
		return true;
	}
}
