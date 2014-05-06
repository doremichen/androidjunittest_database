/*
* 
*/

package com.adam.app.mydatabase.test;

import com.adam.app.mydatabase.MyDBProvider;
import com.adam.app.mydatabase.MyTable;
import com.adam.app.mydatabase.test.utils.PollingCheck;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.ProviderTestCase2;
import android.util.Log;

public class ProviderTestSample extends
        ProviderTestCase2<MyDBProvider> {
    private final static String AUTHORITY_HEAD = "content://"
            + MyDBProvider.AUTHORITY;

    private Uri mTestUri;
    private MyDBProvider mTestMyProvider;
    private Context mProviderContext;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mTestUri = Uri.parse(AUTHORITY_HEAD + "/mydbs");
        mTestMyProvider = getProvider();
        mProviderContext = mTestMyProvider.getContext();
        
        assertNotNull(mTestUri);
    	assertNotNull(mTestMyProvider);
    	assertNotNull(mProviderContext);
    }

    public ProviderTestSample() {
        super(MyDBProvider.class,
        		MyDBProvider.AUTHORITY);
    }

    public void testPreconditions() {
    	// Test fixture of this test case
    	
    }
    
    public void testConstructor() {
        // Test constructor of the content provider if exists
    }
    
    public void testMyTable() {
    	
    	 Uri myUri = null;
    	 Cursor cursor = null;
    	 String[] projection = { MyTable.COLUMN_SUMMARY,
    			 MyTable.COLUMN_DESCRIPTION, MyTable.COLUMN_CATEGORY };
    	
    	 //Insert test
    	ContentValues value = new ContentValues();
        value.put(MyTable.COLUMN_CATEGORY, "Category1");
        value.put(MyTable.COLUMN_DESCRIPTION, "descript1");
        value.put(MyTable.COLUMN_SUMMARY, "summary1");
        
        myUri = mTestMyProvider.insert(MyDBProvider.CONTENT_URI, value);
        
        assertNotNull(myUri);
        
        //query test
        cursor = mTestMyProvider.query(MyDBProvider.CONTENT_URI, projection, null, null, null);
        
        assertNotNull(cursor);        
        assertEquals(1, cursor.getCount());
        assertTrue(cursor.moveToFirst());
        
        Log.i("myprovidertest", cursor.getString(cursor.getColumnIndexOrThrow(MyTable.COLUMN_CATEGORY)));
        
        assertEquals("Category1", cursor.getString(cursor.getColumnIndexOrThrow(MyTable.COLUMN_CATEGORY)));
        assertEquals("descript1", cursor.getString(cursor.getColumnIndexOrThrow(MyTable.COLUMN_DESCRIPTION)));
        assertEquals("summary1", cursor.getString(cursor.getColumnIndexOrThrow(MyTable.COLUMN_SUMMARY)));
        
        cursor.close();
        
        //update test
        value.clear();
        value.put(MyTable.COLUMN_CATEGORY, "Category2");
        value.put(MyTable.COLUMN_DESCRIPTION, "descript2");
        value.put(MyTable.COLUMN_SUMMARY, "summary2");
        
        int rows = mTestMyProvider.update(MyDBProvider.CONTENT_URI, value, null, null);              
        Log.i("myprovidertest", "rows = " + rows);        
        assertEquals(1, rows);
        
        //delete test
        mTestMyProvider.delete(MyDBProvider.CONTENT_URI, null, null);
        cursor = mTestMyProvider.query(MyDBProvider.CONTENT_URI, projection, null, null, null);
        
        assertNotNull(cursor);        
        assertEquals(0, cursor.getCount());
    	
    }
    
    
    
    private void waitForCursorCount(final Uri uri, final String[] projection,
            final int expectedCount) {
        new PollingCheck() {
            @Override
            protected boolean check() {
                Cursor cursor = null;
                try {
                    cursor = mTestMyProvider.query(uri, projection, null, null, null);
                    return cursor != null && cursor.getCount() == expectedCount;
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            }
        }.run();
    }
    


}
