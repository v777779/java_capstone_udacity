package ru.vpcb.footballassistant.dbase;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import ru.vpcb.footballassistant.R;
import ru.vpcb.footballassistant.utils.FDUtils;

import static ru.vpcb.footballassistant.utils.FDUtils.buildItemIdUri;


/**
 * Exercise for course : Android Developer Nanodegree
 * Created: Vadim Voronov
 * Date: 23-Oct-17
 * Email: vadim.v.voronov@gmail.com
 */

/**
 * Recipe Content Provider Class
 */
public class FDProvider extends ContentProvider {

    private UriMatcher mUriMatcher;  // for static methods
    private FDDbHelper mFDDbHelper;

    /**
     * Returns UriMatcher object which recognizes bulk or alone records Uri
     *
     * @return UriMatcher object
     */
    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        for (FDContract.FDParams p : FDContract.MATCH_PARAMETERS) {
            uriMatcher.addURI(FDContract.CONTENT_AUTHORITY, p.tableName, p.tableMatcher);
// support uri/table/100
            uriMatcher.addURI(FDContract.CONTENT_AUTHORITY, p.tableName + "/#", p.tableIdMatcher);

// support uri/table/100/200
            if (p.columnId2 != null) {
                uriMatcher.addURI(FDContract.CONTENT_AUTHORITY, p.tableName + "/#/#", p.tableIdMatcher2);
            }
// support uri/table/txt/txt
            if (p.columnId4 != null) {
                uriMatcher.addURI(FDContract.CONTENT_AUTHORITY, p.tableName + "/*/*", p.tableIdMatcher3);
            }
// support uri/table/txt
            if (p.columnId6 != null) {   //   news.Id  fx.notificationId
                uriMatcher.addURI(FDContract.CONTENT_AUTHORITY, p.tableName + "/*", p.tableIdMatcher4);  // for text
            }
// support uri/table/100/txt
            if (p.columnId7 != null) {   //   news.Id  fx.notificationId
                uriMatcher.addURI(FDContract.CONTENT_AUTHORITY, p.tableName + "/#/*", p.tableIdMatcher5);
            }
        }
        return uriMatcher;
    }


    @Override
    synchronized public boolean onCreate() {
        Context context = getContext();
        mFDDbHelper = new FDDbHelper(context);
// test!!!  // possible static final ???
        mUriMatcher = buildUriMatcher();
        return true;
    }

    /**
     * Returns  Cursor object with RecipeItems loaded from database
     *
     * @param uri           URI   address of RecipeItems
     * @param projection
     * @param selection     String  selection query template
     * @param selectionArgs String[] selection arguments for selection template
     * @param sortOrder     String sorting template
     * @return Cursor object with RecipeItems
     */
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        final SQLiteDatabase db = mFDDbHelper.getReadableDatabase();


        Selection builder = getSelection(uri, selection, selectionArgs);

        Cursor cursor = db.query(builder.table,
                projection,
                builder.selection,
                builder.selectionArgs,
                null,
                null,
                sortOrder);

