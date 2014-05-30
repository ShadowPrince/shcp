(ns shcp.sh.jobs
  (:import [sun.misc Signal SignalHandler]
           [org.sp.shcp.llapi LLAPI]))

(defn storage []
  (atom []))

(defn -get [st i]
  (if (> (count @(:jobs st)) i)
    (nth @(:jobs st) i)
    (let [f (filter #(= (:pid %) i) @(:jobs st))]
      (if (empty? f)
        nil
        (first f)))))

(defn push! [st pid]
  (swap! (:jobs st) conj {:pid pid})
  (dec (count @(:jobs st))))

(defn handle-chld! [st]
  (Signal/handle (Signal. "CHLD") 
                 (proxy [SignalHandler] []
                   (handle [sig] 
                     (loop [code (LLAPI/checkPid -1)]
                       (cond
                         (neg? code) nil
                         (zero? code) (recur (LLAPI/checkPid -1))
                         :else (do
                                 (swap! (:jobs st) #(remove (fn [j] (= (:pid j) code)) %1))
                                 (println (format "Process %d finished!\n" code))
                                 (recur (LLAPI/checkPid -1))))))))
  (Signal/handle (Signal. "INT") 
                 (proxy [SignalHandler] []
                   (handle [sig] 
                     nil))))

