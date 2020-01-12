This service was developed to be deployed on a IoT gateway. 
Its purpose is to collect sensor data from multiple ESP8266 devices each one corresponding to a different team.

- Each team posts sensor data using the following endpoint:
`POST http://localhost:8382/api/students/{team_name}/sensors` </br>
Optional boolean query param: `isReady` </br>
Sample Request Payload:
```json
[
    {
        "name": "Temperature",
        "value": 30.08
    },
    {
        "name": "Humidity",
        "value": "70"
    },
    {
        "name": "Voltage",
        "value": "10"
    }
]
```
Expected Response: </br>
`HTTP/1.1 200 OK 

OK Team:{team_name}`
