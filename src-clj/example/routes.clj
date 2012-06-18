(ns example.routes
  (:use compojure.core
        example.views
        [hiccup.middleware :only (wrap-base-url)])
  (:require [compojure.route :as route]
            [compojure.handler :as handler]
            [compojure.response :as response]
            [ring.util.response]
            [net.cgrand.enlive-html :as enlive]))


(defroutes main-routes
  (GET "/" [] (index-page))
  (GET "/repl-demo" [] (repl-demo-page))
  (route/resources "/")
  ;; (route/not-found "Page not found") ; i like the raw 404 page better
  )

(def app
  (-> (handler/site main-routes)
      (wrap-base-url)))
