package net.bingyan.hustpass;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import net.bingyan.hustpass.Slide;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table SLIDE.
*/
public class SlideDao extends AbstractDao<Slide, Long> {

    public static final String TABLENAME = "SLIDE";

    /**
     * Properties of entity Slide.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Imageurl = new Property(1, String.class, "imageurl", false, "IMAGEURL");
        public final static Property Siteurl = new Property(2, String.class, "siteurl", false, "SITEURL");
    };


    public SlideDao(DaoConfig config) {
        super(config);
    }
    
    public SlideDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'SLIDE' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'IMAGEURL' TEXT," + // 1: imageurl
                "'SITEURL' TEXT);"); // 2: siteurl
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'SLIDE'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Slide entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String imageurl = entity.getImageurl();
        if (imageurl != null) {
            stmt.bindString(2, imageurl);
        }
 
        String siteurl = entity.getSiteurl();
        if (siteurl != null) {
            stmt.bindString(3, siteurl);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Slide readEntity(Cursor cursor, int offset) {
        Slide entity = new Slide( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // imageurl
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2) // siteurl
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Slide entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setImageurl(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setSiteurl(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Slide entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Slide entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
