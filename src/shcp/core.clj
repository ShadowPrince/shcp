(ns shcp.core
  (:gen-class)
  (:require [shcp.ui.core :as ui]
            [shcp.sh.jobs :as jobs]
            [shcp.sh.core :as sh]))

(defn state [prompt]
  {:prompt (or prompt "%s » ")

   :env {:dir "/home/sp"
         :vars {"PATH" (System/getenv "PATH")  "status" 0 "home" (System/getProperty "user.home")}}
   :jobs (atom [])})

(defn plain-loop [-st]
  (print (ui/prompt -st))
  (flush)
  (if (:exit -st)
    nil
    (let [command (read-line)]
      (plain-loop 
        (sh/process-command -st command)))))

(defn -main [& args]
  (let [opts args
        prompt (nth opts (-> (take-while (partial not= "-p") opts) count inc))
        state (state prompt)]
    (jobs/handle-chld! state)
    (plain-loop state)))
