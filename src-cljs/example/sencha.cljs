;; (defn ^:export init []
;;   (Ext.Application.
;;    (make-js-map
;;     {"launch" #(Ext.Panel.
;;                 (make-js-map {"fullscreen" true,
;;                               "html" "Hello World!!"}))})))

;;Ext.Msg.alert ('Status', 'Changes saved successfully.');
;(Ext.Msg.alert "status" "helloworld.")
;(Ext.Msg.show (make-js-map {"title" "hello" "msg" "world" }))

(ns example.sencha)

(defn jsobj
  "makes a javascript map from a clojure one
   (stolen from https://gist.github.com/1098417) "
  [cljmap]
  (let [out (js-obj)]
    (doall (map #(aset out (name (first %)) (second %)) cljmap))
    out))

(defn clj->js
  "Recursively transforms ClojureScript maps into Javascript objects,
   other ClojureScript colls into JavaScript arrays, and ClojureScript
   keywords into JavaScript strings.

   Borrowed and updated from mmcgrana."
  [x]
  (cond
    (string? x) x
    (keyword? x) (name x)
    (map? x) (.-strobj (reduce (fn [m [k v]]
                                 (assoc m (clj->js k) (clj->js v))) {} x))
    (coll? x) (apply array (map clj->js x))
        :else x))

(def panel (clj->js
            {:region "center"
             :xtype "panel"
             :title "main-panel-title"
             :items [{:xtype "textfield" :fieldLabel "name"}]}))

(defn launch []
  (.create js/Ext "Ext.container.Viewport" (clj->js
                                            {"layout" "border"
                                             "items" [panel]})))

(defn ^:export bootstrap []
  (.application js/Ext (clj->js
                        {:name "MyApp"
                         :launch launch})))
