# Parking Management System

A **Spring Boot** application to manage parking lots, vehicles, and parking sessions. It exposes a **REST API** to:

- Register parking lots
- Register vehicles
- Check-in and check-out vehicles
- View lot status
- List vehicles currently parked in a lot
- Automatically remove vehicles parked more than 15 minutes

## Technology Stack

| Technology | Version / Description |
|------------|---------------------|
| Spring Boot | 3.x |
| Java | 17+ |
| H2 Database | In-memory DB for testing |
| MapStruct | DTO mapping |
| Maven | Build and dependency management |

## Build, Run

You can execute these commands directly:

```bash
# Build the project
mvn clean install
```
```bash
# Run the application
mvn spring-boot:run
```
## Testing Endpoints

Extract the postman collections from /postman-collection

1. **Import the Collection**  
   Import the `ParkingService` collection into Postman.

2. **Run the Login Request**
    - Find and execute the `POST Login` request.
    - This will automatically set the `X-Auth-Token` Header for other requests (You can verify it by checking the currentValue of the PostmanCollection variable)

3. **Execute Other Requests**
    - Run the other endpoints. The token will authenticate your requests.

 

