/*
 *
 */
package com.adam.app.mydatabase.test;

import com.adam.app.mydatabase.MyDBProvider;
import com.adam.app.mydatabase.MyTable;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.test.IsolatedContext;
import android.test.RenamingDelegatingContext;
import android.test.mock.MockContentResolver;
import android.test.mock.MockContext;

public class ProviderTestSampleUnderAndroidTestCase extends AndroidTestCase {

    private final static Uri TEST_URI = MyDBProvider.CONTENT_URI;

    private IsolatedContext mProviderContext;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        final String filenamePrefix = "test.";
        final RenamingDelegatingContext targetContextWrapper = new RenamingDelegatingContext(
                new MockContext(), getContext(), filenamePrefix);
        mProviderContext = new IsolatedContext(new MockContentResolver(), targetContextWrapper);
    }

    public void testMyDBProvider() {
   
    	 String[] projection = { MyTable.COLUMN_SUMMARY,
    			 MyTable.COLUMN_DESCRIPTION, MyTable.COLUMN_CATEGORY };
    	
    	final MyDBProvider s = new MyDBProvider();
    	
    	assertFalse(s.isOnCreateCalled());
        s.attachInfo(mProviderContext, null);
        assertTrue(s.isOnCreateCalled());

        Cursor c = s.query(TEST_URI, projection, null, null, null);
        assertEquals(0, c.getCount());

        ContentValues value1 = new ContentValues();
        value1.put(MyTable.COLUMN_CATEGORY, "Category1");
        value1.put(MyTable.COLUMN_DESCRIPTION, "descript1");
        value1.put(MyTable.COLUMN_SUMMARY, "summary1");
              
        s.insert(TEST_URI, value1);
        c = s.query(TEST_URI, projection, null, null, null);
        assertEquals(1, c.getCount());

        ContentValues value2 = new ContentValues();
        value2.put(MyTable.COLUMN_CATEGORY, "Category2");
        value2.put(MyTable.COLUMN_DESCRIPTION, "descript2");
        value2.put(MyTable.COLUMN_SUMMARY, "summary2");
               
        s.insert(TEST_URI, value2);
        c = s.query(TEST_URI, projection, null, null, null);
        assertEquals(2, c.getCount());

        s.delete(TEST_URI, null, null);
        c = s.query(TEST_URI, projection, null, null, null);
        assertEquals(0, c.getCount());

    }
}
