# Setup
```
git clone
```

```
cd wayfare-backend
```

### Switching to a different branch
```
git checkout branchname
```
## Filling in data into application.properties

- Copy and paste application.properties.example and rename it to application.properties
- Fill in the necessary information for the connection to the database.

# To Run
### Windows
```
./gradlew.bat bootRun
```

### MacOS/Linux
```
chmod +x ./gradlew
./gradlew bootRun
```



# API Calls


<br />

 POST ```/api/v1/auth/login```  

> API for logging a user in.   

✅ **Accepts** 
*application/json* with parameters ``username`` and ``password``

↩️ **Returns** 
*application/json* with parameters ``success`` and ``data`` which is the jwt token associated with the user  

<br /><br />

POST ```/api/v1/auth/register```  
  
> API for registering a new user.
  
✅ **Accepts**
*application/json* with parameters ``username``, ``firstName``, ``lastName``, ``password``, ``verifyPassword``, ``email``, ``phoneNumber``

↩️ **Returns**
*application/json* with parameters ``success`` and ``data`` which is the jwt token associated with the user  


<br /><br />

GET ```/api/v1/verify/{GUID}```  
  
> API verifying a users email. Upon visiting, the link expires and the user is verified

↩️ **Returns**
*application/json* with parameters ``success`` and ``data`` containing the username of the user that was just verified.

<br /><br />

GET ```/api/v1/review/{id}```  
  
> API for getting a review by it's id.

↩️ **Returns**
*application/json* with parameters ``success`` and ``data`` with the specified review data 

<br /><br />

POST ```/api/v1/listing/allreviews```  
  
> API getting all reviews from the specified tour listing.
  
✅ **Accepts**
*application/json* with the parameter ``listingId``

↩️ **Returns**
*application/json* with parameters ``success`` and ``data`` with the entire list of reviews for the specified listing. 

<br /><br />

POST ```/api/v1/listing/newest-five-reviews```  
  
> API getting the newest five reviews from the specified tour listing.
  
✅ **Accepts**
*application/json* with the parameter ``listingId``

↩️ **Returns**
*application/json* with parameters ``success`` and ``data`` with the newest five reviews for the specified listing. 

