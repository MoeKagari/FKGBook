package fkg.book.patch.api;

import com.moekagari.tool.compress.Base64;
import com.moekagari.tool.compress.ZLib;
import fkg.book.patch.server.FKGBOOKApiHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.Optional;

public class GetTurningCardSheet extends FKGBOOKApiHandler {
	public final static String filename = "GetTurningCardSheet.data";
	public final static String URI = "/api/v1/turningCard/getTurningCardSheet";

	public GetTurningCardSheet() {
		super(URI);
	}

	@Override
	public void onSuccess(HttpServletRequest httpRequest, HttpServletResponse httpResponse, Map<String, String> headers, ByteArrayOutputStream requestBody, ByteArrayOutputStream responseBody) {
		Optional.of(responseBody.toByteArray())
		        .flatMap(ZLib::decompressOptional)
		        .flatMap(Base64::decompress)
		        .ifPresent(data -> {
			        try {
				        Files.write(new File(filename).toPath(), data);
			        } catch(IOException e) {
				        e.printStackTrace();
			        }
		        });
	}

	@Override
	public boolean storeResponseBody() {
		return true;
	}
}
