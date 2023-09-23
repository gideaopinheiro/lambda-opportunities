(ns lambda-opportunities.services.opening
  (:require [lambda-opportunities.infra.database.db :refer [openings list-openings list-opening-by-id  create-opening! delete-opening-by-id! update-opening-by-id!]]))

(defn delete-opening! [id]
  (delete-opening-by-id! id))

(defn insert-opening! [data]
  (create-opening! data))

(defn find-opening-by-id [id]
  (println id)
  (list-opening-by-id id))

(defn update-opening! [id  key-value-pairs]
  (update-opening-by-id! id key-value-pairs))

(defn find-all-openings []
  (list-openings))

(comment
  (find-opening-by-id "7dccc95f-6183-4da6-81d7-5f1c78e1fa26"))
