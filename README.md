# Akka Streams Demo

## Requisitos

* `Scala 2.12`
* `Sbt`
* `Docker y docker-compose`

## Ejemplos

* `Example 1`: intro a los componentes fundamentales de un pipeline (de ahora en más: grafo) y creación.

* `Example 2`: variantes del ejemplo anterior que favorecen a la reutilización de componentes.

* `TransactionLoader`: definición de un pipeline de procesamiento que soluciona una problemática real, procesando un archivo y volcando el resultado en una dase de datos a través de un pipeline de procesamiento asincrónico.

### Transaction Loader

Pipeline a implementar:

![Alt text](diagrams/pipeline.png?raw=true "Pipeline")

## Run the example

Start up containers:

```
docker-compose up
```

Run batch processing pipeline:

```scala
sbt "runMain io.github.serdeliverance.examples.TransactionLoader"
```

## Transaction generator

To generate transactions use the following script (which also runs a [graph](src/main/scala/io/github/serdeliverance/generator/TransactionGenerator.scala)).

``` scala
./generator.sh
```

It generates the `transaction.txt` file.