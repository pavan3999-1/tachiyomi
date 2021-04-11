package eu.kanade.tachiyomi.ui.catalogue.browse

import android.view.View
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.kanade.tachiyomi.R
import eu.kanade.tachiyomi.data.database.models.Manga
import eu.kanade.tachiyomi.data.glide.GlideApp
import eu.kanade.tachiyomi.widget.StateImageViewTarget
import kotlinx.android.synthetic.main.catalogue_grid_item.*

/**
 * Class used to hold the displayed data of a manga in the catalogue, like the cover or the title.
 * All the elements from the layout file "item_catalogue_grid" are available in this class.
 *
 * @param view the inflated view for this holder.
 * @param adapter the adapter handling this holder.
 * @constructor creates a new catalogue holder.
 */
class CatalogueGridHolder(private val view: View, private val adapter: FlexibleAdapter<*>) :
        CatalogueHolder(view, adapter) {

    /**
     * Method called from [CatalogueAdapter.onBindViewHolder]. It updates the data for this
     * holder with the given manga.
     *
     * @param manga the manga to bind.
     */
    override fun onSetValues(manga: Manga) {
        // Set manga title
        title.text = manga.title

        // Set alpha of thumbnail.
        thumbnail.alpha = if (manga.favorite) 0.3f else 1.0f

        setImage(manga)
    }

    override fun setImage(manga: Manga) {
        GlideApp.with(view.context).clear(thumbnail)

        if (!manga.thumbnail_url.isNullOrEmpty()) {
            val radius = view.context.resources.getDimensionPixelSize(R.dimen.card_radius)
            val requestOptions = RequestOptions().transform(CenterCrop(), RoundedCorners(radius))
            GlideApp.with(view.context)
                    .load(manga)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .apply(requestOptions)
                    .placeholder(android.R.color.transparent)
                    .into(StateImageViewTarget(thumbnail, progress))
        }
    }
}
