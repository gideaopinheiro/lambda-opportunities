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

(def openings (atom {"abc" {:id "abc" :role "Elixir Engineer" :company "Google" :remote true, :link "http://google.com" :location "address, 218" :salary 140000}
                     "def" {:id "def" :role "Haskell Developer" :company "Amazon" :remote true, :link "http://amazon.com" :location "address, 88" :salary 100000}
                     "asdf" {:id "asdf" :role "ClojureScript Engineer" :company "X-Team" :remote true, :link "http://xteam.com" :location "address, 2" :salary 85000}}))

(comment
  (list-opening-by-id "98eb0622-1a1a-4700-a9b7-e9a69694fb0b")
  (update-opening-by-id! "98eb0622-1a1a-4700-a9b7-e9a69694fb0b" {:salary 250080})
  (create-opportunities-table!))
