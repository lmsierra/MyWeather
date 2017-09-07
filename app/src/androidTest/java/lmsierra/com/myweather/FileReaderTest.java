package lmsierra.com.myweather;

import android.test.AndroidTestCase;

import lmsierra.com.myweather.util.FileReaderUtil;

public class FileReaderTest extends AndroidTestCase{

    private static final String TEST_FILE = "CityTestJson.json";

    public void testCanReadFile(){
        assertNotNull(FileReaderUtil.readFileFromAssets(TEST_FILE, getContext()));
    }
}
