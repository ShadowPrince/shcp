(ns shcp.ui.core
  (:require [clojure.string :as str]
            [lanterna.screen :as s])) 

(defn len-split [s l]
  (if (or (empty? s) (= s "\n"))
    []
    (let [l (if (< (.length s) l) (.length s) l)]
      (cons (.substring s 0 l) (len-split (.substring s l) l)))))

(defn format-output [s w]
  (reduce (fn [accum line]
            (concat accum (len-split (str line "\n") w)))
          []
          (str/split s #"\n")))

(defn prompt [st]
  (str  (:last-exit st) " $ "))

(defn paint! [scr st]
  (s/clear scr)
  (let [output (take-last 
                 (-> st :scroll :size)
                 (take 
                   (- (-> st :output count) (-> st :scroll :last))
                   (:output st)))]
    (mapv
      (fn [[i l]]
        (s/put-string scr 0 i l))
      (map vector (range) output))

    (let [y (count output)
          prompt (prompt st)]
      (s/put-string scr 0 y prompt)
      (s/put-string scr (count prompt) y (apply str (:input st)))))
  (s/redraw scr))
