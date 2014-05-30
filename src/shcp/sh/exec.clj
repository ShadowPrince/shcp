(ns shcp.sh.exec
  (:import (java.io File IOException)
           (org.sp.shcp.llapi LLAPI))
  (:require [clojure.java.shell :as sh]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [shcp.sh.util :refer :all]
            [shcp.sh.args :as args]
            [shcp.sh.jobs :as jobs]))

(defn find-exec [st executable]
  (let [PATH (str/split (get-in st [:env :vars "PATH"]) #"\:")]
    (loop [[p & r] PATH]
      (cond
        (nil? p) nil
        (.exists (io/file (str p "/" executable))) (str p "/" executable)
        :else (recur r)))))

(defn exec [st cmd args]
  (list st (out-by-code (LLAPI/exec cmd (into-array args)) "Exec failed with code %d\n")))

(defn exec-background [st cmd args]
  (let [pid (LLAPI/execBackground cmd  (into-array args))
        job (jobs/push! st pid)]
    (list st {:exit 0 :out (format "%d in background, job #%d\n" pid job)})))

(defn execute [st cmd]
  (let [[cmd, & args] (args/parse-args cmd false)
        [args, background] (if (= (last args) "&") [(butlast args) true] [args false])

        args (cons cmd args) 
        cmd (find-exec st cmd)]
    (try 
      (cond
        (nil? cmd) (list st {:exit 1 :out "spsh: command not found\n"})
        background (exec-background st cmd args)
        :else (exec st cmd args))
      (catch java.io.IOException e (list st {:exit -10 :err (str e)}))))) 
