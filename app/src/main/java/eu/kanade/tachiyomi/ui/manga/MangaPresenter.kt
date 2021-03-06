package eu.kanade.tachiyomi.ui.manga

import android.os.Bundle
import eu.kanade.tachiyomi.data.database.DatabaseHelper
import eu.kanade.tachiyomi.data.database.models.Manga
import eu.kanade.tachiyomi.data.track.TrackManager
import eu.kanade.tachiyomi.ui.base.presenter.BasePresenter
import eu.kanade.tachiyomi.ui.manga.info.ChapterCountEvent
import eu.kanade.tachiyomi.ui.manga.info.MangaFavoriteEvent
import eu.kanade.tachiyomi.util.SharedData
import eu.kanade.tachiyomi.util.isNullOrUnsubscribed
import rx.Observable
import rx.Subscription
import uy.kohesive.injekt.injectLazy

/**
 * Presenter of [MangaActivity].
 */
class MangaPresenter : BasePresenter<MangaActivity>() {

    /**
     * Database helper.
     */
    val db: DatabaseHelper by injectLazy()

    /**
     * Tracking manager.
     */
    val trackManager: TrackManager by injectLazy()

    /**
     * Manga associated with this instance.
     */
    lateinit var manga: Manga

    var mangaSubscription: Subscription? = null

    override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)

        // Prepare a subject to communicate the chapters and info presenters for the chapter count.
        SharedData.put(ChapterCountEvent())
        // Prepare a subject to communicate the chapters and info presenters for the chapter favorite.
        SharedData.put(MangaFavoriteEvent())
    }

    fun setMangaEvent(event: MangaEvent) {
        if (mangaSubscription.isNullOrUnsubscribed()) {
            manga = event.manga
            mangaSubscription = Observable.just(manga)
                    .subscribeLatestCache(MangaActivity::onSetManga)
        }
    }

}
