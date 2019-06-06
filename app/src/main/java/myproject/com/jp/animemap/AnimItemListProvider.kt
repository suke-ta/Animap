package myproject.com.jp.animap

class AnimItemListProvider(animIcon: Int, animTitle: String) {
    private var mAnimIcon = animIcon
    private var mAnimTitle = animTitle

    /**
     * アニメアイコンをセットする。
     * @param animIcon アニメのアイコン画像
     */
    fun setAnimIcon(animIcon: Int) {
        mAnimIcon = animIcon
    }

    /**
     * アニメタイトルをセットする。
     * @param animTitle アニメのタイトル
     */
    fun setAnimTitle(animTitle: String) {
        mAnimTitle = animTitle
    }

    /**
     * アニメアイコンを取得する。
     * @return アニメアイコン
     */
    fun getAnimIcon(): Int {
        return mAnimIcon
    }

    /**
     * アニメタイトルを取得する。
     * @return アニメタイトル
     */
    fun getAnimTitle(): String? {
        return mAnimTitle
    }
}