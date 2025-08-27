## smartsheet-java8-time-test

This is project test to demostrate a problem using regular java.util.Date class, where the date stored into the sheet is being shifted one day before. Using the java.time library (Java 8 Time) this problem doesn't present. 


### To run the regular Java Date test:
```
mvn java@java-date
```

### To run the Java 8 Time test:
```
mvn java@java8-time
```

## Note
The project has a dependency of a custom build of smartsheet-sdk-java package, where the source is available [here](https://github.com/ricardoalonso/smartsheet-java-sdk)