package myproject.com.jp.animap

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.util.Linkify
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_anim_detail.*

/**
 * アニメ詳細画面のアクティビティ。
 */
class AnimDetailActivity : AppCompatActivity(), OnMapReadyCallback {

    /** GoogleMap。 */
    private lateinit var map: GoogleMap
    private lateinit var animDBAdapter: AnimDatabaseAdapter
    private lateinit var animTitle: String
    private var maxLongitude = 0.0
    private var minLongitude = 0.0
    private var maxLatitude = 0.0
    private var minLatitude = 0.0

    private val steinsgateImages: IntArray = intArrayOf(
        R.drawable.steinsgate_image1,
        R.drawable.steinsgate_image2
    )

    private val anohanaImages: IntArray = intArrayOf(
        R.drawable.anohana_image1,
        R.drawable.anohana_image2
    )

    private val uchotenImages: IntArray = intArrayOf(
        R.drawable.uchoten_image1,
        R.drawable.uchoten_image2,
        R.drawable.uchoten_image3
    )

    private fun getImage(): Int {
        return when (animTitle) {
            "STEINS;GATE" -> R.drawable.steinsgate_image1
            "あの日見た花の名前を僕達はまだ知らない" -> R.drawable.anohana_image1
            else -> R.drawable.uchoten_image2
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anim_detail)

        createDB()
        // DB情報を読み込む
        animDBAdapter.openDB()

        animTitle = intent.getStringExtra("ANIM_TITLE")
        title = animTitle

        val imageView = findViewById<ImageView>(R.id.imageView)
        imageView.setImageResource(getImage())

        setMap()
        setText()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    /**
     * 詳細画面に表示するテキストをセットする。
     */
    private fun setText() {
        anim_title.text = animTitle
        anim_url.text = animDBAdapter.getUrl(animTitle)
        setLink(anim_url)
    }

    /**
     * テキストにリンクをセットする。
     * @param textView リンクを付与する文字列
     */
    private fun setLink(textView: TextView) {
        Linkify.addLinks(textView, Linkify.ALL)
    }

    /**
     * MapのFragmentを生成する。
     */
    private fun setMap() {
        // MapFragmentの生成
        val mapFragment = MapFragment.newInstance()

        // MapViewをMapFragmentに変換する
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.mapView, mapFragment)
        fragmentTransaction.commit()

        mapFragment.getMapAsync(this)
    }

    /**
     * 聖地の緯度を取得する。
     */
    private fun getLatitude(): ArrayList<String> {
        return animDBAdapter.getLatitude(animTitle)
    }

    /**
     * 聖地の経度を取得する。
     */
    private fun getLongitude(): ArrayList<String> {
        return animDBAdapter.getLongitude(animTitle)
    }

    /**
     * 聖地の場所を取得する。
     */
    private fun getPlace(): ArrayList<String> {
        return animDBAdapter.getPlace(animTitle)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        val latitudeList = getLatitude()
        val longitudeList = getLongitude()
        val placeNameList = getPlace()

        val animIconName =
            when (animTitle) {
                "STEINS;GATE" -> "steinsgate"
                "あの日見た花の名前を僕達はまだ知らない" -> "anohana"
                else -> "uchoten"
            }

        var index = 0

        while (index < latitudeList.size) {
            val latitude = latitudeList[index].toDouble()
            val longitude = longitudeList[index].toDouble()
            if (maxLatitude == 0.0 && maxLongitude == 0.0 && minLatitude == 0.0 && minLongitude == 0.0) {
                maxLatitude = latitude
                minLatitude = latitude
                maxLongitude = longitude
                minLongitude = longitude
            }
            setLatLng(latitude, longitude)
            val placeInfo = LatLng(latitude, longitude)
            val mapIcon =
                resources.getIdentifier(animIconName + "_map_icon" + (index + 1), "drawable", "myproject.com.jp.animap")
            val bitmap = BitmapDescriptorFactory.fromResource(mapIcon)
            map.addMarker(MarkerOptions().position(placeInfo).title(placeNameList[index]).icon(bitmap))
            index++
        }
        val sw = LatLng(minLatitude, minLongitude)
        val ne = LatLng(maxLatitude, maxLongitude)
        val bounds = LatLngBounds(sw, ne)
        map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 300, 500, 10))
    }

    /**
     * 緯度経度の最大最小をセットする。
     * @param latitude 緯度
     * @param longitude 経度
     */
    private fun setLatLng(latitude: Double, longitude: Double) {
        if (maxLatitude < latitude) maxLatitude = latitude
        if (minLatitude > latitude) minLatitude = latitude
        if (maxLongitude < longitude) maxLongitude = longitude
        if (minLongitude > longitude) minLongitude = longitude
    }

    /**
     * DBをセットする。
     */
    private fun createDB() {
        // DBが存在しないなら生成する
        animDBAdapter = AnimDatabaseAdapter(this)
    }
}
