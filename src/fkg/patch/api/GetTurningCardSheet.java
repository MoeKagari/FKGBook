package fkg.patch.api;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;

import fkg.patch.CommunicateHandler.FKGApiHandler;
import tool.ZLibUtils;

public class GetTurningCardSheet extends FKGApiHandler {
	public final static String filename = "GetTurningCardSheet.data";

	public GetTurningCardSheet() {
		super(getUri());
	}

	public static String getUri() {
		return "/api/v1/turningCard/getTurningCardSheet";
	}

	@Override
	public void onSuccess(HttpServletRequest httpRequest, HttpServletResponse httpResponse, ByteArrayOutputStream requestBody, ByteArrayOutputStream responseBody) {
		try {
			FileUtils.writeByteArrayToFile(new File(filename), Base64.getDecoder().decode(ZLibUtils.decompress(responseBody.toByteArray())));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean storeResponseBody() {
		return true;
	}
}
