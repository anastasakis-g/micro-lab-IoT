This service was developed to be deployed on a IoT gateway. </br>
Its purpose is to collect sensor data from multiple ESP8266 devices each one corresponding to a different team.

Each team persists sensor data using the following endpoint:</br>
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
        "value": 70
    },
    {
        "name": "Voltage",
        "value": 10
    }
]
```
Expected Response: </br>
`HTTP/1.1 200 OK` </br>
`OK Team:{team_name}`
If a team doesn't exist and the team name is valid as described in `teams` List at `Constants.java`, a new record will be created in the `team` database table. 


</br>
</br>
</br>
If a team posted within the last 10 minutes then only their latest POST call will be displayed:
`GET http://localhost:8382/api/students` </br>

Sample Response:
```json
[
    {
        "id": 1,
        "name": "A1",
        "sensors": [
            {
                "id": 268,
                "name": "Temperature",
                "value": "22.0",
                "created_at": "2020-01-13T00:50:20.386"
            }
        ],
        "ready": false
    },
    {
        "id": 2,
        "name": "A3",
        "sensors": [
            {
                "id": 270,
                "name": "Temperature",
                "value": "22.0",
                "created_at": "2020-01-13T00:50:22.739"
            }
        ],
        "ready": false
    },
    {
        "id": 3,
        "name": "A5",
        "sensors": [
            {
                "id": 272,
                "name": "Temperature",
                "value": "22.0",
                "created_at": "2020-01-13T00:50:25.152"
            }
        ],
        "ready": false
    },
    {
        "id": 4,
        "name": "B2",
        "sensors": [
            {
                "id": 278,
                "name": "Temperature",
                "value": "22.0",
                "created_at": "2020-01-13T00:50:32.779"
            }
        ],
        "ready": false
    },
    {
        "id": 5,
        "name": "B4",
        "sensors": [
            {
                "id": 280,
                "name": "Temperature",
                "value": "22.0",
                "created_at": "2020-01-13T00:50:35.272"
            }
        ],
        "ready": false
    },
    {
        "id": 6,
        "name": "B6",
        "sensors": [
            {
                "id": 282,
                "name": "Temperature",
                "value": "22.0",
                "created_at": "2020-01-13T00:50:37.922"
            }
        ],
        "ready": true
    },
    {
        "id": 16,
        "name": "A2",
        "sensors": [
            {
                "id": 269,
                "name": "Temperature",
                "value": "22.0",
                "created_at": "2020-01-13T00:50:21.527"
            }
        ],
        "ready": false
    },
    {
        "id": 17,
        "name": "A6",
        "sensors": [
            {
                "id": 273,
                "name": "Temperature",
                "value": "22.0",
                "created_at": "2020-01-13T00:50:26.356"
            }
        ],
        "ready": false
    },
    {
        "id": 18,
        "name": "A8",
        "sensors": [
            {
                "id": 275,
                "name": "Temperature",
                "value": "22.0",
                "created_at": "2020-01-13T00:50:28.77"
            }
        ],
        "ready": false
    },
    {
        "id": 22,
        "name": "B1",
        "sensors": [
            {
                "id": 277,
                "name": "Temperature",
                "value": "22.0",
                "created_at": "2020-01-13T00:50:31.528"
            }
        ],
        "ready": true
    },
    {
        "id": 24,
        "name": "A4",
        "sensors": [
            {
                "id": 271,
                "name": "Temperature",
                "value": "22.0",
                "created_at": "2020-01-13T00:50:23.942"
            }
        ],
        "ready": false
    },
    {
        "id": 29,
        "name": "C4",
        "sensors": [
            {
                "id": 286,
                "name": "Temperature",
                "value": "30.08",
                "created_at": "2020-01-13T00:51:24.479"
            },
            {
                "id": 287,
                "name": "Humidity",
                "value": "70",
                "created_at": "2020-01-13T00:51:24.496"
            },
            {
                "id": 288,
                "name": "Voltage",
                "value": "10",
                "created_at": "2020-01-13T00:51:24.515"
            }
        ],
        "ready": true
    },
    {
        "id": 31,
        "name": "A7",
        "sensors": [
            {
                "id": 274,
                "name": "Temperature",
                "value": "22.0",
                "created_at": "2020-01-13T00:50:27.56"
            }
        ],
        "ready": false
    },
    {
        "id": 32,
        "name": "A9",
        "sensors": [
            {
                "id": 276,
                "name": "Temperature",
                "value": "22.0",
                "created_at": "2020-01-13T00:50:30.13"
            }
        ],
        "ready": true
    },
    {
        "id": 33,
        "name": "B3",
        "sensors": [
            {
                "id": 279,
                "name": "Temperature",
                "value": "22.0",
                "created_at": "2020-01-13T00:50:34.024"
            }
        ],
        "ready": false
    },
    {
        "id": 34,
        "name": "B5",
        "sensors": [
            {
                "id": 281,
                "name": "Temperature",
                "value": "22.0",
                "created_at": "2020-01-13T00:50:36.52"
            }
        ],
        "ready": false
    },
    {
        "id": 35,
        "name": "B7",
        "sensors": [
            {
                "id": 283,
                "name": "Temperature",
                "value": "22.0",
                "created_at": "2020-01-13T00:50:39.17"
            }
        ],
        "ready": false
    },
    {
        "id": 36,
        "name": "B8",
        "sensors": [
            {
                "id": 284,
                "name": "Temperature",
                "value": "22.0",
                "created_at": "2020-01-13T00:50:40.431"
            }
        ],
        "ready": false
    },
    {
        "id": 37,
        "name": "B9",
        "sensors": [
            {
                "id": 285,
                "name": "Temperature",
                "value": "22.0",
                "created_at": "2020-01-13T00:50:41.673"
            }
        ],
        "ready": false
    }
]
```
