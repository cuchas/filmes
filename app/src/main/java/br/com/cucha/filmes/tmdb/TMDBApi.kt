package br.com.cucha.filmes.tmdb

import retrofit2.http.GET
import retrofit2.http.Query
import rx.Observable

/**
 * Created by eduardocucharro on 12/05/17.
 */
interface TMDBApi {
    @GET("/3/movie/popular")
    fun getPopularMovies(@Query("page") page: Int, @Query("api_key") api: String) : Observable<MovieListResponse>

    @GET("/3/search/movie")
    fun searchMovies(@Query("api_key") api: String, @Query("query") q: String, @Query("page") page: Int) : Observable<MovieListResponse>

    @GET("3/configuration")
    fun getConfiguration(@Query("api_key") api: String) : Observable<ConfigurationResponse>
}
