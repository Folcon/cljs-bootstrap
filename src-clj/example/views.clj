(ns example.views
  (:require
    [example.crossover.shared :as shared]
    [hiccup
      [page :refer [html5]]
      [element :refer [javascript-tag]]
      [page :refer [include-js include-css]]]
    [net.cgrand.enlive-html :as enlive]))

; When using {:optimizations :whitespace}, the Google Closure compiler combines
; its JavaScript inputs into a single file, which obviates the need for a "deps.js"
; file for dependencies.  However, true to ":whitespace", the compiler does not remove
; the code that tries to fetch the (nonexistent) "deps.js" file.  Thus, we have to turn
; off that feature here by setting CLOSURE_NO_DEPS.
;
; Note that this would not be necessary for :simple or :advanced optimizations.
(defn- run-clojurescript [path init]
  (list
    (javascript-tag "var CLOSURE_NO_DEPS = true;")
    (include-js path)
    (javascript-tag init)))

;; (defn make-js-map
;;   "makes a javascript map from a clojure one
;;    (stolen from https://gist.github.com/1098417) "
;;   [cljmap]
;;   (let [out (js-obj)]
;;     (doall (map #(aset out (name (first %)) (second %)) cljmap))
;;     out))

;; (defn ^:export init []
;;   (Ext.Application.
;;    (make-js-map
;;     {"launch" #(Ext.Panel.
;;                 (make-js-map {"fullscreen" true,
;;                               "html" "Hello World!!"}))})))

;;Ext.Msg.alert ('Status', 'Changes saved successfully.');
;(Ext.Msg.alert "status" "helloworld.")
;(Ext.Msg.show (make-js-map {"title" "hello" "msg" "world" }))



(defn index-page []
  (html5
   [:head
    [:title "hello clojurescript"]
    (include-css "/extjs-4.1.0/resources/css/ext-all.css")
    (include-js "/extjs-4.1.0/ext-all-debug-w-comments.js")]
   [:body
    (run-clojurescript
     "/js/main-debug.js"
     "example.repl.connect()")]))

(defn repl-demo-page []
  (html5
    [:head
      [:title "REPL Demo"]]
    [:body
      [:h1 "REPL Demo"]
      [:hr]
      "This page is meant to be accessed by running this in one terminal:"
      [:pre "lein ring server-headless 3000"]
      "And then this in a different terminal:"
      [:pre "lein trampoline cljsbuild repl-launch firefox http://localhost:3000/repl-demo"]
      [:hr]
      "Alternately, you can run:"
      [:pre "lein ring server-headless 3000 &
lein trampoline cljsbuild repl-listen"]
      "And then browse to this page manually."]
      [:hr]
      [:h2 {:id "fun"} "Try some fun REPL commands!"]
      [:pre "> (js/alert \"Hello!\")
> (load-namespace 'goog.date.Date)
> (js/alert (goog.date.Date.))
> (.log js/console (reduce + [1 2 3 4 5]))
> (load-namespace 'goog.dom)
> (goog.dom.setTextContent (goog.dom.getElement \"fun\") \"I changed something....\") "]
      (run-clojurescript
        "/js/main-debug.js"
        "example.repl.connect()")))
