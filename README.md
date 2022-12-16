# Video-upload-Streaming-microservices-system-with-docker
Microservices system for uploading and streaming videos, deployed using Docker containers

Containerization assignment
Done by:
Ramzi Alyahya

The microservices communicate on the network using HTTP requests and JDBC. Each microservice has a Dockerfile which creates an image with the smallest size possible and the system is run with a docker compose file which builds each image if they haven’t already been built, and local directories within the project directory have been bound to the appropriate images for data persistence (MySQL and file system service).

The project file and technologies used for each service:

![image](https://user-images.githubusercontent.com/81943021/208061030-f4f00a6a-57ea-4a72-a106-d6f32d0fa238.png)
![image](https://user-images.githubusercontent.com/81943021/208061065-72c04bb9-22e7-413a-a893-9f0e3887ac3f.png)



Note about Dockerfiles:

The Dockerfiles for each microservice have a reduced image size by either using a lightweight base image or multistage build.

Services:

1) MySQL-service (port 3306:3306):

Basic MySQL image pulled from docker hub assigned to its default port of 3306, with password=123456, and the schema being defined by a sql script called “start-mysql.sql” that’s copied into
a folder in the container which runs this script on startup

Dockerfile and “start-mysql.sql” script:

![image](https://user-images.githubusercontent.com/81943021/208061333-34c464be-a695-48d4-8b79-fdb66bbcfd2e.png)
![image](https://user-images.githubusercontent.com/81943021/208061344-f5146d9b-a600-4b78-acdc-dda0b544e1b0.png)


2) Authentication-service(port:8082):

Simple Spring boot application with maven. It has a RestController endpoint “/authenticate” which handles post requests and returns a Boolean true if the user details it received in the http request are (username = “username”, password = “password”).

Dockerfile:

![image](https://user-images.githubusercontent.com/81943021/208061431-c398e722-1f25-4885-9f42-7292e3d4294c.png)

Uses the lightweight base image openjdk:8-jre-alpine to create an image with relatively small size.


3) Video-storage-service (port 8084:8084):

Spring boot application with JPA repository and maven. It uses an internal H2 database which stores its database files in a file in local directory “. /tmp/videoDB”, which is the directory that gets bound to a volume in the docker compose file to ensure data persistence.
It has a RestController that’s mapped to “/videoDB”, the controller has two endpoints:

A) Post endpoint (“”):
for handling requests to store video files into its database,
it receives the file as a multipart file and stores it in the database.

B) Get endpoint(“{name}”):
It receives the video file name as a path variable and uses it to query its database for the video and returns it in a ResponseEntity object.
A limit is set for the max size of the received video file of 50 mb,
Which is set in the application.properties file of the project.
It uses an interface called “VideoRepo” which extends JpaRepository to create and access the H2 database.

Dockerfile:

![image](https://user-images.githubusercontent.com/81943021/208061641-bce60d9f-45f5-407d-ab27-d08b14c943ea.png)

Uses the lightweight base image openjdk:8-jre-alpine to create an image with relatively small size.


4) Upload-service (port 8081:8080):

Spring mvc application with jsp pages for frontend, jdbc, and maven. It uses jdbc api to make queries to the mysql database implementing the DAO pattern in VideoDataDAO class.

Has two jsp pages:
• login.jsp: the login page.

• video-submission.jsp: has a form which the user fills out with the video file and name for upload.
Has two controllers:
• LoginController(“/login”):
Uses a single endpoint for get and post requests.
It returns a login page (login.jsp) asking for user’s credentials and sends them in a post request to the authentication microservice, if the user’s credentials are authentic then it’ll redirect to the video submission page (video-submission.jsp), otherwise it’ll refresh the login page.

• UploadController(“submit-video”):
Uses a single endpoint for get and post request.
The (video-submission.jsp) page prompts the user to upload a video file and video name in a form tag which when triggered makes a post request with the data input from the user to the “submit-video” endpoint which handles it as a multipart file.

The controller’s Post mapping does two things:
• stores the video name and pathURL of the video to the mysql microservice database using videoDataDAO object’s methods.
• Sends the video file to video storage microservice as a multipart file in an http request using RestTemplate API.

Dockerfile:

![image](https://user-images.githubusercontent.com/81943021/208061911-1de6139b-6194-4963-b37f-64706fcfe9f1.png)

Uses lightweight tomcat:9.0.8-jre8-alpine as a base image for this war application.

5) Stream-service (port 8085:8085):

Nodejs application which uses html pages for frontend and Nodejs in the backend.
It plays videos by sending http request to video storage service, downloading the video on local storage, then playing it in browser with <video> tag.

Has two html pages:
• index.html: the login page.
• stream.html: displays list of videos to choose from and the video itself once a video’s been chosen.

Has three endpoints:

• post(“/auth”):
Receives user credentials from the login form in the index.html page and sends them in a post request to the authentication microservice, if the user’s credentials are
authentic then it’ll redirect to the video streaming page (stream.html), otherwise it’ll refresh the login page.

• get(“/getvideosdata”):
Accesses mysql microservice’s database and makes a query for all the videodata(videoname, pathURL) stored there, it then stores the result in the session as a session attribute called “videosData”, and also returns the result as a response.

• get(“/videos/:name”):
It receives the video name as a path variable “:name” and uses it to get the pathURL from the “videosData” variable stored in the session.
It then uses this pathURL to send a get request to the video storage microservice to get the video, it then stores the video file in a local directory where the frontend can access it (the stream.js javascript code makes changes to the src attribute in the stream.html file to point to the downloaded video file in local directory) so the user can watch the requested video on the “stream.html” page.

Dockerfile:

![image](https://user-images.githubusercontent.com/81943021/208062045-8d7b9bf8-eb83-4c06-8ad9-dc12bb92f21a.png)

Uses Multistage build with base image node:14 then the lightweight node:alpine to reduce the file size by a large margin, just using node:14 as base image creates an image size of 963mb, this multistage build creates an image of size 189mb which’s a large improvement.

Docker compose file:

![image](https://user-images.githubusercontent.com/81943021/208062082-f78df545-1ef0-497a-a851-466bc286d519.png)

The docker compose file builds each image if they haven’t already been built by linking the location of each Dockerfile in the “build: “ portion of each service.
It also assigns local directories as volumes that some microservices use.
