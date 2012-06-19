(ns example.sencha)

(defn clj->js
  "Recursively transforms ClojureScript maps into Javascript objects,
   other ClojureScript colls into JavaScript arrays, and ClojureScript
   keywords into JavaScript strings.

   Borrowed and updated from mmcgrana.
  http://techylinguist.com/posts/2012/01/23/clojurescript-getting-started/"
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
                                            {:layout "border"
                                             :items [panel]})))

(defn ^:export bootstrap []
  (.application js/Ext (clj->js
                        {:name "MyApp"
                         :launch launch})))
