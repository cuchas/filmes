package br.com.cucha.filmes

import br.com.cucha.filmes.data.MovieService
import br.com.cucha.filmes.data.MovieServiceImpl
import br.com.cucha.filmes.data.PosterConfiguration
import br.com.cucha.filmes.feed.MovieListPresenter
import br.com.cucha.filmes.feed.MovieListPresenterImpl
import br.com.cucha.filmes.feed.MovieListSubscriber
import br.com.cucha.filmes.feed.MovieListView
import br.com.cucha.filmes.tmdb.TMDBApi
import br.com.cucha.filmes.util.BaseSchedulerProvider
import br.com.cucha.filmes.util.SchedulerProvider
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by eduardocucharro on 12/05/17.
 */
class Injector {

    companion object {
        private val listImages = ArrayList<String>()

        init {
            listImages.add("w342")
        }

        fun provideTMDBApi() : TMDBApi {

            val gson = GsonBuilder().setLenient().create()
            val gsonConverterFactory = GsonConverterFactory.create(gson)

            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .build()

            val callAdapter = RxJavaCallAdapterFactory.create()

            val retrofit = Retrofit.Builder()
                    .baseUrl("https://api.themoviedb.org")
                    .addCallAdapterFactory(callAdapter)
                    .addConverterFactory(gsonConverterFactory)
                    .client(client)
                    .build()

            return retrofit.create(TMDBApi::class.java)
        }

        fun provideMovieListPresenter(view: MovieListView): MovieListPresenter {
            return MovieListPresenterImpl(view, provideMovieService(), provideSchedulersProvider())
        }

        fun provideMovieService() : MovieService {
            return MovieServiceImpl()
        }

        fun provideSchedulersProvider() : BaseSchedulerProvider {
            return SchedulerProvider.INSTANCE
        }

        fun providePosterConfiguration() : PosterConfiguration {
            return PosterConfiguration("https://image.tmdb.org/t/p/", listImages)
        }

        fun provideMovieListSubscriber(view: MovieListView) : MovieListSubscriber {
            return MovieListSubscriber(view, providePosterConfiguration())
        }
    }
}
