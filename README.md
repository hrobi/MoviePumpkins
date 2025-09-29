# MoviePumpkins

## Development environment credentials

| credential type          | credential value        |
|--------------------------|-------------------------|
| postgresql user          | admin                   |
| postgresql password      | gYLWLbQUcY              |
| pgadmin default email    | admin@moviepumpkins.net |
| pgadmin default password | gYLWLbQUcY              |
| keycloak admin user      | admin                   |
| keycloak admin password  | gaO1xZDFl5              |
| keycloak client id       | pumpkins-web            |

## Local usage setup

### Database

In order to run the application locally, you'll need some mock data. Mock data is provided in
backend-core/resources/mock/db
in sql files that need to be run. You can use pgadmin for that but Intellij will do just fine too,

### Keycloak

In order to test how the application works with users, those users need to be added to keycloak. You can add the users
manually using keycloak, but it's much faster to use the add_users.http file to call keycloak with all the test users.
For the call you'll need an admin token which you can get by calling get_admin_token.http. (You have to remove .temp ext
first, then provide the required data between <<>>)