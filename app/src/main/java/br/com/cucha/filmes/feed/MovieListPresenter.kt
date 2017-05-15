package br.com.cucha.filmes.feed

/**
 * Created by eduardocucharro on 12/05/17.
 */
interface MovieListPresenter {
    fun presentMovies()
    fun refresh()
    fun search()
    fun stop()
    fun presentMore()
}
