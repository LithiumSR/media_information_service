# Media Information Service (AKA MSI)

This is a university project that is intended to provide a RESTful service to obtain info about all type of media (Book,Film,Music and VideoGames).

## Getting Started


### Prerequisites

This application is written in Java and can be executed on Linux/Windows/Mac OS environment.
It requires the [Java Runtime Environment](https://www.java.com/it/download/) to be executed correctly, JRE 8 is preffered over JRE 9 because of some incompatibilities with Gradle.

MSI comes with a "logger feature" that requires [RabbitMQ](https://www.rabbitmq.com/download.html) and [Erlang](http://www.erlang.org/downloads) to be installed, if these requisites are not met this feature will not work.


### Build and execute from source

1. Download/clone this repo and make sure you have Maven and a Java Development Kit installed.
2. Open a terminal and reach the directory in which the repo is stored.
3. Write in the terminal ```mvn package``` and wait until the compilation process is finished. At the end you will find a "target" folder in the directory, inside you can find a WAR file called Media_Information_Service-0.x-x.jar that contains everything that is needed to execute MSI.
4. Before you execute the JAR you need to place a "redacted_api.cfg" files in the same directory of the executable. This file must contain the api keys that are needed by MSI to calls 3rd party services to get media data.
5. Now MSI is ready to be deployed. To execute just type ```java -jar ./Media_Information_Service-0.x.x.jar```

### How to use
There are two ways to use MSI. The easiest one is to use the rather simplistic website that you can access on localhost:8080 by default. The website will enable you to search for all type of media or alternatively you will be able to scan you "media" folder in Google Drive or Dropbox.

If you don't want to use the website you can still make rest calls on the endpoints that are going to be explained below.

### Download
- [Executable WAR File](https://www.dropbox.com/s/0t5k2j2nd0fqppd/Media_Information_Service.jar?dl=0)
	
### Issues
- Not that I know


## Endpoints
Endpoints are described [here](https://github.com/LithiumSR/media_information_service/blob/master/ENDPOINTS.md).


## Technology

### REST services used by MSI:
- [Google Books](https://developers.google.com/books/)
- [The Movie Database](https://www.themoviedb.org/)
- [Discogs](https://www.discogs.com/developers/)
- [The Internet Games Database](https://api.igdb.com/)
- [Dropbox](https://www.dropbox.com/developers)
- [Google Drive](https://developers.google.com/drive/)

### Built with:
* [Spring Framework ](https://projects.spring.io/spring-framework/) - Web Framework 
* [Thymeleaf](http://www.thymeleaf.org/) - Java XML/XHTML/HTML5 template engine
* [RabbitMQ](https://www.rabbitmq.com/download.html) - Message broker
* [Unirest](http://unirest.io/) - HTTP Request Client
* [Jersey](https://jersey.github.io/) - RESTful Web Services framework
* [Google GSON](https://github.com/google/gson) - Serialization/deserialization library to convert Java Objects into JSON and back
* [Android JSON](https://developer.android.com/reference/org/json/package-summary.html) - Json parser
* [Dropbox Java Core SDK](https://github.com/dropbox/dropbox-sdk-java) - Client for Dropbox API V2
* [Maven](https://maven.apache.org/) - Dependency Management