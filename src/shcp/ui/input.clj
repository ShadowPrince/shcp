(ns shcp.ui.input)

(defn process-input [st k]
  (case k
    8 (update-in st [:input] #(if (seq %) (pop %) %)) ; backspace
    33 (update-in st [:scroll :last] + 5) ;pg-up
    34 (update-in st [:scroll :last] - 5) ;pg-down
    
    (update-in st [:input] conj (char k))))
