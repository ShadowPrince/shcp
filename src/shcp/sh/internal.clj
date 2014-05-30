(ns shcp.sh.internal
  (:require [clojure.string :as str]
            [shcp.ui.core :as ui]
            [shcp.sh.util :refer :all]
            [shcp.sh.jobs :as jobs])
  (:import [org.sp.shcp.llapi LLAPI]))

(defn exit [st]
  (list (assoc st :exit true) {:exit 0}))

(defn cd [st & [arg & _]]
  (let [dir (get-in st [:env :dir])
        newdir (cond 
                 (nil? arg) (get-in st [:env :vars "home"])
                 (.startsWith arg "/") arg
                 (.startsWith arg "..") (str/join "/" (conj (butlast (str/split dir #"/")) ""))
                 true (str dir "/" arg))]
    (if (= 0 (LLAPI/cd newdir))
      (list (-> st 
                (assoc-in [:env :dir] newdir)) {:exit 0})  
      (list st {:exit -1 :err (format "No such dir: \"%s\"!\n" newdir)}))))

(defn wait [st & [strpid]]
  (if-let [{:keys [pid] :or [pid]} (jobs/-get st (Integer/parseInt strpid))]
    (list st {:exit (LLAPI/waitPid pid) :out (format "%s finished" pid)})
    (list st {:exit -1 :out (format "Pid or job \"%s\" not found!\n" strpid)})))

(defn jobs [st]
  (list st {:out (str (str/join "\n" (map-indexed #(format "#%d: pid %d" %1 (:pid %2)) @(:jobs st))) "\n")}))

(defn setenv [st k v]
  (list st (out-by-code (LLAPI/setEnv k v) "Set env failed with %d\n")))

(defn unsetenv [st k]
  (list st (out-by-code (LLAPI/unsetEnv k) "Unset env failed with %d\n")))

(defn getenv [st & [k & _]]
  (list st {:out (if k (format "variable %s:\n%s\n" k (LLAPI/getEnv k)) (LLAPI/getAllEnv))}))

(defn setvar [st k v]
  (list (assoc-in st [:env :vars k] v) {}))

(defn unsetvar [st k]
  (list (update-in st [:env :vars] dissoc k) {}))

(defn getvar [st k]
  (list st {:out (str (get-in st [:env :vars k]) "\n") :exit 0}))

(defn pwd [st]
  (list st {:out (str (get-in st [:env :dir]) "\n")}))

(defn echo [st & args]
  (list st {:out (str (str/join " " args) "\n")}))

(defn internal? [cmd]
  (let [publics (ns-publics 'shcp.sh.internal)]
    (->> (str/split cmd #" ") first symbol publics)))

(defn internal [st cmd]
  (let [[cmd & args] (str/split cmd #" ")]
    (try
      (apply (->> cmd symbol (ns-resolve 'shcp.sh.internal)) st args)
      (catch Exception e (list st {:exit 1 :out (str "spsh: " e "\n")})))))
