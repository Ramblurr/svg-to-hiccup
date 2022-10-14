(ns svg-to-hiccup
  (:require ["svgo" :as svgo]
            ["fs" :as fs]
            ["posthtml-parser" :as phr]))

(defn posthtml->hiccup
  "Convert posthtml-parser output to hiccup"
  [posthtml]
  (map
   (fn [el]
     (if (:tag el)
       (let [{:keys [tag attrs content]} el]
         (into [(keyword tag) attrs] (posthtml->hiccup content)))
       (str el)))
   posthtml))

(defn html->hiccup
  "Take a string containing HTML and convert it to hiccup in clojure data structures"
  [html-str]
  (-> html-str
      phr/parser
      (js->clj :keywordize-keys true)
      posthtml->hiccup
      first))

(def svgo-settings {:multipass true
                    :plugins [{:name "preset-default"
                               :params {:overrides {:removeViewBox false}}}
                              "removeDimensions"
                              "sortAttrs"
                              {:name "convertColors"
                               :params {:currentColor true}}]})

(defn usage []
  (println "usage: <path to svg file>"))

(defn -main
  [& [svg-path]]
  (if (nil? svg-path)
    (usage)
    (let [svg-string (str (fs/readFileSync svg-path))
          settings-js (clj->js
                       (merge svgo-settings {:path svg-path}))
          {data :data} (-> svg-string
                           (svgo/optimize settings-js)
                           (js->clj  {:keywordize-keys true}))]
      (if data
        (println (-> data html->hiccup))
        (println "An error parsing the svg occured. Did you pass a file containing a single <svg>?")))))
