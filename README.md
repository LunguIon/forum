## 🌟**What is SpeakUp?** 
SpeakUp is a dynamic web forum service built with SpringBoot, designed to foster community engagement and open discussions. Users can create, share, and comment on content across various topics, making it a versatile platform for conversations and content sharing.

## 📑 Table of Contents
- [Installation](#installation)
- [Usage](#usage)
- [Technologies used](#technologies)
- [Contributors](#collaborators)
- [License](#license)

## 🛠<span id="installation">Steps to set up the application</span>
1. Clone the repository:
```bash
 git clone https://github.com/LunguIon/forum
```

## 🚀<span id="usage">Steps to build the application</span>
1. Install dependencies:
```bash
 mvn clean install
```
2. Run the build:
```bash
 java -jar target/forum-0.0.1-SNAPSHOT.jar
```   

## 🔧 <span id="technologies">Technologies used</span>
- **Spring Boot** : Provides a rapid, convention-over-configuration solution for application setup and execution.
- **Java 17**: Modern version of Java, offering improved performance and security features.
- **Spring Data JPA & PostgreSQL**: Manage database interactions seamlessly, supporting the storage and retrieval of forum data.
- **Spring Security & OAuth2**: Ensure robust security by handling authentication and authorization.
- **JWT**: Securely handles user sessions and service requests.
- **Spring Web**: Facilitates request handling and response serving through RESTful web services.
- **Log4J2**: Offers logging capabilities for debugging and operational insights.
- **Springdoc OpenAPI**: Provides automated API documentation, useful for both development and consumption of the API.

## 👨‍💻 <span id="collaborators">Contributors</span>
1. [Ion Lungu](https://github.com/LunguIon)
   - Role: Implemented OAuth and JWT token-based authorization.

2. [Alexandr Melnic](https://github.com/Liebe001)
   - Role: Developed all entities and services in project.

3. [Jeleascov Tatiana](https://github.com/InCreating)
   - Role: Wrote all server unit tests and worked on documentation.


## ⚖️ <span id="license">License</span>
