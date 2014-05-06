/*
 *
 */

package com.adam.app.mydatabase.test;



import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.CallLog;
import android.test.InstrumentationTestCase;
import android.util.Log;

import com.adam.app.mydatabase.MyDBProvider;
import com.adam.app.mydatabase.MyTable;

public class ProviderTestSampleUnderInstrumentationTestCase extends InstrumentationTestCase {
    private ContentResolver mContentResolver;
    private ContentProviderClient mProvider;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mContentResolver = getInstrumentation().getTargetContext().getContentResolver();
        mProvider = mContentResolver.acquireContentProviderClient(MyDBProvider.AUTHORITY);
    }

    /**
     * Test case for the behavior of the ContactsProvider's people table
     * It does not test any APIs in android.provider.Contacts.java
     */
    public void testTable1() {
        final String[] PROJECTION = new String[] {
                MyTable.COLUMN_ID,
                MyTable.COLUMN_CATEGORY,
                MyTable.COLUMN_SUMMARY,
                MyTable.COLUMN_DESCRIPTION};
        final int ID_INDEX = 0;
        final int CATEGORY_INDEX = 1;
        final int SUMMARY_INDEX = 2;
        final int DESCRIPTION_INDEX = 3;
        

        String insertCategory = "category_insert";
        String insertSummary = "summary_insert";
        String insertDescription = "description_insert";
        String updateCategory = "category_update";
        String updateSummary = "summary_update";
        String updateDescription = "description_update";

        try {
            mProvider.delete(MyDBProvider.CONTENT_URI, MyTable.COLUMN_CATEGORY + " = ?",
                    new String[] {insertCategory});
            // Test: insert
            ContentValues value = new ContentValues();
            value.put(MyTable.COLUMN_CATEGORY, insertCategory);
            value.put(MyTable.COLUMN_SUMMARY, insertSummary);
            value.put(MyTable.COLUMN_DESCRIPTION, insertDescription);

            Uri uri = mProvider.insert(MyDBProvider.CONTENT_URI, value);
            Cursor cursor = mProvider.query(MyDBProvider.CONTENT_URI,
                    PROJECTION, MyTable.COLUMN_CATEGORY + " = ?",
                    new String[] {insertCategory}, null, null);
            assertNotNull(cursor);
            Log.i("test3", "cursor.moveToFirst() = " + cursor.moveToFirst());
            assertTrue(cursor.moveToFirst());
            assertEquals(insertCategory, cursor.getString(CATEGORY_INDEX));
            assertEquals(insertSummary, cursor.getString(SUMMARY_INDEX));
        
            // TODO: Figure out what can be tested for the SYNC_* columns
            int id = cursor.getInt(ID_INDEX);
            cursor.close();

            // Test: update
            value.clear();
            value.put(MyTable.COLUMN_CATEGORY, updateCategory);
            value.put(MyTable.COLUMN_SUMMARY, updateSummary);
            value.put(MyTable.COLUMN_DESCRIPTION, updateDescription);

            mProvider.update(MyDBProvider.CONTENT_URI, value, null, null);
            cursor = mProvider.query(MyDBProvider.CONTENT_URI, PROJECTION,
                    MyTable.COLUMN_ID + " = " + id, null, null, null);
            assertTrue(cursor.moveToNext());
            assertEquals(updateCategory, cursor.getString(CATEGORY_INDEX));
            assertEquals(updateSummary, cursor.getString(SUMMARY_INDEX));
            
            // TODO: Figure out what can be tested for the SYNC_* columns
            cursor.close();

            // Test: delete
            mProvider.delete(MyDBProvider.CONTENT_URI, null, null);
            cursor = mProvider.query(MyDBProvider.CONTENT_URI, PROJECTION,
            		MyTable.COLUMN_ID + " = " + id, null, null, null);
            assertEquals(0, cursor.getCount());
        } catch (RemoteException e) {
            fail("Unexpected RemoteException");
        }
    }

    
}
