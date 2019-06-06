package myproject.com.jp.animap

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AnimDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, AnimDBObject.DB_NAME, null, AnimDBObject.DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        createOriginDatabase(db)
    }

    private fun createOriginDatabase(db: SQLiteDatabase?) {
        val sqlCreateEntries =
            "CREATE TABLE " + AnimDBObject.DB_TABLE + " (" +
                    AnimDBObject.ANIM_ID + " INTEGER PRIMARY KEY," +
                    AnimDBObject.ANIM_TITLE + " TEXT NOT NULL," +
                    AnimDBObject.ANIM_PREFECTURE + " TEXT NOT NULL," +
                    AnimDBObject.ANIM_PLACE + " TEXT NOT NULL," +
                    AnimDBObject.ANIM_URL + " TEXT NOT NULL," +
                    AnimDBObject.ANIM_LATITUDE + " TEXT NOT NULL," +
                    AnimDBObject.ANIM_LONGITUDE + " TEXT NOT NULL," +
                    AnimDBObject.ANIM_ICON + " TEXT NOT NULL)"

        db?.execSQL(sqlCreateEntries)

        saveAnimInfoForDatabase(db)
    }

    /**
     * アニメ情報をデータベースに保存する。
     * @param db データベース
     */
    private fun saveAnimInfoForDatabase(db: SQLiteDatabase?) {
        setSteinsGate(db)
        setAnohana(db)
        setUchotenKazoku(db)
    }

    /**
     * 有頂天家族の内容をDBに保存する。
     * @param db データベース
     */
    private fun setUchotenKazoku(db: SQLiteDatabase?) {
        saveDb(
            db, "有頂天家族", "京都府",
            "下鴨神社", "http://uchoten-anime.com/", "35.036490",
            "135.772600", "uchoten"
        )
        saveDb(
            db, "有頂天家族", "京都府",
            "京都タワー", "http://uchoten-anime.com/", "34.987531",
            "135.759296", "uchoten"
        )
        saveDb(
            db, "有頂天家族", "京都府",
            "三嶋亭 本店", "http://uchoten-anime.com/", "35.008440",
            "135.767061", "uchoten"
        )
    }

    /**
     * あの花の内容をDBに保存する。
     * @param db データベース
     */
    private fun setAnohana(db: SQLiteDatabase?) {
        saveDb(
            db, "あの日見た花の名前を僕達はまだ知らない", "埼玉県",
            "旧秩父橋", "https://www.anohana.jp/tv/", "36.018548",
            "139.086475", "anohana"
        )
        saveDb(
            db, "あの日見た花の名前を僕達はまだ知らない", "埼玉県",
            "定林寺", "https://www.anohana.jp/tv/", "36.005745",
            "139.084267", "anohana"
        )
        saveDb(
            db, "あの日見た花の名前を僕達はまだ知らない", "埼玉県",
            "けやき公園", "https://www.anohana.jp/tv/", "36.006828",
            "139.085636", "anohana"
        )
        saveDb(
            db, "あの日見た花の名前を僕達はまだ知らない", "埼玉県",
            "羊山公園", "https://www.anohana.jp/tv/", "35.983051",
            "139.087207", "anohana"
        )
    }

    /**
     * STEINS;GATEの内容をDBに保存する。
     * @param db データベース
     */
    private fun setSteinsGate(db: SQLiteDatabase?) {
        saveDb(
            db, "STEINS;GATE", "東京都", "世界のラジオ会館",
            "http://steinsgate.tv/index.html", "35.698080",
            "139.772035", "steins_gate"
        )
        saveDb(
            db, "STEINS;GATE", "東京都", "万世橋",
            "http://steinsgate.tv/index.html", "35.697124",
            "139.770968", "steins_gate"
        )
        saveDb(
            db, "STEINS;GATE", "東京都", "神田ふれあい橋",
            "http://steinsgate.tv/index.html", "35.697005",
            "139.773306", "steins_gate"
        )
        saveDb(
            db, "STEINS;GATE", "東京都", "柳森神社",
            "http://steinsgate.tv/index.html", "35.696725",
            "139.773484", "steins_gate"
        )
        saveDb(
            db, "STEINS;GATE", "東京都", "秋葉神社",
            "http://steinsgate.tv/index.html", "35.716523",
            "139.785848", "steins_gate"
        )
    }

    /**
     * DBのレコードへ登録する。
     * @param title アニメタイトル
     * @param place アニメ聖地の場所
     * @param url アニメURL
     * @param latitude アニメ聖地の緯度
     * @param longitude アニメ聖地の経度
     * @param icon アニメアイコン
     */
    private fun saveDb(
        db: SQLiteDatabase?, title: String, prefecture: String,
        place: String, url: String, latitude: String, longitude: String, icon: String
    ) {
        val values = ContentValues()
        values.put(AnimDBObject.ANIM_TITLE, title)
        values.put(AnimDBObject.ANIM_PREFECTURE, prefecture)
        values.put(AnimDBObject.ANIM_PLACE, place)
        values.put(AnimDBObject.ANIM_URL, url)
        values.put(AnimDBObject.ANIM_LATITUDE, latitude)
        values.put(AnimDBObject.ANIM_LONGITUDE, longitude)
        values.put(AnimDBObject.ANIM_ICON, icon)

        db?.insert(AnimDBObject.DB_TABLE, null, values)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS " + AnimDBObject.DB_TABLE)
        onCreate(db)
    }
}