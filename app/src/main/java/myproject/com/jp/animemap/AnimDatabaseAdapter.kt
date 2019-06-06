package myproject.com.jp.animap

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

class AnimDatabaseAdapter(context: Context) {
    private val mContext = context
    private val mAnimDatabaseHelper = AnimDatabaseHelper(mContext)
    private lateinit var db: SQLiteDatabase


    /**
     * DBの読み書き
     * @return 自身のオブジェクト
     */
    fun openDB(): AnimDatabaseAdapter {
        db = mAnimDatabaseHelper.readableDatabase
        return this
    }

    /**
     * DBの読み込み
     * @return 自身のオブジェクト
     */
    fun readDB(): AnimDatabaseAdapter {
        db = mAnimDatabaseHelper.readableDatabase
        return this
    }

    /**
     * DBを閉じる
     */
    fun closeDB() {
        db.close()
    }

    /**
     * DBのレコードへ登録する。
     * @param title アニメタイトル
     * @param place アニメ聖地の場所
     * @param url アニメURL
     * @param latitude アニメ聖地の緯度
     * @param longitude アニメ聖地の経度
     */
    fun saveDB(title: String, prefecture: String, place: String, url: String, latitude: String, longitude: String) {
        // トランザクション開始
        db.beginTransaction()

        try {
            // ContentValuesでデータを設定する
            val values = ContentValues()
            values.put(AnimDBObject.ANIM_TITLE, title)
            values.put(AnimDBObject.ANIM_PREFECTURE, prefecture)
            values.put(AnimDBObject.ANIM_PLACE, place)
            values.put(AnimDBObject.ANIM_URL, url)
            values.put(AnimDBObject.ANIM_LATITUDE, latitude)
            values.put(AnimDBObject.ANIM_LONGITUDE, longitude)

            // レコードへ登録
            // 第１引数：DBのテーブル名
            // 第２引数：更新する条件式
            // 第３引数：ContentValues
            db.insert(AnimDBObject.DB_TABLE, null, values)

            // トランザクションへコミット
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            // トランザクションの終了
            db.endTransaction()
        }
    }

    /**
     * DB情報を取得する。
     * @param columns 取得するカラム名 nullの場合は全カラムを取得
     * @return DBのデータ
     */
    fun getDB(columns: Array<String>): Cursor? {
        // 第１引数：テーブル名
        // 第２引数：取得するカラム名
        // 第３引数：選択条件(WHERE句)
        // 第４引数：第3引数のWHERE句において?を使用した場合に使用
        // 第５引数：集計条件(GROUP BY句)
        // 第６引数：選択条件(HAVING句)
        // 第７引数：ソート条件(ODERBY句)
        return db.query(AnimDBObject.DB_TABLE, columns, null, null, null, null, null)

    }

    /**
     * アニメタイトルを取得する。
     */
    fun getAnimTitle(): ArrayList<String> {
        val cursor = db.rawQuery("SELECT DISTINCT title from " + AnimDBObject.DB_TABLE, null)
        return getDataList(cursor)
    }

    /**
     * アニメアイコンを取得する。
     */
    fun getAnimIcon(): ArrayList<String> {
        val selectUrl = "SELECT DISTINCT icon from " + AnimDBObject.DB_TABLE

        val cursor = db.rawQuery(selectUrl, null)
        return getDataList(cursor)
    }

    /**
     * 聖地の経度を取得する。
     */
    fun getLongitude(title: String): ArrayList<String> {
        val selectLongitude = "SELECT longitude from " + AnimDBObject.DB_TABLE + " WHERE " +
                AnimDBObject.ANIM_TITLE + " = \'" + title + "\'"

        val cursor = db.rawQuery(selectLongitude, null)

        return getDataList(cursor)
    }

    /**
     * 聖地の緯度を取得する。
     */
    fun getLatitude(title: String): ArrayList<String> {
        val selectLatitude = "SELECT latitude from " + AnimDBObject.DB_TABLE + " WHERE " +
                AnimDBObject.ANIM_TITLE + " = \'" + title + "\'"

        val cursor = db.rawQuery(selectLatitude, null)

        return getDataList(cursor)
    }

    /**
     * アニメ公式サイトのURLを取得する。
     */
    fun getUrl(title: String): String {
        val selectUrl = "SELECT DISTINCT url from " + AnimDBObject.DB_TABLE + " WHERE " +
                AnimDBObject.ANIM_TITLE + " = \'" + title + "\'"

        val cursor = db.rawQuery(selectUrl, null)
        return getStringData(cursor)
    }

    /**
     * 聖地名を取得する。
     */
    fun getPlace(title: String): ArrayList<String> {
        val selectPlace = "SELECT DISTINCT place from " + AnimDBObject.DB_TABLE + " WHERE " +
                AnimDBObject.ANIM_TITLE + " = \'" + title + "\'"

        val cursor = db.rawQuery(selectPlace, null)
        return getDataList(cursor)
    }

    /**
     * DBからデータを取得する。
     * @param cursor カーソル
     */
    private fun getStringData(cursor: Cursor): String {
        var result = ""
        cursor.use {
            if (cursor.moveToNext()) {
                result = cursor.getString(0)
            }
        }
        return result
    }

    /**
     * DBからデータを取得する。
     * @param cursor カーソル
     */
    private fun getStringDataList(cursor: Cursor): MutableList<String> {
        val result = mutableListOf<String>()
        cursor.use {
            while (cursor.moveToNext()) {
                result.add(cursor.getString(0))
            }
        }
        return result
    }

    /**
     * DBからデータを取得する。
     * @param cursor カーソル
     */
    private fun getDataList(cursor: Cursor): ArrayList<String> {
        val result = ArrayList<String>()
        cursor.use {
            while (cursor.moveToNext()) {
                result.add(cursor.getString(0))
            }
        }
        return result
    }
}