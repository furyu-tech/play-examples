import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import models.Photo;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import play.Play;
import play.db.jpa.Blob;
import play.db.jpa.JPA;
import play.mvc.Http.Response;
import play.test.Fixtures;
import play.test.FunctionalTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

public class ApplicationTest extends FunctionalTest {
    static File preparedFile;
    Photo preparedPhoto;

    @BeforeClass
    public static void prepareFile() {
        preparedFile = Play.getFile("public/images/favicon.png");
    }

    @Before
    public void setUp() throws FileNotFoundException {
        Fixtures.deleteAll();
        Fixtures.load("data.yml");
        preparePhoto();
    }

    private void preparePhoto() throws FileNotFoundException {
        preparedPhoto = new Photo();
        preparedPhoto.title = "example title";
        preparedPhoto.data = new Blob();
        preparedPhoto.data.set(new FileInputStream(preparedFile), "image/png");
        preparedPhoto.save();
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
        assertIsOk(response);
        assertContentType("text/xml; charset=utf-8", response);
    }

    @Test
    public void testIndexJson() {
        Response response = GET("/photos.json");
        assertIsOk(response);
        assertContentType("application/json; charset=utf-8", response);
    }

    @Test
    public void testUpload() throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("photo.title", "title");

        Map<String, File> files = new HashMap<String, File>();
        File file = Play.getFile("public/images/favicon.png");
        files.put("photo.data", file);

        Response response = POST("/photos.json", params, files);

        // レスポンスの検証
        JsonObject responseJson = getJsonObject(response);
        Long actualPhotoId = Long.parseLong(responseJson.getAsJsonObject("photo").getAsJsonPrimitive("id").getAsString());

        assertContentType("application/json; charset=utf-8", response);
        assertStatus(200, response);

        // 副作用の検証
        Photo uploadedPhoto = Photo.findById(actualPhotoId);
        assertNotNull(uploadedPhoto);
        assertEquals(file, uploadedPhoto.data.getFile());
        assertEquals("http://localhost/photos/" + uploadedPhoto.getId() + "/data", uploadedPhoto.dataUrl);
    }

    private void assertEquals(File expected, File actual) throws FileNotFoundException {
        assertEquals(expected.length(), actual.length());
    }

    @Test
    public void testUpdate() {
        String newTitle = "modified title";
        Response response = PUT("/photos/" + preparedPhoto.id, "application/x-www-form-urlencoded", "title=" + newTitle);
        assertStatus(200, response);
    }

    @Test
    public void testDelete() {
        Response response = DELETE("/photos/" + preparedPhoto.id);
        assertStatus(200, response);
    }

    @Test
    public void testIndex() {
        Response response = GET("/photos?start=1&results=1");
        assertStatus(200, response);
    }

    @Test
    public void testData() throws Exception {
        Response response = GET("/photos/" + preparedPhoto.getId() + "/data");
        assertEquals(preparedFile.length(), response.out.toByteArray().length);
        assertContentType("image/png", response);
        assertStatus(200, response);
    }

    private static JsonObject getJsonObject(Response response) {
        JsonElement element = new JsonParser().parse(getContent(response));
        return element.getAsJsonObject();
    }
}