package myproject.com.jp.animap

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.util.Linkify
import android.widget.TextView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_anim_detail.*

/**
 * アニメ詳細画面のアクティビティ。
 */
class AnimDetailActivity : AppCompatActivity(), OnMapReadyCallback {

    /** GoogleMap。 */
    private lateinit var mMap: GoogleMap
    private lateinit var mAnimDBAdapter: AnimDatabaseAdapter
    private lateinit var mAnimTitle: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anim_detail)

        setDB()
        // DB情報を読み込む
        mAnimDBAdapter.openDB()

        mAnimTitle = intent.getStringExtra("ANIM_TITLE")
        title = mAnimTitle

        setMap()
        setText()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    /**
     * 詳細画面に表示するテキストをセットする。
     */
    private fun setText() {
        anim_title.text = mAnimTitle
        anim_url.text = mAnimDBAdapter.getUrl(mAnimTitle)
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
        return mAnimDBAdapter.getLatitude(mAnimTitle)
    }

    /**
     * 聖地の経度を取得する。
     */
    private fun getLongitude(): ArrayList<String> {
        return mAnimDBAdapter.getLongitude(mAnimTitle)
    }

    /**
     * 聖地の場所を取得する。
     */
    private fun getPlace(): ArrayList<String> {
        return mAnimDBAdapter.getPlace(mAnimTitle)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val latitude = getLatitude()
        val longitude = getLongitude()
        val placeName = getPlace()

        var index = 0

        while (index < latitude.size) {
            val placeInfo = LatLng(latitude[index].toDouble(), longitude[index].toDouble())
            mMap.addMarker(MarkerOptions().position(placeInfo).title(placeName[index]))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(placeInfo))
            index++
        }
        mMap.moveCamera(CameraUpdateFactory.zoomTo(13F))
    }

    /**
     * DBをセットする。
     */
    private fun setDB() {
        // DBが存在しないなら生成する
        mAnimDBAdapter = AnimDatabaseAdapter(this)
    }
}
