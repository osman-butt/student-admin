[![Build and deploy JAR app to Azure Web App - student-admin](https://github.com/osman-butt/student-admin/actions/workflows/main_student-admin.yml/badge.svg)](https://github.com/osman-butt/student-admin/actions/workflows/main_student-admin.yml)

# Student admin REST API
The backend is deployed [here](https://student-admin.azurewebsites.net/).

## Deployment
- **Main Branch**: On every push to the main branch, the CI/CD pipeline automatically builds the application, runs tests, and deploys the updated version to the production environment.
- **Pull Requests**: When pull requests are opened, a CI pipeline is triggered, which runs all the tests.

## Getting Started
To run the application locally:

1. Clone the repository:
   ```
   git clone https://github.com/osman-butt/student-admin.git
   ```
2. Navigate to the project directory:
   ```
   cd student-admin-system
   ```
3. Add environment variables
   ```
   spring.datasource.url=${DB_URL}
   spring.datasource.username=${DB_USER}
   spring.datasource.password=${DB_PASS}
   ```
5. Build and run the application using spring boot profile _dev_ (=populates db with predefined data):
   ```
   ./mvnw spring-boot:run -Dspring.profiles.active=dev
   ```
6. Access the application in your web browser at http://localhost:8080

## Feedback
If you encounter any issues or have suggestions for improvement, please feel free to open an issue on GitHub
