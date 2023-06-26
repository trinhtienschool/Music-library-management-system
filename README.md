### Music Library Management System

The Music Library Management System is a Spring Boot restful API designed to manage a music library. It allows users to organize music tracks and playlists, search music tracks by title, artist, album, genre, keyword, search playlists by name, track title, artist name of track, album name of track, or track genre, and search tracks in a given playlist by conditions as above. Each user must register and login to add tracks to a playlist, so each playlist belongs to a user, and only the owner of the playlist can update or delete it. The system also manages albums and artists. Only admins can create, update, and delete tracks, albums, and artists, but all authenticated users can manage playlists. This README provides an overview of the project and instructions for setting up and running the Spring Boot application.

**Table of Contents**

[TOCM]

[TOC]

## Technologies Used

- Spring Boot: Java-based framework for creating restfull api.
- Java: Programming language used for developing the application.
- MySQL: Relational database management system for storing music track data.

## Getting Started

To run the Music Library Management System locally, follow the instructions below.

### Prerequisites

Before starting, ensure that you have the following software installed on your machine:

- Java Development Kit (JDK): Version 11 or above
- MySQL Database: Version 8.0 or above

### Installation and Setup

1.   Clone the repository to your local machine.
`git clone https://github.com/trinhtienschool/Music-library-management-system.git`

2.  Navigate to the project directory.
`cd Music-library-management-system`

3.  Configure the Database

	   - Create a new MySQL database named `music_library_management_system` for the application.
	   - Update the database connection details in the `application.properties` file located in the `src/main/resources` directory. Set the `spring.datasource.url`, `spring.datasource.username`, and `spring.datasource.password` properties according to your MySQL database configuration. 
	   - Take the username and password of Google App Passwords, and update the username, password into the `application.properites` file for configuring the send email feature.


4. Build the application using Maven.
`mvn clean install`

5.  Run the application.
`mvn spring-boot:run`

6. Test application.

	You can test endpoints using Postman, I also included the Postman Collections in the Postman folder, so you can import and test the application. 

