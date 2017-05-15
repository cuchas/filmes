package br.com.cucha.filmes.feed

import br.com.cucha.filmes.data.Movie

/**
 * Created by eduardocucharro on 12/05/17.
 */
class MovieAdapter : android.support.v7.widget.RecyclerView.Adapter<MovieViewHolder>() {

    val data = java.util.Vector<Movie>()
    val movieClickListener: br.com.cucha.filmes.feed.MovieAdapter.MovieClickListener? = null

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: android.view.ViewGroup?, viewType: Int): MovieViewHolder {
        val inflater = android.view.LayoutInflater.from(parent?.context)
        val view = inflater.inflate(br.com.cucha.filmes.R.layout.view_movie, parent, false)

        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder?, position: Int) {
        val movie = data.get(position)

        holder?.bind(movie, movieClickListener)
    }

    fun addMovie(movie: br.com.cucha.filmes.data.Movie) {
        data.add(movie)
        notifyItemInserted(data.size -1)
    }

    interface MovieClickListener {
        fun onMovieClick(m: br.com.cucha.filmes.data.Movie)
    }

    fun clear() {
        data.clear()
        notifyDataSetChanged()
    }
}


