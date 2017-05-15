package br.com.cucha.filmes.data

import br.com.cucha.filmes.Injector
import rx.Observable

/**
 * Created by eduardocucharro on 12/05/17.
 */
class MovieServiceImpl : MovieService {

    override fun search(q: String, page: Int): Observable<Movie> {
        val tmdbApi = Injector.provideTMDBApi()

        return tmdbApi.searchMovies(API_KEY, q, page)
                .flatMap { Observable.from(it.results) }
                .filter { it.poster_path != null && it.release_date != null}
                .map {
                    t ->  Movie(t.id,
                        t.title as String,
                        t.release_date as String,
                        t.poster_path as String,
                        t.overview as String)

                }
    }

    override fun getMovies(page: Int): Observable<Movie> {
        val tmdbApi = Injector.provideTMDBApi()

        return tmdbApi.getPopularMovies(page, API_KEY)
                .flatMap { Observable.from(it.results) }
                .filter { it.poster_path != null && it.release_date != null}
                .map {
                    t ->  Movie(t.id,
                        t.title as String,
                        t.release_date as String,
                        t.poster_path as String,
                        t.overview as String)

                }
    }

    companion object {
        val API_KEY = "a9c97275d05aef30a425b16e049e4dc6"
    }
}