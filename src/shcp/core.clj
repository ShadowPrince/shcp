(ns shcp.core
  (:gen-class)
  (:require [lanterna.screen :as s]
            [shcp.ui.core :as ui]
            [shcp.ui.input :as uin]
            [shcp.sh.core :as sh]))

(defn state [is c r]
  {:input []
   :output []  

   :cols 63
   :rows r
   :is is
   :last-exit 0
   :scroll {:last 0
            :size 30}
   :env {:dir "/home/sp"}})

(def cols-agent (agent 63))
(def rows-agent (agent 40))

(defn istream [scr]
  (proxy [java.io.InputStream] []
    (read [] (let [k (s/get-key-blocking scr)] 
               (case k
                 :enter 13
                 :backspace 8
                 :page-up 33
                 :page-down 34
                 (int k))))))

(defn update-size [st]
  (-> st
      (assoc :cols @cols-agent)
      (assoc :rows @rows-agent)))

(defn lanterna-loop [scr -st] 
  (ui/paint! scr -st)
  (lanterna-loop scr (-> -st 
                         (uin/process-input (-> -st :is .read))
                         (sh/process-command)
                         (update-size))))

(defn plain-loop [-st]
  (print (ui/prompt -st))
  (flush)
  (let [command (read-line)]
    (plain-loop (-> -st
                    (sh/process-command command)))))

(defn resize [ac ar c r]
  (send ac (fn [_] c))
  (send ar (fn [_] r)))

(defn -main [& args]
  (let [opts (set args)]
    (cond
      (opts "--lanterna") 
      (let [s (s/get-screen :swing {:resize-listener (partial resize cols-agent rows-agent)})
            is (istream s)]
        (try
          (s/start s)
          (lanterna-loop s (apply state is (s/get-size s)))
          (finally (s/stop s))))
      :else
      (plain-loop (apply state nil [50 30]))
      )))
