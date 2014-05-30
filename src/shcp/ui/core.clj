(ns shcp.ui.core
  (:require [clojure.string :as str]
            [lanterna.screen :as s])) 

(defn prompt [st]
  (format (:prompt st) (-> st :env :dir)))
