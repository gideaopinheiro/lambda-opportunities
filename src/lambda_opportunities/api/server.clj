(ns lambda-opportunities.api.server
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [reitit.ring :as ring]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [reitit.coercion.spec]
            [reitit.ring.middleware.exception :as exception]
            [reitit.ring.middleware.parameters :as parameters]
            [reitit.ring.coercion :as coercion]
            [muuntaja.core :as m]
            [malli.core :as malli]
            [lambda-opportunities.infra.database.db :refer [list-openings list-opening-by-id  create-opening! delete-opening-by-id! update-opening-by-id!]]))

(def opening-schema
  (malli/schema [:map
                 {:title "Opening"}
                 [:role string?]
                 [:company string?]
                 [:remote boolean?]
                 [:link string?]
                 [:location string?]
                 [:salary number?]]))

(defonce server (atom nil))

(defn show-opening-handler [request]
  (let [id (get-in request [:path-params :id])
        job-details (list-opening-by-id id)]
    {:status (if (empty? job-details) 404 200)
     :body (if (empty? job-details) {:status 404 :message "Opening not found"} job-details)}))

(defn list-openings-handler [_]
  (let [all-openings (list-openings)]
    {:status 200
     :body all-openings}))

(defn create-opening-handler [req]
  (let [payload (:body-params req)
        valid-payload (malli/validate opening-schema payload)]
    (if valid-payload
      (let [id (str (java.util.UUID/randomUUID))
            data (assoc payload :id id)
            result (try
                     (create-opening! data)
                     (catch Exception _
                       nil))]
        (if (nil? result)
          {:status 500
           :body {:status 500 :message "Internal server error"}}
          {:status 201
           :body result}))
      {:status 400
       :body {:status 400 :message "Invalid data"}})))

(defn update-opening-handler [request]
  (let [payload (:body-params request)
        id (get-in request [:path-params :id])
        updated (update-opening-by-id! id payload)]
    {:status (if (empty? updated) 404 200)
     :body (if (empty? updated) {:status 404 :message "Opening not found"} (list-opening-by-id id))}))

(defn delete-opening-handler [request]
  (let [id (get-in request [:path-params :id])
        result (delete-opening-by-id! id)]
    {:status (if (empty? result) 404 204)
     :body (if (empty? result) {:status 404 :message "Opening not found"} nil)}))

(def routes ["/"
             ["opening"
              ["" {:post create-opening-handler}]
              ["/:id" {:get show-opening-handler
                       :put update-opening-handler
                       :delete delete-opening-handler}]]
             ["openings" {:get list-openings-handler}]])

(def app
  (ring/ring-handler
   (ring/router
    routes
    {:data {:coercion reitit.coercion.spec/coercion
            :muuntaja m/instance
            :middleware [parameters/parameters-middleware
                         muuntaja/format-negotiate-middleware
                         muuntaja/format-response-middleware
                         muuntaja/format-request-middleware
                         coercion/coerce-request-middleware
                         coercion/coerce-response-middleware
                         exception/exception-middleware]}})
   (ring/routes
    (ring/create-default-handler))))

(defn start-server! []
  (reset!
   server
   (run-jetty
    app
    {:join? false
     :port 3333})))

(defn stop-server! []
  (.stop @server)
  (reset! server nil))
