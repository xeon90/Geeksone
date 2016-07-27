# Geeksone Library
###### Simplified JSON Based REST Client for Dummies. You get result, we handle the rest

# Features
* Totally REUSEABLE, 1 static Geeksone
* Support GET, POST, PUT, DELET with Request Body (Raw String)
* Support basic authentication, with username and password
* Support header lists
* Auto Pilot Mode, we handle the basic error and prompt the dialog, and also progress dialog. Programmers can easier to maintain their app logic and have cleaner code.

# Next Release 0.3.0
* Support POST form data
* Multi-Geeksone, perform multiple REST with multiple Geeksone and multiple listener. (Not yet confirm)

#Change Log
## Version v0.2.0

>### Add Auto Pilot Mode
* Alert dialog for showing some simple message such as no internet and host unreachable.
* Progress dialog shown, when perform HTTP action
* Support custom error message
* Support set custom progress dialog and alert dialog

>### Library Changes
* Simplify library, is enough for Dummies :p
* Method [getClazz] support GSON Type/TypeToken 
`Type t = new TypeToken<List<String>>(){}.getType();`
* Fix logic of 
* Code and Program flow clean up
* Remove redundant and no sense flow
* Rearrange package

---
##### Release date: 27-07-2016

## Version v0.1.x

>### Change Log
* Project Init
* I have no idea

---
##### Release date: May/June 2016