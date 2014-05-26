(ns shcp.core
  (:gen-class)
  (:require [lanterna.screen :as s]
            [shcp.ui.core :as ui]
            [shcp.ui.input :as uin]
            [shcp.sh.jobs :as jobs]
            [shcp.sh.core :as sh]))

(defn state [prompt]
  {:prompt (or prompt "%s # ")

   :last-exit 0
   :env {:dir "/home/sp"}
   :jobs (atom [])})

(defn plain-loop [-st]
  (print (ui/prompt -st))
  (flush)
  (if (:exit -st)
    nil
    (let [command (read-line)]
      (plain-loop (try 
                    (sh/process-command -st command) 
                    (catch Exception e (println (str "Exception caused: " e))))))))

(defn resize [ac ar c r]
  (send ac (fn [_] c))
  (send ar (fn [_] r)))

(defn -main [& args]
  (let [opts args
        prompt (nth opts (-> (take-while (partial not= "-p") opts) count inc))]
    (try (-> (state prompt) 
             (jobs/start-watcher!)
             (plain-loop))
         (finally (jobs/stop-watcher!)))))
