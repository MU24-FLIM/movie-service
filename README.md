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
---

## Service specific instructions

### Movie ("http://localhost:8080/movies")

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

----

## Project overview

### Assignment in short

A group project to build an application consisting of four separate micro services.
The project group:

- Fredrik: Movies
- Linda: Reviews
- Ivana: Users
- Madeleine: Genres

[Assignment in full (swedish)](https://docs.google.com/document/d/1_aow1u1tyVCmjrIphu-6xV89R8_6t66Eo0fTLQWAh7E/edit?tab=t.0#heading=h.h1riwnerwztv)

---

### Project description

A movie review application built with four separate microservices for handling movies, genres, reviews and users.
Spring Reactive WebClient is used to handle HTTP requests between the services. Each microservice has their own MySQL
database.

---

### Getting started

#### Preparing the MySQL database

Create a MySQL database for each service, for example movie_db, user_service_db, review_service_db, genre_db.

#### Setting up environmental variables

Set up environmental variables in each service:

movie-service:  
`DB_URL` - URL to the database. (jdbc:mysql://localhost:3306/movie_db)  
`DB_USER` - Your server username  
`DB_PASSWORD` - Your server password  
`REVIEW_CLIENT_URL` - http://localhost:8081  
`GENRE_CLIENT_URL` - http://localhost:8085  

user_service:  
`DB_URL` - URL to the database. (jdbc:mysql://localhost:3306/user_service_db)  
`DB_USER` - Your server username  
`DB_PASSWORD` - Your server password  
`REVIEW_CLIENT_URL` - http://localhost:8081  

review_service:  
`MYSQL_PASSWORD` - URL to the database. (jdbc:mysql://localhost:3306/review_service_db)  
`DB_USER` - Your server username  
`DB_PASSWORD` - Your server password  
`USER_CLIENT_URL` - http://localhost:8082  
`MOVIE_CLIENT_URL` - http://localhost:8080  

user_service:  
`DB_URL` - URL to the database. (jdbc:mysql://localhost:3306/genre_db)  
`DB_USER` - Your server username  
`DB_PASSWORD` - Your server password  
`WEB_CLIENT_URL` - http://localhost:8081  

Run each Java-application. They create the necessary tables and mock data for each service.

---

### Interacting with the API

Interacting is done using the endpoints below. When using POST and PUT commands the provided code syntax should be
entered in JSON format in the request body.


- ### Movie ("http://localhost:8080/movies")

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

- ### Genre ("http://localhost:8085/genre")

Syntax for creating/updating a genre
````json
{ 
        "name": "namnet på genre",
        "description": "beskrivning på genre"
    }
````

| COMMAND | ENDPOINTS            | OPERATIONER                                            |
|:--------|----------------------|:-------------------------------------------------------|
| Post    | /genre               | Lägg till en ny genre                                  |
| Put     | /genre/{id}          | Uppdatera en vald genre                                |
| Delete  | /genre({id}          | Tar bort en vald genre                                 |
| Get     | /genre               | Visa alla genres                                       |
| Get     | /genre/{id}          | Visa en specifik genre med ett id                      |
| Get     | /genre/{id}/movies   | Visa alla filmer under en gerne med ett valda gerne id |


- ### Review ("http://localhost:8081/reviews")

Syntax for creating/updating a review

````json
{
  "movieId": 27,
  "userId": 2,
  "title": "Fantastic",
  "content": "What a masterpiece! Great actors, good story.",
  "rating": 5
}
````

| CRUD   | Operation               | Endpoint                |
|--------|-------------------------|-------------------------|
| POST   | Create a new review     | `/reviews`              |
| GET    | Get all reviews         | `/reviews`              |
| GET    | Get review by id        | `/reviews/{id}`         |
| GET    | Get reviews of movie ID | `/reviews/{id}/reviews` |
| PUT    | Update review by ID     | `/reviews/{id}`         |
| DELETE | Delete a review by ID   | `/reviews/{id}`         |


- ### User ("http://localhost:8082/users")
#### Endpoints

This part of the task is intended for the `user_service` and his connection with the `review_service`.
User service has all CRUD operations implemented.
When `/users/id` is called, all reviews of that user are displayed.

#### Body for creating and updating user:
Syntax for creating/updating a review
```json
{
  "username": "ivana",
  "email": "ivana54@gmail.com",
  "age": 30
}
```

| HTTP Method  | URL                                | Descripton        |
|--------------|------------------------------------|-------------------|
| GET          | http:localhost:8080/users          | Get all Users     |
| GET          | http:localhost:8080/users/{userid} | Get User by ID    |
| POST         | http:localhost:8080/users          | Create new User   |
| PUT          | http:localhost:8080/users/{userid} | Update User by ID |
| DELETE       | http:localhost:8080/users/{userid} | Delete User by ID |


|            | DataType | Constraints                     |
|------------|----------|---------------------------------|
| `username` | String   | Not null, size: min: 2, max: 20 |
| `email`    | String   | Not null, size: min: 2, max: 30 |
| `age`      | Byte     | Not null                        |


## Project documentation

- [Jira board  ](https://fredande.atlassian.net/jira/software/projects/MAMSBOM/boards/37)
- [UML diagram on Lucid](https://lucid.app/lucidchart/26244de5-ae26-4119-a72d-2a5f4abbc9a2/edit?viewport_loc=-2493%2C-408%2C5589%2C3184%2C0_0&invitationId=inv_ada08cf4-4d1f-4876-b168-f5f9b75f7635)

---

