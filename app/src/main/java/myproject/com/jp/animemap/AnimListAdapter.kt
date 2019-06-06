package myproject.com.jp.animap

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class AnimListAdapter(context: Context, resource: Int, items: List<AnimItemListProvider>) :
    ArrayAdapter<AnimItemListProvider>(context, resource, items) {

    /** リソースID */
    private var mResource = resource
    /** リストビューの要素 */
    private var mItems = items

    private var mInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view: View
        if (convertView != null) {
            view = convertView
        } else {
            view = mInflater.inflate(mResource, null)
        }

        // リストビューを表示する要素を取得
        val item: AnimItemListProvider = mItems.get(position)

        // アイコン画像を設定
        val icon = view.findViewById<ImageView>(R.id.anim_icon)
        icon.setImageResource(item.getAnimIcon())

        // タイトルを設定
        val title = view.findViewById<TextView>(R.id.anim_title)
        title.setText(item.getAnimTitle())

        return view
    }
}