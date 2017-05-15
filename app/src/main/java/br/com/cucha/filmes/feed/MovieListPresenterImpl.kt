package br.com.cucha.filmes.feed

import br.com.cucha.filmes.Injector

/**
 * Created by eduardocucharro on 12/05/17.
 */
class MovieListPresenterImpl (val view: MovieListView,
                              private val service: br.com.cucha.filmes.data.MovieService,
                              private val schedulerProvider: br.com.cucha.filmes.util.BaseSchedulerProvider) : MovieListPresenter {

    val compositeSubscription = rx.subscriptions.CompositeSubscription()

    override fun presentMovies() {

        if (!view.isNetworksEnabled()) {
            view.showEnableNetworksDialog()
            return
        }

        if(view.isProgressVisible()) return

        view.showProgress()

        val subscription = service.getMovies(view.getPage())
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(Injector.provideMovieListSubscriber(view))

        compositeSubscription.add(subscription)
    }

    override fun presentMore() {

        if(view.isProgressVisible()) return

        if(view.getQuery().isEmpty())
            presentMovies()
        else
            queryMovies()
    }

    override fun refresh() {

        if (!view.isNetworksEnabled()) {
            view.showEnableNetworksDialog()
            return
        }

        if(view.isProgressVisible()) return

        view.setPage(1)
        view.clearMovieList()

        if(view.getQuery().isEmpty())
            presentMovies()
        else
            queryMovies()
    }

    override fun stop() {
        compositeSubscription.unsubscribe()
    }

    override fun search() {

        view.setPage(1)

        view.clearMovieList()

        if(view.getQuery().isEmpty()) {
            presentMovies()
        } else {
            queryMovies()
        }
    }

    private fun queryMovies() {
        if(view.getQuery().length > 2) {

            compositeSubscription.clear()

            view.showProgress()

            val subscription = service.search(view.getQuery(), view.getPage())
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .subscribe(Injector.provideMovieListSubscriber(view))

            compositeSubscription.add(subscription)

        } else {
            compositeSubscription.clear()
            view.hideProgress()
        }
    }
}