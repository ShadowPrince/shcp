(defproject shcp "0.1.0-SNAPSHOT"
  :description "command intepreter"
  :url "http://github.com/shadowprince/shcp"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [clojure-lanterna "0.9.4"]]

  :main ^:skip-aot shcp.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}}

  :jvm-opts ["-Djava.library.path=java/bin"]
  :java-source-paths ["java/src"]

  :repl-options {:init (do 
                         ;; my dev stuff, dont ask about this
                         ;; anyway, I should use Stuart Sierra's reload
                         (in-ns 'user)
                         (load "autoload") 
                         (autoload/autoload-thread! "src/shcp") 
                         (println "Threaded autoloader engaged!")
                         (in-ns 'shcp.core)
                         (use 'clojure.repl)
                         (use 'clojure.pprint))}
  )
