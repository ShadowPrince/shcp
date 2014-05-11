(ns shcp.sh.internal
  (:require [clojure.string :as str]
            [shcp.ui.core :as ui]))

(defn cd [st arg]
  (let [dir (get-in st [:env :dir])
        dir (cond 
              (.startsWith arg "/") arg
              (.startsWith arg "..") (str/join "/" (conj (butlast (str/split dir #"/")) ""))
              true (str dir "/" arg))]
    (list (assoc-in st [:env :dir] dir) {:exit 0 :out (str) :err (str)})))

(defn reformat [st & [arg]]
  (let [l (case arg
            nil (:cols st)
            (Integer/parseInt arg))]
    (list (update-in st [:output] #(ui/format-output (str/join %) l)) {:exit 0 :out (str) :err (str)})))

(defn exit [st]
  (throw (Exception. "Exit")))

(defn internal? [cmd]
  (let [publics (ns-publics 'shcp.sh.internal)]
    (->> (str/split cmd #" ") first symbol publics)))

(defn internal [st cmd]
  (let [[cmd & args] (str/split cmd #" ")]
    (try
      (apply (->> cmd symbol (ns-resolve 'shcp.sh.internal)) st args)
      (catch Exception e {:error -10 :out (str) :err (str e)}))))
