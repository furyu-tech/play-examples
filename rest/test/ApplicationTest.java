import org.junit.AfterClass;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import play.Play;
import play.test.*;
import play.mvc.*;
import play.mvc.Http.*;
import models.*;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

public class ApplicationTest extends FunctionalTest {

    @Before
    public void setUp() {
        Fixtures.deleteAll();
        Fixtures.load("data.yml");
    }

    @Test
    public void testIndexHtml() {
        Response response = GET("/photos");
        assertIsOk(response);
        assertContentType("text/html", response);
        assertCharset("utf-8", response);
    }

    @Test
    public void testIndexXml() {
        Response response = GET("/photos.xml");
        assertContentType("text/xml; charset=utf-8", response);
    }

    @Test
    public void testIndexJson() {
        Response response = GET("/photos.json");
        assertContentType("application/json; charset=utf-8", response);
    }

    @Test
    public void testUpload() throws Exception {
//        Photo.deleteAll();
        Map<String, String> params = new HashMap<String, String>();
        params.put("photo.title", "title");

        Map<String, File> files = new HashMap<String, File>();
        File file = Play.getFile("public/images/favicon.png");
        files.put("photo.data", file);

        Response response = POST("/photos", params, files);

        // 副作用の検証
        Photo uploadedPhoto = Photo.find("title = ?", "title").first();
        assertNotNull(uploadedPhoto);
        assertEquals(file, uploadedPhoto.data.getFile());

        // レスポンスの検証
        assertContentEquals("", response);
        assertStatus(302, response);
        assertEquals("http://localhost/photos/" + uploadedPhoto.getId() + "/data", uploadedPhoto.dataUrl);
    }

    private void assertEquals(File expected, File actual) throws FileNotFoundException {
        assertEquals(expected.length(), actual.length());
    }

    @Test
    public void testUpdate() {
        Photo photoToDelete = Photo.<Photo>findAll().get(0);

        Response response = PUT("/photos/" + photoToDelete.getId(), "application/x-www-form-urlencoded", "title=modified");

        // 成功時のレスポンスボディ
        assertContentEquals("", response);

        // 成功時のレスポンスステータス
        assertStatus(200, response);
    }

    @Test
    public void testDelete() {
        Response response = DELETE("/photos/" + Photo.all().<Photo>first().getId());

        assertContentEquals("", response);
        assertStatus(200, response);
    }

    @Test
    public void testIndex() {
        Response response = GET("/photos?start=1&results=1");

        assertStatus(200, response);
    }

    @Test
    public void testData() throws Exception {
        String fileContent = "content";

        Photo.deleteAll();

        uploadFile("title", fileContent);

        Photo photoToGetData = Photo.all().first();

        Response response = GET("/photos/" + photoToGetData.getId() + "/data");

        assertContentEquals(fileContent, response);
        assertStatus(200, response);
    }

    // テストのため、一時ファイルをアップロードする
    // 本当はFixtureですませたいが、PlayがBlobを含むFixtureに未対応のため。
    private Response uploadFile(String title, String content) throws IOException {
        Map<String, String> parameters = new HashMap<String, String>();
        Map<String, File> files = new HashMap<String, File>();

        File tempFile = File.createTempFile("temp", "png");
        tempFile.deleteOnExit();

        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
        writer.write(content);
        writer.close();

        parameters.put("photo.title", title);
        files.put("photo.data", tempFile);

        return POST("/photos", parameters, files);
    }
}