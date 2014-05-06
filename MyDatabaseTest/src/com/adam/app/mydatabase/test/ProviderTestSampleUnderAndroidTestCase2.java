/*
 *
 */

package com.adam.app.mydatabase.test;


import com.adam.app.mydatabase.MyDBProvider;
import com.adam.app.mydatabase.MyTable;

import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.RemoteException;
import android.test.AndroidTestCase;


public class ProviderTestSampleUnderAndroidTestCase2 extends AndroidTestCase {
    public void testTable1() throws RemoteException {
    	String[] projection = { MyTable.COLUMN_ID, MyTable.COLUMN_CATEGORY, MyTable.COLUMN_SUMMARY,
   			 MyTable.COLUMN_DESCRIPTION};
    	
        final int ID_INDEX = 0;
        final int CATEGORY_INDEX = 1;
        final int SUMMARY_INDEX = 2;
        final int DESCRIPTION_INDEX = 3;

        String insertCategory = "category_insert";
        String insertSummary = "Summary_insert";
        String insertDescription = "Description_insert";

        String updateCategory = "category_update";
        String updateSummary = "Summary_update";
        String updateDescription = "Description_update";

        // get provider
        ContentResolver cr = mContext.getContentResolver();
        ContentProviderClient provider =
                cr.acquireContentProviderClient(MyDBProvider.CONTENT_URI);
        Cursor cursor = null;

        try {
            // Test: insert
            ContentValues value = new ContentValues();
            value.put(MyTable.COLUMN_CATEGORY, insertCategory);
            value.put(MyTable.COLUMN_SUMMARY, insertSummary);
            value.put(MyTable.COLUMN_DESCRIPTION, insertDescription);

            provider.insert(MyDBProvider.CONTENT_URI, value);
            cursor = provider.query(MyDBProvider.CONTENT_URI, projection,
            		MyTable.COLUMN_CATEGORY + "=\"" + insertCategory + "\"", null, null, null);
            assertNotNull(cursor);
            assertEquals(1, cursor.getCount());
            assertTrue(cursor.moveToFirst());

            assertEquals(insertCategory, cursor.getString(CATEGORY_INDEX));
            assertEquals(insertSummary, cursor.getString(SUMMARY_INDEX));
            int Id = cursor.getInt(ID_INDEX);
            cursor.close();

            // Test: update
            value.clear();
            value.put(MyTable.COLUMN_CATEGORY, updateCategory);
            value.put(MyTable.COLUMN_SUMMARY, updateSummary);
            value.put(MyTable.COLUMN_DESCRIPTION, updateDescription);

            provider.update(MyDBProvider.CONTENT_URI, value,
            		MyTable.COLUMN_CATEGORY + "=\"" + insertCategory + "\"", null);
            cursor = provider.query(MyDBProvider.CONTENT_URI, projection,
            		MyTable.COLUMN_ID + " = " + Id, null, null, null);
            assertNotNull(cursor);
            assertEquals(1, cursor.getCount());
            assertTrue(cursor.moveToFirst());
            assertEquals(updateCategory, cursor.getString(CATEGORY_INDEX));
            assertEquals(updateSummary, cursor.getString(SUMMARY_INDEX));
            cursor.close();

            // Test: delete
            provider.delete(MyDBProvider.CONTENT_URI,
            		MyTable.COLUMN_CATEGORY + "=\"" + updateCategory + "\"", null);
            cursor = provider.query(MyDBProvider.CONTENT_URI, projection,
            		MyTable.COLUMN_ID + " = " + Id, null, null, null);
            assertNotNull(cursor);
            assertEquals(0, cursor.getCount());
        } finally {
            // TODO should clean up more better
            if (cursor != null)
                cursor.close();
        }
    }

}
