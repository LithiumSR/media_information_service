# Endpoints

## Generic search endpoint
* **URL**

  /search

* **Method:**
  `GET` 
  
*  **URL Params**
   **Required:**
 	` query=[String]` 
	` type=[Book|Film|Music|Game]` 

* **Success Response:**
  * **Code:** 200 <br />
    **Content:** It will return a JSON Object that follows the guidelines of a specific type of media.
	
	* **Error Response:**

  * **Code:** 400 Bad Request  <br />
    **Content:** `{
    "errorMessage": "Bad Request",
    "status": false
}`
OR
  * **Code:** 400 Bad Request  <br />
    **Content:** `{
    "errorMessage": "Illegal value for max_result",
    "status": false
}`
OR
 * **Code:** 400 Bad Request  <br />
    **Content:** `{
    "errorMessage": "Illegal type value",
    "status": false
}`
OR
  * **Code:** 500 Internal error <br />
    **Content:** `{
    "errorMessage": "Internal error",
    "status": false
}`

* **Sample Call:**
* Unirest.get(http://localhost:8080/search?type=game&query=Heroes%Of%The%Storm&max_result=all)

## Book search endpoint
* **URL**

  /book/search

* **Method:**
  `GET` 
  
*  **URL Params**
   **Required:**
 	` query=[String]` 
	` isbn=[String]` 
	(al least one of the two)
	
	**Optional:**
	` max=[relevance|newest]` 
	` orderBy=[relevance|newest]` 
	
* **Success Response:**
  * **Code:** 200 <br />
    **Content:** `[
    {
        "ISBN": Google ID,
        "author": "Author name",
        "publisher": "Publisher name",
        "releaseDate": "2018-12-13",
        "overview": "Description here",
        "title": "title here"
    }`
	
	* **Error Response:**

  * **Code:** 400 Bad Request  <br />
    **Content:** `{
    "errorMessage": "Bad Request",
    "status": false
}`
OR 
  * **Code:** 400 Bad Request  <br />
    **Content:** `{
    "errorMessage": "Illegal value for max_result",
    "status": false
}`
OR 
  * **Code:** 400 Bad Request  <br />
    **Content:** `{
    "errorMessage": "Name and isbn can not be both empty",
    "status": false
}`
OR	
  * **Code:** 500 Internal error <br />
    **Content:** `{
    "errorMessage": "Internal error",
    "status": false
}`
* **Sample Call:**
* Unirest.get(http://localhost:8080/book/search).header("query,"Harry Potter").header("max_result","all").header("orderBy","relevance)

## Film search endpoint
* **URL**

  /film/search

* **Method:**
  `GET` 
  
*  **URL Params**
   **Required:**
 	` query=[String]` 
	**Optional:**
	` max=[relevance|newest]` 
	` language=[ISO 639-1 value]` 
	
* **Success Response:**
  * **Code:** 200 <br />
    **Content:** `[
    {
        "overview": "Description here",
        "releaseDate": "2018-12-13",
        "vote": "9.5",
        "title": "Title here"
    }`
	
	* **Error Response:**

  * **Code:** 400 Bad Request  <br />
    **Content:** `{
    "errorMessage": "Bad Request",
    "status": false
}`
OR 
  * **Code:** 400 Bad Request  <br />
    **Content:** `{
    "errorMessage": "Illegal value for max_result",
    "status": false
}`
OR	
  * **Code:** 500 Internal error <br />
    **Content:** `{
    "errorMessage": "Internal error",
    "status": false
}`
* **Sample Call:**
* Unirest.get(http://localhost:8080/film/search).header("query,"Harry Potter").header("max_result","all").header("language","it")

## Music search endpoint
* **URL**

  /music/search

* **Method:**
  `GET` 
  
*  **URL Params**
   **Required:**
 	` query=[String]` 
	**Optional:**
	` max=[relevance|newest]` 
	` type=[File|MP3|Single]` 
	
* **Success Response:**
  * **Code:** 200 <br />
    **Content:** `[
    [
    {
        "labels": "Label here ",
        "genre": "Genres here",
        "releaseDate": "2018",
        "title": "Title here"
    }`
	
	* **Error Response:**

  * **Code:** 400 Bad Request  <br />
    **Content:** `{
    "errorMessage": "Bad Request",
    "status": false
}`
OR 
  * **Code:** 400 Bad Request  <br />
    **Content:** `{
    "errorMessage": "Illegal value for max_result",
    "status": false
}`
OR	
  * **Code:** 500 Internal error <br />
    **Content:** `{
    "errorMessage": "Internal error",
    "status": false
}`
* **Sample Call:**
* Unirest.get(http://localhost:8080/music/search).header("query,"Coldplay").header("max_result","all").header("type","FILE,MP3,Single")

## Game search endpoint
* **URL**

  /game/search

* **Method:**
  `GET` 
  
*  **URL Params**
   **Required:**
 	` query=[String]` 
	**Optional:**
	` max=[relevance|newest]` 
	` orderBy=[String]` 
	
* **Success Response:**
  * **Code:** 200 <br />
    **Content:** `[
    {
        "vote": "78.75",
        "overview": "Description here",
        "age_required": "12+",
        "webSite": "http://example.com",
        "releaseDate": "13/12/2018",
        "title": "Title here"
    }]`
	
	* **Error Response:**

  * **Code:** 400 Bad Request  <br />
    **Content:** `{
    "errorMessage": "Bad Request",
    "status": false
}`
OR 
  * **Code:** 400 Bad Request  <br />
    **Content:** `{
    "errorMessage": "Illegal value for max_result",
    "status": false
}`
OR	
  * **Code:** 500 Internal error <br />
    **Content:** `{
    "errorMessage": "Internal error",
    "status": false
}`
* **Sample Call:**
* Unirest.get(http://localhost:8080/game/search).header("query,"Heroes of the storm").header("max_result","all")