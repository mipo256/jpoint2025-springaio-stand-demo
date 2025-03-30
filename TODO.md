# TODO

## Spring Data JDBC Criteria API usage

* Add query with `OR IS NULL`/`COALESCE` + link to Ilya's and Fedor talk
* QR code to README.md

## JPQL Challenges

* Add the test-case, where we have the JPQL like the following: 

```(SQL)
SELECT t FROM Entity t WHERE ...
```

The user might expect that `EAGER`-ly marked relation of `Entity` will be loaded via `JOIN` clause, but in reality 
the Hibernate will load the relation via a separate SQL `SELECT` statement.

* Consider TODOs in the Test files

## Spring Data JDBC Sequences support

* What will the batchUpdate look like
* file a ticket for allocationSize-like support 

## SPEL

* Table name example
* Entity name example

## Kotlin inline classes

* Emphasize that the spring data requires no configuration for this to run - this is just a trick on the edge of kotlin and spring data. 