// notifications about changes underlying data
        if (cursor != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    /**
     * Inserts RecipeItem into database
     * RecipeItem object converted to JSON format and store as string
     *
     * @param uri           Uri address to store in database
     * @param contentValues ContentValues with RecipeItem data
     * @return Uri of inserted record, Uri is valid for successful operation
     */

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = mFDDbHelper.getWritableDatabase();
        Selection builder = getSelection(uri, null, null);
        try {
            long id = db.insertOrThrow(builder.table, null, contentValues);
            getContext().getContentResolver().notifyChange(uri, null);
            return buildItemIdUri(builder.table, id);
        } catch (SQLException e) {
            int nUpdated = db.update(builder.table, contentValues, builder.selection, builder.selectionArgs);
            if (nUpdated != 0) {
                getContext().getContentResolver().notifyChange(uri, null);
            } else {
                return null;  // throws UnsupportedOperationException
            }
            return uri;             // skip insertion
        }

    }


    /**
     * Deletes RecipeItem from database
     * RecipeItem object converted to JSON format and store as string
     *
     * @param uri           Uri address to store in database
     * @param selection     String  selection query template
     * @param selectionArgs String[] selection arguments for selection template
     * @return int number of deleted record, number > 0 valid for successful operation
     */
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[]
            selectionArgs) {
        final SQLiteDatabase db = mFDDbHelper.getWritableDatabase();
        Selection builder = getSelection(uri, selection, selectionArgs);
        int nDeleted = db.delete(builder.table, builder.selection, builder.selectionArgs);

        if (nDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return nDeleted;
    }

    /**
     * Updates RecipeItem in database
     * RecipeItem object converted to JSON format and store as string
     *
     * @param uri           Uri address to store in database
     * @param contentValues ContentValues with RecipeItem data
     * @param selection     String  selection query template
     * @param selectionArgs String[] selection arguments for selection template
     * @return int number of updated record, number > 0 valid for successful operation
     */
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues,
                      @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mFDDbHelper.getReadableDatabase();
        Selection builder = getSelection(uri, selection, selectionArgs);
        int nUpdated = db.update(builder.table, contentValues, builder.selection, builder.selectionArgs);

        if (nUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return nUpdated;
    }


    @Override
    public ContentProviderResult[] applyBatch
            (ArrayList<ContentProviderOperation> operations)
            throws OperationApplicationException {
        final SQLiteDatabase db = mFDDbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            final int numOperations = operations.size();
            final ContentProviderResult[] results = new ContentProviderResult[numOperations];
            for (int i = 0; i < numOperations; i++) {
                results[i] = operations.get(i).apply(this, results, i);
            }
            db.setTransactionSuccessful();
            return results;
        } finally {
            db.endTransaction();
        }
    }


    private Selection getSelection(Uri uri, String selection, String[] selectionArgs) {
        int match = mUriMatcher.match(uri);                             // code for switch
        if (match == -1) {
            throw new UnsupportedOperationException(getContext().getString(R.string.unknown_uri, uri.toString()));
        }
        for (FDContract.FDParams p : FDContract.MATCH_PARAMETERS) {
            if (match == p.tableMatcher) {
                return new Selection(p.tableName,
                        selection,
                        selectionArgs);
            }
            if (match == p.tableIdMatcher) {
                List<String> paths = uri.getPathSegments();
                String _id = paths.get(1);
                return new Selection(p.tableName,
                        p.columnId + "=?",
                        new String[]{_id});
            }
            if (match == p.tableIdMatcher2) {
                List<String> paths = uri.getPathSegments();
                String _id = paths.get(1);
                String _id2 = paths.get(2);
                Selection sel;
                // team
                if (p.tableName.equals(FDContract.TmEntry.TABLE_NAME)) {
                    sel = new Selection(p.tableName, p.columnId2 + "=? OR " + p.columnId3 + "=?",
                            new String[]{_id, _id2});
                } else if (p.tableName.equals(FDContract.FxEntry.TABLE_NAME)) {
                    sel = new Selection(p.tableName,
                            p.columnId2 + "=?" + " AND " + p.columnId3 + "=?" + " OR " +
                                    p.columnId2 + "=?" + " AND " + p.columnId3 + "=?",
                            new String[]{_id, _id2, _id2, _id});
                } else {

                    sel = new Selection(p.tableName, p.columnId2 + "=?" + " AND " + p.columnId3 + "=?",
                            new String[]{_id, _id2});
                }
                return sel;
            }
            if (match == p.tableIdMatcher3) {
                List<String> paths = uri.getPathSegments();
                String _id = paths.get(1);
                String _id2 = paths.get(2);
                Selection sel;
                // fx
                if (p.tableName.equals(FDContract.FxEntry.TABLE_NAME)) {  // dates between
                    sel = new Selection(p.tableName, p.columnId4 + " BETWEEN ? AND ?",
                            new String[]{_id, _id2});
                } else {
                    sel = new Selection(p.tableName, p.columnId4 + "=?" + " AND " + p.columnId5 + "=?",
                            new String[]{_id, _id2});
                }
                return sel;
            }
            if (match == p.tableIdMatcher4) {  // add here table selection if needed
                List<String> paths = uri.getPathSegments();
                String _id = paths.get(1);
                return new Selection(p.tableName,
                        p.columnId6 + "=?",
                        new String[]{_id});
            }
            if (match == p.tableIdMatcher5) {
                List<String> paths = uri.getPathSegments();
                String _id = paths.get(1);
                return new Selection(p.tableName,
                        p.columnId7 + "=?",
                        new String[]{_id});
            }
        }
        throw new UnsupportedOperationException(getContext().getString(R.string.unknown_uri, uri.toString()));
    }

    @SuppressWarnings("SameParameterValue")
    public static Uri buildLoaderIdUri(Context context, int id, String itemId, String itemId2) {
        for (FDContract.FDParams p : FDContract.MATCH_PARAMETERS) {
            if (id == p.id) {
                return FDUtils.buildItemIdUri(p.tableName, itemId, itemId2);
            }
        }
        throw new UnsupportedOperationException(context.getString(R.string.unknown_loader_id, id));
    }


    @SuppressWarnings("SameParameterValue")
    public static Uri buildLoaderIdUri(Context context, int id, long itemId, long itemId2) {
        for (FDContract.FDParams p : FDContract.MATCH_PARAMETERS) {
            if (id == p.id) {
                return FDUtils.buildItemIdUri(p.tableName, itemId, itemId2);
            }
        }
        throw new UnsupportedOperationException(context.getString(R.string.unknown_loader_id, id));
    }

    public static Uri buildLoaderIdUri(Context context, int id) {
        for (FDContract.FDParams p : FDContract.MATCH_PARAMETERS) {
            if (id == p.id) {
                return FDUtils.buildTableNameUri(p.tableName);
            }
        }
        throw new UnsupportedOperationException(context.getString(R.string.unknown_loader_id, id));
    }


    public static String buildLoaderIdSortOrder(Context context, int id) {
        for (FDContract.FDParams p : FDContract.MATCH_PARAMETERS) {
            if (id == p.id) {
                return p.getSortOrder();
            }
        }
        throw new UnsupportedOperationException(context.getString(R.string.unknown_loader_id, id));
    }

    private class Selection {
        String table;
        String selection;
        String[] selectionArgs;
        Uri uri;
        int id;

        public Selection(String table, String selection, String[] selectionArgs) {
            this.table = table;
            this.selection = selection;
            this.selectionArgs = selectionArgs;
        }


    }

}
