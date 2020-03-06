The sample "file upload management API" is to demonstrate the next functionalities:

00
1. Uploading the file with the associated metadata.
The metadata is injected as a set of required/optional parameters, while the file is getting uploaded inside the POST HTTP request.
As HTTP POST is processed, the file mime-type is getting set by the standard HTTP mechanism of mime-type.

URL: /api/demo/uploadFile
Required parameters:
    publiAccess: true or false
	file :  MultipartFile file
Optional parameters: 
    fileDescription: file description	

2. Retrieving metadata for uploaded files.

There are two calls implemented for this type of request.

One is for retrieving the full list of known uploads (used mainly for debugging purposes).
URL: /api/demo/getFilesMetaDataList
Parameters: none

Another is to retrieve metadata for the known id of the uploaded file.
URL: /api/demo/getFileMetaData/{id}
Parameter: id is used as a path parameter.





3. Downloading a previously uploaded file.
This call returns the file with a proper HTTP mime-type set, making it suitable for the "content streaming".

URL: /api/demo/downloadFile/{id}
Parameter: id is used as a path parameter.

4.Searching for the file IDs and relevant metadata using a set of optional parameters.

URL: /api/demo/searchFileId?[fileName=value1]{&fileType=value2}{&addedBefore=value3}{&addedAfter=value4}
{&publicAccess=value5}{&sizeLarger=value6}{&sizeSmaller=value7}
All parameters are optional, but if no any parameter passed , than the method throws the custom Exception.
Parameters:
fileName is the original file name. Searching for the file names "like %fileName%"
fileType: mime-type
addedBefore: files uploaded before the date. Date format is YYYY-MM-DD
addedAfter: files uploaded after the date. Date format is YYYY-MM-DD
publicAccess: true or false
sizeLarger : file size is larger then (size in bytes)
sizeSmaller : file size is smaller then (size in bytes)
----------------------------

The application uses H2 in-memory database.
The table name is "metadata".
For initial testing, several records with metadata are inserted into the table each time the application is restarted.
When testing / debugging is complete, the following code MUST BE REMOVED from schema.sql:

INSERT INTO metadata (filename, filedescription, filetype, uploaded_ts, publicAccess, size ) VALUES
  ('aee.txt', 'text', 'text/plain', CURRENT_TIMESTAMP(),  true,2345),
  ('rrb.jpg', 'picture', 'image/jpeg',CURRENT_TIMESTAMP(), false, 567),
  ('uttt.png', 'image', 'image/png',CURRENT_TIMESTAMP(),  true, 234556);

----------------------------

In addition, the code for the cron-like scheduling was added to identify the upload performed over the last hour .
It sends a summary email to the specified email address.
The sample code does not include the "live credentials" and has to be configured wit the originating SMTP credentials to work properly.

----------------------------
Custom Properties in the application.properies file to match the actual system settings after deployment:
app.pathToUpload=...
app.emailRecipientList=email1@gmail.com,email2@gmail.com
app.emailSender=senderemail@gmail.com



