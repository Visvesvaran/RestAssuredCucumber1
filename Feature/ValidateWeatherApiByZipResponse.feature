Feature: Validate the Weather API when you pass the ZIP in US Location 
	Description: The purpose of this feature is to test the Weather API Response
 
 
Scenario: Validate the Response when the Request is by Zip 
	Given Launch the API 
	
	Then The Response code is successful 
	
	Then The Response code if negative 
	
	Then Validate the temperature range 
	
	Then The City name is displayed as expected 
	Then The Key name is available in the response 
	Then The Key IsInvisible is not available in the response 
	
	Then Validate response time 
	Then Validate standard assertions using ResponseSpec