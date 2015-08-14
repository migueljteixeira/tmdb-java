package com.uwetrottmann.tmdb.services;

import com.uwetrottmann.tmdb.BaseTestCase;
import com.uwetrottmann.tmdb.TestData;
import com.uwetrottmann.tmdb.entities.AppendToResponse;
import com.uwetrottmann.tmdb.entities.Credits;
import com.uwetrottmann.tmdb.entities.Images;
import com.uwetrottmann.tmdb.entities.ListResultsPage;
import com.uwetrottmann.tmdb.entities.Movie;
import com.uwetrottmann.tmdb.entities.MovieAlternativeTitles;
import com.uwetrottmann.tmdb.entities.MovieKeywords;
import com.uwetrottmann.tmdb.entities.MovieResultsPage;
import com.uwetrottmann.tmdb.entities.Releases;
import com.uwetrottmann.tmdb.entities.ReviewResultsPage;
import com.uwetrottmann.tmdb.entities.Translations;
import com.uwetrottmann.tmdb.entities.Videos;
import com.uwetrottmann.tmdb.enumerations.AppendToResponseItem;

import org.junit.Test;

import java.text.ParseException;

import rx.Observable;
import rx.functions.Action1;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

public class MoviesServiceTest extends BaseTestCase {

    @Test
    public void test_summary() throws ParseException {
        Observable<Movie> movie = getManager().moviesService()
                .summary(TestData.MOVIE_ID, null, null);

        movie.toBlocking().forEach(new Action1<Movie>() {
            @Override
            public void call(Movie movie) {
                assertMovie(movie);
                assertThat(movie.original_title).isEqualTo(TestData.MOVIE_TITLE);
            }
        });
    }

    @Test
    public void test_summary_language() throws ParseException {
        Observable<Movie> movie = getManager().moviesService()
                .summary(TestData.MOVIE_ID, "pt", null);

        movie.toBlocking().forEach(new Action1<Movie>() {
            @Override
            public void call(Movie movie) {
                assertThat(movie).isNotNull();
                assertThat(movie.title).isEqualTo("Clube da Luta");
            }
        });
    }

    @Test
    public void test_summary_with_collection() throws ParseException {
        Observable<Movie> movie = getManager().moviesService().summary(TestData.MOVIE_WITH_COLLECTION_ID, null, null);

        movie.toBlocking().forEach(new Action1<Movie>() {
            @Override
            public void call(Movie movie) {
                assertThat(movie.title).isEqualTo(TestData.MOVIE_WITH_COLLECTION_TITLE);
                assertThat(movie.belongs_to_collection).isNotNull();
                assertThat(movie.belongs_to_collection.id).isEqualTo(1241);
                assertThat(movie.belongs_to_collection.name).isEqualTo("Harry Potter Collection");
            }
        });
    }

    private void assertMovie(Movie movie) {
        assertThat(movie).isNotNull();
        assertThat(movie.id).isEqualTo(TestData.MOVIE_ID);
        assertThat(movie.title).isEqualTo(TestData.MOVIE_TITLE);
        assertThat(movie.overview).isNotEmpty();
        assertThat(movie.tagline).isNotEmpty();
        assertThat(movie.adult).isFalse();
        assertThat(movie.backdrop_path).isNotEmpty();
        assertThat(movie.budget).isEqualTo(63000000);
        assertThat(movie.imdb_id).isEqualTo(TestData.MOVIE_IMDB);
        assertThat(movie.poster_path).isNotEmpty();
        assertThat(movie.release_date).isEqualTo("1999-10-14");
        assertThat(movie.revenue).isEqualTo(100853753);
        assertThat(movie.runtime).isEqualTo(139);
        assertThat(movie.vote_average).isPositive();
        assertThat(movie.vote_count).isPositive();
    }

    @Test
    public void test_summary_append_videos() {
        Observable<Movie> movie = getManager().moviesService().summary(TestData.MOVIE_ID,
                null, new AppendToResponse(AppendToResponseItem.VIDEOS));

        movie.toBlocking().forEach(new Action1<Movie>() {
            @Override
            public void call(Movie movie) {
                assertNotNull(movie.videos);
            }
        });
    }

