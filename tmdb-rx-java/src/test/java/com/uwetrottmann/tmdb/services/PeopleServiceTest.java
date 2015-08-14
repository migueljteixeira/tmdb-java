package com.uwetrottmann.tmdb.services;

import com.uwetrottmann.tmdb.BaseTestCase;
import com.uwetrottmann.tmdb.TestData;
import com.uwetrottmann.tmdb.entities.Image;
import com.uwetrottmann.tmdb.entities.Media;
import com.uwetrottmann.tmdb.entities.Person;
import com.uwetrottmann.tmdb.entities.PersonCastCredit;
import com.uwetrottmann.tmdb.entities.PersonCredits;
import com.uwetrottmann.tmdb.entities.PersonCrewCredit;
import com.uwetrottmann.tmdb.entities.PersonIds;
import com.uwetrottmann.tmdb.entities.PersonImages;
import com.uwetrottmann.tmdb.entities.PersonResultsPage;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import rx.Observable;
import rx.functions.Action1;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class PeopleServiceTest extends BaseTestCase {

    private static final SimpleDateFormat JSON_STRING_DATE = new SimpleDateFormat("yyy-MM-dd");

    @Test
    public void test_summary() throws ParseException {
        Observable<Person> person = getManager().personService().summary(TestData.PERSON_ID);

        person.toBlocking().forEach(new Action1<Person>() {
            @Override
            public void call(Person person) {
                assertNotNull("Result was null.", person);
                assertEquals("Person name does not match.", "Brad Pitt", person.name);
                assertNotNull("Person homepage was null", person.homepage);
                assertNotNull("Person TMDB ID was null.", person.id);
                assertNotNull("Person biography was null.", person.biography);
                assertNull("Person deathday does not match", person.deathday);
                assertNotNull("Person place of birth does not match", person.place_of_birth);
                assertNotNull("Movie profile path was null.", person.profile_path);

                try {
                    assertEquals("Person birthday does not match.", JSON_STRING_DATE.parse("1963-12-18"),
                            person.birthday);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Test
    public void test_movie_credits() {
        Observable<PersonCredits> credits = getManager().personService().movieCredits(TestData.PERSON_ID, null);

        credits.toBlocking().forEach(new Action1<PersonCredits>() {
            @Override
            public void call(PersonCredits credits) {
                assertThat(credits.id).isEqualTo(TestData.PERSON_ID);
                assertCastCredits(credits, false);
                assertCrewCredits(credits, false);

                for (PersonCastCredit credit : credits.cast) {
                    assertThat(credit.title).isNotEmpty();
                }
            }
        });
    }

    @Test
    public void test_tv_credits() {
        Observable<PersonCredits> credits = getManager().personService().tvCredits(TestData.PERSON_ID, null);

        credits.toBlocking().forEach(new Action1<PersonCredits>() {
            @Override
            public void call(PersonCredits credits) {
                assertThat(credits.id).isEqualTo(TestData.PERSON_ID);
                assertCastCredits(credits, false);

                for (PersonCastCredit credit : credits.cast) {
                    assertThat(credit.episode_count).isGreaterThanOrEqualTo(0);
                    assertThat(credit.name).isNotEmpty();
                }
            }
        });
    }

    @Test
    public void test_combined_credits() {
        Observable<PersonCredits> credits = getManager().personService().combinedCredits(TestData.PERSON_ID, null);

        credits.toBlocking().forEach(new Action1<PersonCredits>() {
            @Override
            public void call(PersonCredits credits) {
                assertThat(credits.id).isEqualTo(TestData.PERSON_ID);
                assertCastCredits(credits, true);
                assertCrewCredits(credits, true);
            }
        });
    }

    @Test
    public void test_external_ids() {
        Observable<PersonIds> ids = getManager().personService().externalIds(TestData.PERSON_ID);

        ids.toBlocking().forEach(new Action1<PersonIds>() {
            @Override
            public void call(PersonIds ids) {
                assertThat(ids.id).isEqualTo(TestData.PERSON_ID);
                assertEquals("Person IMDB ID was null.", "nm0000093", ids.imdb_id);
                assertEquals("Person FREEBASE MID was null.", "/m/0c6qh", ids.freebase_mid);
                assertEquals("Person FREEBASE ID was null.", "/en/brad_pitt", ids.freebase_id);
                assertThat(ids.tvrage_id).isEqualTo(59436);

            }
        });
    }

    @Test
    public void test_images() {
        Observable<PersonImages> images = getManager().personService().images(TestData.PERSON_ID);

        images.toBlocking().forEach(new Action1<PersonImages>() {
            @Override
            public void call(PersonImages images) {
                assertThat(images.id).isEqualTo(TestData.PERSON_ID);

                for (Image image : images.profiles) {
                    assertThat(image.file_path).isNotEmpty();
                    assertThat(image.width).isNotNull();
                    assertThat(image.height).isNotNull();
                    assertThat(image.aspect_ratio).isGreaterThan(0);
                }
            }
        });
    }

    @Test
    public void test_popular() {
        Observable<PersonResultsPage> popular = getManager().personService().popular(null);

        popular.toBlocking().forEach(new Action1<PersonResultsPage>() {
            @Override
            public void call(PersonResultsPage popular) {
                assertThat(popular.results.get(0).id).isNotNull();
                assertThat(popular.results.get(0).name).isNotNull();
                assertThat(popular.results.get(0).popularity).isNotNull();
                assertThat(popular.results.get(0).profile_path).isNotNull();
                assertThat(popular.results.get(0).adult).isNotNull();

                for (Media media : popular.results.get(0).known_for) {
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
    public void test_latest() throws ParseException {
        Observable<Person> person = getManager().personService().latest();

        person.toBlocking().forEach(new Action1<Person>() {
            @Override
            public void call(Person person) {
                // Latest person might not have a complete TMDb entry, but at should least some basic properties.
                assertThat(person).isNotNull();
                assertThat(person.name).isNotNull();
                assertThat(person.id).isNotNull();
            }
        });
    }

    private void assertCastCredits(PersonCredits credits, boolean hasMediaType) {
        // assert cast credits
        assertThat(credits.cast).isNotEmpty();
        for (PersonCastCredit credit : credits.cast) {
            assertThat(credit.character).isNotNull(); // may be empty

            if (hasMediaType) {
                assertThat(credit.media_type).isNotEmpty();
            }
        }
    }

    private void assertCrewCredits(PersonCredits credits, boolean hasMediaType) {
        // assert crew credits
        assertThat(credits.crew).isNotEmpty();
        for (PersonCrewCredit credit : credits.crew) {
            // may be empty, but should exist
            assertThat(credit.department).isNotNull();
            assertThat(credit.job).isNotNull();

            if (hasMediaType) {
                assertThat(credit.media_type).isNotEmpty();
            }
        }
    }

}
