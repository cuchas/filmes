package br.com.cucha.filmes.feed;

import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import br.com.cucha.filmes.data.Movie;
import br.com.cucha.filmes.data.PosterConfiguration;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;

/**
 * Created by eduardocucharro on 15/05/17.
 */

public class MovieListSubscriberTest {

    @Mock
    MovieListView view;
    private MovieListSubscriber subscriber;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        subscriber = new MovieListSubscriber(view, new PosterConfiguration("", new ArrayList<String>()));
    }

    @Test
    public void onError_hideProgress() {
        subscriber.onError(new Exception());

        verify(view).hideProgress();
    }

    @Test
    public void onNext_showMovie() {
        Movie movie = new Movie(1, "some movie", "2017-04-04", "htto://", "some description");

        subscriber.onNext(movie);

        verify(view).showMovie(movie);
    }

    @Test
    public void onComplete_hideProgress() {
        subscriber.onCompleted();

        verify(view).hideProgress();
    }
}