    @Test
    public void test_summary_append_credits() {
        Observable<Movie> movie = getManager().moviesService().summary(TestData.MOVIE_ID,
                null, new AppendToResponse(AppendToResponseItem.CREDITS));

        movie.toBlocking().forEach(new Action1<Movie>() {
            @Override
            public void call(Movie movie) {
                assertNotNull(movie.credits);
            }
        });
    }

    @Test
    public void test_summary_append_releases() {
        Observable<Movie> movie = getManager().moviesService().summary(TestData.MOVIE_ID,
                null, new AppendToResponse(AppendToResponseItem.RELEASES));

        movie.toBlocking().forEach(new Action1<Movie>() {
            @Override
            public void call(Movie movie) {
                assertNotNull(movie.releases);
            }
        });
    }

    @Test
    public void test_summary_append_similar() {
        Observable<Movie> movie = getManager().moviesService().summary(TestData.MOVIE_ID,
                null, new AppendToResponse(AppendToResponseItem.SIMILAR));

        movie.toBlocking().forEach(new Action1<Movie>() {
            @Override
            public void call(Movie movie) {
                assertNotNull(movie.similar);
            }
        });
    }

    @Test
    public void test_summary_append_all() {
        Observable<Movie> movie = getManager().moviesService().summary(TestData.MOVIE_ID,
                null, new AppendToResponse(AppendToResponseItem.RELEASES,
                        AppendToResponseItem.CREDITS,
                        AppendToResponseItem.VIDEOS,
                        AppendToResponseItem.SIMILAR));

        movie.toBlocking().forEach(new Action1<Movie>() {
            @Override
            public void call(Movie movie) {
                assertNotNull(movie.releases);
                assertNotNull(movie.credits);
                assertNotNull(movie.videos);
                assertNotNull(movie.similar);
            }
        });
    }

    @Test
    public void test_alternative_titles() {
        Observable<MovieAlternativeTitles> titles = getManager().moviesService().alternativeTitles(TestData.MOVIE_ID, null);

        titles.toBlocking().forEach(new Action1<MovieAlternativeTitles>() {
            @Override
            public void call(MovieAlternativeTitles titles) {
                assertThat(titles).isNotNull();
                assertThat(titles.id).isEqualTo(TestData.MOVIE_ID);
                assertThat(titles.titles).isNotEmpty();
                assertThat(titles.titles.get(0).iso_3166_1).isEqualTo("PL");
                assertThat(titles.titles.get(0).title).isEqualTo("Podziemny krąg");
            }
        });
    }

    @Test
    public void test_credits() {
        Observable<Credits> credits = getManager().moviesService().credits(TestData.MOVIE_ID);

        credits.toBlocking().forEach(new Action1<Credits>() {
            @Override
            public void call(Credits credits) {
                assertThat(credits).isNotNull();
                assertThat(credits.id).isEqualTo(TestData.MOVIE_ID);
                assertThat(credits.cast).isNotEmpty();
                assertThat(credits.cast.get(0)).isNotNull();
                assertThat(credits.cast.get(0).name).isEqualTo("Edward Norton");
                assertThat(credits.crew).isNotEmpty();
            }
        });
    }

    @Test
    public void test_images() {
        Observable<Images> images = getManager().moviesService().images(TestData.MOVIE_ID, null);

        images.toBlocking().forEach(new Action1<Images>() {
            @Override
            public void call(Images images) {
                assertThat(images).isNotNull();
                assertThat(images.id).isEqualTo(TestData.MOVIE_ID);
                assertThat(images.backdrops).isNotEmpty();
                assertThat(images.backdrops.get(0).file_path).isNotEmpty();
                assertThat(images.backdrops.get(0).width).isEqualTo(1280);
                assertThat(images.backdrops.get(0).height).isEqualTo(720);
                assertThat(images.backdrops.get(0).iso_639_1).isEqualTo("en");
                assertThat(images.backdrops.get(0).aspect_ratio).isGreaterThan(1.7f);
                assertThat(images.backdrops.get(0).vote_average).isPositive();
                assertThat(images.backdrops.get(0).vote_count).isPositive();
                assertThat(images.posters).isNotEmpty();
                assertThat(images.posters.get(0).file_path).isNotEmpty();
                assertThat(images.posters.get(0).width).isEqualTo(1000);
                assertThat(images.posters.get(0).height).isEqualTo(1500);
                assertThat(images.posters.get(0).iso_639_1).hasSize(2);
                assertThat(images.posters.get(0).aspect_ratio).isGreaterThan(0.6f);
                assertThat(images.posters.get(0).vote_average).isPositive();
                assertThat(images.posters.get(0).vote_count).isPositive();
            }
        });
    }

