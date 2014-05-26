(ns shcp.sh.core
  (:require [shcp.sh.exec :as e]
            [shcp.sh.internal :as i]
            [shcp.ui.core :as ui]
            [shcp.ui.input :as in]
            [clojure.string :as str]))

(defn internal-out! [out]
  (if-let [o (:out out)]
    (print o))
  (if-let [o (:err out)]
    (print o)))

(defn process-command 
  "Process command if available from on state ~st"
  [st cmd]
  (let [[st o] (if (i/internal? cmd) (i/internal st cmd) (e/execute st cmd))]
    (internal-out! o)
    (assoc-in st [:last-exit] (:exit o))))

