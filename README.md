# User CRUD — Java 17

A console application demonstrating CRUD with core Java only. It needs no Maven, database server, or external library. User records are stored in `data/users.dat` and remain available between runs.

## Compile and run

From this project folder, run:

```bash
mkdir -p out
javac -d out src/main/java/com/example/usercrud/*.java
java -cp out com.example.usercrud.Main
```

## Features

- Create a user
- List all users
- Find a user by ID
- Update a user
- Delete a user
- Persistent file storage using Java serialization
- Unique email and input validation