    @Test
    public void test_keywords() {
        Observable<MovieKeywords> keywords = getManager().moviesService().keywords(TestData.MOVIE_ID);

        keywords.toBlocking().forEach(new Action1<MovieKeywords>() {
            @Override
            public void call(MovieKeywords keywords) {
                assertThat(keywords).isNotNull();
                assertThat(keywords.id).isEqualTo(TestData.MOVIE_ID);
                assertThat(keywords.keywords.get(0).id).isEqualTo(825);
                assertThat(keywords.keywords.get(0).name).isEqualTo("support group");
            }
        });
    }

    @Test
    public void test_releases() {
        Observable<Releases> releases = getManager().moviesService().releases(TestData.MOVIE_ID);

        releases.toBlocking().forEach(new Action1<Releases>() {
            @Override
            public void call(Releases releases) {
                assertThat(releases).isNotNull();
                assertThat(releases.id).isEqualTo(TestData.MOVIE_ID);
                assertThat(releases.countries.get(0).iso_3166_1).isEqualTo("US");
                assertThat(releases.countries.get(0).certification).isEqualTo("R");
                assertThat(releases.countries.get(0).release_date).isEqualTo("1999-10-14");
            }
        });
    }

    @Test
    public void test_videos() {
        Observable<Videos> videos = getManager().moviesService().videos(TestData.MOVIE_ID, null);

        videos.toBlocking().forEach(new Action1<Videos>() {
            @Override
            public void call(Videos videos) {
                assertThat(videos).isNotNull();
                assertThat(videos.id).isEqualTo(TestData.MOVIE_ID);
                assertThat(videos.results.get(0).id).isNotNull();
                assertThat(videos.results.get(0).iso_639_1).isNotNull();
                assertThat(videos.results.get(0).key).isNotNull();
                assertThat(videos.results.get(0).name).isNotNull();
                assertThat(videos.results.get(0).site).isEqualTo("YouTube");
                assertThat(videos.results.get(0).size).isNotNull();
                assertThat(videos.results.get(0).type).isEqualTo("Trailer");
            }
        });
    }

    @Test
    public void test_translations() {
        Observable<Translations> translations = getManager().moviesService().translations(TestData.MOVIE_ID, null);

        translations.toBlocking().forEach(new Action1<Translations>() {
            @Override
            public void call(Translations translations) {
                assertThat(translations).isNotNull();
                assertThat(translations.id).isEqualTo(TestData.MOVIE_ID);
                for (Translations.Translation translation : translations.translations) {
                    assertThat(translation.name).isNotNull();
                    assertThat(translation.iso_639_1).isNotNull();
                    assertThat(translation.english_name).isNotNull();
                }
            }
        });
    }

    @Test
    public void test_similar() {
        Observable<MovieResultsPage> results = getManager().moviesService().similar(TestData.MOVIE_ID, 3, null);

        results.toBlocking().forEach(new Action1<MovieResultsPage>() {
            @Override
            public void call(MovieResultsPage results) {
                assertThat(results).isNotNull();
                assertThat(results.page).isNotNull().isPositive();
                assertThat(results.total_pages).isNotNull().isPositive();
                assertThat(results.total_results).isNotNull().isPositive();
                assertThat(results.results).isNotEmpty();
                assertThat(results.results.get(0).adult).isEqualTo(false);
                assertThat(results.results.get(0).backdrop_path).isNotNull();
                assertThat(results.results.get(0).id).isNotNull().isPositive();
                assertThat(results.results.get(0).original_title).isNotNull();
                assertThat(results.results.get(0).release_date).isNotNull();
                assertThat(results.results.get(0).poster_path).isNotNull();
                assertThat(results.results.get(0).popularity).isNotNull().isPositive();
                assertThat(results.results.get(0).title).isNotNull();
                assertThat(results.results.get(0).vote_average).isNotNull().isPositive();
                assertThat(results.results.get(0).vote_count).isNotNull().isPositive();
            }
        });
    }

