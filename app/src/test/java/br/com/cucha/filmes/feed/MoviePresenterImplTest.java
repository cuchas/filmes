package br.com.cucha.filmes.feed;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import br.com.cucha.filmes.data.Movie;
import br.com.cucha.filmes.data.MovieService;
import br.com.cucha.filmes.util.ImmediateSchedulerProvider;
import rx.Observable;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Created by eduardocucharro on 25/04/17.
 */

public class MoviePresenterImplTest {
    @Mock
    MovieListView view;

    MovieListPresenter presenter;

    @Mock
    MovieService service;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        presenter = new MovieListPresenterImpl(
                view,
                service,
                new ImmediateSchedulerProvider());
    }

    @Test
    public void presentMovies_showEnableNetworksDialog_whenDisabled() {

        when(view.isNetworksEnabled()).thenReturn(false);

        presenter.presentMovies();

        verify(view).showEnableNetworksDialog();
    }

    @Test
    public void search_clearMovieList() {

        when(view.getQuery()).thenReturn("");

        presenter.search();

        verify(view).clearMovieList();
    }

    @Test
    public void search_presentPopular_whenQueryEmpty() {

        when(view.isNetworksEnabled()).thenReturn(true);
        when(view.getQuery()).thenReturn("");
        when(view.getPage()).thenReturn(1);
        when(service.getMovies(view.getPage())).thenReturn(Observable.from(new ArrayList<Movie>()));
        when(view.isProgressVisible()).thenReturn(false);

        presenter.search();

        verify(service).getMovies(view.getPage());
    }

    @Test
    public void presentMore_doNothing_when_InProgress() {

        when(view.getQuery()).thenReturn("");
        when(view.isNetworksEnabled()).thenReturn(true);
        when(view.isProgressVisible()).thenReturn(true);

        presenter.presentMore();

        verify(view).isProgressVisible();

        verifyNoMoreInteractions(view);
    }

    @Test
    public void refresh_showProgress() {
        when(view.getQuery()).thenReturn("");
        when(view.isNetworksEnabled()).thenReturn(true);
        when(service.getMovies(view.getPage())).thenReturn(Observable.from(new ArrayList<Movie>()));

        presenter.refresh();

        verify(view).showProgress();
    }
}
