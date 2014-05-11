(ns shcp.sh.core
  (:require [shcp.sh.exec :as e]
            [shcp.sh.internal :as i]
            [shcp.ui.core :as ui]
            [shcp.ui.input :as in]
            [clojure.string :as str]))

(defn process-command 
  "Process command if available from on state ~st"
  ([st]
  (if (not= (char 13) (-> st :input last))
    st
    (let [cmd (apply str (-> st :input butlast))
          [st o] (if (i/internal? cmd) (i/internal st cmd) (e/execute-old st cmd))]
      (-> st
          (assoc-in [:scroll :last] 0) ;; reset scroll
          (update-in [:output] concat (list (str (ui/prompt st) cmd))) ;; add prompt
          (assoc-in [:input] []) ;; reset input
          (update-in [:output] concat (ui/format-output (:out o) (:cols st)) (ui/format-output (:err o) (:cols st))) ;; add outputs
          (assoc-in [:last-exit] (:exit o))))))
  ([st cmd]
   (let [[st o] (if (i/internal? cmd) (i/internal st cmd) (e/execute st cmd))]
     (print (:out o) (:err o))
     (-> st
         (assoc-in [:last-exit] (:exit o))))))