    @Test
    public void test_reviews() {
        Observable<ReviewResultsPage> results = getManager().moviesService().reviews(49026, 1, null);

        results.toBlocking().forEach(new Action1<ReviewResultsPage>() {
            @Override
            public void call(ReviewResultsPage results) {
                assertThat(results).isNotNull();
                assertThat(results.id).isNotNull();
                assertThat(results.page).isNotNull().isPositive();
                assertThat(results.total_pages).isNotNull().isPositive();
                assertThat(results.total_results).isNotNull().isPositive();
                assertThat(results.results).isNotEmpty();
                assertThat(results.results.get(0).id).isNotNull();
                assertThat(results.results.get(0).author).isNotNull();
                assertThat(results.results.get(0).content).isNotNull();
                assertThat(results.results.get(0).url).isNotNull();
            }
        });
    }

    @Test
    public void test_lists() {
        Observable<ListResultsPage> results = getManager().moviesService().lists(49026, 1, null);

        results.toBlocking().forEach(new Action1<ListResultsPage>() {
            @Override
            public void call(ListResultsPage results) {
                assertThat(results).isNotNull();
                assertThat(results.id).isNotNull();
                assertThat(results.page).isNotNull().isPositive();
                assertThat(results.total_pages).isNotNull().isPositive();
                assertThat(results.total_results).isNotNull().isPositive();
                assertThat(results.results).isNotEmpty();
                assertThat(results.results.get(0).id).isNotNull();
                assertThat(results.results.get(0).description).isNotNull();
                assertThat(results.results.get(0).favorite_count).isNotNull().isPositive();
                assertThat(results.results.get(0).item_count).isNotNull().isPositive();
                assertThat(results.results.get(0).iso_639_1).isNotNull();
                assertThat(results.results.get(0).name).isNotNull();
                assertThat(results.results.get(0).poster_path).isNotNull();
            }
        });
    }

    @Test
    public void test_latest() {
        Observable<Movie> movie = getManager().moviesService().latest();

        movie.toBlocking().forEach(new Action1<Movie>() {
            @Override
            public void call(Movie movie) {
                // Latest movie might not have a complete TMDb entry, but should at least some basic properties.
                assertThat(movie).isNotNull();
                assertThat(movie.id).isPositive();
                assertThat(movie.title).isNotEmpty();
            }
        });
    }

    @Test
    public void test_upcoming() {
        Observable<MovieResultsPage> page = getManager().moviesService().upcoming(null, null);

        page.toBlocking().forEach(new Action1<MovieResultsPage>() {
            @Override
            public void call(MovieResultsPage page) {
                assertThat(page).isNotNull();
                assertThat(page.results).isNotEmpty();
            }
        });
    }

    @Test
    public void test_nowPlaying() {
        Observable<MovieResultsPage> page = getManager().moviesService().nowPlaying(null, null);

        page.toBlocking().forEach(new Action1<MovieResultsPage>() {
            @Override
            public void call(MovieResultsPage page) {
                assertThat(page).isNotNull();
                assertThat(page.results).isNotEmpty();
            }
        });
    }

    @Test
    public void test_popular() {
        Observable<MovieResultsPage> page = getManager().moviesService().popular(null, null);

        page.toBlocking().forEach(new Action1<MovieResultsPage>() {
            @Override
            public void call(MovieResultsPage page) {
                assertThat(page).isNotNull();
                assertThat(page.results).isNotEmpty();
            }
        });
    }

    @Test
    public void test_topRated() {
        Observable<MovieResultsPage> page = getManager().moviesService().topRated(null, null);

        page.toBlocking().forEach(new Action1<MovieResultsPage>() {
            @Override
            public void call(MovieResultsPage page) {
                assertThat(page).isNotNull();
                assertThat(page.results).isNotEmpty();
            }
        });
    }

}
