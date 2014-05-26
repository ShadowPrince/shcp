(ns shcp.sh.jobs
  (:import [org.sp.shcp.llapi.LLAPI]))

(def ^:dynamic *watcher* (atom nil))

(defn storage []
  (atom []))

(defn get [st i]
  (if (> (count @(:jobs st)) i)
    (nth @(:jobs st) i)
    nil))

(defn push! [st pid]
  (swap! (:jobs st) conj {:pid pid})
  (dec (count @(:jobs st))))

(defn stop-watcher! []
  (if @*watcher*
    (.interrupt @*watcher*)))

(defn start-watcher! [st]
  (reset! 
    *watcher*
    (Thread.
      (fn []
        (let [finished (filter (comp zero? second) 
                               (map #(list % (org.sp.shcp.llapi.LLAPI/checkPid (:pid %))) 
                                    @(:jobs st)))]
          (swap! (:jobs st) #(filter (comp not (set finished)) %))
          (mapv #(println (format "\nChild %d finished!" (-> % first :pid))) finished))
        (Thread/sleep 1000)
        (recur))))
  (.start @*watcher*)
  st)
