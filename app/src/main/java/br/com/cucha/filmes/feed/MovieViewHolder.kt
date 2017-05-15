package br.com.cucha.filmes.feed

import br.com.cucha.filmes.R

class MovieViewHolder(view: android.view.View) : android.support.v7.widget.RecyclerView.ViewHolder(view) {

    val dateFormat = java.text.SimpleDateFormat("yyyy-MM-dd")
    val yearFormat = java.text.SimpleDateFormat("yyyy")
    val textTitle = view.findViewById(br.com.cucha.filmes.R.id.text_title_movie) as android.widget.TextView
    val imagePoster = view.findViewById(br.com.cucha.filmes.R.id.image_poster_movie) as android.widget.ImageView
    val textRelease = view.findViewById(br.com.cucha.filmes.R.id.text_release_movie) as android.widget.TextView
    val textOverview = view.findViewById(br.com.cucha.filmes.R.id.text_overview_movie) as android.widget.TextView

    fun bind(movie: br.com.cucha.filmes.data.Movie, movieClickListener: MovieAdapter.MovieClickListener?) {

        textTitle.text = movie.title
        textTitle.setOnClickListener { movieClickListener?.onMovieClick(movie) }

        textOverview.text = movie.overview

        if(!movie.release.isEmpty()) {
            val date = dateFormat.parse(movie.release)
            textRelease.text = yearFormat.format(date)
        } else {
            textRelease.text = textRelease.context.getString(br.com.cucha.filmes.R.string.unknown)
        }

        if(movie.posterConfiguration != null ){

            var iSize = movie.posterConfiguration?.sizes?.indexOf(br.com.cucha.filmes.feed.MovieViewHolder.Companion.W342)

            if(iSize == -1)
                iSize = 0

            val size = movie.posterConfiguration?.sizes?.get(iSize as Int)
            val baseUrl = movie.posterConfiguration?.baseUrl

            val url = "$baseUrl/$size/${movie.poster}"

            com.squareup.picasso.Picasso.with(imagePoster.context)
                    .load(url)
                    .into(imagePoster)

            imagePoster.visibility = android.view.View.VISIBLE

        } else {
            imagePoster.visibility = android.view.View.GONE
        }
    }

    companion object {
        val W342 = "w342"
    }
}