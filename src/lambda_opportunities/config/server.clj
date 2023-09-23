(ns lambda-opportunities.config.server
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [reitit.ring :as ring]
            [ring.middleware.json :refer [wrap-json-response wrap-json-body]]
            [lambda-opportunities.services.opening :as s]))

(defonce server (atom nil))

(defn json-handler [handler]
  (-> handler
      (wrap-json-response)
      (wrap-json-body {:keywords? true})))

(defn show-opening-handler [request]
  (let [id (get-in request [:path-params :id])
        job-details (s/find-opening-by-id id)]
    {:status (if (nil? job-details) 404 200)
     :body (if (nil? job-details) {:status 404 :message "Opening not found"} job-details)}))

(defn list-openings-handler [_]
  (let [all-openings (s/find-all-openings)]
    {:status 200
     :body (if (nil? all-openings) [] all-openings)}))

(defn create-opening-handler [request]
  (let [payload (:body request)
        id (.toString (java.util.UUID/randomUUID))
        data (assoc payload :id id)
        result (s/insert-opening! data)]
    {:status 201
     :body result}))

(defn update-opening-handler [request]
  (let [payload (:body request)
        id (get-in request [:path-params :id])
        updated (s/update-opening! id payload)]
    {:status (if (nil? updated) 404 200)
     :body (if (nil? updated) {:status 404 :message "Opening not found"} (s/find-opening-by-id id))}))

(defn delete-opening-handler [request]
  (let [id (get-in request [:path-params :id])
        result (s/delete-opening! id)]
    {:status (if (nil? result) 404 204)
     :body (if (nil? result) {:status 404 :message "Opening doesn't exist"} nil)}))

(def app
  (ring/ring-handler
   (ring/router
    ["/"
     ["opening"
      ["" {:post (json-handler create-opening-handler)}]
      ["/:id" {:get (json-handler show-opening-handler)
               :put (json-handler update-opening-handler)
               :delete (json-handler delete-opening-handler)}]]
     ["openings" {:get (json-handler list-openings-handler)}]])))

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
