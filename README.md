# Converter: PDF to MP3

![GitHub commit activity](https://img.shields.io/github/commit-activity/m/he1ex-tG/PdfReader?logo=GitHub) ![GitHub last commit](https://img.shields.io/github/last-commit/he1ex-tG/PdfReader?logo=GitHub) ![GitHub issues](https://img.shields.io/github/issues/he1ex-tG/PdfReader?logo=GitHub) ![GitHub pull requests](https://img.shields.io/github/issues-pr/he1ex-tG/PdfReader?logo=GitHub) ![GitHub](https://img.shields.io/github/license/he1ex-tG/PdfReader?logo=GitHub)

## Note
__Converter__ is an educational project aimed at gaining practical experience in
working with the Spring Framework, designing and developing multi-component 
systems.

## About what

This project is a service developed to convert PDF files to MP3 audio format.
At the heart of the project is essentially a trivial task. But as mentioned
above, this project is created for educational purposes. It consists of two 
components:

- [Frontend](#frontend) 
- [Backend](#backend)

You can get the conversion result using both the web interface and the 
REST API. It also provides the opportunity to store the converted files on 
the server. 

## Technologies used:

1. [Spring Boot](https://spring.io/projects/spring-boot)
2. [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
3. [Thymeleaf](https://www.thymeleaf.org/)
4. [Nicepage - For autogenerate html and css](https://nicepage.com/)
5. [H2 Database](https://www.h2database.com/html/main.html)
6. [ITextPDF](https://itextpdf.com/)
7. [FreeTTS](https://freetts.sourceforge.io/)
8. [Lame](https://lame.sourceforge.io/)

## TODO

- [x] WAV to MP3 conversion
  - [ ] Support male/female voices 
- [x] Using database instead of storing MP3 in files
  - [x] H2 
  - [ ] PostgresSQL 
- [x] Authorization and authentication (Spring Security)
    - [x] Users authorization via form login
    - [ ] Access to admin API via JWT
    - [ ] Add the corresponding REST API
    - [x] Rework database structure
- [ ] __Rework project structure: split into modules__
  - [ ] __UI__
  - [ ] __Converter API__
  - [ ] __Database API__
- [ ] Exceptions handling
- [ ] Tests
- [ ] README.md ^_^

## Frontend

The main page looks like this:

!!! ДОБАВИТЬ ЧТО-ТО ТИПА "КАРТИНКА ДЛЯ КРАСОТЫ"
[Screenshot here]

As you can see, the interface is quite simple and straightforward. The PDF 
file is selected via a dialog box. The conversion process starts after 
pressing the "Convert" button. The produced MP3 files are displayed in the 
list below. Each file can be played on the page or downloaded to local 
storage.

I don't really enjoy creating UI, so I used [Nicepage](https://nicepage.com/) 
to create the pages. By doing this, I visualized the process of creating html 
pages and saved myself from having to manually write styles. Additionally, 
NicePage creates suitable styles for correct display of http pages on mobile 
devices, tablets, etc.

To process the results of requests to the REST API and display them on the 
page, I used [Thymeleaf](https://www.thymeleaf.org/): displaying a list of 
files and automatically creating links.

## Backend

The entire Backend is written in Kotlin. It can be divided into several main 
parts: [API](#1-api), [file storage](#2-file-storage), [converter](#3-converter).

### 1. API

The API is built using the features provided by [Spring Boot](https://spring.io/projects/spring-boot). 
It provides some endpoints that can be used by third party services:

| __Method__ | __Endpoint__      | __Description__                                           |
|------------|-------------------|-----------------------------------------------------------|
| GET        | /files            | Get a list of files uploaded by the user                  |
| GET        | /files/{fileName} | Download file                                             |
| POST       | /files            | Upload PDF, convert it and store on serverside            |
| POST       | /files/file       | Upload PDF, convert it and download result without saving |

****************************** Добавить про RestController

The API interacts with methods declared in the StorageHandler interface.

### 2. File storage

As mentioned above, work with file storage is carried out through the 
interface. This makes it possible to choose how files are stored. In the 
first edition, files were stored in a directory on the file system. Later, 
I added another implementation of the interface. The files are now stored in
the H2 database. Both implementations are currently available and can be 
used by specifying the appropriate __profile__: "filestorage" or "h2database".

The use of the second implementation (__profile__: "h2database") is
preferred because In addition to files, the database stores user data. 
This makes it possible to determine the ownership of files by a particular 
user, and is also necessary (in the future) for user authentication and
authorisation.

Interaction with [H2 Database](https://www.h2database.com/html/main.html) is 
implemented using [Spring Data JPA](https://spring.io/projects/spring-data-jpa):
the necessary entities are created, as well as repositories for working with 
these entities.

### 3. Converter

Converting a PDF file into text (array of bytes) is done using the 
[ITextPDF](https://itextpdf.com/) library. Then the text is converted to audio - 
the [FreeTTS](https://freetts.sourceforge.io/) library is used for this. 
Here is a small note: by default, FreeTTS does not provide the ability
output the audio stream as a ByteArrayInputStream or ByteArray, for example. 
Therefore, I made my own implementation of the AudioPlayer interface. 
This approach made it possible to abandon the use of intermediate saving of 
audio data to a file, as well as to hot convert from WAV to MP3 using 
[Lame](https://lame.sourceforge.io/). Finally, the MP3 audio stream is used to 
save or give to the user without saving.

## Weaknesses 

There are weaknesses in the project that hardly make sense to eliminate within
the of this work. Listed below are some of them:

- lack of confirmation of registration of new accounts. A malicious user can
create them endlessly, which will eventually lead to a database overflow;
