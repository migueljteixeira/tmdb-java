package com.uwetrottmann.tmdb.services;

import com.uwetrottmann.tmdb.BaseTestCase;
import com.uwetrottmann.tmdb.TestData;
import com.uwetrottmann.tmdb.entities.BaseResultsPage;
import com.uwetrottmann.tmdb.entities.CollectionResultsPage;
import com.uwetrottmann.tmdb.entities.CompanyResultsPage;
import com.uwetrottmann.tmdb.entities.KeywordResultsPage;
import com.uwetrottmann.tmdb.entities.Media;
import com.uwetrottmann.tmdb.entities.MovieResultsPage;
import com.uwetrottmann.tmdb.entities.PersonResultsPage;
import com.uwetrottmann.tmdb.entities.TvResultsPage;

import org.junit.Test;

import java.text.ParseException;

import rx.Observable;
import rx.functions.Action1;

import static org.assertj.core.api.Assertions.assertThat;

public class SearchServiceTest extends BaseTestCase {

    @Test
    public void test_companySearch() throws ParseException {
        Observable<CompanyResultsPage> results = getManager().searchService().company("Sony Pictures", null);

        results.toBlocking().forEach(new Action1<CompanyResultsPage>() {
            @Override
            public void call(CompanyResultsPage results) {
                assertResultsPage(results);
                assertThat(results.results).isNotEmpty();
                assertThat(results.results.get(0).id).isNotNull();
                assertThat(results.results.get(0)).isNotNull();
                assertThat(results.results.get(0).logo_path).isNotNull();
            }
        });
    }

    @Test
    public void test_collectionSearch() throws ParseException {
        Observable<CollectionResultsPage> results = getManager().searchService()
                .collection("The Avengers Collection", null, null);

        results.toBlocking().forEach(new Action1<CollectionResultsPage>() {
            @Override
            public void call(CollectionResultsPage results) {
                assertResultsPage(results);
                assertThat(results.results).isNotEmpty();
                assertThat(results.results.get(0).id).isNotNull();
                assertThat(results.results.get(0).backdrop_path).isNotNull();
                assertThat(results.results.get(0).name).isNotNull();
                assertThat(results.results.get(0).poster_path).isNotNull();
            }
        });
    }

    @Test
    public void test_keywordSearch() throws ParseException {
        Observable<KeywordResultsPage> results = getManager().searchService().keyword("fight", null);

        results.toBlocking().forEach(new Action1<KeywordResultsPage>() {
            @Override
            public void call(KeywordResultsPage results) {
                assertResultsPage(results);
                assertThat(results.results).isNotEmpty();
                assertThat(results.results.get(0).id).isNotNull();
                assertThat(results.results.get(0).name).isNotNull();
            }
        });
    }

    @Test
    public void test_movieSearch() throws ParseException {
        Observable<MovieResultsPage> results = getManager().searchService()
                .movie(TestData.MOVIE_TITLE, null, null, null, null, null, null);

        results.toBlocking().forEach(new Action1<MovieResultsPage>() {
            @Override
            public void call(MovieResultsPage results) {
                assertResultsPage(results);
                assertThat(results.results).isNotEmpty();
            }
        });
    }

    @Test
    public void test_personSearch() throws ParseException {
        Observable<PersonResultsPage> results = getManager().searchService().person(TestData.PERSON_NAME, null, null, null);

        results.toBlocking().forEach(new Action1<PersonResultsPage>() {
            @Override
            public void call(PersonResultsPage results) {
                assertResultsPage(results);
                assertThat(results.results.get(0).id).isNotNull();
                assertThat(results.results.get(0).name).isNotNull();
                assertThat(results.results.get(0).popularity).isNotNull();
                assertThat(results.results.get(0).profile_path).isNotNull();
                assertThat(results.results.get(0).adult).isNotNull();

                for (Media media : results.results.get(0).known_for) {
                    assertThat(media.adult).isNotNull();
                    assertThat(media.backdrop_path).isNotNull();
                    assertThat(media.id).isNotNull();
                    assertThat(media.original_title).isNotNull();
                    assertThat(media.release_date).isNotNull();
                    assertThat(media.poster_path).isNotNull();
                    assertThat(media.popularity).isNotNull().isGreaterThan(0);
                    assertThat(media.title).isNotNull();
                    assertThat(media.vote_average).isNotNull().isGreaterThan(0);
                    assertThat(media.vote_count).isNotNull().isGreaterThan(0);
                    assertThat(media.media_type).isNotNull();
                }
            }
        });
    }

    @Test
    public void test_tv() {
        Observable<TvResultsPage> results = getManager().searchService().tv(TestData.TVSHOW_TITLE, null, null, null, null);

        results.toBlocking().forEach(new Action1<TvResultsPage>() {
            @Override
            public void call(TvResultsPage results) {
                assertResultsPage(results);
                assertThat(results.results).isNotEmpty();
                assertThat(results.results.get(0).name).isEqualTo(TestData.TVSHOW_TITLE);
            }
        });
    }

    private void assertResultsPage(BaseResultsPage results) {
        assertThat(results.page).isPositive();
        assertThat(results.total_pages).isPositive();
        assertThat(results.total_results).isPositive();
    }

}
