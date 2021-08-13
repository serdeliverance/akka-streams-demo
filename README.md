# Akka Streams Demo

This repo has the supporting code for the following posts about Akka Streams:

* [Akka Streams Getting Started (on my personal blog)](https://serdeliverance.github.io/blog/blog/akka-streams-getting-started/)
* [Akka Streams Getting Started (Medium Post)](https://medium.com/@canosergio90/akka-streams-getting-started-32b5ebc60604)

## Requirements

* `Scala 2.12`
* `Sbt`
* `Docker y docker-compose`

## Examples

* `Example 1`: Introduces the main stream components and creation of our first graph.

* `Example 2`: Playing around with our initial graph in order to show component reutilization.

* `Example 3`: Shows how to materialize different values

* `Example 4 and 5 (BatchClosure and BatchClosureAgain)`: First examples that show how to create a graph that solves a real world problems using `Akka Streams` and `Alpakka`.

* `TransactionLoader`: Another real world problem example and its solution using this technology.

* Also, there are generators (TransactionGenerator and XXXX) for creating dummy data for the previous examples. Those are also pipeline examples you can check it out.

## Run the example

Start up containers:

```
docker-compose up
```

Run `SBT` and select the example you want to run :

```scala
sbt run
```

## Scripts for data loading

### BatchClosureGenerator

To generate transactions needed by [Example4_BatchClosure](./src/main/scala/io/github/serdeliverance/examples/part1/Example4_BatchClosure.scala) and [Example5_BatchClosureAgain](./src/main/scala/io/github/serdeliverance/examples/part1/Example5_BatchClosureAgain.scala) run:

``` scala
./batchClosureGenerator.sh
```

### TransactionLoaderGenerator

To generate transactions needed by [TransactionLoader](./src/main/scala/io/github/serdeliverance/examples/part2/TransactionLoader.scala) use the following script.

``` scala
./transactionLoaderGenerator.sh
```

It generates the `transaction.txt` file.
