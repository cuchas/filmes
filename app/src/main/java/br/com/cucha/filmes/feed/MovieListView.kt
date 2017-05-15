package br.com.cucha.filmes.feed

import br.com.cucha.filmes.data.Movie

/**
 * Created by eduardocucharro on 12/05/17.
 */
interface MovieListView {
    fun showMovie(movie: br.com.cucha.filmes.data.Movie)
    fun getPage() : Int
    fun setPage(page: Int)
    fun hideProgress()
    fun clearMovieList()
    fun getQuery() : String
    fun isProgressVisible() : Boolean
    fun showProgress()
    fun showEnableNetworksDialog()
    fun isNetworksEnabled(): Boolean
}
