package eu.kanade.tachiyomi.ui.library

import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import com.f2prateek.rx.preferences.Preference
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IFilterable
import eu.kanade.tachiyomi.R
import eu.kanade.tachiyomi.data.database.models.LibraryManga
import eu.kanade.tachiyomi.data.preference.getOrDefault
import eu.kanade.tachiyomi.source.SourceManager
import eu.kanade.tachiyomi.widget.AutofitRecyclerView
import kotlinx.android.synthetic.main.catalogue_grid_item.view.*
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

class LibraryItem(val manga: LibraryManga, private val libraryAsList: Preference<Boolean>) :
        AbstractFlexibleItem<LibraryHolder>(), IFilterable {

    private val sourceManager: SourceManager = Injekt.get()


    var downloadCount = -1

    override fun getLayoutRes(): Int {
        return if (libraryAsList.getOrDefault())
            R.layout.catalogue_list_item
        else
            R.layout.catalogue_grid_item
    }

    override fun createViewHolder(view: View, adapter: FlexibleAdapter<*>): LibraryHolder {
        val parent = adapter.recyclerView
        return if (parent is AutofitRecyclerView) {
            view.apply {
                val coverHeight = parent.itemWidth / 3 * 4
                card.layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, coverHeight)
                gradient.layoutParams = FrameLayout.LayoutParams(
                        MATCH_PARENT, coverHeight / 2, Gravity.BOTTOM)
            }
            LibraryGridHolder(view, adapter)
        } else {
            LibraryListHolder(view, adapter)
        }
    }

    override fun bindViewHolder(adapter: FlexibleAdapter<*>,
                                holder: LibraryHolder,
                                position: Int,
                                payloads: List<Any?>?) {

        holder.onSetValues(this)
    }

    /**
     * Filters a manga depending on a query.
     *
     * @param constraint the query to apply.
     * @return true if the manga should be included, false otherwise.
     */
    override fun filter(constraint: String): Boolean {
        return manga.title.contains(constraint, true) ||
            (manga.author?.contains(constraint, true) ?: false) ||
            (manga.artist?.contains(constraint, true) ?: false) ||
            (manga.description?.contains(constraint, true) ?: false) ||
            sourceManager.getOrStub(manga.source).name.contains(constraint, true) ||
            if (constraint.contains(",")) {
                val genres = manga.genre?.split(", ")
                constraint.split(",").all { containsGenre(it.trim(), genres) }
            }
            else containsGenre(constraint, manga.genre?.split(", "))
    }

    private fun containsGenre(tag: String, genres: List<String>?): Boolean {
        return if (tag.startsWith("-"))
            genres?.find {
                it.trim().toLowerCase() == tag.substringAfter("-").toLowerCase()
            } == null
        else
            genres?.find {
                it.trim().toLowerCase() == tag.toLowerCase() } != null
    }

    override fun equals(other: Any?): Boolean {
        if (other is LibraryItem) {
            return manga.id == other.manga.id
        }
        return false
    }

    override fun hashCode(): Int {
        return manga.id!!.hashCode()
    }
}
