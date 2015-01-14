package org.rbdc.sra;

import android.test.InstrumentationTestCase;
import android.util.Log;

import org.rbdc.sra.helperClasses.UrlBuilder;

/**
 * Created by imac on 1/14/15.
 */
public class UUIDTest extends InstrumentationTestCase {
    public void test() throws Exception {
        Log.i("UUID: ",UrlBuilder.buildUUID());
        assertEquals(20,UrlBuilder.buildUUID().length());
        assertNotNull(UrlBuilder.buildUUID());

    }
}
