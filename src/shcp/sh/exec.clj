(ns shcp.sh.exec
  (:import (java.io File IOException)
           (org.sp.shcp.llapi LLAPI))
  (:require [clojure.java.shell :as sh]
            [clojure.java.io :as io]
            [clojure.string :as str]))

(defn execute-old [st cmd]
  (try 
    (let [o (apply sh/sh (concat (str/split cmd #" ") (list :in (:in st) :dir (get-in st [:env :dir]))))]
      (list st o))
    (catch java.io.IOException e (list st {:exit -10 :out (str) :err (str e)}))))

(defn execute [st cmd]
  (try 
    (LLAPI/popen (str "cd " (get-in st [:env :dir]) " ; " cmd))
    (list st {:out "" :exit 0 :err ""})
    (catch java.io.IOException e (list st {:exit -10 :out (str) :err (str e)})))) 
