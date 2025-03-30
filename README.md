# Jpoint 2025 Spring Aio Stand Demo

## Glossary:

1. **SDJ** - Spring Data JDBC
2. **CRI** - Custom Repository Implementations (Sometimes referred to as Repressory Fragments)

## 1. Criteria API Usage in Spring JDBC.

FAG:

Q: Any way of executing Criteria without JdbcAggregateTemplate? <br/>
A: **Currently, no, unfortunately**. BUT! There is a discussion about that, stay
tuned in Spring Aio Chat!

Q: Any analog of JPA Criteria API `distinct()`/`join()`/`groupBy`? <br/>
A: **No, not yet**. As a consequence, there is no way to express `JOIN` SQL construct 
via Criteria API. If you need this one, please, let Mikhail Polivakha know.
He had a discussion about it internally, but there seems to be no urgent need in this

Q: In Hibernate, we have a type-safe `StaticMetamodel` API generated by APT. Anything
similar in SDJ? <br/>
A: No, not yet. It is not inherently hard to implement it, it is just that it requires
a lot of engineering time and nobody really did it before. But if you need it, again,
reach out to Mikhail, he will look into it.

## 2. SPEL in Spring Data Queries

FAG: 

Q: Do all modules support SPEL in String-based queries? <br/>
A: **Most of, maybe not all, but the majority - yes**. 

Q: Can I have a custom `BeanResolver` in SPEL queries? <br/>
A: **Not directly, no**. BUT! You can get a very similar effect with `EvaluationContextExtension`.
The Spring Aio expert will provide you with necessary information.

Q: How Spring Data internally handles the SPEL in queries? <br/>
A: Well, there two different scenarios here. Let me explain. Most of the time, users actually
want the SPEL to evaluate into a value of the column in the `WHERE` clause, like this:

```
WHERE name = :#{root.property}
```

in this case, **we replace all the spel/value references in the query with named parameters**, and the 
evaluated values are added into the underlying `SqlParameterFactory`. So, after parsing, the query looks 
like this:

```
WHERE name = :__$$synthetic$$__1
```

So, **the synthetic named parameter is generated**. 

Another case is that many modules allow for SPEL to be used in other places for schema/table name substitution.
That allows for polymorphic HQL to be expressed easier, for instance. So, this approach looks like that:

```(HQL)
SELECT t FROM #{entityName} t WHERE t.name = ?
```

In this case, the entity name to be substituted is actually replaced in-place. **So, beware of SQL injections!**

Q: As far as I know, SPEL actually starts with `#{`, and not `:#{`, am I right ? <br/>
A: Yes, you're correct. And Spring Data is not an exception 8-) We retain the `:` at the beginning 
while parsing the rest, so the expression like this `WHERE name = :#{name}` becomes a named parameter reference,
like that `WHERE name = :name`. The colon at the beginning is not a part of the SPEL, actually 8-)

Q: Any performance penalty for that ? <br/>
A: We do not have stable JMH tests to prove the degradation of perf in here, but, just beware, that spel evaluation logic
would obviously work on each executed query, so, apply with caution.

## 3. JPQL Challanges

Q. Am I correct that ALL JPQL queries bypass the first level cache? <br/>
A: **Well, yes.**. And it is not only the JPQL queries that bypass the 1-st level cache, bu criteria queries as well.
In fact, the only queries that will hit the cache first (if you have **NOT** tweaked the `CacheMode` yourself) are only
`entityManager.find(Class domainClass, T id)` queries. The `UPDATE`/`DELETE` queries would also bypass it.

Q: Do all types of cascades does not work when you do JPQL? <br/>
A: **Yes, all defined in JPA**. Read me _**very**_ carefully right now - Cascades in general are designed to cascade the 
corresponding transitions of entities. With JPQL, you're not interacting with the persistence context, your queries are 
directly converted into SQL and sent to database. There is no "entities" involved, no transitions happen. Therefore, 
there is nothing to cascade.

Q: So, why Spring Data JPA / Hibernate issues the separate SELECT for each cascade removed entity? <br/>
A: The same answer! **Entity transitions**! Cascade removal means that entities needs to be moved to a removed state in the 
persistence context. In order to be placed in "removed" state in persistence context they need to be loaded into memory! 

## 4. Inline Classes in Spring Data Repositories

Q. Aren't inline classes similar to typeliases in Kotlin? <br/>
A: **Well, not quite.**. Assume you have declared the typealias such as follows:

```
typealias MyString = String
```

and then you also have the following repo method:

```
interface MyRepo : CrudRepository<Entity, Long> {
   
   fun findByMyString(source: MyString);
}
```

The problem is that `MyString` is considered to be an alias for `String`, and therefore you can pass an instance of `String` 
into the `findMyMyString()` method, and it would compile! That is not what we actually want. We want to solve the problem of
type-safety. In other words, we do not want the program to compile if we have passed the `String` in here, not `MyString`. But
we also do not want to impose an additional overhead of creating wrapper classes. That is why inline classes come into play here.
They provide type-safety on the compile time, while the classes will be inlined in the actual JVM bytecode into the field they hold

Q: Do all Spring Data modules support them? <br/>
A: **Yes**. It is important to understand, that we do not do really anything in particular to support them. The kotlin compiler will
do the most work for us. Just make sure that the generated JVM methods will not clash in terms of signature.

## 5. Spring Data Sequences Support

Q. When this will be available? <br/>
A: **We target 3.5.0 minor release**.

Q: Can you do optimizations, similar to JPA's `allocationSize`? <br/>
A: **Well, not yet**. Most likely we would consider it, as a matter of fact, we have already discussed it internally. But this is 
kind of low priority now. If you want this optimization in place, then please, file an issue or if any already exists - like it!

Q: Is it available for all DBs? <br/>
A: **Almost, for all except MySQL**. The problem with MySQL is that this RDMBS does not support sequences in general. But other 
databases do, including the MariaDB. So for every other the answer is yes.

## 6. Client Generated Ids Spring Data JDBC

Q. Is it required to implement Persistable? <br/>
A: **No, not really**. There are other ways of doing an UPDATE with client generated ID, for instance you can write a custom
`BeforeConvertCallback`. It is also possible, but I prefer the approach via persistable becuase it is portable - `Persistable`
is supported by all spring data modules, `BeforeConvertCallback` is a strict Spring Data JDBC feature.

Q. Why not just making some annotation like `@ClientSideGenerated` and be done with it? <br/>
A: **It is possible, but you would not like the consequences**. Because in this case, when you call `save()` on `CrudRepository`
it would be impossible to determine if we need to issue an `INSERT`, or we want to issue an `UPDATE` it. We cannot know it since
for both `INSERT` and `UPDATE` we need a pre-set id.  In order to gain the knowledge of what query to issue, we need to do 
what Hibernate does - issue a separate `SELECT`. Do you want extra `SELECT`s into your database? No, so shut your mouth.

## 7. Allocation Size in JPA

Q. Why still multiple selects to sequence? <br/>
A: Well, this is mostly because Hibernate wants to check if the sequence in database is indeed configured correctly. In other words,
it wants to double-check that it indeed has allocated N ids for operations. So, Hibernate would have to check it anyway, either by 
querying the `information_schema` or smth like that, or by issuing another `SELECT nextval`/`SELECT NEXT VALUE FOR`. The second approach is
of course better, since it further reduces the number of queries to be done
