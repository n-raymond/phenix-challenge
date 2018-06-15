Phenix Challenge
================

## Getting Started

The program must be aware of the location of the directory `data` containing the entry data files.
Thus, you must specify from the command line the path of the directory containing the `data` directory.
We will thereafter name this directory the `root` directory.

>*For example* :
>
>If my `data` directory path is `/home/nraymond/phenix/data`, then the `root` path that must be used
is `/home/nraymond/phenix`.

When running the program, a `result` directory will be created in the `root` directory. This folder
will contain the solution indicators and the intermediate files created as a pre-computing as well.
The solution files will be located in :
* `<root_path>/result/top_100_ca`
* `<root_path>/result/top_100_ventes`


### Run it with SBT

Use the following command :
```
sbt "run <root_path>"
```

where `<root_path>` is the path of the `root` directory.

### Package and run the app

#### Packaging

To package the application use following command:
```
sbt dist
```

This will create a zip file at path `target/universal/phenix-challenge<version>.zip`.

#### Running the app from the zip

Unzip the zip archive. It will give you a folder named `phenix-challenge<version>`.

Then, run the following command :
```
phenix-challenge<version>/bin/phenix-challenge <root_path>
```

## Presentation of the solution

### Patterns and algorithms

* The main idea of the solution was to use some MapReduce algorithms to divide the work
between several workers (2 in this case because we only have two core in our processor)
and then reduce the intermediate work to get a solution. To get the final indicators,
the work was made in several steps, each one using a MapReduce approach. Because of the
memory size constraint (Only 512M of ram ! Holy cow ! :D), each step is stored on the file system.
To be efficient, the goal was then to read each file only one time.

* Another important way to be light on memory was to use Lazy data structures (like Iterators and
Stream). This was really useful to read big files without having to load it fully in memory.

* The approach was as far as possible a TDD approach whereas it was impossible to maintain
this methodology because of short time. However, approximately 50% of the code is tested anyway.

* I used compile time dependency injection and factories to get a better decoupling in the code
and this was very useful to use mock in an efficient way inside tests.

* Since a great part of the program used the filesystem to store data, I decided to use the
adapter pattern to handle IO operations. This was very helpful to mock those operations in tests.

* To avoid a leak on file descriptors, I handle file closing in a functional way to imitate Java Try with
resources (see trait ResourceCloseable).

### Program architecture

The program is divided into several packages :
* **app:** Contains the entry point of the program. In charge of command line handling, compile time 
dependency injection and aggregation launching.
* **models:** Contains classes used to model the problem (transactions, etc...). 
* **dataFiles:** Contains an abstraction to handle datafiles (location, data serialization, reading, writing, etc...)
* **io:** Contains IO operations adapters.
* **dataProcessors:** Contains the aggregation algorithms using a MapReduce approach. 
* **utils:** Some useful helpers.

### Difficulties encountered

* The given data set contains an error : The transactions file references a product with id 0 but this
product is never referenced inside shop reference files. It was important to me that te program was
resilient to this kind of problems.

* The main difficulty in doing MapReduce is to be careful not to use side effects because it can cause
issues in regard of thread safety. Unfortunately, write and read files causes side effects.
To avoid the problem, the idea was to work on very distinct files inside map operations and then
try to aggregate the different groups of resulting files into one solution in the reduce operation.

#### Possible Improvements

* This is a partial solution. Only two indicators were treated :
    - top_100_ventes_<MAGASIN_ID>_YYYYMMDD.data
    - top_100_ca_<MAGASIN_ID>_YYYYMMDD.data


* Whereas the approach was based on MapReduce, aggregators actually use a simpler and mono-thread
implementation. A good improvement would be to add parallelism to get more efficient algorithm.
This would not be so hard to step further this because current algorithms already decompose the
work into several individual parts (actually treated sequentially).