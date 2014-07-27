(ns ^{:doc "Core functionality"
      :author "Mayur Jadhav <mj.jadhav13@gmail.com>"}
  charles-clj.core
  (:require [clojure.java.io :as io]
            [clojure.data.xml :as cdx]
            [clojure.pprint :refer :all]))

(def transaction-summary "output/transaction-summary-%s.txt")

(def transaction-content "output/transaction-content-%s.txt")


(defn get-current-time
  []
  (.format (java.text.SimpleDateFormat. "yyyyMMddHHmm")
           (java.util.Date.)))


(defn indexed-transaction-content
  [index transaction-content]
  {index transaction-content})


(defn xml->map
  [xml-file]
  (let [transaction-data (cdx/parse (java.io.StringReader. (slurp xml-file)))
        transaction-summary (mapv indexed-transaction-content
                                  (range)
                                  (mapv :attrs (:content transaction-data)))
        transaction-content (mapv indexed-transaction-content
                                  (range)
                                  (mapv :content
                                        (:content transaction-data)))]
    (binding [*out* (java.io.FileWriter. (format transaction-summary
                                                 (get-current-time)))]
      (pprint transaction-summary))
    (binding [*out* (java.io.FileWriter. (format transaction-content
                                                 (get-current-time)))]
      (pprint transaction-content))))
