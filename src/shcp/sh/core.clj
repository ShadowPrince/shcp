(ns shcp.sh.core
  (:import [org.sp.shcp.llapi LLAPI])
  (:require [shcp.sh.exec :as e]
            [shcp.sh.internal :as i]
            [shcp.ui.core :as ui]
            [shcp.ui.input :as in]
            [shcp.sh.args :as args]
            [clojure.string :as str]))

(defn internal-out! [out]
  (if-let [o (:out out)]
    (print o))
  (if-let [o (:err out)]
    (print o)))

(defn process-command 
  "Process command if available from on state ~st"
  [st cmd]
  (if (empty? cmd)
    st
    (let [cmd (->> cmd
                   (args/sub-vars st)
                   (args/sub-env st (LLAPI/getAllEnv)))
          [st o] (if (i/internal? cmd) (i/internal st cmd) (e/execute st cmd))]
      (internal-out! o)
      (assoc-in st [:env :vars "status"] (:exit o)))))

