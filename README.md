**Pull requests (e.g. support for more API endpoints, bugfixes) against dev are welcome!**

tmdb-rx-java
============

A Java wrapper around the [TMDb v3 API][1] using [retrofit][2] with RxAndroid support.

Usage
-----
![Maven Central version](https://img.shields.io/maven-central/v/com.github.migueljteixeira/tmdb-rx-java.svg?style=flat-square)

Add the following dependency to your Gradle project:

```groovy
compile 'com.github.migueljteixeira:tmdb-rx-java:0.9.1'
```

or your Maven project:

```xml
<dependency>
    <groupId>com.github.migueljteixeira</groupId>
    <artifactId>tmdb-rx-java</artifactId>
    <version>0.9.1</version>
</dependency>
```

Additional binaries and dependency information for can be found at [http://search.maven.org](http://search.maven.org/#search%7Cga%7C1%7Ctmdb-rx-java).

Example
-------

```java
// Create an instance of the service you wish to use
// you can keep this around
Tmdb tmdb = new Tmdb();
tmdb.setApiKey("yourapikey");
MovieService movieService = tmdb.movieService();
//
// Call any of the available endpoints
movieService.summary(550)
    .subscribeOn(Schedulers.newThread())
    .observeOn(AndroidSchedulers.mainThread())
    .subscribe(/* an Observer */);
```

See test cases in `src/test/` for more examples.

License
-------

    Copyright 2015 Miguel Teixeira

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.




 [1]: http://docs.themoviedb.apiary.io/
 [2]: https://github.com/square/retrofit
 [3]: https://github.com/migueljteixeira/tmdb-rx-java/releases
