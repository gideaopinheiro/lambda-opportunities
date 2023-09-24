(ns lambda-opportunities.infra.database.db
  (:require [next.jdbc.sql :as sql]
            [next.jdbc :as jdbc]
            [next.jdbc.result-set :as rs]))

(def postgres-db-spec
  {:dbtype "postgresql"
   :dbname "lopportunities"
   :host "db"
   :port 5432
   :user "postgres"
   :password "postgres"})

(defn create-opportunities-table! []
  (jdbc/execute! postgres-db-spec
                 ["CREATE TABLE IF NOT EXISTS openings 
                 (
                  id TEXT PRIMARY KEY,
                  role TEXT,
                  company TEXT,
                  remote BOOLEAN,
                  link TEXT,
                  location TEXT,
                  salary NUMERIC
                  )"]))

(defn list-openings []
  (jdbc/execute!
   postgres-db-spec
   ["SELECT * FROM openings"]
   {:return-keys true :builder-fn rs/as-unqualified-lower-maps}))

(defn list-opening-by-id [id]
  (jdbc/execute!
    postgres-db-spec
    [(str "SELECT * FROM openings WHERE id='" id "'")]
    {:return-keys true :builder-fn rs/as-unqualified-lower-maps }))

(defn delete-opening-by-id! [id]
  (sql/delete! postgres-db-spec :openings {:id id}))

(defn update-opening-by-id! [id data]
  (sql/update!
    postgres-db-spec
    :openings
    data
    {:id id}))

(defn create-opening! [{:keys [id role company remote link location salary]}]
  (sql/insert!
   postgres-db-spec
   :openings {:id id
              :role role
              :company company
              :remote remote
              :link link
              :location location
              :salary salary}
   {:return-keys true :builder-fn rs/as-unqualified-lower-maps}))

