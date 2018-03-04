

package com.inspect.util.common;

import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.net.URLCodec;

public abstract class URLUtils {
	
	public static String getEscapedPath(URL url) {
		try {
			return new String(URLCodec.decodeUrl(url.getPath().getBytes()));
		} catch (DecoderException e) {
			//ReflectionUtils.rethrowRuntimeException(e);
		}
		throw new IllegalStateException("Should never get here");
	}
	
	public static String enrichUrl(String url, Map<String, String> parameters) {
		StringBuilder enriched = new StringBuilder(url);
		for (Entry<String, String> entry : parameters.entrySet()) {
			enriched.append(enriched.indexOf("?") != -1 ? "&" : "?");
			enriched.append(entry.getKey())
					.append("=")
					.append(entry.getValue());
		}
		return enriched.toString();
	}
	
}
