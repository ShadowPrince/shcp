(ns shcp.sh.args
  (:require [clojure.string :as str]))

(defn parse-args [line in-quotes]
  (let [breaking-characters (cond 
                              in-quotes #{\"}
                              :else #{\space \\ \"})
        [index, character] (loop [[ch & rest-line] line]
                             (cond
                               (nil? ch) [(count line) , nil]
                               (breaking-characters ch) [(- (count line) (count rest-line)) (breaking-characters ch)]
                               :else (recur rest-line)))
        from 0
        [cto, lto, in-quotes] (case character
                                \space [(dec index) index false]
                                \\ [index (-> index inc) false]
                                \" [(dec index) index (not in-quotes)]
                                nil [index nil nil])
        arg (case character
              \\ (str (.substring line 0 (dec index)) (.substring line index lto))
              (.substring line from cto))]
    (cond 
      (nil? lto) []
      (empty? arg) (parse-args (.substring line lto) in-quotes)
      :else (cons arg (parse-args (.substring line lto) in-quotes)))))

(defn sub-vars [st line]
  (reduce (fn [line [k v]]
            (str/replace line (str "$" k "") (str "" v "")))
          (str line " ") 
          (get-in st [:env :vars])))

(defn sub-env [st env line]
  (reduce (fn [line kv]
            (let [[k, v] (str/split kv #"=")]
              (str/replace line (str "$" k "") (str "" v ""))))
          (str line " ") 
          (str/split env #"\n")))
