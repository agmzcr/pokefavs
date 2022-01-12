package dev.agmzcr.pokefavs.util


import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.github.florent37.glidepalette.BitmapPalette
import com.github.florent37.glidepalette.GlidePalette
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.tabs.TabLayout
import dev.agmzcr.pokefavs.R
import java.util.*

@BindingAdapter("urlToInt")
fun TextView.setIdFromUrl(url: String) {
    text = String.format("#%03d", url.extractPokemonIdFromUrl())
}

@BindingAdapter("idFormatter")
fun TextView.setIdFormatter(id: Int) {
    text = String.format("#%03d", id)
}
@BindingAdapter("weightFormatter")
fun TextView.setWeight(data: Int) {
    text = String.format("%.1f kg", data.toFloat() / 10)
}

@BindingAdapter("heightFormatter")
fun TextView.setHeight(data: Int) {
    text = String.format("%.1f m", data.toFloat() / 10)
}

@BindingAdapter("captureRateFormatter")
fun TextView.setCaptureRate(data: Int) {
    text = "$data%"
}

@BindingAdapter("capitalizeFormatter")
fun TextView.setCapitalize(data: String?) {
    text = data?.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}

@BindingAdapter(
    "imagePokemonEvo", "paletteCardEvo")
fun loadImagePokemonEvo(
    view: ImageView,
    id: Int?,
    paletteCard: CardView
) {
    val drawable = id?.let { getSpriteFromId(it) }
    Glide.with(view.context)
        .load(drawable)
        .listener(
            GlidePalette.with(drawable)
                .use(BitmapPalette.Profile.MUTED_DARK)
                .intoCallBack { palette ->
                    val rgb = palette?.getMutedColor(Color.GRAY)
                    if (rgb != null) {
                        paletteCard.setCardBackgroundColor(rgb)
                    }
                }
        )
        .into(view)
}

@BindingAdapter("setIsSaved")
fun ImageView.setSaved(saved: Boolean) {
    var imageView = R.drawable.ic_pokeball
    imageView = if (!saved) {
        R.drawable.ic_favorite_nosaved
    } else {
        R.drawable.ic_favorite_saved
    }
    setImageResource(imageView)
}

@BindingAdapter("setVisibility")
fun setVisibility(view: View, value: Boolean) {
    view.setVisibility(if (value) View.VISIBLE else View.GONE)
}

@BindingAdapter("imageUrl", "paletteCard")
fun loadImageAndCardColor(view: ImageView, url: String, paletteCard: CardView) {
    val drawableUrl = getSpriteFromUrlPokemon(url)
    Glide.with(view.context)
        .load(drawableUrl)
        .listener(
            GlidePalette.with(drawableUrl)
                .use(BitmapPalette.Profile.MUTED)
                .intoCallBack { palette ->
                    val rgb = palette?.getMutedColor(Color.GRAY)
                    if (rgb != null) {
                        paletteCard.setCardBackgroundColor(rgb)
                    }
                }.crossfade(true)
        ).centerCrop()
        .into(view)
}

@BindingAdapter(
    "imagePokemon",
    "appBarColor",
    "collapsingToolbarColor",
    "tabsColor")
fun loadImagePokemon(
    view: ImageView,
    id: Int?,
    appBar: AppBarLayout,
    collapsingToolbar: CollapsingToolbarLayout,
    tabsLayout: TabLayout
) {
    val drawable = id?.let { getSpriteFromId(it) }
    Glide.with(view.context)
        .load(drawable)
        .listener(
            GlidePalette.with(drawable)
                .use(BitmapPalette.Profile.MUTED_DARK)
                .intoCallBack { palette ->
                    val rgb = palette?.getMutedColor(Color.GRAY)
                    if (rgb != null) {
                        appBar.setBackgroundColor(rgb)
                        collapsingToolbar.setBackgroundColor(rgb)
                        collapsingToolbar.contentScrim!!.setTint(rgb)
                        tabsLayout.setSelectedTabIndicatorColor(rgb)
                    }
                }.crossfade(true)
        ).into(view)
}



@BindingAdapter("pokemonId")
fun TextView.setPokemonId(pokemonId: Int) {
    text = String.format("#%03d", pokemonId)
}

@BindingAdapter("typeImage")
fun ImageView.setIcon(data: String?) {
    if (data != null) {
        var typeId = R.drawable.ic_bug_type_icon
        data.let {
            val item = data
            typeId = item.asResourceId()
        }
        setImageResource(typeId)
    }
}
