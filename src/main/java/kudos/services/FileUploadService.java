package kudos.services;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSInputFile;

import java.io.InputStream;
import java.net.UnknownHostException;

/**
 * Created by vytautassugintas on 19/07/16.
 */
public class FileUploadService {

    public void uploadFile(InputStream fileInputStream, String fileName, String timestamp) throws UnknownHostException {
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        DB mongoDB = mongoClient.getDB("tutorial");
        GridFS fileStore = new GridFS(mongoDB, "images");
        GridFSInputFile inputFile = fileStore.createFile(fileInputStream);
        inputFile.setFilename(fileName);
        inputFile.save();
    }

}
