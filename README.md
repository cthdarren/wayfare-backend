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

 POST ```/api/auth/login```  

> API for logging a user in.   

✅ **Accepts** 
*application/json* with parameters ``username`` and ``password``

↩️ **Returns** 
*application/json* with parameters ``success`` and ``data`` which is the jwt token associated with the user  
or "Invalid Credentials" if false.

<br /><br />

POST ```/api/auth/register```  
  
> API for registering a new user.
  
✅ **Accepts**
*application/json* with parameters ``username``, ``firstName``, ``lastName``, ``password``, ``verifyPassword``, ``email``, ``phoneNumber``

↩️ **Returns**
*application/json* with parameters ``success`` and ``data`` which is the jwt token associated with the user  
or "Invalid Credentials" if false.

<br /><br />

GET ```/api/auth/verify```  
  
> API for verifying a user.
  
✅ **Accepts**
Bearer token to authenticate user in the Authorization headers.

↩️ **Returns**
*application/json* with parameters ``success`` and ``data`` which is username of the newly verified account.

<br /><br />

POST ```/api/auth/wayfarersignup```  
  
> API for making a regular user into a WayFarer.

✅ **Accepts**
Accepts *application/json* with parameters ``username`` and ``password``

↩️ **Returns**
*application/json* with parameters ``success`` and ``data`` with string "you are now a wayfarer!".

<br /><br />

GET ```/api/auth/generateVerifyLink```  
  
> API for generating a one time verification link for the user

✅ **Accepts**
Bearer token to authenticate user in the Authorization headers.

↩️ **Returns**
*application/json* with parameters ``success`` and ``data`` containing the email that the link was sent to.

<br /><br />

GET ```/api/auth/verify/{GUID}```  
  
> API verifying a users email. Upon visiting, the link expires and the user is verified

↩️ **Returns**
*application/json* with parameters ``success`` and ``data`` containing the username of the user that was just verified.

