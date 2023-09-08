package com.anahuac.rest.api.Utilities
import org.apache.poi.util.IOUtils

class FileDownload {
	public byte[]  getByteFromUrl(String imageUrl, String SSA) throws IOException {
			
		URL url = new URL(imageUrl + SSA);
		InputStream is = url.openStream();
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	   
		int nRead;
		byte[] data = new byte[16384];
		
		while ((nRead = is.read(data, 0, data.length)) != -1) {
		  buffer.write(data, 0, nRead);
		}
		return buffer.toByteArray();
	}
	
	public String b64Url(imageUrl, SSA) {
		
		String imgString = Base64.getEncoder().encodeToString(getByteFromUrl(imageUrl, SSA))
		return imgString
	}
}
