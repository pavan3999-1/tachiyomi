package eu.kanade.tachiyomi.ui.catalogue.filter

import android.support.graphics.drawable.VectorDrawableCompat
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.CheckedTextView
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractSectionableItem
import eu.davidea.viewholders.FlexibleViewHolder
import eu.kanade.tachiyomi.R
import eu.kanade.tachiyomi.source.model.Filter
import eu.kanade.tachiyomi.util.getResourceColor

class SortItem(val name: String, val group: SortGroup) : AbstractSectionableItem<SortItem.Holder, SortGroup>(group) {

    override fun getLayoutRes(): Int {
        return R.layout.navigation_view_checkedtext
    }

    override fun getItemViewType(): Int {
        return 102
    }

    override fun createViewHolder(view: View, adapter: FlexibleAdapter<*>): Holder {
        return Holder(view, adapter)
    }

    override fun bindViewHolder(adapter: FlexibleAdapter<*>, holder: Holder, position: Int, payloads: List<Any?>?) {
        val view = holder.text
        view.text = name
        val filter = group.filter

        val i = filter.values.indexOf(name)

        fun getIcon() = when (filter.state) {
            Filter.Sort.Selection(i, false) -> VectorDrawableCompat.create(view.resources, R.drawable.ic_arrow_down_white_32dp, null)
                    ?.apply { setTint(view.context.getResourceColor(R.attr.colorAccent)) }
            Filter.Sort.Selection(i, true) -> VectorDrawableCompat.create(view.resources, R.drawable.ic_arrow_up_white_32dp, null)
                    ?.apply { setTint(view.context.getResourceColor(R.attr.colorAccent)) }
            else -> ContextCompat.getDrawable(view.context, R.drawable.empty_drawable_32dp)
        }

        view.setCompoundDrawablesWithIntrinsicBounds(getIcon(), null, null, null)
        holder.itemView.setOnClickListener {
            val pre = filter.state?.index ?: i
            if (pre != i) {
                filter.state = Filter.Sort.Selection(i, false)
            } else {
                filter.state = Filter.Sort.Selection(i, filter.state?.ascending == false)
            }

            group.subItems.forEach { adapter.notifyItemChanged(adapter.getGlobalPositionOf(it)) }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as SortItem
        return name == other.name && group == other.group
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + group.hashCode()
        return result
    }

    class Holder(view: View, adapter: FlexibleAdapter<*>) : FlexibleViewHolder(view, adapter) {
        val text: CheckedTextView = itemView.findViewById(R.id.nav_view_item)
    }

}