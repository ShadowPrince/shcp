# shcp

Had some fun making command intepretator built with clojure and JNI (Clojure -> Java -> JNI -> C++ lib -> posix libs -> system calls). Proudly named "Shellception".

## Installation

    `make` in java/bin
    `lein uberjar` in .

## Usage

No practical use.
    ` $ java -Djava.library.path=java/bin -jar target/shcp*jar`

Use `--lanterna` option to use lanterna UI backend instead of print/read-line.

## License

Copyright © 2014 Vasiliy Horbachenko

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
