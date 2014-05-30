(ns shcp.sh.util)

(defn out-by-code [code error]
  (if (zero? code)
    {:exit 0}
    {:err (format error code) :exit code}))
