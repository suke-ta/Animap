package myproject.com.jp.animap

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ListView

/**
 * アニメ一覧のアクティビティ。
 */
class AnimListActivity : AppCompatActivity() {

    private lateinit var mAnimDBAdapter: AnimDatabaseAdapter
    private lateinit var mAdapter: AnimListAdapter
    private lateinit var mItems: ArrayList<AnimItemListProvider>

    private lateinit var mAnimListView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        waitSplash()
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_anim_list)
        setTitle(R.string.anim_list_title)

        setDB()
        // DB情報を読み込む
        mAnimDBAdapter.openDB()

        setView()

        setItem()

        setAnimAdapter()

        // リストビューにアダプターをセット
        mAnimListView.adapter = mAdapter

        // ArrayAdapterに対してListViewのリスト（item）q  の更新
        mAdapter.notifyDataSetChanged()

        intentClickList()
    }

    /**
     * タップしたリストのアニメ詳細画面に遷移する。
     */
    private fun intentClickList() {
        mAnimListView.setOnItemClickListener { parent, _, position, _ ->
            val listView = parent as ListView
            val item: AnimItemListProvider = listView.getItemAtPosition(position) as AnimItemListProvider

            val intent = Intent(this, AnimDetailActivity::class.java)
            intent.putExtra("ANIM_TITLE", item.getAnimTitle())
            startActivity(intent)
        }
    }

    /**
     * ArrayAdapterのコンストラクタ
     */
    private fun setAnimAdapter() {
        // 第1引数：Context
        // 第2引数：リソースとして登録されたTextViewに対するリソースID 今回は元々用意されている定義済みのレイアウトファイルのID
        // 第3引数：一覧させたいデータの配列
        mAdapter = AnimListAdapter(this, R.layout.animlist_item, mItems)
    }

    /**
     * リストに表示するitemをセットする。
     */
    private fun setItem() {
        mItems = ArrayList()

        val animTitleList = mAnimDBAdapter.getAnimTitle()
        var animIconList = mAnimDBAdapter.getAnimIcon()

        var count = 0
        while (count < animTitleList.size) {
            val iconDrawable =
                resources.getIdentifier("icon_" + animIconList[count], "drawable", "myproject.com.jp.animap")
            val listItems = AnimItemListProvider(iconDrawable, animTitleList[count])
            mItems.add(listItems)
            count++
        }
    }

    /**
     * DBの生成
     */
    private fun setDB() {
        // DBが存在しないなら生成する
        mAnimDBAdapter = AnimDatabaseAdapter(this)
    }

    /**
     * アニメ一覧画面のビューをセットする。
     */
    private fun setView() {
        mAnimListView = findViewById(R.id.anim_list)
    }

    /**
     * スプラッシュ画面を1秒間表示するために、1秒間sleepする。
     */
    private fun waitSplash() {
        try {
            Thread.sleep(1000)
        } catch (e: InterruptedException) {
        }
    }
}
