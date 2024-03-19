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




 POST ```/api/auth/login```  

> API for logging a user in.   

✅ **Accepts** 
*application/json* with parameters ``username`` and ``password``

↩️ **Returns** 
*application/json* with parameters ``success`` and ``data`` which is the jwt token associated with the user  
or "Invalid Credentials" if false.





POST ```/api/auth/register```  
  
> API for registering a new user.
  
✅ **Accepts**
*application/json* with parameters ``username``, ``firstName``, ``lastName``, ``password``, ``verifyPassword``, ``email``, ``phoneNumber``

↩️ **Returns**
*application/json* with parameters ``success`` and ``data`` which is the jwt token associated with the user  
or "Invalid Credentials" if false.

GET ```/api/auth/verify```  
  
> API for verifying a user.
  
✅ **Accepts**
Bearer token to authenticate user in the Authorization headers.

↩️ **Returns**
*application/json* with parameters ``success`` and ``data`` which is username of the newly verified account.



POST ```/api/auth/wayfarersignup```  
  
> API for making a regular user into a WayFarer.

✅ **Accepts**
Accepts *application/json* with parameters ``username`` and ``password``

↩️ **Returns**
*application/json* with parameters ``success`` and ``data`` with string "you are now a wayfarer!"
