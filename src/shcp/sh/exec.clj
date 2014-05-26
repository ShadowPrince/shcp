(ns shcp.sh.exec
  (:import (java.io File IOException)
           (org.sp.shcp.llapi LLAPI))
  (:require [clojure.java.shell :as sh]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [shcp.sh.jobs :as jobs]))

(defn exec [st cmd args]
  (list st {:exit (LLAPI/exec cmd (into-array args))}))

(defn exec-background [st cmd args]
  (let [pid (LLAPI/execBackground cmd  (into-array args))
        job (jobs/push! st pid)]
    (list st {:exit 0 :out (format "%d in background, job #%d\n" pid job)})))

(defn execute [st cmd]
  (let [[cmd, & args] (str/split cmd #" ")
        [args, background] (if (= (last args) "&") [(butlast args) true] [args false])

        args (cons cmd args) 
        cmd (str "/usr/bin/" cmd)]
    (try 
      (if background
        (exec-background st cmd args)
        (exec st cmd args))
      (catch java.io.IOException e (list st {:exit -10 :out (str) :err (str e)}))))) 
