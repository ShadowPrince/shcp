# shcp

Had some fun making command intepretator built with clojure and JNI (_Clojure -> Java -> JNI -> C++ lib -> posix libs -> system calls_). Proudly named __Shellception__.

## Installation

* `make` in _java/bin_
* `lein uberjar` in _._

## Usage

No practical use.

    $ java -Djava.library.path=java/bin -jar target/shcp*jar

Use `--lanterna` option to use lanterna UI backend instead of print/read-line.

## License

Copyright © 2014 Vasiliy Horbachenko

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

## .

Not shure we need this, but why not? 

![dicaprio](http://i.imgur.com/3Afjw1u.jpg)
