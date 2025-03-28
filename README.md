# Microservices application with Spring Boot and MySQL

## Personal abilities used for this assignment

### Technical

- Java
- Spring
    - Spring Data JPA
    - Spring Validation
    - Spring Web
- Maven
- Documentation
- IntelliJ Idea
- MySQL Workbench
- Postman
- MySQL

### Soft

- Team work
- Problem solving
- Structuring
- Adaptability
- Creativity
- Presentation

## Service specific

### Movie ("/movies")

Syntax for creating/updating a movie

````json
{
  "title": "Movie title",
  "director": "Name of the director",
  "released": 1970,
  "genreId": 1
}
````

|            | Data type | Constraints                                  |
|------------|-----------|----------------------------------------------|
| `title`    | String    | Not null, Max 32 characters                  |
| `director` | String    | Not null, Max 64 characters                  |
| `released` | int       | Not null, Min 1900, Max 2050                 |
| `genreId`  | int       | Not null                                     |
|            |           | `title` and `released` paired must be unique |

| Command | Operation                              | Endpoint                      | Returns           |
|---------|----------------------------------------|-------------------------------|-------------------|
| POST    | Create a new movie                     |                               | `<Movie>`         |
| GET     | Get all movies                         |                               | `<List<Movie>`    |
|         | Get a movie by ID                      | `/{id}`                       | `<MovieResponse>` |
|         | Get movies by genre ID                 | `/genre/{genreId}`            | `<List<Movie>`    |
|         | Get movies by search query in title    | `/find/byTitle/{query}`       | `<List<Movie>`    |
|         | Get movies by search query in director | `/find/byDirector/{query}`    | `<List<Movie>`    |
|         | Get movies by search query in released | `/find/byReleaseYear/{query}` | `<List<Movie>`    |
| PUT     | Update a movie by ID                   | `/{id}`                       | `Movie`           |
| DELETE  | Delete a movie by ID                   | `/{id}`                       | `void`            |

## Project overview

### Assignment in short

A group project to build an application consisting of four separate micro services.
The project group:

- Fredrik
- Linda
- Ivana
- Madeleine

[Assignment in full (swedish)](https://docs.google.com/document/d/1_aow1u1tyVCmjrIphu-6xV89R8_6t66Eo0fTLQWAh7E/edit?tab=t.0#heading=h.h1riwnerwztv)

---

### Project description

A movie review application built with four separate microservices for handling movies, genres, reviews and users.
Spring Reactive WebClient is used to handle HTTP requests between the services. Each microservice has their own MySQL
database.

---

### Getting started

#### Preparing the MySQL database

Create a MySQL database for each service, for example movie_db, user_db_ review_db, genre_db.

#### Setting up environmental variables

Set up environmental variables in each service:

movie-service:
DB_URL - URL to the database. For example jdbc:mysql://localhost:3306/movie_db  
DB_USERNAME - Your server username  
DB_PASSWORD - Your server password
REVIEW_CLIENT_URL - http://localhost:8081
GENRE_CLIENT_URL - http://localhost:8085

Run each Java-application. They create the necessary tables and mock data for each service.

---

### Interacting with the API

Interacting is done using the endpoints below. When using POST commands the provided code syntax should be
entered in JSON format in the request body.

### Movie ("/movies")

Syntax for creating/updating a movie

````json
{
  "title": "Movie title",
  "director": "Name of the director",
  "released": 1970,
  "genreId": 1
}
````

|            | Data type | Constraints                                  |
|------------|-----------|----------------------------------------------|
| `title`    | String    | Not null, Max 32 characters                  |
| `director` | String    | Not null, Max 64 characters                  |
| `released` | int       | Not null, Min 1900, Max 2050                 |
| `genreId`  | int       | Not null                                     |
|            |           | `title` and `released` paired must be unique |

| Command | Operation                                | Endpoint                   | Returns                         |
|---------|------------------------------------------|----------------------------|---------------------------------|
| POST    | Create a new movie                       |                            | `ResponseEntity<Movie>`         |
| GET     | Get all movies                           |                            | `ResponseEntity<List<Movie>>`   |
|         | Get a movie by ID                        | `/{id}`                    | `ResponseEntity<MovieResponse>` |
|         | Get movies by genre ID                   | `/genre/{genreId}`         | `ResponseEntity<List<Movie>>`   |
|         | Get movies by a search query in title    | `/find/byTitle/{query}`    | `ResponseEntity<List<Movie>>`   |
|         | Get movies by a search query in director | `/find/byDirector/{query}` | `ResponseEntity<List<Movie>>`   |
|         | Get movies by a search query in released | `/find/byReleased/{query}` | `ResponseEntity<List<Movie>>`   |
| PUT     | Update a movie by ID                     | `/{id}`                    | `Movie`                         |
| DELETE  | Delete a movie by ID                     | `/{id}`                    | `void`                          |

### Genre ("/genres")

### Review ("/reviews")

### User ("/users")

## Project documentation

- [Jira board  ](https://fredande.atlassian.net/jira/software/projects/MAMSBOM/boards/37)
- [UML diagram on Lucid](https://lucid.app/lucidchart/26244de5-ae26-4119-a72d-2a5f4abbc9a2/edit?viewport_loc=-2493%2C-408%2C5589%2C3184%2C0_0&invitationId=inv_ada08cf4-4d1f-4876-b168-f5f9b75f7635)

---

