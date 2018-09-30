Feature: Validate the Weather API when you pass the ZIP in US Location 
	Description: The purpose of this feature is to test the Weather API Request
 
 
Scenario: Validate the Request when the Request is by Zip 
	Given Launch the API 
	
	Then Validate Invalid Request Header
	Then Validate Bad Request
	Then Validate Bad AppId
	Then Validate UnAuthorization