package com.anahuac.rest.api.DAO

import com.anahuac.rest.api.DB.DBConnect
import com.anahuac.rest.api.DB.Statements
import com.anahuac.rest.api.Entity.Result
import com.microsoft.azure.storage.CloudStorageAccount
import com.microsoft.azure.storage.StorageException
import com.microsoft.azure.storage.StorageUri
import com.microsoft.azure.storage.blob.BlobProperties
import com.microsoft.azure.storage.blob.BlobType
import com.microsoft.azure.storage.blob.CloudBlobClient
import com.microsoft.azure.storage.blob.CloudBlobContainer
import com.microsoft.azure.storage.blob.CloudBlockBlob
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.sql.Blob
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Statement
import java.util.logging.Level;
import java.util.logging.Logger;

class AzureBlobFileUpload {
	
	private static final Logger LOGGER = Logger.getLogger(AzureBlobFileUpload.class.getName());
	Connection con;
	Statement stm;
	ResultSet rs;
	PreparedStatement pstm;
	
	public Boolean validarConexion() {
		Boolean retorno=false
		if (con == null || con.isClosed()) {
			con = new DBConnect().getConnection();
			retorno=true
		}
	}
/*
    

    public static void main(String[] args) throws URISyntaxException, StorageException, InvalidKeyException, IOException {

        AzureBlobFileUpload ab = new AzureBlobFileUpload();
        ab.uploadFile(new File(args[0]));
    }
    */

    public Result uploadFile(InputStream file, String filename, String filetype, String contenedor, Long length) throws URISyntaxException, StorageException, InvalidKeyException, IOException {
		Result result = new Result();
		String defaultEndpointsProtocol=""
		String accountName=""
		String accountKey=""
		String aContainer= "";
		List<String> data = new ArrayList<String>();
		Boolean closeCon=false;
		
		String errorlog = "";
        try {
			closeCon = validarConexion();
			
			pstm = con.prepareStatement(Statements.CONFIGURACIONES)
			rs = pstm.executeQuery()
			while (rs.next()) {
				switch(rs.getString("clave")) {
					case "AzureAccountName":
						accountName=rs.getString("valor")
					break;
					case "AzureAccountKey":
						accountKey=rs.getString("valor")
					break;
					case "AzureDefaultEndpointsProtocol":
						defaultEndpointsProtocol=rs.getString("valor")
					break;
					case "AzureContainer":
						aContainer=rs.getString("valor")
					break;
				}
			}
			
			String STORAGE_CONNECTION_STRING ="DefaultEndpointsProtocol="+defaultEndpointsProtocol+";" + "AccountName="+accountName+";" + "AccountKey="+accountKey;
            CloudStorageAccount account = CloudStorageAccount.parse(STORAGE_CONNECTION_STRING);
            CloudBlobClient serviceClient = account.createCloudBlobClient();
            // Container name must be lower case.
            CloudBlobContainer container = serviceClient.getContainerReference(contenedor);
            container.createIfNotExists();
			String newUrl = (aContainer.split("/").length > 1) ? aContainer.replace("privado/", "") + "/"  + filename : filename;
            CloudBlockBlob blob = container.getBlockBlobReference(newUrl);
//			CloudBlockBlob blob = container.getBlockBlobReference(filename);
			blob.upload(file,length)
			BlobProperties bp = blob.getProperties();
			bp.setContentType(filetype)
			blob.uploadProperties();
            System.out.println("File uploaded successfully");
			result.setSuccess(true);
			String urlfinal = "https://"+accountName+".blob.core.windows.net/"+aContainer+"/"+filename;
//			String urlfinal = "https://"+accountName+".blob.core.windows.net/"+contenedor+"/"+filename;
			data.add(urlfinal.replace(" ", "%20"))
			result.setData(data)
        } catch (StorageException | IOException | URISyntaxException | InvalidKeyException exception) {
			result.setSuccess(false)
			result.setError(exception.getMessage())
        } finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return result;
    }
	
	
	public Result downloadFile() throws URISyntaxException, StorageException, InvalidKeyException, IOException {
		Result result = new Result();
		String defaultEndpointsProtocol=""
		String accountName=""
		String accountKey=""
		List<String> data = new ArrayList<String>();
		Boolean closeCon=false;
		try {
			
			closeCon = validarConexion();
			
			pstm = con.prepareStatement(Statements.CONFIGURACIONES)
			rs = pstm.executeQuery()
			while (rs.next()) {
				switch(rs.getString("clave")) {
					case "AzureAccountName":
						accountName=rs.getString("valor")
					break;
					case "AzureAccountKey":
						accountKey=rs.getString("valor")
					break;
					case "AzureDefaultEndpointsProtocol":
						defaultEndpointsProtocol=rs.getString("valor")
					break;
				}
			}
			
			String STORAGE_CONNECTION_STRING = "DefaultEndpointsProtocol=" + defaultEndpointsProtocol+";" + "AccountName=" + accountName+";" + "AccountKey=" + accountKey;
			CloudStorageAccount account = CloudStorageAccount.parse(STORAGE_CONNECTION_STRING);
			CloudBlobClient serviceClient = account.createCloudBlobClient();
//			CloudBlobClient storageClient = new BlobServiceClientBuilder().endpoint(endpoint).credential(credential).buildClient();
			StorageUri baseUri = serviceClient.getStorageUri();
			CloudBlobClient storageClient = new CloudBlobClient(baseUri);
			
			String datad = storageClient.listContainers().getAt(0).listBlobs().toString();
			data.add(storageClient.listContainers().toString());
//			storageClient.listContainers().getAt(0).listBlobs();
			
//			CloudBlobClient serviceClient = account.createCloudBlobClient();
//			// Container name must be lower case.
//			CloudBlobContainer container = serviceClient.getContainerReference(contenedor);
//			container.createIfNotExists();
//			CloudBlockBlob blob = container.getBlockBlobReference(filename);
//			blob.upload(file,length)
//			BlobProperties bp = blob.getProperties();
//			bp.setContentType(filetype)
//			blob.uploadProperties();
//			System.out.println("File uploaded successfully");
//			result.setSuccess(true);
//			String urlfinal = "https://"+accountName+".blob.core.windows.net/"+contenedor+"/"+filename;
//			data.add(urlfinal.replace(" ", "%20"))
			result.setData(data)
		} catch (StorageException | IOException | URISyntaxException | InvalidKeyException exception) {
			result.setSuccess(false)
			result.setError(exception.getMessage())
		}finally {
			if(closeCon) {
				new DBConnect().closeObj(con, stm, rs, pstm)
			}
		}
		return result;
	}
}