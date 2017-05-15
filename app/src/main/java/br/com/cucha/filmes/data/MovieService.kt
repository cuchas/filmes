package br.com.cucha.filmes.data

import rx.Observable

/**
 * Created by eduardocucharro on 12/05/17.
 */
interface MovieService {
    fun getMovies(page: Int) : Observable<Movie>
    fun search(q: String, page:Int) : Observable<Movie>
}