package br.com.cucha.filmes.feed

import android.app.SearchManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.Fragment
import android.support.v4.view.MenuItemCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.*
import br.com.cucha.filmes.Injector
import br.com.cucha.filmes.R
import br.com.cucha.filmes.data.Movie
import kotlinx.android.synthetic.main.fragment_movie_list.*
import java.util.*

class MovieListFragment : Fragment(),
        MovieListView,
        SwipeRefreshLayout.OnRefreshListener,
        SearchView.OnQueryTextListener {


    private lateinit var presenter: MovieListPresenter
    private lateinit var adapter: MovieAdapter
    private var page = 1
    private var query = ""
    private var enableNetworkDialog: AlertDialog? = null
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            if (isNetworksEnabled()) {
                enableNetworkDialog?.dismiss()

                presenter.presentMovies()
            }
        }
    }

    override fun showEnableNetworksDialog() {
        if (enableNetworkDialog == null) {
            enableNetworkDialog = AlertDialog.Builder(context)
                    .setTitle(getString(R.string.network_required))
                    .setMessage(getString(R.string.internet_access_is_required_))
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.enable_internet)) { dialog, which ->
                        dialog.dismiss()
                        val intent = Intent(Settings.ACTION_SETTINGS)
                        startActivity(intent)
                    }
                    .create()
        }

        hideProgress()

        enableNetworkDialog?.show()
    }

    override fun isNetworksEnabled(): Boolean {
        val connectivityManager = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
        val wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)

        return mobile != null && mobile.isConnected || wifi != null && wifi.isConnected
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = Injector.provideMovieListPresenter(this)

        setHasOptionsMenu(true)
    }

    private fun setInstanceState(bundle: Bundle?) {
        if(bundle == null) return

        page = bundle.getInt(EXTRA_PAGE)
        query = bundle.getString(EXTRA_QUERY)

        val movies = bundle.getSerializable(EXTRA_MOVIES) as Vector<Movie>

        adapter.data.addAll(movies)
        adapter.notifyDataSetChanged()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_movie_list, container, false)

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MovieAdapter()

        recycler_movie_list.adapter = adapter

        val layoutManager = LinearLayoutManager(context)

        recycler_movie_list.layoutManager = layoutManager
        recycler_movie_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val itensOnAdapter = recycler_movie_list.layoutManager.itemCount

                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (firstVisibleItemPosition >= (itensOnAdapter - 10)) {
                    presenter.presentMore()
                }
            }
        })

        swiper_movie_list.setOnRefreshListener(this)

        setInstanceState(savedInstanceState ?: arguments)
    }

    override fun isProgressVisible(): Boolean {
        return swiper_movie_list.isRefreshing
    }

    override fun showProgress() {
        swiper_movie_list.isRefreshing = true
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater?.inflate(R.menu.menu_search, menu)

        val menuItem = menu?.findItem(R.id.menu_search)

        val searchManager = context.getSystemService(Context.SEARCH_SERVICE) as SearchManager

        val searchView = SearchView((activity as AppCompatActivity).supportActionBar?.themedContext)

        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity.componentName))
        searchView.setOnQueryTextListener(this)

        if(!query.isEmpty()) searchView.setQuery(query, false)

        MenuItemCompat.setShowAsAction(menuItem, MenuItemCompat.SHOW_AS_ACTION_ALWAYS)
        MenuItemCompat.setActionView(menuItem, searchView)
    }

    override fun onStart() {
        super.onStart()

        val filter = IntentFilter()
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)

        activity.registerReceiver(receiver, filter)

        presenter.presentMovies()
    }

    override fun onStop() {
        presenter.stop()

        activity.unregisterReceiver(receiver)

        super.onStop()
    }

    override fun onRefresh() {
        presenter.refresh()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putString(EXTRA_QUERY, query)
        outState?.putInt(EXTRA_PAGE, page)
        outState?.putSerializable(EXTRA_MOVIES, adapter.data)
        super.onSaveInstanceState(outState)
    }

    override fun showMovie(movie: Movie) {
        adapter.addMovie(movie)
    }

    override fun hideProgress() {
        swiper_movie_list.isRefreshing = false
    }

    override fun getPage(): Int {
        return page
    }

    override fun setPage(page: Int) {
        this.page = page
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(newText: String): Boolean {
        query = newText

        presenter.search()

        return true
    }

    override fun clearMovieList() {
        adapter.clear()
    }

    override fun getQuery(): String {
        return query
    }

    companion object {
        val EXTRA_PAGE = "EXTRA_PAGE"
        val EXTRA_QUERY = "EXTRA_QUERY"
        val EXTRA_MOVIES = "EXTRA_MOVIES"
    }
}
