(ns lambda_opportunities.core
  (:require [lambda-opportunities.config.server :as s])
  (:gen-class))

(defn -main
  [& args]
  (s/start-server!))

(comment
  (s/start-server!)
  (s/stop-server!))
