package com.uwetrottmann.tmdb.services;

import com.uwetrottmann.tmdb.BaseTestCase;
import com.uwetrottmann.tmdb.entities.Configuration;

import org.junit.Test;

import rx.Observable;
import rx.functions.Action1;

import static org.assertj.core.api.Assertions.assertThat;

public class ConfigurationServiceTest extends BaseTestCase {

    @Test
    public void test_configuration() {
        Observable<Configuration> config = getManager().configurationService().configuration();

        config.toBlocking().forEach(new Action1<Configuration>() {
            @Override
            public void call(Configuration config) {
                assertThat(config).isNotNull();
                assertThat(config.images).isNotNull();
                assertThat(config.images.base_url).isNotEmpty();
                assertThat(config.images.secure_base_url).isNotEmpty();
                assertThat(config.images.poster_sizes).isNotEmpty();
                assertThat(config.images.backdrop_sizes).isNotEmpty();
                assertThat(config.images.profile_sizes).isNotEmpty();
                assertThat(config.images.logo_sizes).isNotEmpty();
                assertThat(config.change_keys).isNotEmpty();
            }
        });
    }
}
