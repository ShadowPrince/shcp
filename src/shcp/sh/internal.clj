(ns shcp.sh.internal
  (:require [clojure.string :as str]
            [shcp.ui.core :as ui]
            [shcp.sh.jobs :as jobs])
  (:import [org.sp.shcp.llapi.LLAPI]))

(defn cd [st arg]
  (let [dir (get-in st [:env :dir])
        newdir (cond 
              (.startsWith arg "/") arg
              (.startsWith arg "..") (str/join "/" (conj (butlast (str/split dir #"/")) ""))
              true (str dir "/" arg))]
    (if (= 0 (org.sp.shcp.llapi.LLAPI/cd newdir))
      (list (assoc-in st [:env :dir] dir) {:exit 0})  
      (list st {:exit -1 :err (format "No such dir: \"%s\"!\n" newdir)}))))

(defn wait [st & [strpid]]
  (if-let [{:keys [pid] :or [pid]} (jobs/get st (Integer/parseInt strpid))]
    (list st {:exit (org.sp.shcp.llapi.LLAPI/waitPid pid) :out (format "%s finished" pid)})
    (list st {:exit -1 :out (format "Pid or job \"%s\" not found!\n" strpid)})))

(defn exit [st]
  (list (assoc st :exit true) {:exit 0}))

(defn jobs [st]
  (list st {:out (str (str/join "\n" (map-indexed #(format "#%d: pid %d" %1 (:pid %2)) @(:jobs st))) "\n")}))

(defn internal? [cmd]
  (let [publics (ns-publics 'shcp.sh.internal)]
    (->> (str/split cmd #" ") first symbol publics)))

(defn internal [st cmd]
  (let [[cmd & args] (str/split cmd #" ")]
    (apply (->> cmd symbol (ns-resolve 'shcp.sh.internal)) st args)))
