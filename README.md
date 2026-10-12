# User CRUD — Advanced Java

A console application demonstrating CRUD with Java 17, JDBC, prepared statements, validation, and an H2 embedded database.

## Run

```bash
mvn compile exec:java
```

The application creates `data/users.mv.db` automatically and keeps records between runs.

## Features

- Create a user
- List all users
- Find a user by ID
- Update a user
- Delete a user
- Unique email validation and safe SQL through `PreparedStatement`
