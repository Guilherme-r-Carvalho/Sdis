# gRPC testing

This is the base project for the gRPC testing application, composed of three modules:

- [contract](contract/) - protocol buffers definition
- [server](server/) - implementation of service
- [client](client/) - invocation of service

To compile and install all modules:

```
mvn clean install -DskipTests
```

The integration tests are skipped because they require the servers to be running.

----

[SD Faculty](mailto:leti-sod@disciplinas.tecnico.ulisboa.pt)

### Team Members


| Number | Name              | User                             	    | Email                               	             |
|--------|-------------------|------------------------------------------|----------------------------------------------------|
| 79762  | Duarte Silva	     | <https://github.com/DuarteVieiraSilva>   | <mailto:duarte.da.silva@tecnico.ulisboa.pt>        |
| 96868  | Guilherme Carvalho| <https://github.com/Guilherme-r-Carvalho>| <mailto:guilherme.r.carvalho@tecnico.ulisboa.pt>   |
| 96773  | Tomás Cunha       | <https://github.com/TomAsh01>            | <mailto:tomas.da.cunha@tecnico.ulisboa.pt>         |